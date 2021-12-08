package year2021.day08

import readInput

fun main() {
    fun part1(input: List<String>): Int =
        input
            .flatMap {
                it
                    .split(" | ")[1]
                    .split(" ")
            }
            .count { it.length in setOf(2, 3, 4, 7) }

    fun List<String>.figureOutWiring(): CharArray {
        val wiring = CharArray(7)

        val one = requireNotNull(find { it.length == 2 })
        val seven = requireNotNull(find { it.length == 3 })
        //compare seven with one for top part
        wiring[0] = seven.asIterable().subtract(one.toSet()).first()
        val rightBorder = one.asIterable().intersect(seven.toSet())

        val six = requireNotNull(find {
            //six is the only number of size 6 with single wiring from right border
            it.length == 6 && rightBorder.intersect(it.toSet()).size == 1
        })
        wiring[5] = rightBorder.intersect(six.toSet()).first()
        wiring[2] = rightBorder.minus(wiring[5]).first()

        val three = requireNotNull(find {
            //three is the only number of size 5 with both wiring from right border
            it.length == 5 && rightBorder.intersect(it.toSet()).size == 2
        })
        val four = requireNotNull(find { it.length == 4 })
        wiring[3] = three.asIterable().intersect(four.toSet()).minus(rightBorder).first()
        wiring[6] = three.asIterable()
            .minus(setOf(wiring[0], wiring[2], wiring[3], wiring[5]))
            .first()
        wiring[1] = four.asIterable().minus(setOf(wiring[2], wiring[3], wiring[5])).first()
        wiring[4] = six.asIterable()
            .minus(setOf(wiring[0], wiring[1], wiring[3], wiring[5], wiring[6]))
            .first()

        return wiring
    }

    fun String.toDigit(wiring: CharArray): Int =
        when {
            length == 6 && !contains(wiring[3]) -> 0
            length == 2 -> 1
            length == 5 && !contains(wiring[5]) -> 2
            length == 4 -> 4
            length == 5 && !contains(wiring[2]) -> 5
            length == 5 -> 3
            length == 6 && !contains(wiring[2]) -> 6
            length == 3 -> 7
            length == 6 -> 9
            else -> 8
        }

    fun part2(input: List<String>): Int {
        val sanitizedInput = input
            .map { line ->
                line
                    .split(" | ")
                    .map { it.split(" ") }
            }
        return sanitizedInput
            .sumOf { (test, output) ->
                val wiring: CharArray = test.figureOutWiring()
                buildString {
                    output.forEach { append(it.toDigit(wiring)) }
                }.toInt()
            }
    }

    val testInput = readInput(2021, 8, "test")
    val input = readInput(2021, 8)

    check(part1(testInput) == 26)
    println(part1(input))

    check(part2(testInput) == 61229)
    println(part2(input))
}
