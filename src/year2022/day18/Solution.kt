package year2022.day18

import readInput
import kotlin.math.max

fun main() {
    val testInput = readInput(2022, 18, "test").parse()
    val input = readInput(2022, 18).parse()

    check(
        part1(
            setOf(
                Point(1, 1, 1),
                Point(2, 1, 1)
            )
        ) == 10
    )
    check(part1(testInput) == 64)
    println(part1(input))

    check(part2(testInput) == 58)
    println(part2(input))
}

private fun part1(input: Set<Point>): Int {
    return input.sumOf { point ->
        6 - point.neighbours().count { input.contains(it) }
    }
}

private fun part2(input: Set<Point>): Int {
    var maxX = input.first().x
    var maxY = input.first().y
    var maxZ = input.first().z
    input.forEach { (x, y, z) ->
        maxX = max(maxX, x)
        maxY = max(maxY, y)
        maxZ = max(maxZ, z)
    }

    val field = Array((0..maxX).count()) {
        Array((0..maxY).count()) {
            BooleanArray((0..maxZ).count())
        }
    }
    input.forEach { (x, y, z) -> field[x][y][z] = true }

    val borders = mutableListOf<Point>()

    (0..maxX).forEach { x ->
        (0..maxY).forEach { y ->
            borders.add(Point(x, y, 0))
            borders.add(Point(x, y, maxZ))
        }
    }

    (0..maxY).forEach { y ->
        (0..maxZ).forEach { z ->
            borders.add(Point(0, y, z))
            borders.add(Point(maxX, y, z))
        }
    }

    (0..maxX).forEach { x ->
        (0..maxZ).forEach { z ->
            borders.add(Point(x, 0, z))
            borders.add(Point(x, maxY, z))
        }
    }

    borders.forEach { field.mark(it) }

    val totalSides = input.sumOf { point ->
        6 - point.neighbours().count { input.contains(it) }
    }

    val innerPoints = mutableListOf<Point>()
    field.forEachIndexed { x, xArrays ->
        xArrays.forEachIndexed { y, yArray ->
            yArray.forEachIndexed { z, value ->
                if (!value) innerPoints.add(Point(x, y, z))
            }
        }
    }

    val innerSides = innerPoints.sumOf { point ->
        6 - point.neighbours().count { innerPoints.contains(it) }
    }

    return totalSides - innerSides
}

private fun Array<Array<BooleanArray>>.mark(point: Point) {
    val current = getOrNull(point.x)?.getOrNull(point.y)?.getOrNull(point.z)
    if (current == false) {
        this[point.x][point.y][point.z] = true
        point.neighbours().forEach { mark(it) }
    }
}

private fun List<String>.parse(): Set<Point> = map { line ->
    val (x, y, z) = line.split(",").map { it.toInt() }
    Point(x, y, z)
}.toSet()

private data class Point(
    val x: Int,
    val y: Int,
    val z: Int
) {
    fun neighbours(): Set<Point> = setOf(
        Point(x - 1, y, z),
        Point(x + 1, y, z),
        Point(x, y - 1, z),
        Point(x, y + 1, z),
        Point(x, y, z - 1),
        Point(x, y, z + 1),
    )
}
