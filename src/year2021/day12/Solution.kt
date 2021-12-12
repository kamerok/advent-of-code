package year2021.day12

import readInput

fun main() {
    val input = readInput(2021, 12)

    check(part1(readInput(2021, 12, "test1")) == 10)
    check(part1(readInput(2021, 12, "test2")) == 19)
    check(part1(readInput(2021, 12, "test3")) == 226)
    println(part1(input))

    check(part2(readInput(2021, 12, "test1")) == 36)
    check(part2(readInput(2021, 12, "test2")) == 103)
    check(part2(readInput(2021, 12, "test3")) == 3509)
    println(part2(input))
}

private fun part1(input: List<String>): Int {
    val points = mutableSetOf<String>()
    val connections = mutableSetOf<Pair<String, String>>()
    input.forEach { line ->
        val (from, to) = line.split('-')
        points.add(from)
        points.add(to)
        connections.add(from to to)
        connections.add(to to from)
    }
    return findFinalPaths("start", points.minus("start"), connections)
        .filter { it.last() == "end" }.size
}

private fun part2(input: List<String>): Int {
    val points = mutableSetOf<String>()
    val connections = mutableSetOf<Pair<String, String>>()
    input.forEach { line ->
        val (from, to) = line.split('-')
        points.add(from)
        points.add(to)
        connections.add(from to to)
        connections.add(to to from)
    }
    return findFinalPaths(
        startPoint = "start",
        points = points.minus("start"),
        connections = connections,
        isDoubleCheckAvailable = true,
    )
        .filter { it.last() == "end" }
        .size
}

private fun findFinalPaths(
    startPoint: String,
    points: Set<String>,
    connections: Set<Pair<String, String>>,
    isDoubleCheckAvailable: Boolean = false,
): List<List<String>> {
    if (startPoint == "end") return listOf(listOf(startPoint))
    val availablePoints = points.filter { nextPoint ->
        connections.contains(nextPoint to startPoint) || connections.contains(startPoint to nextPoint)
    }
    return availablePoints
        .flatMap { nextPoint ->
            val nextAvailablePoints = if (nextPoint.all(Char::isUpperCase)) {
                points
            } else {
                points.minus(nextPoint)
            }
            val pathWithoutCheck = findFinalPaths(
                nextPoint,
                nextAvailablePoints,
                connections,
                isDoubleCheckAvailable
            )
            val pathsWithCheck = if (isDoubleCheckAvailable) {
                if (nextAvailablePoints.size != points.size) {
                    findFinalPaths(
                        nextPoint,
                        points,
                        connections,
                        false
                    )
                } else {
                    emptyList()
                }
            } else {
                emptyList()
            }
            (pathWithoutCheck + pathsWithCheck).distinct()
        }
        .map { listOf(startPoint) + it }
}
