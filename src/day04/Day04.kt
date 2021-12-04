package day04

import readInput

fun main() {

    fun checkBoard(board: List<List<Int>>, numbers: List<Int>): Boolean {
        return board.any { numbers.containsAll(it) } ||
            IntRange(0, 4).any { i-> numbers.containsAll(board.map { it[i] }) }
    }

    fun getWinningBoardScore(board: List<List<Int>>, numbers: List<Int>): Int {
        return board.flatten()
            .filter { !numbers.contains(it) }
            .sum()
    }

    fun part1(input: Bingo): Int {
        IntRange(5, input.numbers.size - 1)
            .map { input.numbers.subList(0, it + 1) }
            .forEach { numbers ->
                input.boards.forEach {
                    val winning = checkBoard(it, numbers)
                    if (winning){
                        return@part1 getWinningBoardScore(it, numbers) * numbers[numbers.lastIndex]
                    }
                }
        }
        return 0
    }

    fun part2(input: Bingo): Int {
        var boardsLeft = input.boards
        IntRange(5, input.numbers.size - 1)
            .map { input.numbers.subList(0, it + 1) }
            .forEach { numbers ->
                val nonWinningBoards = input.boards.filter { !checkBoard(it, numbers) }
                if (boardsLeft.size == 1 && nonWinningBoards.isEmpty()){
                    return@part2 getWinningBoardScore(boardsLeft[0], numbers) * numbers[numbers.lastIndex]
                }
                boardsLeft = nonWinningBoards
            }
        return 0
    }

    fun getInput(file: String): Bingo{
        val input = readInput(file)
        val numbers = input[0].split(",").map { it.toInt() }
        val boards = mutableListOf<List<List<Int>>>()
        var currentBoard = mutableListOf<List<Int>>()
        input.subList(1, input.size).forEach { s ->
            if(s.isEmpty()){
                currentBoard = mutableListOf()
                boards.add(currentBoard)
            }
            else {
                currentBoard.add(s.split(Regex(" +"))
                    .filter { it.isNotEmpty() }
                    .map { it.toInt() })
            }
        }
        return Bingo(numbers, boards)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = getInput("day04/test")
    check(part1(testInput) == 4512)
    check(part2(testInput) == 1924)

    val input = getInput("day04/input")
    println(part1(input))
    println(part2(input))
}
