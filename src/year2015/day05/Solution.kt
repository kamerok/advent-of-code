package year2015.day05

import readInput


fun main() {
    val input = readInput(2015, 5)

    check(part1(listOf("ugknbfddgicrmopn")) == 1)
    check(part1(listOf("aaa")) == 1)
    check(part1(listOf("jchzalrnumimnmhp")) == 0)
    check(part1(listOf("haegwjzuvuyypxyu")) == 0)
    check(part1(listOf("dvszwmarrgswjxmb")) == 0)

    println(part1(input))

    check(part2(listOf("qjhvhtzxzqqjkmpb")) == 1)
    check(part2(listOf("xxyxx")) == 1)
    check(part2(listOf("uurcxstgmygtbstg")) == 0)
    check(part2(listOf("ieodomkazucvgmuy")) == 0)

    println(part2(input))
}

private fun part1(input: List<String>): Int =
    input.count { line ->
        val vowels = setOf('a', 'e', 'i', 'o', 'u')
        val restricted = setOf("ab", "cd", "pq", "xy")
        var vowelCount = if (vowels.contains(line[0])) 1 else 0
        var doubleLetter = false
        var hasRestricted = false
        line.zipWithNext { a, b ->
            if (restricted.contains("$a$b")) {
                hasRestricted = true
            }
            if (a == b) doubleLetter = true
            if (vowels.contains(b)) vowelCount++
        }
        !hasRestricted && vowelCount >= 3 && doubleLetter
    }

private fun part2(input: List<String>): Int =
    input.count { line ->
        Regex("(..).*\\1").containsMatchIn(line) && Regex("(.).\\1").containsMatchIn(line)
    }

