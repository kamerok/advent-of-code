package year2015.day18

import readInput

fun main() {
    check(part1(readInput(2015, 18, "test").parse(), 4) == 4)
    println(part1(readInput(2015, 18).parse(), 100))

    check(part2(readInput(2015, 18, "test").parse(), 5) == 17)
    println(part2(readInput(2015, 18).parse(), 100))
}

private fun part1(grid: List<BooleanArray>, steps: Int): Int =
    grid.apply { repeat(steps) { advance() } }.count()

private fun part2(grid: List<BooleanArray>, steps: Int): Int =
    grid.apply {
        turnCornersOn()
        repeat(steps) {
            advance()
            turnCornersOn()
        }
    }.count()

private fun List<String>.parse(): List<BooleanArray> =
    map { BooleanArray(it.length) { i -> it[i] == '#' } }

private fun List<BooleanArray>.advance() {
    var cachedTopRow: BooleanArray? = null
    var cachedLeft: Boolean? = null
    indices.forEach { i ->
        val nextCachedTopRow = this[i].copyOf()
        first().indices.forEach { j ->
            val adjacent = listOfNotNull(
                cachedTopRow?.getOrNull(j - 1),
                cachedTopRow?.getOrNull(j),
                cachedTopRow?.getOrNull(j + 1),
                cachedLeft,
                getOrNull(i)?.getOrNull(j + 1),
                getOrNull(i + 1)?.getOrNull(j - 1),
                getOrNull(i + 1)?.getOrNull(j),
                getOrNull(i + 1)?.getOrNull(j + 1)
            ).count { it }

            val currentValue = this[i][j]
            val newValue = if (currentValue) {
                adjacent == 2 || adjacent == 3
            } else {
                adjacent == 3
            }

            this[i][j] = newValue
            cachedLeft = currentValue
        }
        cachedLeft = null
        cachedTopRow = nextCachedTopRow
    }
}

private fun List<BooleanArray>.turnCornersOn() {
    this[0][0] = true
    this[0][first().lastIndex] = true
    this[lastIndex][0] = true
    this[lastIndex][first().lastIndex] = true
}

private fun List<BooleanArray>.count(): Int = sumOf { row -> row.count { it } }

private fun List<BooleanArray>.print() {
    println()
    forEach { row ->
        row.forEach { print(if (it) '#' else '.') }
        println()
    }
}
