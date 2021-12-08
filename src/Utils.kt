import java.io.File
import java.math.BigInteger
import java.security.MessageDigest

/**
 * Reads lines from the given input txt file.
 */
fun readInput(year: Int, day: Int, suffix: String = ""): List<String> {
    val path = buildString {
        val dayNumber = day.toString().padStart(2, '0')
        append("year$year/day$dayNumber/")
        append("input")
        if (suffix.isNotEmpty()) {
            append("_$suffix")
        }
        append(".txt")
    }
    return File("src", path).readLines()
}

/**
 * Converts string to md5 hash.
 */
fun String.md5(): String =
    BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray())).toString(16)
