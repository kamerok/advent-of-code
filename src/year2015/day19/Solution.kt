package year2015.day19

import readInput

fun main() {
    val testReplacements = readInput(2015, 19, "test").parse().first
    val (replacements, seed) = readInput(2015, 19).parse()

    check(part1(testReplacements, "HOH") == 4)
    check(part1(testReplacements, "HOHOHO") == 7)
    println(part1(replacements, seed))

    check(part2(testReplacements, "HOH") == 3)
    check(part2(testReplacements, "HOHOHO") == 6)
    println(part2(replacements, seed))
}

private fun part1(replacements: List<Pair<String, String>>, seed: String): Int {
    return replacements.flatMap { (from, to) ->
        Regex(from).findAll(seed).map { matchResult ->
            seed.replaceRange(matchResult.range, to)
        }.toList()
    }.distinct().count()
}

private fun part2(replacements: List<Pair<String, String>>, result: String): Int = 0

private fun List<String>.parse(): Pair<List<Pair<String, String>>, String> =
    (0..lastIndex - 2).map {
        val (from, to) = this[it].split(" => ")
        from to to
    } to last()
