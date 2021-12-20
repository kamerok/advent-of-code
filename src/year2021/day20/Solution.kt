package year2021.day20

import readInput

fun main() {
    val (testKey, testGrid) = readInput(2021, 20, "test").parse()
    val (key, grid) = readInput(2021, 20).parse()

    check(part1(testKey, testGrid) == 35)
    println(part1(key, grid))

    check(part2(testKey, testGrid) == 3351)
    println(part2(key, grid))
}

private fun part1(key: String, grid: List<BooleanArray>): Int {
    val default = key.first() == '#'
    return grid.enhance(key, false).enhance(key, default).countTrue()
}

private fun part2(key: String, grid: List<BooleanArray>): Int {
    val switchDefaultKey = key.first() == '#'
    var enhanced = grid
    repeat(50) { count ->
        val default = if (!switchDefaultKey || count == 0) {
            false
        } else {
            count.mod(2) != 0
        }
        enhanced = enhanced.enhance(key, default)
    }
    return enhanced.countTrue()
}

private fun List<BooleanArray>.enhance(key: String, defaultValue: Boolean): List<BooleanArray> =
    List(size + 2) { i ->
        BooleanArray(first().size + 2) { j ->
            val keyIndex = listOf(
                i - 1 to j - 1,
                i - 1 to j,
                i - 1 to j + 1,
                i to j - 1,
                i to j,
                i to j + 1,
                i + 1 to j - 1,
                i + 1 to j,
                i + 1 to j + 1,
            )
                .map { (i, j) ->
                    i - 1 to j - 1
                }
                .map { (i, j) ->
                    if (getOrNull(i)?.getOrNull(j) ?: defaultValue) 1 else 0
                }
                .joinToString(separator = "")
                .toInt(2)
            key[keyIndex] == '#'
        }
    }

private fun List<BooleanArray>.countTrue() = sumOf { row -> row.count { it } }

private fun List<String>.parse(): Pair<String, List<BooleanArray>> {
    return first() to drop(2).map { line ->
        BooleanArray(line.length) { line[it] == '#' }
    }
}