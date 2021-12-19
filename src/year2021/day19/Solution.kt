package year2021.day19

import readInput
import kotlin.math.max
import kotlin.math.min

fun main() {
    val simpleInput = readInput(2021, 19, "simple").parse()
    val testInput = readInput(2021, 19, "test").parse()
    val input = readInput(2021, 19).parse()

    check(part1(testInput) == 79)
    println(part1(input))

//    check(part2(testInput) == 3993)
//    println(part2(input))
}

private fun part1(scanners: List<Scanner>): Int {
    val knownScanners = mutableListOf(scanners.first())
    val unknownScanners = scanners.drop(1).toMutableList()
    while (unknownScanners.isNotEmpty()) {
        unknownScanners.removeIf { scanner ->
            val normalized = scanner.tryToNormalize(knownScanners)
            if (normalized != null) {
                knownScanners.add(normalized)
            }
            normalized != null
        }
    }
    return knownScanners.allPoints().size
}

private fun part2(scanners: List<Scanner>): Int {

    return 0
}

private fun List<String>.parse(): List<Scanner> {
    val result = mutableListOf<Scanner>()
    val iterator = iterator()
    var scannerPoints = mutableListOf<Point>()
    while (iterator.hasNext()) {
        val line = iterator.next()
        when {
            line.isEmpty() -> {
                result.add(Scanner(scannerPoints))
                scannerPoints = mutableListOf()
            }
            !line.startsWith("---") -> {
                val (x, y, z) = line.split(',').map { it.toInt() }
                scannerPoints.add(Point(x, y, z))
            }
        }
    }
    result.add(Scanner(scannerPoints))
    return result
}

private fun Scanner.tryToNormalize(scanners: List<Scanner>): Scanner? {
    val knownPoints = scanners.allPoints()
    knownPoints.iterate { knownTriangle ->
        points.iterate { unknownTriangle ->
            unknownTriangle.rotate { rotatedTriangle ->
                val isPan = false
                if (isPan) {
                    //apply pan and rotation to scanner
                }
            }
        }
    }
    return null
}

private fun Collection<Scanner>.allPoints(): Set<Point> = fold(mutableSetOf()) { acc, scanner ->
    acc.also { acc.addAll(scanner.points) }
}

private inline fun Collection<Point>.iterate(operation: (Set<Point>) -> Unit) {
    TODO()
}

private inline fun Set<Point>.rotate(operation: (Set<Point>) -> Unit) {
    TODO()
}

private data class Point(val x: Int, val y: Int, val z: Int) {

    fun belong(space: Space): Boolean = x in (space.xMin..space.xMax) &&
            y in (space.yMin..space.yMax) &&
            z in (space.zMin..space.zMax)

}

private data class Space(
    val xMin: Int,
    val xMax: Int,
    val yMin: Int,
    val yMax: Int,
    val zMin: Int,
    val zMax: Int
) {
    fun isOverlap(space: Space): Boolean = xMin <= space.xMax || space.xMin <= xMax &&
            yMin <= space.yMax || space.yMin <= yMax &&
            zMin <= space.zMax || space.zMin <= zMax

    infix fun and(space: Space): Space = Space(
        xMin = max(xMin, space.xMin),
        xMax = min(xMax, space.xMax),
        yMin = max(yMin, space.yMin),
        yMax = min(yMax, space.yMax),
        zMin = max(zMin, space.zMin),
        zMax = min(zMax, space.zMax)
    )
}

private data class Scanner(val points: List<Point>) {
    val space = Space(
        xMin = points.minOf { it.x },
        xMax = points.maxOf { it.x },
        yMin = points.minOf { it.y },
        yMax = points.maxOf { it.y },
        zMin = points.minOf { it.z },
        zMax = points.maxOf { it.z }
    )

    fun checkOverlappingPoints(scanner: Scanner): Boolean {
        if (!space.isOverlap(scanner.space)) return false
        val overlap = space and scanner.space
        val pointsFromA = points.filter { it.belong(overlap) }.toSet()
        val pointsFromB = scanner.points.filter { it.belong(overlap) }.toSet()
        return pointsFromA.isNotEmpty() && pointsFromA == pointsFromB
    }
}