package year2019.day02

import readInput

fun runIntcode(input: List<Int>): List<Int> {
    val result = input.toMutableList()
    var i = 0
    while (true) {
        when {
            result[i] == 1 -> {
                result[result[i + 3]] = result[result[i + 1]] + result[result[i + 2]]
                i += 4
            }
            result[i] == 2 -> {
                result[result[i + 3]] = result[result[i + 1]] * result[result[i + 2]]
                i += 4
            }
            result[i] == 99 -> return result
            else -> throw Exception("unknown command")
        }
    }
}

private val testCases1 = mapOf(
    listOf(1, 9, 10, 3, 2, 3, 11, 0, 99, 30, 40, 50) to
            listOf(3500, 9, 10, 70, 2, 3, 11, 0, 99, 30, 40, 50),
    listOf(1, 0, 0, 0, 99) to listOf(2, 0, 0, 0, 99),
    listOf(2, 3, 0, 3, 99) to listOf(2, 3, 0, 6, 99),
    listOf(2, 4, 4, 5, 99, 0) to listOf(2, 4, 4, 5, 99, 9801),
    listOf(1, 1, 1, 4, 99, 5, 6, 0, 99) to listOf(30, 1, 1, 4, 2, 5, 6, 0, 99)
)

private fun test1() {
    testCases1.forEach { (input, output) ->
        check(output == runIntcode(input))
    }
}

fun main() {
    test1()

    val input = readInput(2019, 2).first().split(',').map { it.toInt() }

    //part1
    val formattedInput = input.toMutableList().apply {
        set(1, 12)
        set(2, 2)
    }
    println(runIntcode(formattedInput).first())

    var output = 0
    var noun = 0
    var verb = 0
    while (output != 19690720) {
        verb++
        if (verb == 100) {
            verb = 0
            noun++
        }
        val formattedInput1 = input.toMutableList().apply {
            set(1, noun)
            set(2, verb)
        }
        output = runIntcode(formattedInput1).first()
    }
    println(100 * noun + verb)
}