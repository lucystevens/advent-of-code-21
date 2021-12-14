package day14

import checkAnswer
import readInput

fun main() {

    // Original solution for part 1
    fun polymerise(startPair: String, rules: Map<String,String>, steps:  Int): String {
        var chain = startPair
        for(i in 1..steps){
            chain = chain.allElementPairs()
                .map { rules[it] ?: "XX"  }
                .joinElementPairs()
        }
        return chain
    }

    fun recursivePolymerise(startPair: String, rules: Map<String,String>, steps: Int, lookup: MutableMap<String, Map<Char, Long>> = mutableMapOf()): Map<Char, Long> {
        return lookup.computeIfAbsent("$startPair$steps") {
            when (steps) {
                1 -> mapOf(rules[startPair]!![1] to 1L)
                else -> rules[startPair]!!.let {
                    it.allElementPairs()
                        .fold(mutableMapOf(it[1] to 1L)) { map, pair ->
                            map.merge(recursivePolymerise(pair, rules, steps - 1, lookup))
                        }
                }
            }
        }
    }

    fun solve(input: Pair<String, Map<String,String>>, steps: Int): Long {
        val startMap = input.first.groupBy { it }.mapValues { it.value.size.toLong() }.toMutableMap()
        val counts = input.first.allElementPairs()
            .fold(startMap) { map, pair ->
                map.merge(recursivePolymerise(pair, input.second, steps))
            }

        return counts.maxOf { it.value } - counts.minOf { it.value }
    }

    fun part1(input: Pair<String, Map<String,String>>): Long {
        return solve(input, 10)
    }

    fun part2(input: Pair<String, Map<String,String>>): Long {
        return solve(input, 40)
    }

    fun getInput(file: String): Pair<String, Map<String,String>> {
        val input = readInput(file)
        return Pair(
            input[0],
            input.subList(2, input.size).fold(mutableMapOf()){ map, line ->
                line.split(" -> ").apply {
                    map[this[0]] = this[0][0] +  this[1] + this[0][1]
                }
                map
            }
        )
    }

    // test if implementation meets criteria from the description, like:

    val testInput = getInput("day14/test")
    checkAnswer(part1(testInput), 1588)
    checkAnswer(part2(testInput), 2188189693529)

    val input = getInput("day14/input")
    println(part1(input))
    println(part2(input))

    //2447
    //Starting part 2 calculation
    //3018019237563
}

fun String.allElementPairs(): List<String> {
    return substring(1).foldIndexed(mutableListOf()){ index, list, char ->
        list.apply { add("${this@allElementPairs[index]}$char") }
    }
}

fun List<String>.joinElementPairs(): String {
    return mapIndexed { index, s ->
        when(index){
            0 -> s
            else -> s.substring(1)
        }
    }.joinToString("")
}

fun <K> MutableMap<K, Long>.merge(other: Map<K, Long>): MutableMap<K, Long> {
    other.forEach {
        merge(it.key, it.value){old, _ -> old+it.value}
    }
    return this
}

