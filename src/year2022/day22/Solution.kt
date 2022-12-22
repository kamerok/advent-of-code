package year2022.day22

import readInput

private typealias Field = Array<Array<Cell>>

fun main() {
    val (testField, testInstructions) = readInput(2022, 22, "test").parse()
    val (field, instructions) = readInput(2022, 22).parse()

    val flatDevice = FlatDevice(testField)
    check(flatDevice.nextPosition(Position(6, 11, Direction.Right)) == Position(6, 0, Direction.Right))
    check(flatDevice.nextPosition(Position(7, 5, Direction.Down)) == Position(4, 5, Direction.Down))
    check(part1(testField, testInstructions) == 6032)
    println(part1(field, instructions))

    val testDevice = CubeDevice(testField) { buildSampleMappings(it) }
    check(testDevice.nextPosition(Position(5, 11, Direction.Right)) == Position(8, 14, Direction.Down))
    check(testDevice.nextPosition(Position(11, 10, Direction.Down)) == Position(7, 1, Direction.Up))
    check(part2(testDevice, testInstructions) == 5031)
    println(part2(CubeDevice(field) { buildRealMappings(50) }, instructions))
}

private fun part1(field: Field, instructions: List<Instruction>): Int {
    return solve(FlatDevice(field), instructions)
}

private fun part2(device: Device, instructions: List<Instruction>): Int {
    return solve(device, instructions)
}

private fun solve(
    device: Device,
    instructions: List<Instruction>
): Int {
    val (iStart, jStart) = device.field.findFirstPosition()
    var position = Position(iStart, jStart, Direction.Right)
    instructions.forEach {
        position = position.perform(it, device)
    }
    return position.calculatePoints()
}

private fun List<String>.parse(): Pair<Field, List<Instruction>> {
    val rawField = dropLast(2)
    val width = rawField.maxOf { it.length }
    val field = Array(rawField.size) { i ->
        Array(width) { j ->
            when (rawField.getOrNull(i)?.getOrNull(j)) {
                '#' -> Cell.Wall
                '.' -> Cell.Empty
                else -> Cell.Skip
            }
        }
    }

    val instructions = mutableListOf<Instruction>()
    val queue = ArrayDeque(last().toList())
    while (queue.isNotEmpty()) {
        if (!queue.first().isDigit()) {
            instructions.add(
                when (queue.removeFirst()) {
                    'R' -> Turn(Rotation.Right)
                    else -> Turn(Rotation.Left)
                }
            )
        } else {
            var number = ""
            while (queue.isNotEmpty() && queue.first().isDigit()) {
                number += queue.removeFirst()
            }
            instructions.add(Move(number.toInt()))
        }
    }
    return field to instructions
}

private fun Device.print(position: Position? = null) {
    println(buildString {
        field.forEachIndexed { i, row ->
            row.forEachIndexed { j, cell ->
                append(
                    when {
                        i == position?.i && j == position.j -> "*"
                        cell == Cell.Skip -> " "
                        cell == Cell.Empty -> "."
                        cell == Cell.Wall -> "#"
                        else -> throw IllegalArgumentException("Unknown cell")
                    }
                )
            }
            appendLine()
        }
    })
}

private fun Field.findFirstPosition(): Point {
    forEachIndexed { i, row ->
        row.forEachIndexed { j, cell ->
            if (cell == Cell.Empty) return Point(i, j)
        }
    }
    return Point(-1, -1)
}

private operator fun Device.get(position: Position) = this.field[position.i][position.j]

private fun Device.getOrNull(position: Position) = this.field.getOrNull(position.i)?.getOrNull(position.j)

private fun Position.calculatePoints(): Int {
    val directionPoints = when (direction) {
        Direction.Left -> 2
        Direction.Right -> 0
        Direction.Up -> 3
        Direction.Down -> 1
    }
    return 1000 * (i + 1) + 4 * (j + 1) + directionPoints
}

private enum class Cell {
    Skip, Empty, Wall
}

private sealed class Instruction

private data class Move(
    val steps: Int
) : Instruction()

private data class Turn(
    val rotation: Rotation
) : Instruction()

private enum class Rotation {
    Left, Right
}

private enum class Direction {
    Left, Right, Up, Down
}

private interface Device {
    val field: Field

    fun adjustBorders(position: Position): Position

    fun nextPosition(position: Position): Position {
        var nextPoint = adjustBorders(position.freeMove())
        while (this[nextPoint] == Cell.Skip) {
            nextPoint = adjustBorders(nextPoint.freeMove())
        }
        return nextPoint
    }

    private fun Position.freeMove(): Position {
        val (dX, dY) = moves.getValue(direction)
        return copy(i = i + dX, j = j + dY)
    }

    companion object {
        private val moves = mapOf(
            Direction.Down to (1 to 0),
            Direction.Up to (-1 to 0),
            Direction.Left to (0 to -1),
            Direction.Right to (0 to 1),
        )
    }
}

private class FlatDevice(
    override val field: Field
) : Device {
    override fun adjustBorders(position: Position): Position {
        val i = when {
            position.i < 0 -> field.lastIndex
            position.i > field.lastIndex -> 0
            else -> position.i
        }
        val j = when {
            position.j < 0 -> field.first().lastIndex
            position.j > field.first().lastIndex -> 0
            else -> position.j
        }
        return position.copy(i = i, j = j)
    }
}

private fun buildSampleMappings(sideSize: Int) = listOf(
    Position(
        i = -1,
        j = sideSize * 2,
        Direction.Up
    ) to Position(
        sideSize,
        sideSize - 1,
        Direction.Down
    ),
    Position(
        i = 0,
        j = sideSize * 3,
        Direction.Right
    ) to Position(
        sideSize * 3,
        sideSize * 4 - 1,
        Direction.Left
    ),
    Position(
        i = sideSize,
        j = sideSize * 3,
        Direction.Right
    ) to Position(
        sideSize * 2,
        sideSize * 4 - 1,
        Direction.Down
    ),
    Position(
        i = sideSize * 2 - 1,
        j = sideSize * 3,
        Direction.Up
    ) to Position(
        sideSize * 2 - 1,
        sideSize * 3 - 1,
        Direction.Left
    ),
    Position(
        i = sideSize * 2,
        j = sideSize * 4,
        Direction.Right
    ) to Position(
        sideSize - 1,
        sideSize * 3 - 1,
        Direction.Left
    ),
    Position(
        i = sideSize * 3,
        j = sideSize * 4 - 1,
        Direction.Down
    ) to Position(
        sideSize,
        0,
        Direction.Right
    ),
    Position(
        i = sideSize * 3,
        j = sideSize * 3 - 1,
        Direction.Down
    ) to Position(
        sideSize * 2 - 1,
        0,
        Direction.Up
    ),
    Position(
        i = sideSize * 3 - 1,
        j = sideSize * 2 - 1,
        Direction.Left
    ) to Position(
        sideSize * 2 - 1,
        sideSize,
        Direction.Up
    ),
    Position(
        i = sideSize * 2,
        j = sideSize * 2 - 1,
        Direction.Down
    ) to Position(
        sideSize * 2,
        sideSize * 2,
        Direction.Right
    ),
    Position(
        i = sideSize * 2,
        j = sideSize - 1,
        Direction.Down
    ) to Position(
        sideSize * 3 - 1,
        sideSize * 2,
        Direction.Up
    ),
    Position(
        i = sideSize * 2 - 1,
        j = -1,
        Direction.Left
    ) to Position(
        sideSize * 3 - 1,
        sideSize * 3,
        Direction.Up
    ),
    Position(
        i = sideSize - 1,
        j = 0,
        Direction.Up
    ) to Position(
        0,
        sideSize * 3 - 1,
        Direction.Down
    ),
    Position(
        i = sideSize - 1,
        j = sideSize,
        Direction.Up
    ) to Position(
        0,
        sideSize * 2,
        Direction.Right
    ),
    Position(
        i = sideSize - 1,
        j = sideSize * 2 - 1,
        Direction.Left
    ) to Position(
        sideSize,
        sideSize * 2 - 1,
        Direction.Down
    ),
).map { (fromStart, toStart) ->
    val fromList = (0..sideSize).map { generateSidePosition(fromStart, it) }
    val toList = (0..sideSize).map { generateSidePosition(toStart, it) }
    fromList to toList
}

private fun buildRealMappings(sideSize: Int) = listOf(
    //1
    Position(
        i = -1,
        j = sideSize,
        Direction.Up
    ) to Position(
        sideSize * 3,
        0,
        Direction.Right
    ),

    //2
    Position(
        i = -1,
        j = sideSize * 2,
        Direction.Up
    ) to Position(
        sideSize * 4 - 1,
        0,
        Direction.Up
    ),

    //3
    Position(
        i = 0,
        j = sideSize * 3,
        Direction.Right
    ) to Position(
        sideSize * 3 - 1,
        sideSize * 2 - 1,
        Direction.Left
    ),

    //4
    Position(
        i = sideSize,
        j = sideSize * 3 - 1,
        Direction.Down
    ) to Position(
        sideSize * 2 - 1,
        sideSize * 2 - 1,
        Direction.Left
    ),

    //5
    Position(
        i = sideSize,
        j = sideSize * 2,
        Direction.Right
    ) to Position(
        sideSize - 1,
        sideSize * 2,
        Direction.Up
    ),

    //6
    Position(
        i = sideSize * 2,
        j = sideSize * 2,
        Direction.Right
    ) to Position(
        sideSize - 1,
        sideSize * 3 - 1,
        Direction.Left
    ),

    //7
    Position(
        i = sideSize * 3,
        j = sideSize * 2 - 1,
        Direction.Down
    ) to Position(
        sideSize * 4 - 1,
        sideSize - 1,
        Direction.Left
    ),

    //8
    Position(
        i = sideSize * 3,
        j = sideSize,
        Direction.Right
    ) to Position(
        sideSize * 3 - 1,
        sideSize,
        Direction.Up
    ),

    //9
    Position(
        i = sideSize * 4,
        j = sideSize - 1,
        Direction.Down
    ) to Position(
        0,
        sideSize * 3 - 1,
        Direction.Down
    ),

    //10
    Position(
        i = sideSize * 4 - 1,
        j = -1,
        Direction.Left
    ) to Position(
        0,
        sideSize * 2 - 1,
        Direction.Down
    ),

    //11
    Position(
        i = sideSize * 3 - 1,
        j = -1,
        Direction.Left
    ) to Position(
        0,
        sideSize,
        Direction.Right
    ),

    //12
    Position(
        i = sideSize * 2 - 1,
        j = 0,
        Direction.Up
    ) to Position(
        sideSize,
        sideSize,
        Direction.Right
    ),

    //13
    Position(
        i = sideSize * 2 - 1,
        j = sideSize - 1,
        Direction.Left
    ) to Position(
        sideSize * 2,
        sideSize - 1,
        Direction.Down
    ),

    //14
    Position(
        i = sideSize - 1,
        j = sideSize - 1,
        Direction.Left
    ) to Position(
        sideSize * 2,
        0,
        Direction.Right
    ),
).map { (fromStart, toStart) ->
    val fromList = (0..sideSize).map { generateSidePosition(fromStart, it) }
    val toList = (0..sideSize).map { generateSidePosition(toStart, it) }
    fromList to toList
}

private fun generateSidePosition(sideStart: Position, index: Int): Position {
    val (dI, dJ) = startChange.getValue(sideStart.direction)
    return sideStart.copy(i = sideStart.i + dI * index, j = sideStart.j + dJ * index)
}

private val startChange = mapOf(
    Direction.Up to (0 to 1),
    Direction.Down to (0 to -1),
    Direction.Left to (-1 to 0),
    Direction.Right to (1 to 0)
)

private class CubeDevice(
    override val field: Field,
    buildMapping: (Int) -> List<Pair<List<Position>, List<Position>>>
) : Device {
    private val sideSize = field.first().count { it != Cell.Skip }

    val sideMappings = buildMapping(sideSize)

    override fun adjustBorders(position: Position): Position {
        val cell = getOrNull(position)
        if (cell == null || cell == Cell.Skip) {
            sideMappings.forEach { (fromSide, toSide) ->
                val index = fromSide.indexOf(position)
                if (index != -1) return toSide[index]
            }
        }
        return position
    }
}

private data class Position(
    val i: Int,
    val j: Int,
    val direction: Direction
) {

    fun perform(instruction: Instruction, device: Device): Position {
        return when (instruction) {
            is Turn -> performTurn(instruction)
            is Move -> performMove(instruction, device)
        }
    }

    private fun performTurn(turn: Turn): Position {
        val newDirection: Direction = if (turn.rotation == Rotation.Right) {
            when (direction) {
                Direction.Left -> Direction.Up
                Direction.Right -> Direction.Down
                Direction.Up -> Direction.Right
                Direction.Down -> Direction.Left
            }
        } else {
            when (direction) {
                Direction.Left -> Direction.Down
                Direction.Right -> Direction.Up
                Direction.Up -> Direction.Left
                Direction.Down -> Direction.Right
            }
        }
        return copy(direction = newDirection)
    }

    private fun performMove(move: Move, device: Device): Position {
        var position = this
        repeat(move.steps) {
            val next = device.nextPosition(position)
            if (device[next] == Cell.Empty) {
                position = next
            }
        }
        return position
    }

}

private data class Point(
    val i: Int,
    val j: Int
)


