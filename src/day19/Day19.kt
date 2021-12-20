package day19

import checkAnswer
import readInput
import kotlin.math.abs
import kotlin.math.max

fun main() {

    // this returns double the number of orientations, so there are likely some that
    // are equivalent, but I don't care
    fun findOrientations(): Set<Orientation>{
        val orientations = mutableSetOf<Orientation>()
        val axis = listOf('x', 'y', 'z')
        val mods = listOf(-1, 1)
        for (xAxis in axis){
            for (yAxis in axis.filterNot { it == xAxis }){
                val zAxis = axis.find { it != xAxis && it != yAxis }!!
                for(xMod in mods){
                    for(yMod in mods){
                        for(zMod in mods){
                            orientations.add(
                                Orientation(
                                xAxis, xMod,
                                yAxis, yMod,
                                zAxis, zMod)
                            )
                        }
                    }
                }
            }
        }
        return orientations
    }

    val orientations = findOrientations()

    fun matchAxis(scanner1: Set<Point3D>, scanner2: List<Point3D>, axis: Char, possibleScanners: List<Scanner>): List<Scanner> {
        val results = mutableListOf<Scanner>()
        val axisSet = scanner1.map { it.getAxis(axis) }.toHashSet()
        val maxDiff = max(
            scanner1.maxOf { it.getAxis(axis) } - scanner2.minOf { it.getAxis(axis) },
            scanner2.maxOf { it.getAxis(axis) } - scanner1.minOf { it.getAxis(axis) })
        for(scanner in possibleScanners){
            for(baseDiff in 0..maxDiff) {
                for(mod in listOf(-1, 1)) {
                    val diff = baseDiff * mod
                    val matchingBeacons = scanner2.map { it.orientate(scanner.orientation) }
                        .map { it.getAxis(axis) + diff }
                        .filter { axisSet.contains(it) }
                    if (matchingBeacons.size >= 12) {
                        scanner.position.setAxis(axis, diff)
                        results.add(scanner)
                    }
                }
            }
        }

        return results
    }

    fun findScannerOrientation(beacons: Set<Point3D>, nextScanner: List<Point3D>): Scanner? {
        // try every orientation
        var possibleScanners = orientations.map { Scanner(Point3D(0,0,0), it) }

        // Check each possible scanner for each axis individually
        listOf('x', 'y', 'z').forEach {
            possibleScanners = matchAxis(beacons, nextScanner, it, possibleScanners)
        }
        return if(possibleScanners.isNotEmpty()) possibleScanners[0] else null
    }

    fun mapScannersAndBeacons(input: List<List<Point3D>>): Pair<List<Scanner>, Set<Point3D>> {
        val mappedScanners = mutableMapOf<Int, Scanner>()
        mappedScanners[0] = Scanner(
            Point3D(0,0,0),
            Orientation('x', 1,'y',1,'z',1)
        )
        val beacons = input[0].toHashSet()
        while(mappedScanners.size < input.size){
            for(i in 1 until input.size){
                // if not already mapped
                if(!mappedScanners.containsKey(i)){
                    findScannerOrientation(beacons, input[i])?.let{
                        mappedScanners[i] = it
                        beacons.addAll(it.getBeaconsRelativeToZero(input[i]))
                    }
                }
            }
        }
        return Pair(mappedScanners.values.toList(), beacons)
    }

    fun part1(scannersAndBeacons: Pair<List<Scanner>, Set<Point3D>>): Int {
        return scannersAndBeacons.second.size
    }

    fun part2(scannersAndBeacons: Pair<List<Scanner>, Set<Point3D>>): Int {
        return scannersAndBeacons.first.flatMap { s1 ->
            scannersAndBeacons.first.map { s2 ->
                abs(s1.position.x - s2.position.x) +
                        abs(s1.position.y - s2.position.y) +
                        abs(s1.position.z - s2.position.z)
            }
        }.maxOrNull()!!
    }

    fun getInput(file: String): List<List<Point3D>> {
        val scanners = mutableListOf<List<Point3D>>()
        var currentScanner = mutableListOf<Point3D>()
        val input = readInput(file)
        input.subList(1, input.size).forEach { line ->
            if(line.contains("scanner")){
                scanners.add(currentScanner)
                currentScanner = mutableListOf()
            }
            else if (line.isNotEmpty()) {
                currentScanner.add(line.split(",").let {
                    Point3D(it[0].toInt(), it[1].toInt(), it[2].toInt())
                })
            }
        }
        scanners.add(currentScanner)
        return scanners
    }

    // test orientation
    val testPoint = Point3D(2, -1, 3)
        .orientate(Orientation('x', -1, 'z', -1, 'y', -1))
    checkAnswer(testPoint, Point3D(-2, -3, 1), "test orientation")

    // test if implementation meets criteria from the description, like:
    val testInput = getInput("day19/test")
    val testResult = mapScannersAndBeacons(testInput)
    checkAnswer(part1(testResult), 79, "testInput part1")
    checkAnswer(part2(testResult), 3621, "testInput part2")

    val input = getInput("day19/input")
    val result = mapScannersAndBeacons(input)
    println(part1(result)) // 308
    println(part2(result)) // 12124
}

data class Point3D(var x: Int, var y: Int, var z: Int){

    fun orientate(orientation: Orientation): Point3D {
        return Point3D(
            getAxis(orientation.xAxis) * orientation.xMod,
            getAxis(orientation.yAxis) * orientation.yMod,
            getAxis(orientation.zAxis) * orientation.zMod
        )
    }

    fun transform(delta: Point3D): Point3D{
        return transform(delta.x, delta.y, delta.z)
    }

    fun transform(xDelta: Int, yDelta: Int, zDelta: Int): Point3D{
        return Point3D(x + xDelta, y+ yDelta, z+zDelta)
    }

    fun setAxis(a: Char, value: Int) = when(a) {
        'x' -> x = value
        'y' -> y = value
        'z' -> z = value
        else -> throw IllegalArgumentException("Axis $a not recognised")
    }

    fun getAxis(a: Char) = when(a) {
        'x' -> x
        'y' -> y
        'z' -> z
        else -> throw IllegalArgumentException("Axis $a not recognised")
    }

}

data class Orientation(
    val xAxis: Char, val xMod: Int,
    val yAxis: Char, val yMod: Int,
    val zAxis: Char, val zMod: Int
)

data class Scanner(val position: Point3D, val orientation: Orientation){
    fun getBeaconsRelativeToZero(beacons: List<Point3D>): List<Point3D> {
        return beacons.map { it.orientate(orientation).transform(position) }
    }
}
