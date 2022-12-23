package year2022.day23

import readInput
import kotlin.math.max
import kotlin.math.min

fun main() {
    val testInput = readInput(2022, 23, "test").parse()
    val input = readInput(2022, 23).parse()

    check(part1(testInput) == 110)
    println(part1(input))

    check(part2(testInput) == 20) { "${part2(testInput)}" }
    println(part2(input))
}

private fun part1(input: Set<Elf>): Int {
    var state = input
    repeat(10) {
        state = state.advance(it)
    }
    return state.calculateAnswer()
}

private fun part2(input: Set<Elf>): Int {
    var state = input
    var answer = 0
    while (state.advance(answer) != state) {
        state = state.advance(answer)
        answer++
    }
    return answer + 1
}

private fun List<String>.parse(): Set<Elf> {
    return flatMapIndexed { i: Int, line: String ->
        line.flatMapIndexed { j, c ->
            if (c == '#') {
                listOf(Elf(i, j))
            } else {
                emptyList()
            }
        }
    }.toSet()
}

private fun Set<Elf>.printField(range: IntRange) {
    range.forEach { i ->
        range.forEach { j ->
            print(if (contains(Elf(i, j))) '#' else ".")
        }
        println()
    }
}

private fun Set<Elf>.advance(stepIndex: Int): Set<Elf> {
    val directions = (0 until Direction.values().size).map { offset ->
        Direction.values()[(stepIndex + offset) % Direction.values().size]
    }
    val consideredMoves = associate { elf ->
        val neighbours = elf.neighbours().count { contains(it) }
        val newPosition = if (neighbours == 0) {
            elf
        } else {
            directions.firstOrNull { direction ->
                direction.toCheck.all { checkInstruction ->
                    val pointToCheck = Elf(elf.i + checkInstruction.first, elf.j + checkInstruction.second)
                    !contains(pointToCheck)
                }
            }?.let { direction -> Elf(elf.i + direction.move.first, elf.j + direction.move.second) } ?: elf
        }
        elf to newPosition
    }
    val moves = consideredMoves.values.groupBy { it }.mapValues { it.value.count() }

    return consideredMoves.map { (from, to) ->
        if (moves[to] == 1) {
            to
        } else {
            from
        }
    }.toSet()
}

private fun Set<Elf>.calculateAnswer(): Int {
    var minI = first().i
    var maxI = first().i
    var minJ = first().j
    var maxJ = first().j
    forEach { elf ->
        minI = min(elf.i, minI)
        maxI = max(elf.i, maxI)
        minJ = min(elf.j, minJ)
        maxJ = max(elf.j, maxJ)
    }
    return (maxI - minI + 1) * (maxJ - minJ + 1) - size
}

private data class Elf(
    val i: Int,
    val j: Int
) {

    fun neighbours(): Set<Elf> = (-1..1).flatMap { dI ->
        (-1..1).map { dJ ->
            Elf(i + dI, j + dJ)
        }
    }.toSet().minusElement(this)

}

private enum class Direction(
    val toCheck: List<Pair<Int, Int>>,
    val move: Pair<Int, Int>,
) {
    North(toCheck = listOf(-1 to -1, -1 to 0, -1 to 1), move = -1 to 0),
    South(toCheck = listOf(1 to -1, 1 to 0, 1 to 1), move = 1 to 0),
    West(toCheck = listOf(-1 to -1, 0 to -1, 1 to -1), move = 0 to -1),
    East(toCheck = listOf(-1 to 1, 0 to 1, 1 to 1), move = 0 to 1)
}
