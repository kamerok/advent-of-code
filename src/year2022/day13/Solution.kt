package year2022.day13

import readInput

fun main() {
    val testInput = readInput(2022, 13, "test").parse()
    val input = readInput(2022, 13).parse()

    check(part1(testInput) == 13)
    println(part1(input))

    check(part2(testInput) == 140)
    println(part2(input))
}

private fun part1(input: List<Node>): Int {
    var sum = 0
    input
        .chunked(2)
        .forEachIndexed { index, (left, right) ->
            if (left compareTo right == CheckResult.Right) sum += index + 1
        }
    return sum
}

private fun part2(input: List<Node>): Int {
    val dividers = listOf(
        "[[2]]",
        "[[6]]"
    ).map { it.parse() }
    val organized = (input + dividers).sortedWith { left, right ->
        when (CheckResult.Right) {
            left compareTo right -> -1
            right compareTo left -> 1
            else -> 0
        }
    }

    return (organized.indexOf(dividers[0]) + 1) * (organized.indexOf(dividers[1]) + 1)
}

private fun List<String>.parse(): List<Node> = filter { it.isNotEmpty() }
    .map { it.parse() }

private fun String.parse(): Node = ArrayDeque(toList()).parse()

private fun ArrayDeque<Char>.parse(): Node {
    return if (first() == '[') {
        parseList()
    } else {
        parseCell()
    }
}

private fun ArrayDeque<Char>.parseList(): ListNode {
    val list = mutableListOf<Node>()
    //remove [
    removeFirst()
    while (first() != ']') {
        list.add(parse())
        if (first() == ',') removeFirst()
    }
    //remove ]
    removeFirst()
    return ListNode(list)
}

private fun ArrayDeque<Char>.parseCell(): CellNode {
    var raw = ""
    while (first().isDigit()) raw += removeFirst()
    return CellNode(raw.toInt())
}

private infix fun Node.compareTo(right: Node): CheckResult {
    return when {
        this is CellNode && right is CellNode -> this compareTo right
        this is ListNode && right is ListNode -> this compareTo right
        else -> this.wrapIfCell() compareTo right.wrapIfCell()
    }
}

private infix fun CellNode.compareTo(right: CellNode): CheckResult {
    return when {
        this.value < right.value -> CheckResult.Right
        this.value > right.value -> CheckResult.Wrong
        else -> CheckResult.Inconclusive
    }
}

private infix fun ListNode.compareTo(right: ListNode): CheckResult {
    list.indices.forEach { leftIndex ->
        if (leftIndex > right.list.lastIndex) return CheckResult.Wrong
        val check = list[leftIndex] compareTo right.list[leftIndex]
        if (check != CheckResult.Inconclusive) return check
    }
    if (list.size < right.list.size) return CheckResult.Right
    return CheckResult.Inconclusive
}

private fun Node.wrapIfCell(): Node = if (this is CellNode) {
    ListNode(listOf(this))
} else {
    this
}

private sealed class Node

private data class ListNode(
    val list: List<Node>
) : Node()

private data class CellNode(
    val value: Int
) : Node()

private enum class CheckResult {
    Right, Wrong, Inconclusive
}
