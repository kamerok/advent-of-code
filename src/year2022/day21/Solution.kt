package year2022.day21

import readInput

fun main() {
    val testInput = readInput(2022, 21, "test").parse()
    val input = readInput(2022, 21).parse()

    check(part1(testInput) == 152L)
    println(part1(input))

    check(part2(testInput) == 301L)
    println(part2(input))
}

private fun part1(input: List<Monkey>): Long {
    return input.find { it.id == "root" }!!.resolve()
}

private fun part2(input: List<Monkey>): Long {
    val root = (input.find { it.id == "root" } as OperationMonkey)
    return if (root.right.containsHuman()) {
        root.right.findToMatch(root.left.resolve())
    } else {
        root.left.findToMatch(root.right.resolve())
    }
}

private fun List<String>.parse(): List<Monkey> {
    val map = associateBy { it.substringBefore(":") }
    return map { it.parseMonkey(map) }
}

private fun String.parseMonkey(map: Map<String, String>): Monkey {
    val words = split(" ")

    return if (words.size == 2) {
        NumberMonkey(
            id = words[0].dropLast(1),
            number = words[1].toInt()
        )
    } else {
        OperationMonkey(
            id = words[0].dropLast(1),
            operation = when (words[2]) {
                "+" -> Operation.Plus
                "-" -> Operation.Minus
                "*" -> Operation.Multiply
                "/" -> Operation.Divide
                else -> throw IllegalArgumentException("Unsupported operation")
            },
            left = map[words[1]]!!.parseMonkey(map),
            right = map[words[3]]!!.parseMonkey(map)
        )
    }
}

private sealed class Monkey {
    abstract val id: String

    abstract fun resolve(context: MutableMap<String, Long> = mutableMapOf()): Long

    abstract fun containsHuman(): Boolean

    abstract fun findToMatch(number: Long): Long
}

private data class NumberMonkey(
    override val id: String,
    val number: Int
) : Monkey() {
    override fun resolve(context: MutableMap<String, Long>): Long {
        return number.toLong().also {
            context[id] = it
        }
    }

    override fun containsHuman(): Boolean = id == "humn"

    override fun findToMatch(number: Long): Long = if (id == "humn") number else -1

}

private data class OperationMonkey(
    override val id: String,
    val operation: Operation,
    val left: Monkey,
    val right: Monkey
) : Monkey() {

    override fun resolve(context: MutableMap<String, Long>): Long {
        context[id]?.let { return it }
        val leftValue = left.resolve(context)
        val rightValue = right.resolve(context)
        return calculate(leftValue, rightValue, operation).also {
            context[id] = it
        }
    }

    override fun containsHuman(): Boolean = left.containsHuman() || right.containsHuman()

    override fun findToMatch(number: Long): Long {
        return if (left.containsHuman()) {
            matchInLeft(number)
        } else {
            matchInRight(number)
        }
    }

    private fun matchInLeft(number: Long): Long {
        val right = right.resolve()
        val numberToMatch = when (operation) {
            Operation.Plus -> number - right
            Operation.Minus -> number + right
            Operation.Multiply -> number / right
            Operation.Divide -> number * right
        }
        return left.findToMatch(numberToMatch)
    }

    private fun matchInRight(number: Long): Long {
        val left = left.resolve()
        val numberToMatch = when (operation) {
            Operation.Plus -> number - left
            Operation.Minus -> left - number
            Operation.Multiply -> number / left
            Operation.Divide -> number / left
        }
        return right.findToMatch(numberToMatch)
    }

    private fun calculate(leftValue: Long, rightValue: Long, operation: Operation): Long {
        return when (operation) {
            Operation.Plus -> leftValue + rightValue
            Operation.Minus -> leftValue - rightValue
            Operation.Multiply -> leftValue * rightValue
            Operation.Divide -> leftValue / rightValue
        }
    }
}

private enum class Operation {
    Plus, Minus, Multiply, Divide
}
