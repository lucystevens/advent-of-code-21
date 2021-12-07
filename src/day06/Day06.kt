package day06

import checkAnswer
import readInput
import rotateLeft

fun main() {

    fun calculateFish(input: List<Int>, days: Int): Long{
        val generations = IntRange(0,8).map {
                d -> input.filter { it == d }.size.toLong()
        }.toMutableList()
        IntRange(1, days).forEach {
            generations.rotateLeft()
            generations[6] += generations[8]
            println("($it, ${generations.sum()})")
        }
        return generations.sum()
    }

    fun part1(input: List<Int>): Long {
        return calculateFish(input, 80)
    }

    fun part2(input: List<Int>): Long {
        return calculateFish(input, 256)
    }


    fun getInput(file: String): List<Int> {
        return readInput(file)[0].split(",").map { it.toInt() }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = getInput("day06/test")
    //checkAnswer(part1(testInput), 5934)
    //checkAnswer(part2(testInput), 26984457539L)

    val input = getInput("day06/input")
    //println(part1(input))
    //println(part2(input))

    calculateFish(mutableListOf(3), 100)

}
