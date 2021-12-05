package day05

import readInput
import toPair
import java.lang.Integer.max
import kotlin.math.abs

fun main() {

    fun getMod(num: Int): Int {
        return when {
            num > 0 -> 1
            num < 0 -> -1
            else -> 0
        }
    }

    fun parseBoard(input: List<Pair<Point, Point>>): MutableMap<Point, Int> {
        return input.fold(mutableMapOf()) { board, line ->
            val xDiff = line.second.x - line.first.x
            val yDiff = line.second.y - line.first.y
            val xMod = getMod(xDiff)
            val yMod = getMod(yDiff)
            val diff = max(abs(xDiff), abs(yDiff))
            IntRange(0, diff)
                .map {d -> Point(line.first.x + (d*xMod), line.first.y + (d*yMod)) }
                .forEach { board.merge(it, 1){ old, _ -> old+1}}
            board
        }
    }

    fun part1(input: List<Pair<Point, Point>>): Int {
        return parseBoard(
            input.filter { it.first.x == it.second.x || it.first.y == it.second.y }
        ).values.filter { it > 1 }.count()
    }

    fun part2(input: List<Pair<Point, Point>>): Int {
        return parseBoard(input).values.filter { it > 1 }.count()
    }

    fun getInput(file: String): List<Pair<Point, Point>> {
        return readInput(file).map { line ->
            line.split(" -> ").map {
                it.split(",")
                    .map { it.toInt() }
                    .let { Point( it[0], it[1]) }
            }.toPair()
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = getInput("day05/test")
    check(part1(testInput) == 5){ "Actual val: ${part1(testInput)}"}
    check(part2(testInput) == 12){ "Actual val: ${part2(testInput)}"}

    val input = getInput("day05/input")
    println(part1(input))
    println(part2(input))
}

data class Point(val x: Int, val y: Int)