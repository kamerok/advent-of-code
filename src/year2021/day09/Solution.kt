package year2021.day09

import readInput

fun main() {
    val testInput = readInput(2021, 9, "test")
    val input = readInput(2021, 9)

    check(part1(testInput) == 15)
    println(part1(input))

    check(part2(testInput) == 1134)
    println(part2(input))
}

private fun part1(input: List<String>): Int =
    input.parseGrid()
        .findLowestPoints()
        .sumOf { it + 1 }

private fun part2(input: List<String>): Int =
    input.parseGrid()
        .findBasins()
        .sortedDescending().take(3)
        .reduce { a, b -> a * b }

private fun List<String>.parseGrid(): List<IntArray> =
    map { line -> IntArray(line.length) { line[it].digitToInt() } }

private fun List<IntArray>.findLowestPoints(): List<Int> {
    val lowestPoints = mutableListOf<Int>()
    forEachIndexed { i, row ->
        row.forEachIndexed { j, value ->
            val adjusted = setOfNotNull(
                getOrNull(i - 1)?.getOrNull(j),
                getOrNull(i)?.getOrNull(j - 1),
                getOrNull(i + 1)?.getOrNull(j),
                getOrNull(i)?.getOrNull(j + 1)
            )
            if (adjusted.none { it <= value }) {
                lowestPoints.add(value)
            }
        }
    }
    return lowestPoints
}

private fun List<IntArray>.findBasins(): List<Int> {
    val basins = mutableListOf<Int>()
    val markedPoints = mutableSetOf<Pair<Int, Int>>()
    forEachIndexed { i, row ->
        row.indices.forEach { j ->
            val size = calculateBasinSize(markedPoints, i, j)
            if (size > 0) {
                basins.add(size)
            }
        }
    }
    return basins
}

private fun List<IntArray>.calculateBasinSize(
    marked: MutableSet<Pair<Int, Int>>,
    i: Int,
    j: Int
): Int {
    if (marked.contains(i to j)) return 0
    val value = getOrNull(i)?.getOrNull(j)
    if (value == null || value == 9) return 0
    marked.add(i to j)
    return 1 +
            calculateBasinSize(marked, i + 1, j) +
            calculateBasinSize(marked, i - 1, j) +
            calculateBasinSize(marked, i, j + 1) +
            calculateBasinSize(marked, i, j - 1)
}