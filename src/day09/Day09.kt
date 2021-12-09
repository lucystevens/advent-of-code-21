package day09

import checkAnswer
import common.Point
import readInput

fun main() {
    fun part1(input: Map<Point, Int>): Int {
        return input.keys.mapNotNull { point ->
            val location = input[point]!!
            val lowestAdj = point.adjacentPoints().minOf { input[it]?: 10 }
            if (location < lowestAdj) location + 1 else null
        }.sum()
    }

    fun findBasinFromPoint(point: Point, input: Map<Point, Int>, basin: MutableSet<Point> = mutableSetOf()): Set<Point> {
        basin.add(point)
        point.adjacentPoints() // Get all adjacent points
            .filterNot { basin.contains(it) } // If not already in basin
            .filterNot { input[it]?: 9 == 9 } // And not an edge
            .forEach { findBasinFromPoint(it, input, basin) } // Then find points in basin from that point
        return basin
    }

    fun part2(input: Map<Point, Int>): Int {
        return input.keys.fold(mutableListOf<Set<Point>>()) { basins, point ->
            if(input[point]?: 9 != 9  && !basins.flatten().contains(point)){
                basins.add(findBasinFromPoint(point, input))
            }
            basins
        }.map { it.size }.sortedDescending().let {
            it[0] * it[1] * it[2]
        }
    }

    fun getInput(file: String): Map<Point, Int> {
        return readInput(file).foldIndexed(mutableMapOf()){ y, map, line ->
            line.forEachIndexed { x, height ->
                map[Point(x,y)] = height.toString().toInt()
            }
            map
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = getInput("day09/test")
    checkAnswer(part1(testInput), 15)
    checkAnswer(part2(testInput), 1134)

    val input = getInput("day09/input")
    println(part1(input))
    println(part2(input))
}
