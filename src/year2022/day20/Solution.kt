package year2022.day20

import readInput
import kotlin.math.absoluteValue

fun main() {
    val testInput = readInput(2022, 20, "test").parse()
    val input = readInput(2022, 20).parse()

    check(part1(testInput) == 3L)
    println(part1(input))

    check(part2(testInput) == 1623178306L)
    println(part2(input))
}

private fun part1(input: List<Node>): Long {
    return solve(input)
}

private fun solve(input: List<Node>, repeat: Int = 1): Long {
    val nodes = input.map { CircularNode(it.index, it.value) }

    nodes.first().previous = nodes.last()
    (1..nodes.lastIndex).forEach {
        nodes[it].previous = nodes[it - 1]
    }
    nodes.last().next = nodes.first()
    (0 until nodes.lastIndex).forEach {
        nodes[it].next = nodes[it + 1]
    }

    fun CircularNode.move() {
        previous!!.next = next
        next!!.previous = previous
        var newPrevious = previous
        if (value > 0) {
            repeat((value % (input.size - 1)).toInt()) {
                newPrevious = newPrevious!!.next
            }
        } else {
            repeat((value.absoluteValue % (input.size - 1)).toInt()) {
                newPrevious = newPrevious!!.previous
            }
        }
        next = newPrevious!!.next
        previous = newPrevious
        previous!!.next = this
        next!!.previous = this
    }

    repeat(repeat) {
        nodes.forEach { it.move() }
    }

    val zeroNode = nodes.find { it.value == 0L }!!
    return listOf(1000L, 2000L, 3000L).sumOf {
        val iterations = it % nodes.size
        var currentNode: CircularNode = zeroNode
        repeat(iterations.toInt()) {
            currentNode = currentNode.next!!
        }
        currentNode.value
    }
}

private fun part2(input: List<Node>): Long {
    return solve(input.map { it.copy(value = it.value * 811589153) }, 10)
}

private fun List<String>.parse(): List<Node> = map { it.toLong() }.toNodeList()

private fun List<Long>.toNodeList() = mapIndexed { i, value -> Node(i, value) }

private data class Node(
    val index: Int,
    val value: Long
)

private data class CircularNode(
    val index: Int,
    val value: Long,
    var next: CircularNode? = null,
    var previous: CircularNode? = null
)
