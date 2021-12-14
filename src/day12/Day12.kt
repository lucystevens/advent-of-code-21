package day12

import checkAnswer
import readInput
import toPair

fun main() {
    val start = "start"
    val end = "end"



    fun processPaths(
        point: String,
        routes: Map<String,Set<String>>,
        paths: MutableSet<List<String>>,
        currentPath: List<String>,
        ignoreSmallCave: (String, List<String>) -> Boolean
    ){
        val newCurrentPath = currentPath.toMutableList().apply { add(point) }
        if(point == end){
            paths.add(newCurrentPath)
        }
        else if (
            point.lowercase() == point && ignoreSmallCave(point, currentPath)){
            return
        }
        else if(!paths.contains(newCurrentPath)){
            paths.add(newCurrentPath)
            val nextPoints = routes[point]?: listOf()
            nextPoints.forEach {
                processPaths(it, routes, paths, newCurrentPath, ignoreSmallCave)
            }
        }
    }

    fun part1(input: Map<String,MutableSet<String>>): Int {
        val paths = mutableSetOf<List<String>>()
        processPaths(start, input, paths, mutableListOf()){ point, path ->
            path.contains(point)
        }
        return paths.filter { it.contains(end) }.count()
    }

    fun part2(input: Map<String,MutableSet<String>>): Int {
        val paths = mutableSetOf<List<String>>()
        processPaths(start, input, paths, mutableListOf()){ point, path ->
            if(!path.contains(point)){
                false
            }
            else if(point == start){
                true
            }
            else {
                // find all small caves on path, and make a map of the number of times visited
                path.filter { it.lowercase() == it }
                    .fold(mutableMapOf<String, Int>()) { map, p ->
                        map.merge(p, 1) { count, _ -> count + 1 }
                        map
                    }.maxOf { it.value } > 1 // if any are visited more than once, then ignore
            }
        }
        return paths.filter { it.contains(end) }.count()
    }

    fun getInput(file: String): Map<String,MutableSet<String>> {
        return readInput(file).map {
            it.split("-").toPair()
        }.fold(mutableMapOf()){ map, path ->
            map.computeIfAbsent(path.first){ mutableSetOf() }
                .add(path.second)
            // also add inverse relation
            map.computeIfAbsent(path.second){ mutableSetOf() }
                .add(path.first)
            map
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput1 = getInput("day12/test1")
    checkAnswer(part1(testInput1), 10)
    checkAnswer(part2(testInput1), 36)

    val testInput2 = getInput("day12/test2")
    checkAnswer(part1(testInput2), 19)
    checkAnswer(part2(testInput2), 103)

    val testInput3 = getInput("day12/test3")
    checkAnswer(part1(testInput3), 226)
    checkAnswer(part2(testInput3), 3509)

    val input = getInput("day12/input")
    println(part1(input))
    println(part2(input))
}

fun MutableSet<List<String>>.print(){
    forEach{ println(it.joinToString(",")) }
    println("-".repeat(20))
}
