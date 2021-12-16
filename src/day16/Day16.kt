package day16

import checkAnswer
import readInput

fun main() {
    fun part1(input: List<Long>): Long {
        return input.size.toLong()
    }

    fun part2(input: List<Long>): Long {
        return input.size.toLong()
    }

    fun getInput(file: String): List<Long> {
        return readInput(file)[0].split(",").map { it.toLong() }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = getInput("day16/test")
    checkAnswer(part1(testInput), 1)
    //checkAnswer(part2(testInput), 1)

    val input = getInput("day16/input")
    println(part1(input))
    println(part2(input))
}
