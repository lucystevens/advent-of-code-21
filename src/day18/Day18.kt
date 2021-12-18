package day18

import checkAnswer
import readInput
import toPair
import java.util.*
import kotlin.math.ceil
import kotlin.math.floor

fun main() {

    val digitRegex = """\d+""".toRegex()

    fun doSplits(num: String): String {
        val splitRegex = """\d{2,}""".toRegex()
        var first = true
        return splitRegex.replace(num){ res ->
            if(first){
                first = false
                res.value.toLong().let {
                    "[${floor(it/2.0).toLong()},${ceil(it/2.0).toLong()}]"
                }
            } else res.value
        }
    }

    fun doExplodes(num: String): String{
        val stack = Stack<Int>()
        var index = 0
        while(index < num.length){
            val char = num[index]
            if(char == '['){
                stack.push(index)
            }
            else if(char == ']' && stack.size > 4){
                val pairStart = stack.pop()!!
                val pair = num.substring(pairStart+1, index)
                    .split(",")
                    .map { it.toLong() }
                    .toPair()

                var pre = num.substring(0, pairStart)
                digitRegex.findAll(pre).lastOrNull()?.groups?.get(0)?.apply {
                    val newDigit = value.toLong() + pair.first
                    pre = pre.replaceRange(range, newDigit.toString())
                }

                var post = num.substring(index+1)
                digitRegex.findAll(post).firstOrNull()?.groups?.get(0)?.apply {
                    val newDigit = value.toLong() + pair.second
                    post = post.replaceRange(range, newDigit.toString())
                }

                return "${pre}0$post"

            }
            else if(char == ']'){
                stack.pop()
            }
            index++
        }
        return num
    }

    fun reduce(num: String): String{
        var changed = true
        var current = num
        var new = ""
        while(changed){
            new = doExplodes(current)
            if(current == new) {
                new = doSplits(new)
            }
            changed = current != new
            current = new
        }
        return new
    }

    fun add(num1: String, num2: String): String {
        val result = "[$num1,$num2]"
        return reduce(result)
    }

    fun addAll(input: List<String>): String {
        return input.subList(1, input.size).fold(input[0]){ sum, num ->
            add(sum, num)
        }
    }

    fun magnitude(num: String): Long {
        val pairRegex = """\[(\d+),(\d+)]""".toRegex()
        var result = num
        while(result.contains("[")) {
            result = pairRegex.replace(result) { res ->
                (3 * res.groupValues[1].toLong() +
                        2 * res.groupValues[2].toLong()).toString()
            }
        }
        return result.toLong()
    }

    fun part1(input: List<String>): Long {
        return magnitude(addAll(input))
    }

    fun part2(input: List<String>): Long {
        return input.flatMap { num1 ->
            input.mapNotNull { num2 ->
                if(num1 != num2){
                    magnitude(add(num1, num2))
                } else null
            }
        }.maxOrNull()!!
    }

    fun getInput(file: String): List<String> {
        return readInput(file)
    }

    // doExplodes tests
    checkAnswer(doExplodes("[[[[[9,8],1],2],3],4]"), "[[[[0,9],2],3],4]", "doExplodes1")
    checkAnswer(doExplodes("[7,[6,[5,[4,[3,2]]]]]"), "[7,[6,[5,[7,0]]]]", "doExplodes2")
    checkAnswer(doExplodes("[[6,[5,[4,[3,2]]]],1]"), "[[6,[5,[7,0]]],3]", "doExplodes3")
    checkAnswer(doExplodes("[[3,[2,[1,[7,3]]]],[6,[5,[4,[3,2]]]]]"), "[[3,[2,[8,0]]],[9,[5,[4,[3,2]]]]]", "doExplodes4")
    checkAnswer(doExplodes("[[3,[2,[8,0]]],[9,[5,[4,[3,2]]]]]"), "[[3,[2,[8,0]]],[9,[5,[7,0]]]]", "doExplodes5")

    // doSplits tests
    checkAnswer(doSplits("[[[[0,7],4],[15,[0,13]]],[1,1]]"), "[[[[0,7],4],[[7,8],[0,13]]],[1,1]]", "doSplits1")
    checkAnswer(doSplits("[[[[0,7],4],[[7,8],[0,13]]],[1,1]]"), "[[[[0,7],4],[[7,8],[0,[6,7]]]],[1,1]]", "doSplits2")

    checkAnswer(reduce("[[[[[4,3],4],4],[7,[[8,4],9]]],[1,1]]"), "[[[[0,7],4],[[7,8],[6,0]]],[8,1]]", "reduce")

    // test if implementation meets criteria from the description, like:
    val testInput = getInput("day18/test")
    checkAnswer(addAll(testInput), "[[[[6,6],[7,6]],[[7,7],[7,0]]],[[[7,7],[7,7]],[[7,8],[9,9]]]]", "testInput addAll")
    checkAnswer(part1(testInput), 4140L, "testInput part1")
    checkAnswer(part2(testInput), 3993L, "testInput part2")

    val input = getInput("day18/input")
    println(part1(input))
    println(part2(input))
}
