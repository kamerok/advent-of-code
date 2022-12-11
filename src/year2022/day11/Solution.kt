package year2022.day11

import readInput

fun main() {
    val testInput = readInput(2022, 11, "test").parse()
    val input = readInput(2022, 11).parse()

    check(part1(testInput) == 10605)
    println(part1(input))

    check(part2(testInput) == 2713310158)
    println(part2(input))
}

private fun part1(monkeys: List<Monkey>): Int {
    val items = Array(monkeys.size) { i -> monkeys[i].items.toMutableList() }
    val inspected = IntArray(monkeys.size)
    repeat(20) {
        monkeys.forEach { monkey ->
            items[monkey.id].forEach { item ->
                inspected[monkey.id]++
                val lvl = item.apply(monkey.operation) / 3
                if (lvl % monkey.testDivider == 0) {
                    items[monkey.positiveMonkey].add(lvl)
                } else {
                    items[monkey.negativeMonkey].add(lvl)
                }
            }
            items[monkey.id].clear()
        }
    }
    inspected.sort()
    return inspected.last() * inspected[inspected.lastIndex - 1]
}

private fun part2(monkeys: List<Monkey>): Long {
    val common = monkeys.fold(1) { acc, monkey -> acc * monkey.testDivider }
    val items = Array(monkeys.size) { i ->
        monkeys[i].items.map { it.toLong() }.toMutableList()
    }
    val inspected = IntArray(monkeys.size)
    repeat(10000) {
        monkeys.forEach { monkey ->
            items[monkey.id].forEach { item ->
                inspected[monkey.id]++
                val lvl = item.apply(monkey.operation) % common
                if (lvl % monkey.testDivider == 0L) {
                    items[monkey.positiveMonkey].add(lvl)
                } else {
                    items[monkey.negativeMonkey].add(lvl)
                }
            }
            items[monkey.id].clear()
        }
    }
    inspected.sort()
    return inspected.last().toLong() * inspected[inspected.lastIndex - 1]
}

private fun List<String>.parse(): List<Monkey> {
    return chunked(7).map { monkeyData ->
        Monkey(
            id = monkeyData[0].split(" ").last().dropLast(1).toInt(),
            items = monkeyData[1].substringAfter(": ").split(", ").map { it.toInt() },
            operation = monkeyData[2].let { line ->
                val words = line.split(" ")
                when {
                    words.last() == "old" -> Square
                    words[words.lastIndex - 1] == "*" -> {
                        Multiply(words.last().toInt())
                    }

                    else -> {
                        Add(words.last().toInt())
                    }
                }
            },
            testDivider = monkeyData[3].split(" ").last().toInt(),
            positiveMonkey = monkeyData[4].split(" ").last().toInt(),
            negativeMonkey = monkeyData[5].split(" ").last().toInt()
        )
    }
}

private fun Int.apply(operation: Operation): Int =
    when (operation) {
        is Add -> this + operation.addition
        is Multiply -> this * operation.multiplier
        is Square -> this * this
    }

private fun Long.apply(operation: Operation): Long =
    when (operation) {
        is Add -> this + operation.addition
        is Multiply -> this * operation.multiplier
        is Square -> this * this
    }

private data class Monkey(
    val id: Int,
    val items: List<Int>,
    val operation: Operation,
    val testDivider: Int,
    val positiveMonkey: Int,
    val negativeMonkey: Int
)

private sealed class Operation

private data class Multiply(
    val multiplier: Int
) : Operation()

private data class Add(
    val addition: Int
) : Operation()

private object Square : Operation()
