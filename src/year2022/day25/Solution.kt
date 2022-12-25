package year2022.day25

import readInput
import kotlin.math.pow

fun main() {
    val testInput = readInput(2022, 25, "test")
    val input = readInput(2022, 25)

    val decimalToSnafu = mapOf(
        1 to "1",
        2 to "2",
        3 to "1=",
        4 to "1-",
        5 to "10",
        6 to "11",
        7 to "12",
        8 to "2=",
        9 to "2-",
        10 to "20",
        15 to "1=0",
        20 to "1-0",
        2022 to "1=11-2",
        12345 to "1-0---0",
        314159265 to "1121-1110-1=0",
        1747 to "1=-0-2",
        906 to "12111",
        198 to "2=0=",
        11 to "21",
        201 to "2=01",
        31 to "111",
        1257 to "20012",
        32 to "112",
        353 to "1=-1=",
        107 to "1-12",
        7 to "12",
        3 to "1=",
        37 to "122",
    ).mapKeys { it.key.toLong() }

    decimalToSnafu.forEach { (decimal, snafu) ->
        val value = snafu.snafuToDecimal()
        check(value == decimal) {
            """
            
            Convert $snafu to decimal
            Expected: $decimal
            But was: $value
        """.trimIndent()
        }
    }

    decimalToSnafu.forEach { (decimal, snafu) ->
        val value = decimal.snafuSize()
        check(value == snafu.length) {
            """
            
            Chars in $decimal to $snafu
            Expected: ${snafu.length}
            But was: $value
        """.trimIndent()
        }
    }

    decimalToSnafu.forEach { (decimal, snafu) ->
        val value = decimal.toSnafu()
        check(value == snafu) {
            """
            
            Convert $decimal to SNAFU
            Expected: $snafu
            But was: $value
        """.trimIndent()
        }
    }

    check(part1(testInput) == "2=-1=0")
    println(part1(input))
}

private fun part1(input: List<String>): String {
    return input.sumOf { it.snafuToDecimal() }.toSnafu()
}

private fun Long.toSnafu(): String {
    val result = IntArray(snafuSize())
    var remains = this
    result.indices.reversed().forEach { place ->
        val symbol = listOf(2, 1, 0, -1, -2).first { factor ->
            val maxMinus = (place - 1 downTo 0).sumOf { minusPlace ->
                5.0.pow(minusPlace) * 2
            }
            5.0.pow(place) * factor - maxMinus <= remains
        }
        remains -= (5.0.pow(place) * symbol).toLong()
        result[result.lastIndex - place] = symbol
    }
    return result.joinToString("") {
        when (it) {
            -2 -> "="
            -1 -> "-"
            0 -> "0"
            1 -> "1"
            2 -> "2"
            else -> throw IllegalArgumentException("Unknown SNAFU number $it")
        }
    }
}

private fun Long.snafuSize(): Int {
    val significantNumber = (0..1000).first { count ->
        buildString { repeat(count) { append('2') } }.snafuToDecimal() >= this
    }
    return significantNumber
}

private fun String.snafuToDecimal(): Long {
    var result = 0L
    reversed().forEachIndexed { index, c ->
        val multiplier = when (c) {
            '-' -> -1
            '=' -> -2
            '0' -> 0
            '1' -> 1
            '2' -> 2
            else -> throw IllegalArgumentException("Unknown SNAFU symbol $c")
        }
        result += 5.0.pow(index).toLong() * multiplier
    }
    return result
}
