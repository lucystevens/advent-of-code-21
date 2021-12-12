package day10

import checkAnswer
import readInput
import java.util.*

fun main() {

    val open = listOf('(', '[', '{', '<')
    val close = listOf(')', ']', '}', '>')

    fun part1(input: List<String>): Long {
        return input.mapNotNull { line ->
            val stack = Stack<Char>()
            line.forEach {
                if(open.contains(it)){
                    stack.push(it)
                }
                else if(open[close.indexOf(it)] != stack.pop()?: 'x'){
                    return@mapNotNull it
                }
            }
            null
        }.fold(0){ sum, c ->
            sum + when(c){
                ')' -> 3
                ']' -> 57
                '}' -> 1197
                '>' -> 25137
                else -> 0
            }
        }
    }

    fun part2(input: List<String>): Long {
        return input.mapNotNull { line ->
            val stack = Stack<Char>()
            line.forEach {
                if(open.contains(it)){
                    stack.push(it)
                }
                else if(open[close.indexOf(it)] != stack.pop()?: 'x'){
                    return@mapNotNull null
                }
            }
            stack.toList().reversed().map { close[open.indexOf(it)] }
        }.map { line->
            line.fold(0L){sum, c ->
                (sum*5L) + close.indexOf(c)+1
            }
        }.sorted().let {
            it[it.size/2]
        }
    }

    fun getInput(file: String): List<String> {
        return readInput(file)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = getInput("day10/test")
    checkAnswer(part1(testInput), 26397)
    checkAnswer(part2(testInput), 288957)

    val input = getInput("day10/input")
    println(part1(input))
    println(part2(input))
}

