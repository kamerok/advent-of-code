package year2021.day15

import readInput
import kotlin.math.min

fun main() {
    val testInput = readInput(2021, 15, "test").parse()
    val input = readInput(2021, 15).parse()

    check(part1(testInput) == 40)
    println(part1(input))

    check(part2(testInput) == 315)
    println(part2(input))
}

private fun part1(grid: List<IntArray>): Int =
    grid.fillDijkstraMap().getValue(Point(grid.lastIndex, grid.first().lastIndex))

private fun part2(grid: List<IntArray>): Int =
    grid.multiply(5)
        .let { largeGrid ->
            largeGrid
                .aStar(Point(0, 0), Point(largeGrid.lastIndex, largeGrid.first().lastIndex))
                .drop(1).sumOf { largeGrid.get(it) }
        }

private fun List<IntArray>.fillDijkstraMap(): Map<Point, Int> {
    val result = mutableMapOf<Point, Int>()
    val visited = mutableSetOf<Point>()
    val unvisited = flatMapIndexed { i: Int, row: IntArray ->
        row.indices.map { j -> Point(i, j) }
    }.toMutableList()
    while (unvisited.isNotEmpty()) {
        val current = result.keys
            .filter { !visited.contains(it) }
            .minByOrNull { result.getValue(it) }
            ?: unvisited.first()
        val adjacentPoints = current.adjacentPoints().filter { point ->
            !visited.contains(point) && getOrNull(point.x)?.getOrNull(point.y) != null
        }
        adjacentPoints.forEach { point ->
            val possibleNewValue = result.getOrDefault(current, 0) + get(point)
            if (result.contains(point)) {
                val newValue = min(
                    result.getValue(point),
                    possibleNewValue
                )
                result[point] = newValue
            } else {
                result[point] = possibleNewValue
            }
        }
        visited.add(current)
        unvisited.remove(current)
    }
    return result
}

private fun List<IntArray>.aStar(start: Point, goal: Point): List<Point> {
    val h: (Point) -> Int = { current ->
        (goal.x - current.x) + (goal.y - current.y)
    }
    val openSet = mutableSetOf(start)
    val cameFrom = mutableMapOf<Point, Point>()
    val gScore = mutableMapOf<Point, Int>()
    gScore[start] = 0
    val fScore = mutableMapOf<Point, Int>()
    fScore[start] = h(start)
    while (openSet.isNotEmpty()) {
        val current = openSet.minByOrNull { fScore.getValue(it) }!!
        if (current == goal) return reconstructPath(cameFrom, current)
        openSet.remove(current)
        val adjacentPoints = current.adjacentPoints().filter { point ->
            getOrNull(point.x)?.getOrNull(point.y) != null
        }
        adjacentPoints.forEach { neighbor ->
            val tentativeScore = gScore.getOrDefault(current, 0) + get(neighbor)
            if (tentativeScore < (gScore[neighbor] ?: Int.MAX_VALUE)) {
                cameFrom[neighbor] = current
                gScore[neighbor] = tentativeScore
                fScore[neighbor] = tentativeScore + h(neighbor)
                if (!openSet.contains(neighbor)) openSet.add(neighbor)
            }
        }
    }
    return emptyList()
}

private fun reconstructPath(cameFrom: Map<Point, Point>, end: Point): List<Point> {
    val path = mutableListOf<Point>()
    var current: Point? = end
    while (current != null) {
        path.add(0, current)
        current = cameFrom[current]
    }
    return path
}

private fun List<String>.parse(): List<IntArray> =
    map { line -> IntArray(line.length) { line[it].digitToInt() } }

private fun List<IntArray>.get(point: Point) = this[point.x][point.y]

private fun List<IntArray>.multiply(multiplier: Int): List<IntArray> =
    List(size * multiplier) { i ->
        val originalRowIndex = i.mod(size)
        val chunkRowIndex = i / size
        val rowSize = get(originalRowIndex).size
        IntArray(rowSize * multiplier) { j ->
            val originalColumnIndex = j.mod(rowSize)
            val chunkColumnIndex = j / rowSize
            val originalValue = this[originalRowIndex][originalColumnIndex]
            val rawValue = originalValue + chunkColumnIndex + chunkRowIndex
            if (rawValue / 10 < 1) {
                rawValue
            } else {
                rawValue.mod(10) + 1
            }
        }
    }

private data class Point(val x: Int, val y: Int) {
    fun adjacentPoints(): Set<Point> = buildSet {
        add(Point(x, y - 1))
        add(Point(x, y + 1))
        add(Point(x - 1, y))
        add(Point(x + 1, y))
    }
}
