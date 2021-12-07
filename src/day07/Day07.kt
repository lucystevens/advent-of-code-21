package day07

import checkAnswer
import readInput

fun main() {
    fun part1(input: List<String>): Int {
        return input.size
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    fun getInput(file: String): List<String> {
        return readInput(file)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = getInput("day07/test")
    checkAnswer(part1(testInput), 1)
    //checkAnswer(part2(testInput), 1)

    val input = getInput("day07/input")
    println(part1(input))
    println(part2(input))
}
