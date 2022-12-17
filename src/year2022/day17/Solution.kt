package year2022.day17

import readInput

fun main() {
    val testInput = readInput(2022, 17, "test").first()
    val input = readInput(2022, 17).first()

    check(part1(testInput) == 3068)
    println(part1(input))

    check(part2(testInput) == 1_514_285_714_288L)
    println(part2(input))
}

private fun part1(input: String): Int {
    val game = Game(input)
    repeat(2022) {
        game.placeRock()
    }
    return game.getHeight()
}

private fun part2(input: String): Long {
    val diffList = mutableListOf<Int>()
    val game = Game(input)
    var oldHeight = 0
    repeat(100000) {
        game.placeRock()
        diffList.add(game.getHeight() - oldHeight)
        oldHeight = game.getHeight()
    }

    val patternRange = findPattern(diffList.joinToString(""))

    val iterations = 1000000000000L
    val pattern = diffList.subList(patternRange.first, patternRange.last)
    val tail = diffList.take(patternRange.first)
    val headSize = (iterations - tail.size) % pattern.size
    val head = pattern.take(headSize.toInt())
    val patternCount = (iterations - tail.size) / pattern.size

    return tail.sum().toLong() + pattern.sum().toLong() * patternCount + head.sum()
}

private fun findPattern(data: String): IntRange {
    for (i in data.indices) {
        val pattern = data.substring(i, i + 20).toRegex()
        if (pattern.findAll(data).count() > 10) {
            val nextRange = pattern.find(data, i + 1)!!.range
            return i .. nextRange.first
        }
    }
    return 0..0
}

private fun MutableList<BooleanArray>.findTopIndex(): Int =
    indexOfLast { it.contains(true) }

private fun MutableList<BooleanArray>.expandUntil(yIndex: Int) {
    if (lastIndex < yIndex) {
        repeat(yIndex - lastIndex) {
            add(BooleanArray(7))
        }
    }
}

private class Game(
    private val jetInstruction: String
) {
    val field = mutableListOf<BooleanArray>()

    private var jetIndex = 0
    private var figureIndex = 0

    fun placeRock() {
        val figure = spawnFigure()

        do {
            figure.move(field, nextJet())
        } while (figure.move(field, Direction.Down))

        figure.place(field)
    }

    fun getHeight(): Int {
        return field.findTopIndex() + 1
    }

    private fun nextJet(): Direction {
        val instruction = if (jetInstruction[jetIndex] == '<') {
            Direction.Left
        } else {
            Direction.Right
        }
        jetIndex++
        if (jetIndex > jetInstruction.lastIndex) jetIndex = 0
        return instruction
    }

    private fun spawnFigure(): Figure {
        val figure = when (figureIndex) {
            0 -> Minus(field)
            1 -> Plus(field)
            2 -> Arrow(field)
            3 -> Column(field)
            4 -> Box(field)
            else -> throw IllegalArgumentException("Unknown index $figureIndex")
        }

        figureIndex++
        if (figureIndex > 4) figureIndex = 0

        return figure
    }

    override fun toString(): String {
        return field.toPrettyString()
    }
}

private fun Iterable<BooleanArray>.toPrettyString() =
    buildString {
        this@toPrettyString.reversed().forEach { array ->
            append('|')
            append(array.joinToString("") { if (it) "#" else "." })
            append('|')
            appendLine()
        }
        appendLine("+-------+")
    }

private abstract class Figure {
    protected abstract var x: Int
    protected abstract var y: Int

    fun move(field: MutableList<BooleanArray>, direction: Direction): Boolean {
        return when (direction) {
            Direction.Left -> if (canMoveLeft(field)) {
                x--
                true
            } else {
                false
            }

            Direction.Right -> if (canMoveRight(field)) {
                x++
                true
            } else {
                false
            }

            Direction.Down -> if (canMoveDown(field)) {
                y--
                true
            } else {
                false
            }
        }
    }

    private fun canMoveLeft(field: MutableList<BooleanArray>): Boolean {
        return getLeftPoints().all { (x, y) ->
            x >= 0 && !field[y][x]
        }
    }

    private fun canMoveRight(field: MutableList<BooleanArray>): Boolean {
        return getRightPoints().all { (x, y) ->
            x <= 6 && !field[y][x]
        }
    }

    private fun canMoveDown(field: MutableList<BooleanArray>): Boolean {
        return getDownPoints().all { (x, y) ->
            y >= 0 && !field[y][x]
        }
    }

    protected abstract fun getCurrentPoints(): List<Pair<Int, Int>>
    protected abstract fun getLeftPoints(): List<Pair<Int, Int>>
    protected abstract fun getRightPoints(): List<Pair<Int, Int>>
    protected abstract fun getDownPoints(): List<Pair<Int, Int>>

    fun place(field: MutableList<BooleanArray>) {
        getCurrentPoints()
            .forEach { (x, y) ->
                field[y][x] = true
            }
    }
}

private class Minus(field: MutableList<BooleanArray>) : Figure() {

    override var y = field.findTopIndex() + 4
    override var x = 2

    init {
        field.expandUntil(y)
    }

    override fun getCurrentPoints(): List<Pair<Int, Int>> = listOf(
        x to y,
        x + 1 to y,
        x + 2 to y,
        x + 3 to y,
    )

    override fun getLeftPoints(): List<Pair<Int, Int>> = listOf(
        x - 1 to y
    )

    override fun getRightPoints(): List<Pair<Int, Int>> = listOf(
        x + 4 to y
    )

    override fun getDownPoints(): List<Pair<Int, Int>> = listOf(
        x to y - 1,
        x + 1 to y - 1,
        x + 2 to y - 1,
        x + 3 to y - 1,
    )
}

private class Plus(field: MutableList<BooleanArray>) : Figure() {
    override var x = 3
    override var y = field.findTopIndex() + 5

    init {
        field.expandUntil(y + 1)
    }

    override fun getCurrentPoints(): List<Pair<Int, Int>> = listOf(
        x to y,
        x to y - 1,
        x to y + 1,
        x - 1 to y,
        x + 1 to y
    )

    override fun getLeftPoints(): List<Pair<Int, Int>> = listOf(
        x - 1 to y - 1,
        x - 2 to y,
        x - 1 to y + 1,
    )

    override fun getRightPoints(): List<Pair<Int, Int>> = listOf(
        x + 1 to y - 1,
        x + 2 to y,
        x + 1 to y + 1,
    )

    override fun getDownPoints(): List<Pair<Int, Int>> = listOf(
        x - 1 to y - 1,
        x to y - 2,
        x + 1 to y - 1,
    )
}

private class Arrow(field: MutableList<BooleanArray>) : Figure() {
    override var x = 4
    override var y = field.findTopIndex() + 4

    init {
        field.expandUntil(y + 2)
    }

    override fun getCurrentPoints(): List<Pair<Int, Int>> = listOf(
        x - 2 to y,
        x - 1 to y,
        x to y,
        x to y + 1,
        x to y + 2
    )

    override fun getLeftPoints(): List<Pair<Int, Int>> = listOf(
        x - 3 to y,
        x - 1 to y + 1,
        x - 1 to y + 2
    )

    override fun getRightPoints(): List<Pair<Int, Int>> = listOf(
        x + 1 to y,
        x + 1 to y + 1,
        x + 1 to y + 2
    )

    override fun getDownPoints(): List<Pair<Int, Int>> = listOf(
        x - 2 to y - 1,
        x - 1 to y - 1,
        x to y - 1
    )
}

private class Column(field: MutableList<BooleanArray>) : Figure() {
    override var x: Int = 2
    override var y: Int = field.findTopIndex() + 4

    init {
        field.expandUntil(y + 3)
    }

    override fun getCurrentPoints(): List<Pair<Int, Int>> = listOf(
        x to y,
        x to y + 1,
        x to y + 2,
        x to y + 3,
    )

    override fun getLeftPoints(): List<Pair<Int, Int>> = listOf(
        x - 1 to y,
        x - 1 to y + 1,
        x - 1 to y + 2,
        x - 1 to y + 3,
    )

    override fun getRightPoints(): List<Pair<Int, Int>> = listOf(
        x + 1 to y,
        x + 1 to y + 1,
        x + 1 to y + 2,
        x + 1 to y + 3,
    )

    override fun getDownPoints(): List<Pair<Int, Int>> = listOf(
        x to y - 1
    )
}

private class Box(field: MutableList<BooleanArray>) : Figure() {
    override var x: Int = 2
    override var y: Int = field.findTopIndex() + 4

    init {
        field.expandUntil(y + 1)
    }

    override fun getCurrentPoints(): List<Pair<Int, Int>> = listOf(
        x to y,
        x + 1 to y,
        x to y + 1,
        x + 1 to y + 1,
    )

    override fun getLeftPoints(): List<Pair<Int, Int>> = listOf(
        x - 1 to y,
        x - 1 to y + 1,
    )

    override fun getRightPoints(): List<Pair<Int, Int>> = listOf(
        x + 2 to y,
        x + 2 to y + 1,
    )

    override fun getDownPoints(): List<Pair<Int, Int>> = listOf(
        x to y - 1,
        x + 1 to y - 1,
    )
}

private enum class Direction {
    Left, Right, Down
}
