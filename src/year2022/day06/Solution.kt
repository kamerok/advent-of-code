package year2022.day06

import readInput

fun main() {
    data class TestEntry(val input: String, val first: Int, val second: Int)

    val testData = listOf(
        TestEntry("mjqjpqmgbljsphdztnvjfqwrcgsmlb", 7, 19),
        TestEntry("bvwbjplbgvbhsrlpgdmjqwftvncz", 5, 23),
        TestEntry("nppdvjthqldpwncqszvftbrmjlhg", 6, 23),
        TestEntry("nznrnfrfntjfmvfwmzdfjlvtqnbhcprsg", 10, 29),
        TestEntry("zcfzfwzzqfrljwzlrfnpqdbhtmscgvjw", 11, 26)
    )
    val input = readInput(2022, 6).first()

    testData.forEach { entry ->
        check(part1(entry.input) == entry.first)
    }
    println(part1(input))

    testData.forEach { entry ->
        check(part2(entry.input) == entry.second)
    }
    println(part2(input))
}

private fun part1(input: String): Int = getStartMarker(input, 4)

private fun part2(input: String): Int = getStartMarker(input, 14)

private fun getStartMarker(input: String, packetLength: Int): Int {
    var processed = packetLength
    input.asSequence().windowed(packetLength).forEach { signal ->
        if (signal.toSet().size == packetLength) return processed
        processed++
    }
    return 0
}
