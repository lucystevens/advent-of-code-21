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

fun <T> checkAnswer(actual: T, expected: T) = check(actual == expected){ "Actual val: $actual"}

fun <T> MutableList<T>.rotateLeft(){
    if (isEmpty()) return
    val item0 = get(0)
    for (i in 0 until lastIndex){
        set(i, get(i+1))
    }
    set(lastIndex, item0)
}