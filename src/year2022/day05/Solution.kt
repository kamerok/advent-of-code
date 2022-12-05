package year2022.day05

import readInput

fun main() {
    val testInput = readInput(2022, 5, "test")
    val input = readInput(2022, 5)

    check(part1(testInput) == "CMZ")
    println(part1(input))

    check(part2(testInput) == "MCD")
    println(part2(input))
}

private fun part1(input: List<String>): String {
    val (crates, operations) = input.parse()
    operations.forEach { operation ->
        repeat(operation.count) {
            crates.stacks[operation.to].addLast(crates.stacks[operation.from].removeLast())
        }
    }
    return crates.stacks.map { it.last() }.joinToString(separator = "")
}

private fun part2(input: List<String>): String {
    val (crates, operations) = input.parse()
    operations.forEach { operation ->
        val buffer = Array(operation.count) { crates.stacks[operation.from].removeLast() }
        buffer.reverse()
        buffer.forEach { c ->
            crates.stacks[operation.to].addLast(c)
        }
    }
    return crates.stacks.map { it.last() }.joinToString(separator = "")
}

private fun List<String>.parse(): Pair<Crates, List<Operation>> {
    val cratesRaw = takeWhile { it.isNotEmpty() }
    val stacksCount = cratesRaw.last().split(" ").takeLast(2).first().toInt()
    val crates = Array<ArrayDeque<Char>>(stacksCount) { ArrayDeque() }
    for (row in cratesRaw.lastIndex - 1 downTo 0) {
        repeat(stacksCount) { stackIndex ->
            val element = cratesRaw[row][stackIndex + 1 + stackIndex * 3]
            if (!element.isWhitespace()) {
                crates[stackIndex].addLast(element)
            }
        }
    }
    val operation = drop(cratesRaw.size + 1).map { line ->
        val words = line.split(" ")
        Operation(
            from = words[3].toInt() - 1,
            to = words[5].toInt() - 1,
            count = words[1].toInt()
        )
    }
    return Crates(crates) to operation
}

private data class Crates(
    val stacks: Array<ArrayDeque<Char>>
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Crates

        if (!stacks.contentEquals(other.stacks)) return false

        return true
    }

    override fun hashCode(): Int {
        return stacks.contentHashCode()
    }
}

private data class Operation(
    val from: Int,
    val to: Int,
    val count: Int
)
