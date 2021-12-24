package common

data class Point3D(var x: Int, var y: Int, var z: Int){

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