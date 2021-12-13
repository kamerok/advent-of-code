package year2015.day14

import readInput

fun main() {
    val testInput = readInput(2015, 14, "test").parseReindeers()
    val input = readInput(2015, 14).parseReindeers()

    check(part1(testInput, 1000) == 1120)
    println(part1(input, 2503))

    check(part2(testInput, 1000) == 689)
    println(part2(input, 2503))
}

private fun part1(input: List<Reindeer>, time: Int): Int = input.maxOf { it.distance(time) }

private fun part2(input: List<Reindeer>, time: Int): Int {
    val points = mutableMapOf<Reindeer, Int>()
    repeat(time) { i ->
        val currentTime = i + 1
        val maxDistance = input.maxOf { it.distance(currentTime) }
        input.forEach { reindeer ->
            if (reindeer.distance(currentTime) == maxDistance) {
                points[reindeer] = points.getOrDefault(reindeer, 0) + 1
            }
        }
    }
    return points.values.maxOf { it }
}

private fun List<String>.parseReindeers(): List<Reindeer> = map { line ->
    val words = line.split(' ')
    Reindeer(words[3].toInt(), words[6].toInt(), words[13].toInt())
}

data class Reindeer(
    val speed: Int,
    val stamina: Int,
    val rest: Int
) {
    fun distance(seconds: Int): Int {
        val cycle = stamina + rest
        val fullCycles = seconds / cycle
        val currentCycleProgress = seconds - cycle * fullCycles
        return fullCycles * (speed * stamina) + currentCycleProgress.coerceAtMost(stamina) * speed
    }
}
