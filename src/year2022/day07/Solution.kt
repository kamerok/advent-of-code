package year2022.day07

import readInput
import kotlin.math.min

fun main() {
    val testInput = readInput(2022, 7, "test").parse()
    val input = readInput(2022, 7).parse()

    check(part1(testInput) == 95437L)
    println(part1(input))

    check(part2(testInput) == 24933642L)
    println(part2(input))
}

private fun part1(directory: Directory): Long {
    var sum = 0L
    if (directory.size <= 100_000) sum += directory.size
    directory.files.forEach { if (it is Directory) sum += part1(it) }
    return sum
}

private fun part2(directory: Directory): Long {
    val target = directory.size - 40_000_000
    return findClosest(directory, target)
}

private fun findClosest(directory: Directory, target: Long): Long {
    var closest = if (directory.size >= target) directory.size else Long.MAX_VALUE
    directory.files.forEach { node ->
        if (node is Directory) {
            val candidate = findClosest(node, target)
            closest = min(closest, candidate)
        }
    }
    return closest
}

private fun List<String>.parse(): Directory {
    return Directory("home").also { ArrayDeque(this.drop(1)).parse(it) }
}

private fun ArrayDeque<String>.parse(directory: Directory) {
    removeFirst()
    while (isNotEmpty()) {
        val nodeString = removeFirst().split(" ")
        if (nodeString.first() != "$") {
            directory.files.add(
                if (nodeString.first() == "dir") {
                    Directory(nodeString.last())
                } else {
                    File(nodeString.first().toLong(), nodeString.last())
                }
            )
        } else {
            if (nodeString.last() == "..") return
            if (nodeString[1] == "cd") {
                val dir = directory.files.first { it is Directory && it.name == nodeString.last() }
                parse(dir as Directory)
            }
        }
    }
}

private interface Node {
    val size: Long
    val name: String
}

private data class File(
    override val size: Long,
    override val name: String
) : Node

private data class Directory(
    override val name: String,
    val files: MutableList<Node> = mutableListOf()
) : Node {
    override val size: Long by lazy { files.sumOf { it.size } }
}
