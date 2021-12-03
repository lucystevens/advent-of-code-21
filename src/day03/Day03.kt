package day03

import readInput
import kotlin.math.abs

fun main() {

    fun getMostCommonValues(input: List<String>): List<Int> {
        return input[0].mapIndexed { i, _ ->
            input.count { it[i] == '1' }
        }.map { if(it*2 >= input.size) 1 else 0 }
    }

    fun part1(input: List<String>): Int {
        val commonValues = getMostCommonValues(input)
        val gammaRate = commonValues.fold(""){s, i -> s+i}.toInt(2)
        val epsilonRate = commonValues.fold(""){s, i -> s+abs(i-1)}.toInt(2)
        println("Gamma = $gammaRate, Epsilon = $epsilonRate")
        return gammaRate * epsilonRate
    }

    fun applyBitCriteria(input: List<String>, position: Int, transformMcv: (mcv: Int) -> Int = { it }): String {
        return when (input.size) {
            1 -> input[0]
            else -> {
                val bitCriteria = getMostCommonValues(input)[position].let(transformMcv)
                applyBitCriteria(input.filter { it[position].digitToInt() == bitCriteria }, position + 1, transformMcv)
            }
        }
    }

    fun part2(input: List<String>): Int {
        val oxygenRating = applyBitCriteria(input, 0).toInt(2)
        val c02Rating = applyBitCriteria(input, 0) { abs(it - 1) }.toInt(2)
        println("Oxygen rating = $oxygenRating, C02 Rating = $c02Rating")
        return oxygenRating * c02Rating
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day03/test")
    check(part1(testInput) == 198)
    check(part2(testInput) == 230)

    val input = readInput("day03/input")
    println(part1(input))
    println(part2(input))
}
