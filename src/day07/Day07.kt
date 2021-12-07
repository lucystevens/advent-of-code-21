package day07

import checkAnswer
import readInput
import kotlin.math.abs
import kotlin.math.roundToLong

fun main() {
    fun part1(input: List<Long>): Long {
        val mode = input.sorted().let { it[it.size/2] }
        return input.sumOf { abs(it - mode) }
    }

    // was hoping this would work but unfortunately I had to resort to brute force
    fun part2simple(input: List<Long>): Long {
        val avg = input.average().roundToLong()
        return input.sumOf { abs(it - avg).factorial() }
    }

    fun part2(input: List<Long>): Long {
        val max = input.maxOrNull()!!
        val min = input.minOrNull()!!
        var smallestFuel = Long.MAX_VALUE
        for(num in min..max){
            val fuel = input.sumOf { abs(it - num).factorial() }
            if(fuel < smallestFuel) smallestFuel = fuel
        }
        return smallestFuel
    }

    fun getInput(file: String): List<Long> {
        return readInput(file)[0].split(",").map { it.toLong() }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = getInput("day07/test")
    checkAnswer(part1(testInput), 37)
    checkAnswer(part2(testInput), 168)

    val input = getInput("day07/input")
    println(part1(input))
    println(part2simple(input))
}

//90040997

fun Long.factorial(): Long {
    return LongRange(1, this).fold(0L){ sum, num ->
        sum + num
    }
}
