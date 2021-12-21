import java.io.File
import java.math.BigInteger
import java.security.MessageDigest

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = File("src", "$name.txt").readLines()

/**
 * Converts string to md5 hash.
 */
fun String.md5(): String = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray())).toString(16)

fun <T> List<T>.toPair(): Pair<T,T> = Pair(get(0), get(1))

fun <T> checkAnswer(actual: T, expected: T, name: String = "Testing") {
    check(actual == expected) { "$name: Actual val: $actual" }
    println("$name: Success")
}

fun <T> MutableList<T>.rotateLeft(){
    if (isEmpty()) return
    val item0 = get(0)
    for (i in 0 until lastIndex){
        set(i, get(i+1))
    }
    set(lastIndex, item0)
}

fun <T, R> Pair<T,T>.mapBoth(mappingFunction: (T) -> R): Pair<R,R> {
    return Pair(mappingFunction.invoke(first), mappingFunction.invoke(second))
}

// Combines every value from this list, with every value from the other list
fun <T,U,V> Iterable<T>.combine(iterable: Iterable<U>, combiner: (T,U) -> V): List<V>{
    return flatMap { t ->
        iterable.map { u -> combiner.invoke(t,u) }
    }
}