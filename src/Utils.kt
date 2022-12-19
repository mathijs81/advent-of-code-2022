import java.io.File
import java.math.BigInteger
import java.security.MessageDigest

/**
 * Reads lines from the given input txt file.
 */
fun readAsLines(name: String) = File("src", "$name.txt")
        .readLines()

fun readAsString(name: String) = File("src", "$name.txt").readText()

/**
 * Converts string to md5 hash.
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
        .toString(16)
        .padStart(32, '0')

// Helper function to prevent "Overload resolution ambiguity" on sumOf
// Also see https://youtrack.jetbrains.com/issue/KT-46360
inline fun <reified T> Iterable<T>.sumInt(intMapping: (T) -> Int): Int {
    return sumOf { intMapping(it) }
}

fun gcd(a: Long, b: Long): Long = if (b == 0L) a else gcd(b, a % b)

val NUM_REGEX = "-?[0-9]*\\.?[0-9]*".toRegex()
fun String.splitNumbers(): List<Number> =
        NUM_REGEX.findAll(this).filter { it.value.isNotEmpty() }.map { if ("." in it.value) it.value.toDouble() else it.value.toInt() }.toList()

val INT_REGEX = "-?[0-9]*".toRegex()
fun String.splitInts(): List<Int> =
        INT_REGEX.findAll(this).filter { it.value.isNotEmpty() }.map { it.value.toInt() }.toList()
