package day20

import checkAnswer
import common.Point
import readInput

fun main() {
    fun part1(input: Image): Int {
        return input.process().process().points.values.filter { it == '#' }.count()
    }

    fun part2(input: Image): Int {
        var image = input
        for(i in 1..50){
            image = image.process()
        }
        return image.points.values.filter { it == '#' }.count()
    }

    fun getInput(file: String): Image {
        val input = readInput(file)
        val algorithm = input[0]
        val points = mutableMapOf<Point, Char>()
        input.subList(2, input.size).forEachIndexed{ y, line ->
            line.forEachIndexed { x, c ->
                points[Point(x,y)] = c
            }
        }
        return Image(algorithm, points)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = getInput("day20/test")
    checkAnswer(part1(testInput), 35, "testInput part1")
    checkAnswer(part2(testInput), 3351, "testInput part2")

    val input = getInput("day20/input")
    println(part1(input))
    println(part2(input))
}

data class Image(val algorithm: String, val points: Map<Point, Char>, var backgroundPixel: Char = '.'){

    private val minX = points.minOf { it.key.x }
    private val maxX = points.maxOf { it.key.x }
    private val minY = points.minOf { it.key.y }
    private val maxY = points.maxOf { it.key.y }

    fun process(): Image {
        val newPoints = mutableMapOf<Point, Char>()
        for (x in minX-1..maxX+1){
            for(y in minY-1..maxY+1){
                val point = Point(x, y)
                val pixel = listOf(
                    point.topLeft(), point.above(), point.topRight(),
                    point.left(), point, point.right(),
                    point.bottomLeft(), point.below(), point.bottomRight()
                ).map { points[it]?:backgroundPixel }
                    .joinToString("") { if(it == '.') "0" else "1" }
                    .toInt(2).let { algorithm[it] }
                newPoints[point] = pixel
            }
        }
        val newBackgroundPixel = if(backgroundPixel == '.') algorithm[0] else algorithm.last()
        return Image(algorithm, newPoints, newBackgroundPixel)
    }

    fun print(){
        for(y in minY-2..maxY+2){
            for (x in minX-2..maxX+2){
                print(points[Point(x, y)]?:backgroundPixel)
            }
            println()
        }
    }
}
