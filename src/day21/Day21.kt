package day21

import checkAnswer
import combine
import mapBoth
import readInput
import kotlin.math.max

fun main() {
    fun part1(player1: Player, player2: Player, die: Die): Int {
        var round = 0
        while(player1.score < 1000 && player2.score < 1000){
            val player = if(round%2 == 0) player1 else player2
            player.move(die.roll() + die.roll() + die.roll())
            round++
        }
        val losingPlayer = if(player1.score > player2.score) player2 else player1
        return losingPlayer.score * round * 3
    }

    fun findPossibleRolls(): Map<Int, Int> {
       return IntRange(1,3)
           .combine(IntRange(1,3)){die1, die2 -> die1+die2}
           .combine(IntRange(1,3)){sum, die2 -> sum+die2}
           .groupBy { it }
           .mapValues { it.value.size }
    }

    // key = sum of 3 rolls, value = number of times this happens
    val possibleRolls = findPossibleRolls()

    fun simulateGame(currentPlayer: Player, nextPlayer: Player, round: Int = 0): Pair<Long, Long> {
        // nextPlayer just moved
        if(nextPlayer.score >= 21){
            return if(round%2==1) Pair(1L,0L) else Pair(0L,1L)
        }

        return possibleRolls.map {
            simulateGame(
                nextPlayer.clone(),
                currentPlayer.clone().apply { move(it.key) },
                round+1
            ).mapBoth { res -> res * it.value }
        }.fold(Pair(0L,0L)){ sum, res ->
            Pair(sum.first + res.first, sum.second + res.second)
        }
    }

    fun part2(player1: Player, player2: Player): Long {
        val results = simulateGame(player1, player2, 0)
        return max(results.first, results.second)
    }

    // test if implementation meets criteria from the description, like:
    checkAnswer(part1(Player(4), Player(8), Die(100)), 739785, "testInput part1")
    checkAnswer(part2(Player(4), Player(8)), 444356092776315L, "testInput part2")

    println(part1(Player(2), Player(7), Die(100)))
    println(part2(Player(2), Player(7)))
}

data class Player(var position: Int){
    var score = 0

    fun move(delta: Int){
        position += delta
        while(position > 10){
            position -= 10
        }
        score += position
    }

    fun clone(): Player =
        Player(position).apply {
            this.score = this@Player.score
        }

}

class Die(private val sides: Int){
    private var value = sides

    fun roll(): Int =
        when (++value) {
            sides + 1 -> 1
            else -> value
        }
}