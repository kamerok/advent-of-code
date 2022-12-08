package year2022.day08

import readInput

fun main() {
    val testInput = readInput(2022, 8, "test").parse()
    val input = readInput(2022, 8).parse()

    check(part1(testInput) == 21)
    println(part1(input))

    check(score(testInput, 1, 2) == 4)
    check(part2(testInput) == 8)
    println(part2(input))
}

private fun part1(input: Array<IntArray>): Int {
    val visibleCount = (1 until input.lastIndex).sumOf { row ->
        (1 until input.first().lastIndex).count { column ->
            isVisible(input, row, column)
        }
    }
    return input.size * input.first().size - visibleCount
}

private fun part2(input: Array<IntArray>): Int {
    return (1 until input.lastIndex).maxOf { row ->
        (1 until input.first().lastIndex).maxOf { column ->
            score(input, row, column)
        }
    }
}

private fun List<String>.parse(): Array<IntArray> {
    return Array(size) { row ->
        IntArray(first().length) { char ->
            this[row][char].digitToInt()
        }
    }
}

private fun isVisible(grid: Array<IntArray>, i: Int, j: Int): Boolean {
    var count = 0
    for (row in (i - 1 downTo 0)) {
        if (grid[row][j] >= grid[i][j]) {
            count++
            break
        }
    }
    for (row in (i + 1 until grid.first().size)) {
        if (grid[row][j] >= grid[i][j]) {
            count++
            break
        }
    }
    for (column in (j - 1 downTo 0)) {
        if (grid[i][column] >= grid[i][j]) {
            count++
            break
        }
    }
    for (column in (j + 1 until grid.size)) {
        if (grid[i][column] >= grid[i][j]) {
            count++
            break
        }
    }
    return count == 4
}

private fun score(grid: Array<IntArray>, i: Int, j: Int): Int {
    var count = 1
    var local = 0
    for (row in (i - 1 downTo 0)) {
        if (grid[row][j] >= grid[i][j]) {
            local++
            break
        } else {
            local++
        }
    }
    count *= local
    local = 0

    for (row in (i + 1 until grid.first().size)) {
        if (grid[row][j] >= grid[i][j]) {
            local++
            break
        } else {
            local++
        }
    }
    count *= local
    local = 0

    for (column in (j - 1 downTo 0)) {
        if (grid[i][column] >= grid[i][j]) {
            local++
            break
        } else {
            local++
        }
    }
    count *= local
    local = 0

    for (column in (j + 1 until grid.size)) {
        if (grid[i][column] >= grid[i][j]) {
            local++
            break
        } else {
            local++
        }
    }
    count *= local

    return count
}
