package day15

import checkAnswer
import common.Point
import readInput
import java.util.*


fun main() {

    fun calculateMap(input: Map<Point, Long>): NodeMap {
        val width = input.maxOf { it.key.x } + 1
        val height = input.maxOf { it.key.y } + 1
        val map = mutableMapOf<Point, Node>()
        for(xMod in 0..4){
            for(yMod in 0..4){
                input.forEach{
                    var risk = xMod + yMod + it.value
                    if(risk > 9) risk-=9
                    val point = Point(it.key.x+(xMod*width), it.key.y+(yMod*height))
                    map[point] = Node(point, risk)
                }
            }
        }
        return NodeMap(map)
    }

    fun buildNodeMap(map: Map<Point, Long>): NodeMap = NodeMap(map.mapValues { Node(it.key, it.value) })

    fun part1(input: Map<Point, Long>): Long {
        val endPoint = Point(input.maxOf { it.key.x },input.maxOf { it.key.y })
        return RouteFinder(buildNodeMap(input)).traverse(Point(0,0), endPoint)
    }

    fun part2(input: Map<Point, Long>): Long {
        val width = input.maxOf { it.key.x } + 1
        val height = input.maxOf { it.key.y } + 1
        val endPoint = Point(width*5-1, height*5-1)
        return RouteFinder(calculateMap(input)).traverse(Point(0,0), endPoint)
    }

    fun getInput(file: String): Map<Point, Long> {
        return readInput(file).foldIndexed(mutableMapOf()){ y, map, line ->
            line.forEachIndexed { x, risk ->
                map[Point(x,y)] = risk.toString().toLong()
            }
            map
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = getInput("day15/test")
    checkAnswer(part1(testInput), 40)
    checkAnswer(part2(testInput), 315)

    val input = getInput("day15/input")
    println(part1(input))
    println(part2(input))
}

class Node(val point: Point, val risk: Long): Comparable<Node> {
    var totalRisk = Long.MAX_VALUE
    var visited = false

    override fun compareTo(other: Node): Int = totalRisk.compareTo(other.totalRisk)
}

class NodeMap(val map: Map<Point, Node>){
    private val connections = mutableMapOf<Point, Set<Node>>()

    fun getNode(point: Point): Node = map[point]!!
    fun getAdjacentNodes(node: Node): Set<Node> = connections.computeIfAbsent(node.point){
        it.adjacentPoints().mapNotNull { pt -> map[pt] }.toSet()
    }
}

class RouteFinder(val map: NodeMap){

    fun traverse(start: Point, end: Point): Long{
        return traverse(map.getNode(start), map.getNode(end))
    }

    fun traverse(start: Node, end: Node): Long{
        start.totalRisk = 0
        val queue = PriorityQueue<Node>()

        var point = start
        while (point != end){
            map.getAdjacentNodes(point).forEach {
                val distance = point.totalRisk + it.risk
                if(distance < it.totalRisk) {
                    it.totalRisk = distance
                    queue.add(it)
                }
            }
            point.visited = true
            point = queue.poll()!!
            while(point.visited){
                point = queue.poll()!!
            }
        }

        return end.totalRisk
    }

}
