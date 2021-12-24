package day22

import checkAnswer
import combine
import common.Point
import common.Point3D
import readInput
import java.util.*
import java.util.concurrent.BlockingQueue
import java.util.concurrent.atomic.AtomicLong
import kotlin.concurrent.thread
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

fun main() {
    fun part1(input: List<Pair<Cuboid, Boolean>>): Long {
        val onCubes = mutableSetOf<Point3D>()
        input.filter { it.first.isInInitialRange() }.forEach {
            when(it.second){
                true -> onCubes.addAll(it.first.getAllPoints())
                false -> onCubes.removeAll(it.first.getAllPoints())
            }
        }
        return onCubes.size.toLong()
    }

    fun part2(input: List<Pair<Cuboid, Boolean>>): Long {
        val cuboidsSeen = mutableListOf<Cuboid>()
        var count = 0L

        // work backwards as commands get overridden
        input.asReversed().forEach { command ->
            if(command.second) {

                // If it doesn't intersect with any cuboids we've seen, then this is the command
                // for these points
                val cuboid = command.first
                val intersectsWith = cuboidsSeen.filter { it.intersects(cuboid) }
                count += if (intersectsWith.isEmpty()) {
                    cuboid.size()
                }
                // Otherwise find points that won't be overridden later
                else {
                    // Find the number of points in this cuboid, that aren't in any other cuboid we've seen
                    cuboid.pointsNotIn(intersectsWith)
                }
            }
            cuboidsSeen.add(command.first)
        }
        return count
    }

    fun getInput(file: String): List<Pair<Cuboid, Boolean>> {
        return readInput(file).map { line ->
            val onOff = line.startsWith("on")
            val cuboid = line.substringAfter(" ").split(",").map {
                it.substringAfter("=")
                    .split("..")
                    .map { i -> i.toInt() }
                    .let { i -> IntRange(i[0], i[1]) }
            }.let { Cuboid(it[0], it[1], it[2]) }
            Pair(cuboid, onOff)
        }
    }

    val testPointsIn = Cuboid(IntRange(6,14), IntRange(6,14),IntRange(2,3)).pointsNotIn(listOf(
        Cuboid(IntRange(1,8),IntRange(1,8),IntRange(1,3)),
        Cuboid(IntRange(11,12), IntRange(7,11),IntRange(1,2)),
        Cuboid(IntRange(11,12), IntRange(7,10),IntRange(1,3))
    ))
    checkAnswer(testPointsIn, 126L, "testPointsIn")

    // test if implementation meets criteria from the description, like:
    val testSimple = getInput("day22/test-simple")
    checkAnswer(part2(testSimple), 39L, "test-simple")

    val testInput1 = getInput("day22/test")
    checkAnswer(part1(testInput1), 590784L, "testInput part1")
    checkAnswer(part2(testInput1.filter { it.first.isInInitialRange() }), 590784L, "testInput1 part1/part2")
    val testInput2 = getInput("day22/test2")
    checkAnswer(part2(testInput2), 2758514936282235L, "testInput part2")

    val input = getInput("day22/input")
    println(part1(input))
    println(part2(input))
    // 623748
    //1227345351869476
}

data class Cuboid(val x: IntRange, val y: IntRange, val z: IntRange){
    fun contains(point: Point3D): Boolean {
        return x.contains(point.x) && y.contains(point.y) && z.contains(point.z)
    }

    fun contains(cuboid: Cuboid): Boolean {
        return x.contains(cuboid.x) && y.contains(cuboid.y) && z.contains(cuboid.z)
    }

    fun size(): Long{
        return  ((x.last - x.first) + 1).toLong() *
                ((y.last - y.first) + 1).toLong() *
                ((z.last - z.first) + 1).toLong()
    }

    fun getAllPoints(): List<Point3D> {
        return x.combine(y){ x, y -> Point(x,y,) }
            .combine(z){point, z -> Point3D(point.x, point.y, z)}
    }

    fun isInInitialRange(): Boolean {
        return  abs(x.first) <= 50 && abs(x.last) <= 50 &&
                abs(y.first) <= 50 && abs(y.last) <= 50 &&
                abs(z.first) <= 50 && abs(z.last) <= 50
    }

    fun intersects(cuboid: Cuboid): Boolean {
        return x.intersects(cuboid.x) && y.intersects(cuboid.y) && z.intersects(cuboid.z)
    }

    // This assumes the cuboids intersect
    fun findOverlap(cuboid: Cuboid): Cuboid? {
        return if(intersects(cuboid)) Cuboid(
            x.findOverlap(cuboid.x),
            y.findOverlap(cuboid.y),
            z.findOverlap(cuboid.z)) else null
    }
    
    fun removeCuboid(cuboid: Cuboid): List<Cuboid> {
        return findOverlap(cuboid)?.let { overlap ->
            val xRanges = listOf(
                IntRange(x.first, overlap.x.first -1),
                overlap.x,
                IntRange(overlap.x.last + 1, x.last)
            ).filter { it.first <= it.last }
            val yRanges = listOf(
                IntRange(y.first, overlap.y.first -1),
                overlap.y,
                IntRange(overlap.y.last + 1, y.last)
            ).filter { it.first <= it.last }
            val zRanges = listOf(
                IntRange(z.first, overlap.z.first -1),
                overlap.z,
                IntRange(overlap.z.last + 1, z.last)
            ).filter { it.first <= it.last }
            xRanges.combine(yRanges){xr,yr -> Pair(xr,yr)}
                .combine(zRanges){pair,zr -> Cuboid(pair.first, pair.second, zr)}
                .filterNot { it == overlap }
        }?: listOf(this)
    }

    // Get the number of points in this cuboid, that are not in others
    fun pointsNotIn(cuboids: List<Cuboid>): Long {
        var remainingPoints = listOf(this)
        cuboids.forEach { cuboid ->
            remainingPoints = remainingPoints.flatMap { it.removeCuboid(cuboid) }
        }
        return remainingPoints.sumOf { it.size() }
    }

}

fun IntRange.intersects(other: IntRange): Boolean {
    return contains(other.first) || contains(other.last) || other.contains(first) || other.contains(last)
}

fun IntRange.contains(other: IntRange): Boolean {
    return contains(other.first) && contains(other.last)
}

// This assumes intersection
fun IntRange.findOverlap(range: IntRange): IntRange {
    return IntRange(max(this.first, range.first), min(this.last, range.last))
}

