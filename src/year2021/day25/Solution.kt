package year2021.day25

import readInput

fun main() {
    val testInput = readInput(2021, 25, "test").parse()
    val input = readInput(2021, 25).parse()

    check(
        """...>>>>>...""".lines().parse().apply {
            advance()
        }.contentDeepEquals(
            """...>>>>.>..""".lines().parse()
        )
    )

    check(
        """
        ..........
        .>v....v..
        .......>..
        ..........
    """.trimIndent().lines().parse().apply {
            advance()
        }.contentDeepEquals(
            """
                ..........
                .>........
                ..v....v>.
                ..........
            """.trimIndent().lines().parse()
        )
    )

    check(part1(testInput).also { println(it) } == 58)
    println(part1(input))

//    check(part2(testInput) == 2758514936282235)
//    println(part2(input))
}

private fun part1(input: Array<Array<State>>): Int {
    var result = 0
    do {
        val change = input.advance()
        result++
    } while (change)
    return result
}

private fun part2(input: Array<Array<State>>): Int {
    return 0
}

private fun List<String>.parse(): Array<Array<State>> = Array(size) {
    val line = get(it)
    Array(line.length) { char ->
        when (line[char]) {
            '>' -> State.RIGHT
            'v' -> State.DOWN
            else -> State.EMPTY
        }
    }
}

private fun Array<Array<State>>.advance(): Boolean = advanceRight() or advanceDown()

private fun Array<Array<State>>.advanceDown(): Boolean {
    var advanced = false
    first().indices.forEach { columnIndex ->
        val newColumn = Array(size) { rowIndex ->
            this[rowIndex][columnIndex]
        }
        indices.forEach { rowIndex ->
            val state = this[rowIndex][columnIndex]
            if (state == State.DOWN) {
                val nextRowIndex = if (rowIndex == lastIndex) 0 else rowIndex + 1
                if (this[nextRowIndex][columnIndex] == State.EMPTY) {
                    advanced = true
                    newColumn[nextRowIndex] = State.DOWN
                    newColumn[rowIndex] = State.EMPTY
                }
            }
        }
        newColumn.forEachIndexed { rowIndex, state ->
            this[rowIndex][columnIndex] = state
        }
    }
    return advanced
}

private fun Array<Array<State>>.advanceRight(): Boolean {
    var advanced = false
    forEachIndexed { rowIndex, row ->
        val newRow = row.copyOf()
        row.forEachIndexed { columnIndex, state ->
            if (state == State.RIGHT) {
                val nextColumnIndex = if (columnIndex == row.lastIndex) 0 else columnIndex + 1
                if (this[rowIndex][nextColumnIndex] == State.EMPTY) {
                    advanced = true
                    newRow[nextColumnIndex] = State.RIGHT
                    newRow[columnIndex] = State.EMPTY
                }
            }
        }
        this[rowIndex] = newRow
    }
    return advanced
}

private fun Array<Array<State>>.contentToString() = buildString {
    this@contentToString.forEach { row ->
        row.forEach { state ->
            val c = when (state) {
                State.DOWN -> 'v'
                State.RIGHT -> '>'
                State.EMPTY -> '.'
            }
            append(c)
        }
        append('\n')
    }
}

private enum class State {
    EMPTY, DOWN, RIGHT
}
