package day23

import checkAnswer
import common.Point
import readInput
import kotlin.math.abs

fun main() {

    fun testPath(path: List<Pair<Point,Point>>, input: Map<Point, Char>): Long {
        val map = input.toMutableMap()
        var energy = 0L
        path.forEach {
            val route = map.getPathBetween(it.first, it.second)
            val toMove = map[it.first]!!
            map[it.first] = '.'
            map[it.second] = toMove
            val moveEnergy = route!!.size * toMove.getEnergy()
            energy += moveEnergy
        }

        if(map.isComplete()){
            return energy
        } else throw IllegalStateException("Map not complete!")
    }

    var paths = mutableMapOf<String, Long?>()
    fun findBestPath(map: Map<Point, Char>, totalEnergy: Long = 0L, visitedStates: Set<String> = setOf()): Long? {
        val mapKey = map.toKey()

        // If we've already been here then exit, avoiding loops
        if(visitedStates.contains(mapKey)){
            return null
        }

        // If we've already computed the best path for this map, then return it
        // Or if we know it's a dead end
        if(paths.containsKey(mapKey)){
            return paths[mapKey]?.plus(totalEnergy)
        }

        if(map.isComplete()){
            return totalEnergy
        }

        if(totalEnergy > 19067){
            return null
        }

        //println("$mapKey = $totalEnergy")

        // Find every possible move, for every amphipod
        val bestPath = map.getAmphipods().mapNotNull { amp ->
            val inHallway = amp.second.isInHallway()
            map.getEmptyPoints()
                .asSequence()
                .filterNot { it.isInHallway() && inHallway }
                .filterNot { it.isOutsideRoom() }
                .filter {
                    // If destination is room
                    if(it.y > 1){
                        val otherSpace = map[Point(it.x, abs(it.y-5))]
                        it.x == amp.first.getRoom() &&
                                (otherSpace == '.' || otherSpace == amp.first)
                    } else true
                }
                .mapNotNull { point ->
                    map.getPathBetween(amp.second, point)?.let{
                        val newMap = map.toMutableMap()
                        newMap[amp.second] = '.'
                        newMap[point] = amp.first
                        val energyRequired = it.size * amp.first.getEnergy()
                        val newVisitedStates = visitedStates.toMutableSet().apply { add(mapKey) }
                        findBestPath(newMap, totalEnergy + energyRequired, newVisitedStates)
                    }
                }.minOrNull()
        }.minOrNull()

        paths[mapKey] = bestPath
        return bestPath
    }

    fun part1(input: Map<Point, Char>): Long? {
        paths = mutableMapOf()
        return findBestPath(input)
    }

    fun part2(input: Map<Point, Char>): Long {
        return input.size.toLong()
    }

    fun getInput(file: String): Map<Point, Char> {
        return readInput(file).foldIndexed(mutableMapOf()){ y, map, line ->
            line.forEachIndexed { x, value ->
                map[Point(x,y)] = value
            }
            map
        }
    }


    val testInput = getInput("day23/test")
    checkAnswer(
        testInput.toMutableMap().apply { put(Point(5,2), '.') }
            .getPathBetween(Point(3,2), Point(5,2)),
        listOf(Point(3,1), Point(4,1), Point(5,1), Point(5,2)),
        "getPathBetween"
    )
    checkAnswer(testPath(listOf(
        Point(7,2) to Point(4,1),
        Point(5,2) to Point(7,2),
        Point(5,3) to Point(6,1),
        Point(4,1) to Point(5,3),
        Point(3,2) to Point(5,2),
        Point(9,2) to Point(8,1),
        Point(9,3) to Point(10,1),
        Point(8,1) to Point(9,3),
        Point(6,1) to Point(9,2),
        Point(10,1) to Point(3,2)
    ), testInput), 12521L, "testPath")
    checkAnswer(part1(testInput), 12521, "testInput part1")
    //checkAnswer(part2(testInput), 1L, "testInput part2")

    val input = getInput("day23/input")
    println(testPath(listOf(
        Point(7,2) to Point(8,1),
        Point(7,3) to Point(1,1),
        Point(5,2) to Point(7,3),
        Point(5,3) to Point(2,1),
        Point(8,1) to Point(5,3),
        Point(9,2) to Point(7,2),
        Point(9,3) to Point(5,2),
        Point(3,2) to Point(9,3),
        Point(3,3) to Point(9,2),
        Point(2,1) to Point(3,3),
        Point(1,1) to Point(3,2),
    ), input))
    checkAnswer(part1(testInput), 19059, "input part1")
    //println(part2(input))
}

fun Point.isInHallway(): Boolean{
    return y == 1
}

fun Point.isOutsideRoom(): Boolean{
    return y == 1 && (x == 3 || x == 5 || x == 7 || x == 9)
}

fun Point.distanceTo(point: Point): Long {
    return abs(x-point.x).toLong() + abs(y-point.y).toLong()
}

// Get all points in hallway
fun Map<Point,Char>.getHallwayPoints(): List<Point>{
    return IntRange(1,11).map { Point(it, 1) }
}

// Get all points in rooms
fun Map<Point,Char>.getRoomPoints(): List<Point>{
    val points = mutableListOf<Point>()
    for(y in 2..3){
        for(x in 3..9 step 2){
            points.add(Point(x,y))
        }
    }
    return points
}

// All points that could contain an amphipod, e.g. are not walls
fun Map<Point,Char>.getPoints(): List<Point>{
    return listOf(getHallwayPoints(), getRoomPoints()).flatten()
}

// All unoccupied positions in map
fun Map<Point,Char>.getEmptyPoints(): List<Point>{
    return getPoints().filter { get(it) == '.' }
}

// All amphipods and their positions
fun Map<Point,Char>.getAmphipods(): List<Pair<Char, Point>>{
    return getPoints().map { Pair(get(it)!!, it) }.filterNot { it.first == '.' }
}

// Unique key representing current positions in map
fun Map<Point,Char>.toKey(): String{
    return getPoints().joinToString("") { get(it).toString() }
}

// Can an amphipod move from point A to point B, assuming start and end are valid points
fun Map<Point,Char>.getPathBetween(pointA: Point, pointB: Point, path: List<Point> = listOf()): List<Point>? {
    return if(pointA == pointB) path
    else return pointA.adjacentPoints()
        .filter { get(it) == '.' }
        .filterNot { path.contains(it) }
        .mapNotNull { getPathBetween(it, pointB, path.toMutableList().apply { add(it) }) }
        .firstOrNull()
}

fun Map<Point,Char>.isComplete(): Boolean {
    return get(Point(3, 2)) == 'A' && get(Point(3,3)) == 'A' &&
            get(Point(5, 2)) == 'B' && get(Point(5,3)) == 'B' &&
            get(Point(7, 2)) == 'C' && get(Point(7,3)) == 'C' &&
            get(Point(9, 2)) == 'D' && get(Point(9,3)) == 'D'
}

// For debugging
fun Map<Point,Char>.print(): String {
    var s = ""
    for(y in 0..4){
        for(x in 0..12){
            s += get(Point(x,y))?:' '
        }
        s += "\n"
    }
    return s
}

fun Char.getEnergy(): Long {
    return when(this){
        'A' -> 1
        'B' -> 10
        'C' -> 100
        'D' -> 1000
        else -> throw IllegalStateException("Char $this not recognised")
    }
}

fun Char.getRoom(): Int {
    return when(this){
        'A' -> 3
        'B' -> 5
        'C' -> 7
        'D' -> 9
        else -> throw IllegalStateException("Char $this not recognised")
    }
}
