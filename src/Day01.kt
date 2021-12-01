fun main() {
    fun part1(input: List<String>): Int {
        return input.mapIndexed { index, depth ->
            Pair(
                if(index > 0) input[index-1].toInt() else depth.toInt(),
                depth.toInt()
            )
        }.count { it.second > it.first }
    }

    fun part2(input: List<String>): Int {
        val depths = input.map { it.toInt() }
        return depths.mapIndexed { i, d ->
            if (i > 2){
                val first = depths[i-3]+depths[i-2]+depths[i-1]
                val second = depths[i-2]+depths[i-1]+d
                Pair(first, second)
            }
            else Pair(0, 0)
        }.filter { it.first > 0 }
            .count { it.second > it.first }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
    check(part1(testInput) == 7)
    check(part2(testInput) == 5)

    val input = readInput("Day01")
    println(part1(input))
    println(part2(input))
}
