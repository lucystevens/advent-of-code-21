package common

data class Point(val x: Int, val y: Int) {
    fun above(): Point = Point(x, y-1)
    fun below(): Point = Point(x, y+1)
    fun left(): Point = Point(x-1, y)
    fun right(): Point = Point(x+1, y)

    fun adjacentPoints(includeDiagonals: Boolean = false): List<Point> =
        when(includeDiagonals){
            true -> IntRange(-1,1).flatMap {
                        xMod -> IntRange(-1,1).map {
                        yMod -> Point(x+xMod, y+yMod)
                    }
            }.filterNot { it == this }
            false -> listOf(above(), below(), left(), right())
        }
}
