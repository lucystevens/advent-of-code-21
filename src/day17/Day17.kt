package day17

import checkAnswer
import common.Point
import kotlin.math.max

fun main() {

    // targetLowerBound = lowest x and highest y (top left)
    // targetUpperBound = highest x and lowest y (bottom right)

    fun calculateTrajectory(startingVelocity: Point, targetUpperBound: Point): List<Point>{
        var position = Point(0,0)
        var velocity = startingVelocity
        val trajectory = mutableListOf<Point>()
        while(position.x <= targetUpperBound.x && position.y >= targetUpperBound.y){
            position = Point(position.x + velocity.x, position.y + velocity.y)
            velocity = Point(max(velocity.x-1, 0), velocity.y - 1)
            trajectory.add(position)
        }
        return trajectory
    }

    fun isInTargetArea(trajectory: List<Point>, targetLowerBound: Point, targetUpperBound: Point): Point? {
        return trajectory.find {
            it.x >= targetLowerBound.x && it.x <= targetUpperBound.x &&
                    it.y <= targetLowerBound.y && it.y >= targetUpperBound.y
        }
    }

    fun part1(targetLowerBound: Point, targetUpperBound: Point): Int {
        var maxY = 0
        for(x in 1..1000){
            for(y in 1..1000){
                val trajectory = calculateTrajectory(Point(x, y), targetUpperBound)
                if(isInTargetArea(trajectory, targetLowerBound, targetUpperBound) != null){
                    val yHeight = trajectory.maxOf { it.y }
                    maxY = max(maxY, yHeight)
                }
            }
        }
        return maxY
    }

    fun part2(targetLowerBound: Point, targetUpperBound: Point): Int {
        var count = 0
        // yes this is a terrible brute force solution
        for(x in 1..10000){
            for(y in -115..10000){
                val trajectory = calculateTrajectory(Point(x, y), targetUpperBound)
                if(isInTargetArea(trajectory, targetLowerBound, targetUpperBound) != null){
                    count++
                }
            }
        }
        return count
    }

    // test if implementation meets criteria from the description, like:
    val testTargetLowerBound = Point(20, -5)
    val testTargetUpperBound = Point(30, -10)
    checkAnswer(part1(testTargetLowerBound, testTargetUpperBound), 45, "testInput part1")
    checkAnswer(part2(testTargetLowerBound, testTargetUpperBound), 112, "testInput part2")

    val targetLowerBound = Point(207, -63)
    val targetUpperBound = Point(263, -115)
    println(part1(targetLowerBound, targetUpperBound))
    println(part2(targetLowerBound, targetUpperBound))
}
