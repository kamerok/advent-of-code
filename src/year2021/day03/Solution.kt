package year2021.day03

import readInput

fun main() {

    fun List<String>.mostFrequentBit(index: Int): Boolean {
        val ones = count { binaryNumber -> binaryNumber[index] == '1' }
        val zeroes = size - ones
        return ones >= zeroes
    }

    fun Boolean.toChar() = if (this) '1' else '0'

    fun List<String>.filterBySignificantBit(
        getSignificantBit: (List<String>, Int) -> Boolean
    ): String {
        val remainingList = toMutableList()
        val bitCount = remainingList.first().indices
        bitCount.forEach { i ->
            if (remainingList.size == 1) return remainingList.first()
            val significantBit = getSignificantBit(remainingList, i)
            remainingList.removeIf { it[i] == significantBit.toChar() }
        }
        return remainingList.firstOrNull() ?: ""
    }

    fun part1(input: List<String>): Int {
        val epsilonRate: Int = CharArray(input.first().length) { i ->
            input.mostFrequentBit(i).toChar()
        }.let { String(it) }.toInt(2)
        val gammaRate: Int = CharArray(input.first().length) { i ->
            (!input.mostFrequentBit(i)).toChar()
        }.let { String(it) }.toInt(2)
        return epsilonRate * gammaRate
    }

    fun part2(input: List<String>): Int {
        val oxygen = input.filterBySignificantBit { list, index ->
            list.mostFrequentBit(index)
        }.toInt(2)
        val co2 = input.filterBySignificantBit { list, index ->
            !list.mostFrequentBit(index)
        }.toInt(2)
        return oxygen * co2
    }

    val testInput = readInput(2021, 3, "test")
    val input = readInput(2021, 3)

    check(part1(testInput) == 198)
    println(part1(input))

    check(part2(testInput) == 230)
    println(part2(input))

}
