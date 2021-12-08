package year2015.day02

import readInput


fun main() {
    val input = readInput(2015, 2)
        .map { size -> size.split("x").map { it.toInt() } }

    check(calculateWrapping(2, 3, 4) == 58)
    check(calculateWrapping(1, 1, 10) == 43)

    input
        .sumOf { (l, w, h) -> calculateWrapping(l, w, h) }
        .also { println(it) }

    check(calculateRibbon(2, 3, 4) == 34)
    check(calculateRibbon(1, 1, 10) == 14)

    input
        .sumOf { (l, w, h) -> calculateRibbon(l, w, h) }
        .also { println(it) }
}

fun calculateWrapping(l: Int, w: Int, h: Int): Int {
    val sides = arrayOf(l * w, w * h, h * l)
    return sides.sumOf { it * 2 } + sides.minOf { it }
}

fun calculateRibbon(l: Int, w: Int, h: Int): Int =
    2 * (l + w + h - maxOf(l, w, h)) + l * w * h