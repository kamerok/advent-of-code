package year2022.day24

import readInput

fun main() {
    val testInput = readInput(2022, 24, "test").parse()
    val input = readInput(2022, 24).parse()

    check(part1(testInput) == 18)
    println(part1(input))

    check(part2(testInput) == 54)
    println(part2(input))
}

private fun part1(input: State): Int {
    val start = -1 to 0
    val finish = input.height to input.width - 1
    val (_, steps) = move(input, start, finish)
    return steps
}

private fun part2(input: State): Int {
    val start = -1 to 0
    val finish = input.height to input.width - 1
    val (firstState, firstPath) = move(input, start, finish)
    val (secondState, secondPath) = move(firstState, finish, start)
    val (_, thirdPath) = move(secondState, start, finish)
    return firstPath + secondPath + thirdPath
}

private fun move(
    input: State,
    startPosition: Pair<Int, Int>,
    finalPosition: Pair<Int, Int>
): Pair<State, Int> {
    var step = 1
    var currentState = input.advance()
    val pointsQueue = ArrayDeque(listOf(startPosition))

    while (!pointsQueue.contains(finalPosition)) {
        step++
//        println("$step $pointsQueue")
        val nextPoints = mutableSetOf<Pair<Int, Int>>()
        currentState = currentState.advance()
//        currentState.print()
        repeat(pointsQueue.size) {
            val point = pointsQueue.removeFirst()
            nextPoints.addAll(currentState.nextPoints(point, startPosition, finalPosition))
        }
        pointsQueue.addAll(nextPoints)
    }
    return currentState to step
}

private fun List<String>.parse(): State {
    val width = first().length - 2
    val height = size - 2
    val blizzards: List<Blizzard> = drop(1).dropLast(1).flatMapIndexed { i: Int, line: String ->
        line.drop(1).dropLast(1).flatMapIndexed { j, c ->
            if (c == '.') {
                emptyList()
            } else {
                val direction = when (c) {
                    '>' -> Direction.Right
                    '<' -> Direction.Left
                    'v' -> Direction.Down
                    '^' -> Direction.Up
                    else -> throw IllegalArgumentException("Unknown direction")
                }
                listOf(Blizzard(i, j, direction))
            }
        }
    }
    return State(blizzards, width, height)
}

private data class State(
    val blizzards: List<Blizzard>,
    val width: Int,
    val height: Int
) {
    private val occupied: Set<Pair<Int, Int>> by lazy { blizzards.map { it.i to it.j }.toSet() }

    fun print() {
        val blizzards = blizzards.groupBy { it.i to it.j }
        (0 until height).forEach { i ->
            (0 until width).forEach { j ->
                print(
                    if (blizzards.containsKey(i to j)) {
                        if (blizzards[i to j]!!.size > 1) {
                            blizzards[i to j]!!.size.toString()
                        } else {
                            blizzards[i to j]!!.first().asPrettyString()
                        }
                    } else {
                        "."
                    }
                )
            }
            println()
        }
    }

    fun advance(): State = copy(
        blizzards = blizzards.map { old ->
            when (old.direction) {
                Direction.Up -> old.copy(i = if (old.i - 1 >= 0) old.i - 1 else height - 1)
                Direction.Down -> old.copy(i = if (old.i + 1 < height) old.i + 1 else 0)
                Direction.Left -> old.copy(j = if (old.j - 1 >= 0) old.j - 1 else width - 1)
                Direction.Right -> old.copy(j = if (old.j + 1 < width) old.j + 1 else 0)
            }
        }
    )

    fun nextPoints(point: Pair<Int, Int>, start: Pair<Int, Int>, target: Pair<Int, Int>): List<Pair<Int, Int>> {
        return transform
            .map { (dI, dJ) -> point.first + dI to point.second + dJ }
            .filter { (i, j) ->
                (i to j == target || i to j == start) ||
                        (i in 0 until height && j in 0 until width && !occupied.contains(i to j))
            }
    }

    companion object {
        private val transform = listOf(
            -1 to 0,
            0 to 0,
            1 to 0,
            0 to -1,
            0 to 1
        )
    }
}

private data class Blizzard(
    val i: Int,
    val j: Int,
    val direction: Direction
) {

    fun asPrettyString(): String = when (direction) {
        Direction.Up -> "^"
        Direction.Down -> "v"
        Direction.Left -> "<"
        Direction.Right -> ">"
    }
}

private enum class Direction {
    Up, Down, Left, Right
}
