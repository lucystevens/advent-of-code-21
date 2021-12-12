package day11

import checkAnswer
import common.Point
import readInput

fun main() {


    fun part1(input: Map<Point, Long>): Long {
        val map = input.toMutableMap()
        var count = 0L
        for(step in 1..100){
            map.forEach { map.compute(it.key){_,v -> v!!+1} }
            var toFlash = map.filter { it.value > 9 }
            while(toFlash.isNotEmpty()){
                count += toFlash.size
                toFlash.forEach {
                    it.key.adjacentPoints(true).forEach { p ->
                        map.computeIfPresent(p){ _, v -> v+1 }
                    }
                    map[it.key] = Long.MIN_VALUE
                }
                toFlash = map.filter { it.value > 9 }
            }
            map.filter { it.value < 0 }.forEach { map[it.key] = 0 }
        }
        return count
    }

    fun part2(input: Map<Point, Long>): Long {
        val map = input.toMutableMap()
        var step = 0L
        while(map.filter { it.value != 0L }.isNotEmpty()){
            step++
            map.forEach { map.compute(it.key){_,v -> v!!+1} }
            var toFlash = map.filter { it.value > 9 }
            while(toFlash.isNotEmpty()){
                toFlash.forEach {
                    it.key.adjacentPoints(true).forEach { p ->
                        map.computeIfPresent(p){ _, v -> v+1 }
                    }
                    map[it.key] = Long.MIN_VALUE
                }
                toFlash = map.filter { it.value > 9 }
            }
            map.filter { it.value < 0 }.forEach { map[it.key] = 0 }
        }
        return step
    }

    fun getInput(file: String): Map<Point, Long> {
        return readInput(file).foldIndexed(mutableMapOf()){ y, map, line ->
            line.forEachIndexed { x, energy ->
                map[Point(x,y)] = energy.toString().toLong()
            }
            map
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = getInput("day11/test")
    checkAnswer(part1(testInput), 1656)
    checkAnswer(part2(testInput), 195)

    val input = getInput("day11/input")
    println(part1(input))
    println(part2(input))
}
