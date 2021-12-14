package year2015.day15

import readInput

fun main() {
    val testInput = readInput(2015, 15, "test").parse()
    val input = readInput(2015, 15).parse()

    check(part1(testInput) == 62842880)
    println(part1(input))

    check(part2(testInput) == 57600000)
    println(part2(input))
}

private fun part1(input: List<Ingredient>): Int {
    var bestCookie = 0
    iterate(100, input.size) { variant ->
        val recipe = input.mapIndexed { index, ingredient ->
            ingredient to variant[index]
        }.toMap()
        val quality = recipe.quality()
        if (quality > bestCookie) bestCookie = quality
    }
    return bestCookie
}

private fun part2(input: List<Ingredient>): Int {
    var bestCookie = 0
    iterate(100, input.size) { variant ->
        val recipe = input.mapIndexed { index, ingredient ->
            ingredient to variant[index]
        }.toMap()
        val quality = recipe.quality()
        val calories = recipe.entries.sumOf { it.key.calories * it.value }
        if (quality > bestCookie && calories == 500) bestCookie = quality
    }
    return bestCookie
}

private fun List<String>.parse(): List<Ingredient> = map { line ->
    val properties = line.split(", ").map { it.split(" ") }
    Ingredient(
        name = properties[0][0].dropLast(1),
        capacity = properties[0].last().toInt(),
        durability = properties[1].last().toInt(),
        flavor = properties[2].last().toInt(),
        texture = properties[3].last().toInt(),
        calories = properties[4].last().toInt()
    )
}

private fun Map<Ingredient, Int>.quality(): Int {
    val capacity: Int = entries.sumOf { it.key.capacity * it.value }.coerceAtLeast(0)
    val durability: Int = entries.sumOf { it.key.durability * it.value }.coerceAtLeast(0)
    val flavor: Int = entries.sumOf { it.key.flavor * it.value }.coerceAtLeast(0)
    val texture: Int = entries.sumOf { it.key.texture * it.value }.coerceAtLeast(0)
    return capacity * durability * flavor * texture
}

private fun iterate(sum: Int, size: Int, operation: (IntArray) -> Unit) {
    val array = IntArray(size) { i -> if (i == size - 1) sum else 0 }
    operation(array)
    while (array.first() != sum) {
        val lastIndex = array.indexOfLast { it > 0 }
        array[lastIndex]--
        val previous = lastIndex - 1
        if (lastIndex != array.lastIndex) {
            array[array.lastIndex] = array[lastIndex]
            array[lastIndex] = 0
        }
        array[previous]++
        operation(array)
    }
}

private data class Ingredient(
    val name: String,
    val capacity: Int,
    val durability: Int,
    val flavor: Int,
    val texture: Int,
    val calories: Int
)