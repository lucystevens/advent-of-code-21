package common

data class Point(val x: Int, val y: Int) {
    fun above(): Point = Point(x, y-1)
    fun below(): Point = Point(x, y+1)
    fun left(): Point = Point(x-1, y)
    fun right(): Point = Point(x+1, y)

    fun adjacentPoints(): List<Point> = listOf(above(), below(), left(), right())
}
