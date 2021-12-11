package year2015.day09

import readInput

fun main() {
    val testInput = readInput(2015, 9, "test")
    val input = readInput(2015, 9)

    check(part1(testInput) == 605)
    println(part1(input))

    check(part2(testInput) == 982)
    println(part2(input))
}

private fun part1(input: List<String>): Int =
    findPossibleDistances(input).minOf { it }

private fun part2(input: List<String>): Int =
    findPossibleDistances(input).maxOf { it }

private fun findPossibleDistances(input: List<String>): List<Int> {
    val places = mutableSetOf<String>()
    val distances = mutableMapOf<Pair<String, String>, Int>()
    input.forEach { line ->
        val (placesString, distanceString) = line.split(" = ")
        val (from, to) = placesString.split(" to ")
        places.add(from)
        places.add(to)
        val distance = distanceString.toInt()
        distances[from to to] = distance
        distances[to to from] = distance
    }
    fun getPaths(places: Set<String>): List<List<String>> {
        if (places.size == 1) return listOf(places.toList())
        return places.flatMap { place ->
            getPaths(places.minus(place)).map {
                listOf(place) + it
            }
        }
    }

    val possibleDistances = getPaths(places).map { path ->
        path.zipWithNext { a, b -> distances.getValue(a to b) }.sumOf { it }
    }
    return possibleDistances
}
