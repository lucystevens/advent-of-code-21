package day25

import checkAnswer
import common.Point
import readInput

fun main() {

    fun getNextPoint(e: Map.Entry<Point, Char>): Point {
        return when(e.value){
            '>' -> e.key.right()
            'v' -> e.key.below()
            else -> throw IllegalArgumentException("Bad cucumber: ${e.value}")
        }
    }

    fun doStep(map: OverflowMap, type: Char): OverflowMap {
        val nextMap = map.clone()
        map.map.filter { it.value == type }.forEach {
            val next = getNextPoint(it)
            if(map.isEmpty(next)){
                nextMap.set(it.key, '.')
                nextMap.set(next, type)
            }
        }
        return nextMap
    }

    fun part1(input: Map<Point, Char>): Long {
        var steps = 0L
        var map = OverflowMap(input.toMutableMap())
        var hasMoved = true
        while(hasMoved){
            map = doStep(map, '>')
            hasMoved = map.changed
            map = doStep(map, 'v')
            hasMoved = hasMoved || map.changed
            steps ++
        }
        return steps
    }

    fun part2(input: Map<Point, Char>): Long {
        return input.size.toLong()
    }

    fun getInput(file: String): Map<Point, Char> {
        return readInput(file).foldIndexed(mutableMapOf()){ y, map, line ->
            line.forEachIndexed { x, value ->
                map[Point(x,y)] = value
            }
            map
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = getInput("day25/test")
    checkAnswer(part1(testInput), 58L, "testInput part1")
    //checkAnswer(part2(testInput), 1L, "testInput part2")

    val input = getInput("day25/input")
    println(part1(input))
    println(part2(input))
}

class OverflowMap(val map: MutableMap<Point, Char>) {
    val maxX = map.maxOf { it.key.x }
    val maxY = map.maxOf { it.key.y }
    var changed = false

    fun get(point: Point): Char {
        return map[Point(
            point.x % (maxX+1),
            point.y % (maxY+1)
        )]?: '.'
    }

    fun isEmpty(point: Point): Boolean {
        return get(point) == '.'
    }

    fun set(point: Point, value: Char){
        changed = true
        map[Point(
            point.x % (maxX+1),
            point.y % (maxY+1)
        )] = value
    }

    fun clone(): OverflowMap{
        return OverflowMap(map.toMutableMap())
    }
}
