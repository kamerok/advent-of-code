package year2015.day04

import md5
import readInput


fun main() {
    val input = readInput(2015, 4)

    check(part1("abcdef") == 609043)
    check(part1("pqrstuv") == 1048970)

    println(part1(input.first()))
    println(part2(input.first()))
}

fun part1(input: String): Int {
    var output = 0
    while (!"$input$output".md5().padStart(32, '0').startsWith("00000")) {
        output++
    }
    return output
}

fun part2(input: String): Int {
    var output = 0
    while (!"$input$output".md5().padStart(32, '0').startsWith("000000")) {
        output++
    }
    return output
}
