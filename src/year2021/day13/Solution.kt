package year2021.day13

import readInput

fun main() {
    val (testPoints, testFolds) = readInput(2021, 13, "test").parse()
    val (points, folds) = readInput(2021, 13).parse()

    check(part1(testPoints, testFolds) == 17)
    println(part1(points, folds))

    part2(points, folds)
}

private fun part1(points: Set<Point>, folds: List<Fold>): Int {
    return points.fold(folds.first()).size
}

private fun part2(points: Set<Point>, folds: List<Fold>) {
    var foldedPoints = points
    folds.forEach { fold ->
        foldedPoints = foldedPoints.fold(fold)
    }
    foldedPoints.prettyPrint()
}

private fun List<String>.parse(): Pair<Set<Point>, List<Fold>> {
    val splitIndex = indexOf("")
    val points = (0 until splitIndex).map { i ->
        val (xString, yString) = get(i).split(',')
        Point(xString.toInt(), yString.toInt())
    }.toSet()
    val folds = (splitIndex + 1..lastIndex).map { i ->
        val (axis, lineString) = get(i).split(' ').last().split('=')
        Fold(
            line = lineString.toInt(),
            isX = axis == "x"
        )
    }
    return points to folds
}

private fun Set<Point>.prettyPrint() {
    val maxX = maxOf { it.x }
    val maxY = maxOf { it.y }
    println()
    (0..maxY).forEach { y ->
        (0..maxX).forEach { x ->
            print(if (contains(Point(x, y))) '█' else ' ')
        }
        println()
    }
}

private fun Set<Point>.fold(fold: Fold) = if (fold.isX) foldX(fold.line) else foldY(fold.line)

private fun Set<Point>.foldX(axis: Int): Set<Point> = map { point ->
    Point(
        x = point.x.fold(axis),
        y = point.y
    )
}.toSet()

private fun Set<Point>.foldY(axis: Int): Set<Point> = map { point ->
    Point(
        x = point.x,
        y = point.y.fold(axis)
    )
}.toSet()

private fun Int.fold(axis: Int) = if (this < axis) {
    this
} else {
    axis - (this - axis)
}

private data class Point(val x: Int, val y: Int)

private data class Fold(val line: Int, val isX: Boolean)
