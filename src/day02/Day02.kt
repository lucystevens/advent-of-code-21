package day02

import readInput

fun main() {
    fun part1(input: List<String>): Int {
        var horizontal = 0
        var depth = 0
        input.map {
            Pair(
                it.split(" ")[0],
                it.split(" ")[1].toInt())
        }.forEach {
            when(it.first){
                "forward" -> horizontal += it.second
                "up" -> depth -= it.second
                "down" -> depth += it.second
            }
        }
        println("Part 1: Horizontal: $horizontal Depth: $depth")
        return horizontal * depth
    }

    fun part2(input: List<String>): Int {
        var horizontal = 0
        var depth = 0
        var aim = 0
        input.map {
            Pair(
                it.split(" ")[0],
                it.split(" ")[1].toInt())
        }.forEach {
            when(it.first){
                "forward" -> {
                    horizontal += it.second
                    depth += it.second * aim
                }
                "up" -> aim -= it.second
                "down" -> aim += it.second
            }
        }
        println("Part 2: Horizontal: $horizontal Depth: $depth")
        return horizontal * depth
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day02/test")
    check(part1(testInput) == 150)
    check(part2(testInput) == 900)

    val input = readInput("day02/input")
    println(part1(input))
    println(part2(input))
}
