package year2015.day16

import readInput

fun main() {
    val input = readInput(2015, 16)

    println(part1(input))
    println(part2(input))
}

private fun part1(input: List<String>): Int {
    val reference = mapOf(
        "children" to 3,
        "cats" to 7,
        "samoyeds" to 2,
        "pomeranians" to 3,
        "akitas" to 0,
        "vizslas" to 0,
        "goldfish" to 5,
        "trees" to 3,
        "cars" to 2,
        "perfumes" to 1
    )
    //Sue 1: goldfish: 6, trees: 9, akitas: 0
    input.forEach { line ->
        val properties = line.substringAfter(": ")
            .split(", ")
            .associate {
                val (name, valueString) = it.split(": ")
                name to valueString.toInt()
            }
        if (properties.all { (name, value) -> reference[name] == value }) {
            return line.substringBefore(": ").split(' ').last().toInt()
        }
    }
    return -1
}

private fun part2(input: List<String>): Int {
    val reference: Map<String, (Int) -> Boolean> = mapOf(
        "children" to { it == 3 },
        "cats" to { it > 7 },
        "samoyeds" to { it == 2 },
        "pomeranians" to { it < 3 },
        "akitas" to { it == 0 },
        "vizslas" to { it == 0 },
        "goldfish" to { it < 5 },
        "trees" to { it > 3 },
        "cars" to { it == 2 },
        "perfumes" to { it == 1 }
    )
    //Sue 1: goldfish: 6, trees: 9, akitas: 0
    input.forEach { line ->
        val properties = line.substringAfter(": ")
            .split(", ")
            .associate {
                val (name, valueString) = it.split(": ")
                name to valueString.toInt()
            }
        if (properties.all { (name, value) -> reference.getValue(name).invoke(value) }) {
            return line.substringBefore(": ").split(' ').last().toInt()
        }
    }
    return -1
}