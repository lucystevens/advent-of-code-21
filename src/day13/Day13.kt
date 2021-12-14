package day13

import checkAnswer
import common.Point
import readInput
import toPair
import java.lang.IllegalStateException

fun main() {

    fun fold(points: Set<Point>, fold: Point): MutableSet<Point> {
        return if(fold.x == 0){
            points.map {
                if(it.y > fold.y){
                    Point(it.x, 2*fold.y - it.y)
                } else it
            }.toMutableSet()
        }
        else {
            points.map {
                if(it.x > fold.x){
                    Point(2*fold.x - it.x, it.y)
                } else it
            }.toMutableSet()
        }
    }


    fun part1(input: Pair<List<Point>,List<Point>>): Long {
        var points = input.first.toMutableSet()

        // only do first fold
        val fold = input.second[0]
        points = fold(points, fold)

        return points.size.toLong()
    }

    fun part2(input: Pair<List<Point>,List<Point>>): Long {
        var points = input.first.toMutableSet()

        input.second.forEach { fold ->
            points = fold(points, fold)
        }

        // print it
        val maxX = points.maxOf { it.x }
        val maxY = points.maxOf { it.y }
        for(y in 0..maxY){
            for(x in 0..maxX){
                print(
                    if(points.contains(Point(x, y))) "#" else " "
                )
            }
            println()
        }

        return 0L
    }

    fun getInput(file: String): Pair<List<Point>,List<Point>> {
        val points = mutableListOf<Point>()
        val folds = mutableListOf<Point>()
        readInput(file).forEach {
            if(it.startsWith("fold along")){
                val num = it.substring(13).toInt()
                folds.add(when(it[11]){
                    'y' -> Point(0, num)
                    'x' -> Point(num, 0)
                    else -> throw IllegalStateException("Bad char ${it[11]}")
                })
            }
            else if(it.isNotEmpty()){
                points.add(it.split(",").map { it.toInt() }.let { Point(it[0], it[1]) })
            }
        }
        return Pair(points, folds)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = getInput("day13/test")
    checkAnswer(part1(testInput), 17)
    //checkAnswer(part2(testInput), 1)

    val input = getInput("day13/input")
    println(part1(input))
    part2(input)
}
