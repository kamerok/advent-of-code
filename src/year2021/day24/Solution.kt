package year2021.day24

import readInput

fun main() {
    val input = readInput(2021, 24).parse()

    println(part1(input))
    println(part2(input))
}

private fun part1(input: List<Parameters>): Long {
    return solve(input).second
}

private fun part2(input: List<Parameters>): Long {
    return solve(input).first
}

private data class Parameters(val a: Int, val b: Int, val c: Int)

private fun List<String>.parse(): List<Parameters> =
    chunked(18).map {
        Parameters(
            it[4].substringAfterLast(" ").toInt(),
            it[5].substringAfterLast(" ").toInt(),
            it[15].substringAfterLast(" ").toInt()
        )
    }

private fun magicFunction(parameters: Parameters, z: Long, w: Long): Long =
    if (z % 26 + parameters.b != w) ((z / parameters.a) * 26) + w + parameters.c
    else z / parameters.a

private fun solve(input: List<Parameters>): Pair<Long, Long> {
    var zValues = mutableMapOf(0L to (0L to 0L))
    input.forEach { parameters ->
        val zValuesThisRound = mutableMapOf<Long, Pair<Long, Long>>()
        zValues.forEach { (z, minMax) ->
            (1..9).forEach { digit ->
                val newValueForZ = magicFunction(parameters, z, digit.toLong())
                if (parameters.a == 1 || (parameters.a == 26 && newValueForZ < z)) {
                    zValuesThisRound[newValueForZ] =
                        minOf(zValuesThisRound[newValueForZ]?.first ?: Long.MAX_VALUE, minMax.first * 10 + digit) to
                                maxOf(
                                    zValuesThisRound[newValueForZ]?.second ?: Long.MIN_VALUE,
                                    minMax.second * 10 + digit
                                )
                }
            }
        }
        zValues = zValuesThisRound
    }
    return zValues.getValue(0)
}
