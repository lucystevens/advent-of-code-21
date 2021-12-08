package day08

import checkAnswer
import readInput
import toPair

fun main() {
    fun part1(input: List<Pair<List<String>, List<String>>>): Long {
        return input.sumOf {
            it.second.filter { out ->
                listOf(2, 4, 3, 7).contains(out.length)
            }.count()
        }.toLong()
    }

    fun createMapping(input: List<String>): MutableMap<String, Int> {
        val numsLeft = input.toMutableList()
        val nums = IntRange(0,9).map { "" }.toMutableList()
        // Get easy nums
        nums[1] = numsLeft.removeFirstWhere { it.length == 2 }
        nums[4] = numsLeft.removeFirstWhere { it.length == 4 }
        nums[7] = numsLeft.removeFirstWhere { it.length == 3 }
        nums[8] = numsLeft.removeFirstWhere { it.length == 7 }

        // Whichever 6-length doesn't contain 1 is 6
        nums[6] = numsLeft.removeFirstWhere { it.length == 6 && !it.containsAllChars(nums[1])}
        // Whichever 6-length contains 4 is 9, and the other is 0
        nums[9] = numsLeft.removeFirstWhere { it.length == 6 && it.containsAllChars(nums[4])}
        nums[0] = numsLeft.removeFirstWhere { it.length == 6 }

        // Whichever 5-length contains 1 is 3
        nums[3] = numsLeft.removeFirstWhere { it.length == 5 && it.containsAllChars(nums[1])}
        // Whichever 5-length 9 contains is 5, and the other is 2
        nums[5] = numsLeft.removeFirstWhere { it.length == 5 && nums[9].containsAllChars(it)}
        nums[2] = numsLeft.removeFirstWhere { it.length == 5 }

        return nums.foldIndexed(mutableMapOf()){ index, map, num ->
            map[num.sort()] = index
            map
        }
    }

    fun part2(input: List<Pair<List<String>, List<String>>>): Long {
        return input.sumOf { line ->
            val mapping = createMapping(line.first)
            line.second
                .map { mapping[it.sort()] }
                .joinToString("") { it.toString() }
                .toLong()
        }
    }

    fun getInput(file: String): List<Pair<List<String>, List<String>>> {
        return readInput(file).map { line ->
            line.split(" | ").map {
                it.split(" ")
            }.toPair()
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = getInput("day08/test")
    checkAnswer(part1(testInput), 26)
    checkAnswer(part2(testInput), 61229)

    val input = getInput("day08/input")
    println(part1(input))
    println(part2(input))
}

fun <T> MutableList<T>.removeFirstWhere(predicate: (T) -> Boolean): T = first(predicate).apply { remove(this) }

fun String.containsAllChars(chars: String): Boolean = chars.all { contains(it) }

fun String.sort(): String = toSortedSet().toCharArray().concatToString()
