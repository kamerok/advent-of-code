package year2022.day12

import readInput

fun main() {
    val testInput = readInput(2022, 12, "test").parse()
    val input = readInput(2022, 12).parse()

    check(part1(testInput) == 31)
    println(part1(input))

    check(part2(testInput) == 29)
    println(part2(input))
}

private fun part1(grid: Array<CharArray>): Int {
    val startRow = grid.indexOfFirst { it.contains('S') }
    val startCol = grid[startRow].indexOf('S')
    return findExit(grid, startRow to startCol)
}

private fun part2(grid: Array<CharArray>): Int {
    val starts = mutableListOf<Pair<Int, Int>>()
    grid.indices.forEach { row ->
        grid.first().indices.forEach { col ->
            if (grid[row][col] == 'a' || grid[row][col] == 'S') starts.add(row to col)
        }
    }
    return starts.map { findExit(grid, it) }.filter { it != 0 }.minOf { it }
}

private fun findExit(
    reference: Array<CharArray>,
    start: Pair<Int, Int>
): Int {
    val grid = Array(reference.size) { reference[it].clone() }
    reference[start.first][start.second] = 'a'

    var iteration = 0
    val queue = ArrayDeque(listOf(start))
    while (!queue.isEmpty()) {
        repeat(queue.size) {
            val (x, y) = queue.removeFirst()
            grid[x][y] = '#'
            val currentLetter = reference[x][y]
            listOf(
                x - 1 to y,
                x + 1 to y,
                x to y - 1,
                x to y + 1,
            ).forEach { (cX, cY) ->
                val candidateValue = grid.getOrNull(cX)?.getOrNull(cY)
                if ((candidateValue == 'E' && currentLetter == 'z') || (candidateValue == 'E' && currentLetter == 'y')) {
                    return iteration + 1
                }
                if (
                    candidateValue != null &&
                    candidateValue != '#' &&
                    candidateValue != 'E' &&
                    candidateValue <= currentLetter + 1
                ) {
                    if (!queue.contains(cX to cY)) queue.add(cX to cY)
                }
            }
        }
        iteration++
    }
    return 0
}

private fun List<String>.parse(): Array<CharArray> = map { it.toCharArray() }.toTypedArray()

private fun Array<CharArray>.prettyPrint() {
    forEach {
        println(it.joinToString(""))
    }
}
