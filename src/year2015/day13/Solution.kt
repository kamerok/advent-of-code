package year2015.day13

import readInput

fun main() {
    val testInput = readInput(2015, 13, "test")
    val input = readInput(2015, 13)

    check(part1(testInput) == 330)
    println(part1(input))
    println(part2(input))
}

private fun part1(input: List<String>): Int {
    val relations = input.parse()
    return relations.names().permutations().maxHappiness(relations)
}

private fun part2(input: List<String>): Int {
    val relations = input.parse()
    val names = relations.names()
    val me: String = "me"
    val relationsWithMe = buildMap<Pair<String, String>, Int> {
        putAll(relations)
        names.forEach { name ->
            put(me to name, 0)
            put(name to me, 0)
        }
    }
    return names.plus(me).permutations().maxHappiness(relationsWithMe)
}

private fun List<String>.parse(): Map<Pair<String, String>, Int> = associate { line ->
    val words = line.dropLast(1).split(' ')
    val change = words[3].toInt() * if (words[2] == "gain") 1 else -1
    words.first() to words.last() to change
}

private fun Map<Pair<String, String>, Int>.names(): Set<String> =
    keys.fold(mutableSetOf()) { set, key ->
        set.add(key.first)
        set.add(key.second)
        set
    }

private fun Set<String>.permutations(): List<List<String>> {
    if (size == 1) return listOf(listOf(first()))
    return flatMap { first ->
        minus(first).permutations().map {
            listOf(first) + it
        }
    }
}

private fun List<List<String>>.maxHappiness(relations: Map<Pair<String, String>, Int>): Int =
    maxOf { seating ->
        var sum = 0
        seating.plus(seating.first()).zipWithNext { a, b ->
            sum += relations.getValue(a to b)
            sum += relations.getValue(b to a)
        }
        sum
    }
