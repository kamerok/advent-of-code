package year2015.day17

import readInput
import kotlin.math.pow

fun main() {
    val input = readInput(2015, 17).map { it.toInt() }

    check(part1(listOf(20, 15, 10, 5, 5), 25) == 4)
    println(part1(input, 150))

    check(part2(listOf(20, 15, 10, 5, 5), 25) == 3)
    println(part2(input, 150))
}

private fun part1(containers: List<Int>, liters: Int): Int =
    containers.permutations()
        .count { it.sum() == liters }

private fun part2(containers: List<Int>, liters: Int): Int =
    containers.permutations()
        .filter { it.sum() == liters }
        .let { fit ->
            val minSize = fit.minByOrNull { it.size }?.size ?: 0
            fit.filter { it.size == minSize }
        }
        .count()

private fun <T> List<T>.permutations(): List<List<T>> {
    val result = mutableListOf<List<T>>()
    repeat(2.0.pow(size).toInt()) { variant ->
        val binary = variant.toString(2).padStart(size, '0')
        result.add(binary.mapIndexedNotNull { index, c -> if (c == '0') null else get(index) })
    }
    return result
}