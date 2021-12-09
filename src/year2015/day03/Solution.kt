package year2015.day03

import readInput


fun main() {
    val input = readInput(2015, 3)

    check(calculateHouses(">") == 2)
    check(calculateHouses("^>v<") == 4)
    check(calculateHouses("^v^v^v^v^v") == 2)

    println(calculateHouses(input.first()))

    check(calculateHouses2("^v") == 3)
    check(calculateHouses2("^>v<") == 3)
    check(calculateHouses2("^v^v^v^v^v") == 11)

    println(calculateHouses2(input.first()))
}

fun calculateHouses(input: String): Int {
    val marked = mutableSetOf(0 to 0)
    var current = 0 to 0
    input.forEach { c ->
        current = current.move(c)
        marked.add(current)
    }
    return marked.size
}

fun calculateHouses2(input: String): Int {
    val marked = mutableSetOf(0 to 0)
    var santa = 0 to 0
    var robo = 0 to 0
    var isSantaTurn = true
    input.forEach { c ->
        if (isSantaTurn) {
            santa = santa.move(c)
            marked.add(santa)
        } else {
            robo = robo.move(c)
            marked.add(robo)
        }
        isSantaTurn = !isSantaTurn
    }
    return marked.size
}

fun Pair<Int, Int>.move(instruction: Char): Pair<Int, Int> = when (instruction) {
    '>' -> copy(first = first + 1)
    '<' -> copy(first = first - 1)
    '^' -> copy(second = second + 1)
    else -> copy(second = second - 1)
}
