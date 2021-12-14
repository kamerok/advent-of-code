package year2021.day14

import readInput

fun main() {
    val (testTemplate, testRules) = readInput(2021, 14, "test").parse()
    val (template, rules) = readInput(2021, 14).parse()

    check(solution(testTemplate, testRules, 10) == 1588L)
    println(solution(template, rules, 10))

    check(solution(testTemplate, testRules, 40) == 2188189693529)
    println(solution(template, rules, 40))

}

private fun solution(template: String, rules: Map<String, Char>, steps: Int): Long {
    val result = template.polymer(steps, rules)
    return result.values.maxOf { it } - result.values.minOf { it }
}

private fun String.polymer(steps: Int, rules: Map<String, Char>): Map<Char, Long> {
    val cache = mutableMapOf<Pair<Pair<Char, Char>, Int>, Map<Char, Long>>()
    val result = mutableMapOf<Char, Long>()
    result.inc(first())
    zipWithNext { a, b ->
        val localResult = (a to b).depthPolymer(1, steps, rules, cache)
        localResult.forEach { (key, value) ->
            result[key] = result.getOrDefault(key, 0) + value
        }
        result.dec(a)
    }
    return result
}

private fun Pair<Char, Char>.depthPolymer(
    step: Int,
    totalSteps: Int,
    rules: Map<String, Char>,
    cache: MutableMap<Pair<Pair<Char, Char>, Int>, Map<Char, Long>>
): Map<Char, Long> {
    cache[this to step]?.run { return this }
    val result = mutableMapOf<Char, Long>()
    val polymer = (first to second).polymer(rules)
    if (step == totalSteps) {
        polymer.forEach { result.inc(it) }
    } else {
        result.inc(polymer.first())
        polymer.zipWithNext { a, b ->
            val localResult = (a to b).depthPolymer(step + 1, totalSteps, rules, cache)
            localResult.forEach { (key, value) ->
                result[key] = result.getOrDefault(key, 0) + value
            }
            result.dec(a)
        }
    }
    cache[this to step] = result
    return result
}

private fun MutableMap<Char, Long>.inc(char: Char) =
    set(char, getOrDefault(char, 0) + 1)

private fun MutableMap<Char, Long>.dec(char: Char) {
    val newValue = getOrDefault(char, 0) - 1
    if (newValue <= 0) {
        remove(char)
    } else {
        set(char, newValue)
    }
}

private fun Pair<Char, Char>.polymer(rules: Map<String, Char>): String =
    buildString {
        append(first)
        val newChar = rules.getValue("$first$second")
        append(newChar)
        append(second)
    }

private fun List<String>.parse(): Pair<String, Map<String, Char>> =
    first() to subList(2, size).associate { line ->
        val (pair, result) = line.split(" -> ")
        pair to result.first()
    }
