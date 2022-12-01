package year2022.day01

import readInput

private data class Elf(
    val food: List<Food>,
    val totalCalories: Int = food.sumOf { it.calories }
)

private data class Food(
    val calories: Int
)

fun main() {
    val testInput = readInput(2022, 1, "test")
    val input = readInput(2022, 1)

    check(part1(testInput.parse()) == 24000)
    println(part1(input.parse()))

    check(part2(testInput.parse()) == 45000)
    println(part2(input.parse()))
}

private fun List<String>.parse(): List<Elf> =
    fold(mutableListOf<MutableList<Food>>(mutableListOf())) { elfs, food ->
        if (food.isNotEmpty()) {
            elfs.last().add(Food(food.toInt()))
        } else {
            elfs.add(mutableListOf())
        }
        elfs
    }.map { Elf(it) }

private fun part1(input: List<Elf>): Int = input.maxOf { it.totalCalories }

private fun part2(input: List<Elf>): Int = input
    .sortedBy { it.totalCalories }
    .takeLast(3)
    .sumOf { it.totalCalories }
