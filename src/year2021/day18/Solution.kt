package year2021.day18

import readInput
import kotlin.math.ceil
import kotlin.math.max

fun main() {
    check(Leaf(10).split() == Parent(Leaf(5), Leaf(5)))
    check(Leaf(11).split() == Parent(Leaf(5), Leaf(6)))

    mapOf(
        "[[[[[9,8],1],2],3],4]" to "[[[[0,9],2],3],4]",
        "[7,[6,[5,[4,[3,2]]]]]" to "[7,[6,[5,[7,0]]]]",
        "[[6,[5,[4,[3,2]]]],1]" to "[[6,[5,[7,0]]],3]",
        "[[3,[2,[1,[7,3]]]],[6,[5,[4,[3,2]]]]]" to "[[3,[2,[8,0]]],[9,[5,[4,[3,2]]]]]",
        "[[3,[2,[8,0]]],[9,[5,[4,[3,2]]]]]" to "[[3,[2,[8,0]]],[9,[5,[7,0]]]]",
        "[[[[0,7],4],[7,[[8,4],9]]],[1,1]]" to "[[[[0,7],4],[15,[0,13]]],[1,1]]",
        "[[[[4,0],[5,0]],[[[4,5],[2,6]],[9,5]]],[7,[[[3,7],[4,3]],[[6,3],[8,8]]]]]" to "[[[[4,0],[5,4]],[[0,[7,6]],[9,5]]],[7,[[[3,7],[4,3]],[[6,3],[8,8]]]]]",
        "[[[[0,[4,5]],[0,0]],[[[4,5],[2,6]],[9,5]]],[7,[[[3,7],[4,3]],[[6,3],[8,8]]]]]" to "[[[[4,0],[5,0]],[[[4,5],[2,6]],[9,5]]],[7,[[[3,7],[4,3]],[[6,3],[8,8]]]]]"
    ).forEach { (arg, expected) ->
        val result = arg.toNode().explode().node.contentToString()
        check(result == expected) {
            "For\n$arg\nexpected\n$expected\nbut was\n$result"
        }
    }

    check("[[[[4,3],4],4],[7,[[8,4],9]]]".toNode() + "[1,1]".toNode() == "[[[[0,7],4],[[7,8],[6,0]]],[8,1]]".toNode())
    check("[[[0,[4,5]],[0,0]],[[[4,5],[2,6]],[9,5]]]".toNode() + "[7,[[[3,7],[4,3]],[[6,3],[8,8]]]]".toNode() == "[[[[4,0],[5,4]],[[7,7],[6,0]]],[[8,[7,7]],[[7,9],[5,0]]]]".toNode())

    mapOf(
        "[[1,2],[[3,4],5]]" to 143,
        "[[[[0,7],4],[[7,8],[6,0]]],[8,1]]" to 1384,
        "[[[[1,1],[2,2]],[3,3]],[4,4]]" to 445,
        "[[[[3,0],[5,3]],[4,4]],[5,5]]" to 791,
        "[[[[5,0],[7,4]],[5,5]],[6,6]]" to 1137,
        "[[[[8,7],[7,7]],[[8,6],[7,7]]],[[[0,7],[6,6]],[8,7]]]" to 3488
    ).forEach { (arg, expected) ->
        val result = arg.toNode().magnitude
        check(result == expected) {
            "For $arg expected $expected but was $result"
        }
    }

    mapOf(
        listOf(
            "[1,1]",
            "[2,2]",
            "[3,3]",
            "[4,4]",
        ) to "[[[[1,1],[2,2]],[3,3]],[4,4]]",
        listOf(
            "[1,1]",
            "[2,2]",
            "[3,3]",
            "[4,4]",
            "[5,5]"
        ) to "[[[[3,0],[5,3]],[4,4]],[5,5]]",
        listOf(
            "[1,1]",
            "[2,2]",
            "[3,3]",
            "[4,4]",
            "[5,5]",
            "[6,6]"
        ) to "[[[[5,0],[7,4]],[5,5]],[6,6]]",
        listOf(
            "[[[0,[4,5]],[0,0]],[[[4,5],[2,6]],[9,5]]]",
            "[7,[[[3,7],[4,3]],[[6,3],[8,8]]]]",
            "[[2,[[0,8],[3,4]]],[[[6,7],1],[7,[1,6]]]]",
            "[[[[2,4],7],[6,[0,5]]],[[[6,8],[2,8]],[[2,1],[4,5]]]]",
            "[7,[5,[[3,8],[1,4]]]]",
            "[[2,[2,2]],[8,[8,1]]]",
            "[2,9]",
            "[1,[[[9,3],9],[[9,0],[0,7]]]]",
            "[[[5,[7,4]],7],1]",
            "[[[[4,2],2],6],[8,7]]"
        ) to "[[[[8,7],[7,7]],[[8,6],[7,7]]],[[[0,7],[6,6]],[8,7]]]"
    ).forEach { (arg, expected) ->
        val result = arg.parse().sum().contentToString()
        check(result == expected) {
            "For list of size ${arg.size} expected $expected but was $result"
        }
    }

    val testInput = readInput(2021, 18, "test").parse()
    val input = readInput(2021, 18).parse()

    check(part1(testInput) == 4140)
    println(part1(input))

    check(part2(testInput) == 3993)
    println(part2(input))
}

private fun part1(numbers: List<Node>): Int = numbers.sum().magnitude

private fun part2(numbers: List<Node>): Int {
    var largest = 0
    numbers.forEach { left ->
        numbers.forEach { right ->
            if (left != right) {
                largest = max(largest, (left + right).magnitude)
            }
        }
    }
    return largest
}

private fun List<String>.parse(): List<Node> = map { it.toNode() }

private fun String.toNode() = iterator().readNode()

private fun List<Node>.sum() = reduce { acc, node -> acc + node }

private operator fun Node.plus(node: Node): Node = Parent(this, node).reduce()

private fun Node.reduce(): Node {
    var result: Node = this
    var exploded = result.explodeWhilePossible().split().node
    while (result != exploded) {
        result = exploded
        exploded = result.explodeWhilePossible().split().node
    }
    return result
}

private fun Node.explodeWhilePossible(): Node {
    var result: Node = this
    var exploded = result.explode().node
    while (result != exploded) {
        result = exploded
        exploded = result.explode().node
    }
    return result
}

private fun Node.explode(
    step: Int = 0,
    leftRemains: Int? = null,
    rightRemains: Int? = null,
    checkForReplacements: Boolean = true
): Result {
    return when (this) {
        is Leaf -> {
            val newValue = value + (leftRemains ?: rightRemains ?: 0)
            Result(Leaf(newValue), false)
        }
        is Parent -> when {
            step == 4 && checkForReplacements && left is Leaf && right is Leaf -> {
                Result(
                    node = Leaf(0),
                    changeApplied = true,
                    leftRemains = left.value,
                    rightRemains = right.value
                )
            }
            else -> {
                val leftResult: Result
                val rightResult: Result
                if (!checkForReplacements) {
                    leftResult = left.explode(step + 1, leftRemains, null, false)
                    rightResult = right.explode(step + 1, null, rightRemains, false)
                } else {
                    val possibleLeftResult = left.explode(step + 1, null, null, true)
                    if (possibleLeftResult.changeApplied) {
                        leftResult = possibleLeftResult
                        rightResult = right.explode(step + 1, leftResult.rightRemains, null, false)
                    } else {
                        rightResult = right.explode(step + 1, null, null, true)
                        leftResult = left.explode(
                            step + 1,
                            null,
                            rightResult.leftRemains,
                            !rightResult.changeApplied
                        )
                    }
                }
                Result(
                    node = Parent(leftResult.node, rightResult.node),
                    leftRemains = leftResult.leftRemains,
                    rightRemains = rightResult.rightRemains,
                    changeApplied = rightResult.changeApplied || leftResult.changeApplied
                )
            }
        }
    }
}

private fun Node.split(
    checkForReplacements: Boolean = true
): Result {
    return when (this) {
        is Leaf ->
            if (value < 10) {
                Result(this, false)
            } else {
                Result(split(), true)
            }
        is Parent -> {
            val leftResult: Result = left.split(checkForReplacements)
            if (leftResult.changeApplied) {
                Result(Parent(leftResult.node, right), true)
            } else {
                val rightResult: Result = right.split(checkForReplacements)
                Result(Parent(leftResult.node, rightResult.node), rightResult.changeApplied)
            }
        }
    }
}

private data class Result(
    val node: Node,
    val changeApplied: Boolean,
    val leftRemains: Int? = null,
    val rightRemains: Int? = null
)

private fun Leaf.split(): Parent = Parent(Leaf(value / 2), Leaf(ceil(value / 2f).toInt()))

private fun CharIterator.readNode(): Node {
    val firstChar = nextChar()
    return if (firstChar == '[') {
        val left = readNode()
        nextChar() //,
        val right = readNode()
        nextChar() //]
        Parent(left, right)
    } else {
        Leaf(firstChar.digitToInt())
    }
}

private fun Node.contentToString(): String = when (this) {
    is Leaf -> value.toString()
    is Parent -> "[${this.left.contentToString()},${this.right.contentToString()}]"
}

private sealed class Node {
    abstract val magnitude: Int
}

private data class Parent(
    val left: Node,
    val right: Node
) : Node() {
    override val magnitude: Int
        get() = 3 * left.magnitude + 2 * right.magnitude
}

private data class Leaf(
    val value: Int
) : Node() {
    override val magnitude: Int = value
}
