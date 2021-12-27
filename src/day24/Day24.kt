package day24

import readInput

fun main() {

    fun convertToFunction(monad: Monad<String>){
        val input = IntRange(1,14).map { "n$it" }
        monad.execute(input)
        println(monad.get("z"))
    }

    fun findRulesDifferences(input: List<String>): MutableList<MutableList<String>> {
        val ruleSets = mutableListOf<MutableList<String>>()
        var currentSet = mutableListOf("inp w")
        input.subList(1, input.size).forEach {
            if(it == "inp w"){
                ruleSets.add(currentSet)
                currentSet = mutableListOf("inp w")
            }
            else {
                currentSet.add(it)
            }
        }
        ruleSets.add(currentSet)
        for(i in 0..17){
            println(
                "${i.toString().padStart(2,' ')}: " +
                        ruleSets.map { it[i] }.toSet().joinToString(" | "))
        }
        return ruleSets
    }

    fun solveForZ(a: Long, b: Long, c: Long, z: Long, num: Long): Long {
        val chunk = if((z%26)+b!=num) 1 else 0
        return ((z/a)*((25*chunk)+1)) + ((num+c)*chunk)
    }

    // if b > 9, then chunk must be 1 (because max of num is 9)
    // a is 1 or 26 starting at 1, 26 every other time
    // if chunk == 0, z = z/a
    // if chunk == 1, z = 26z/a + num + c

    fun solveForZ(num: String, input: List<List<String>>): Long {
        var z = 0L
        for(index in 0 until 14){
            val i = num[index].toString().toLong()
            val commands = input[index]
            val a = commands[4].last().toString().toLong()
            val b = commands[5].last().toString().toLong()
            val c = commands[15].last().toString().toLong()
            z = solveForZ(a,b,c,z,i)
        }
        return z
    }

    fun part1(input: List<List<String>>): Long {
        var num = 100000000000000L
        val totalNums = num - 10000000000000L
        var z = 1L
        var count = 0.0
        do {
            if(count % 1000000000 == 0.0){
                println("${(count/totalNums)*100}% done")
            }
            num--
            val input2 = num.toString()
            if(input2.length != 14){
                throw IllegalStateException("Bad input: $input")
            }
            if(!input2.contains("0")){
                z = solveForZ(input2, input)
            }
            count++
        } while (z != 0L)
        return num
    }

    fun part2(input: Monad<Long>): Long {
        return 0L
    }

    fun getInput(file: String): List<String> {
        return readInput(file)
    }

    val testInput = getInput("day24/test")
    convertToFunction(CalculatingMonad(testInput))

    val input = getInput("day24/input")
    val ruleSets = findRulesDifferences(input)
    //convertToFunction(CalculatingMonad(input))
    println(part1(ruleSets))
    //println(part2(input))
}

abstract class Monad<T>(val program: List<String>, val variables: MutableMap<Char,T>){

    abstract fun get(variable: String): T

    fun set(variable: Char, value: T) {
        variables[variable] = value
    }

    fun execute(input: Iterable<T>){
        val iter = input.iterator()
        program.forEachIndexed { index,cmd ->
            val instruction = cmd.substring(0,3)
            val args = cmd.substring(4)
            when(instruction){
                "inp" -> input(args, iter.next())
                "add" -> add(args)
                "mul" -> multiply(args)
                "div" -> divide(args)
                "mod" -> mod(args)
                "eql" -> equal(args)
                else -> throw IllegalArgumentException("Bad instruction: $instruction")
            }
        }
    }

    fun reset(){
        variables.mapValues { 0L }
    }

    abstract fun input(args: String, input: T)
    abstract fun add(args: String)
    abstract fun multiply(args: String)
    abstract fun divide(args: String)
    abstract fun mod(args: String)
    abstract fun equal(args: String)

    fun instr(args: String, op:(T,T)->T){
        args.split(" ").apply {
            instr(get(0), get(1), op)
        }
    }

    fun instr(var1: String, var2: String, op:(T,T)->T){
        set(var1[0], op.invoke(get(var1), get(var2)))
    }
}
