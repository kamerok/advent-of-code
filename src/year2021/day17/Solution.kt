package year2021.day17

import readInput
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sign

fun main() {
    val testInput = readInput(2021, 17, "test").first().parse()
    val input = readInput(2021, 17).first().parse()

    check(part1(testInput) == 45)
    println(part1(input))

    check(part2(testInput) == 112)
    println(part2(input))
}

private fun part1(input: Rect): Int {
    var result = Int.MIN_VALUE
    (0..input.right).forEach { x ->
        (input.bottom ..1000).forEach { y ->
            val (path, isHit) = Point(0, 0).calculatePath(x, y, input)
            if (isHit) {
                result = max(result, path.maxOf { it.y })
            }
        }
    }
    return result
}

private fun part2(input: Rect): Int {
    var result = 0
    (0..input.right).forEach { x ->
        (input.bottom ..1000).forEach { y ->
            val isHit = Point(0, 0).calculatePath(x, y, input).second
            if (isHit) result++
        }
    }
    return result
}

private fun String.parse(): Rect = split(": ")[1].split(", ").let { (xString, yString) ->
    val (left, right) = xString.substringAfter('=').split("..").map { it.toInt() }.sorted()
    val (top, bottom) = yString.substringAfter('=').split("..").map { it.toInt() }
        .sortedDescending()
    Rect(left, right, top, bottom)
}

private fun Point.calculatePath(velocityX: Int, velocityY: Int, target: Rect): Pair<List<Point>, Boolean> {
    val result = mutableListOf(this)
    var currentPoint = this
    var currentVelocityX = velocityX
    var currentVelocityY = velocityY
    while (!currentPoint.isAfter(target) && !currentPoint.isInside(target)) {
        currentPoint = Point(currentVelocityX + currentPoint.x, currentVelocityY + currentPoint.y)
        currentVelocityX -= currentVelocityX.sign
        currentVelocityY--
        result.add(currentPoint)
    }
    return result to currentPoint.isInside(target)
}

private fun print(points: List<Point>, target: Rect) {
    val fromX = min(target.left, points.minOf { it.x })
    val toX = max(target.right, points.maxOf { it.x })
    val fromY = max(target.top, points.maxOf { it.y })
    val toY = min(target.bottom, points.minOf { it.y })
    (fromY downTo toY).forEach { y ->
        (fromX..toX).forEach { x ->
            val point = Point(x, y)
            print(
                when {
                    point == points.first() -> 'S'
                    points.contains(point) -> '#'
                    point.isInside(target) -> 'T'
                    else -> '.'
                }
            )
        }
        println()
    }
}

private data class Point(val x: Int, val y: Int) {
    fun isInside(rect: Rect): Boolean = with(rect) { x in (left..right) && y in (top downTo bottom) }

    fun isAfter(rect: Rect): Boolean = with(rect) { x > right || y < bottom }
}

private data class Rect(val left: Int, val right: Int, val top: Int, val bottom: Int)
