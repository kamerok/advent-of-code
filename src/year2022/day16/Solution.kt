package year2022.day16

import readInput
import kotlin.math.max

fun main() {
    val testInput = readInput(2022, 16, "test").parse()
    val input = readInput(2022, 16).parse()

    check(part1(testInput) == 1651)
    println(part1(input))

    check(part2(testInput) == 1707)
    println(part2(input))
}

private fun part1_old(input: List<Room>): Int {
    val roomsWithValves = input.filter { it.valvePressure > 0 }
    val paths: Map<Pair<String, String>, Int> = findPaths(input)

    fun getMaxPressure(pressure: Int = 0, visited: Set<String> = setOf("AA"), seconds: Int = 30): Int {
        return (roomsWithValves
            .filter { !visited.contains(it.id) }
            //is enough time to walk and open
            .filter { paths.getValue(it.id to visited.last()) + 1 < seconds }
            .maxOfOrNull { nextRoom ->
                val path = paths.getValue(visited.last() to nextRoom.id)
                val secondsRemaining = seconds - (path + 1)
                (getMaxPressure(
                    pressure = pressure + nextRoom.valvePressure * secondsRemaining,
                    visited = visited + nextRoom.id,
                    seconds = secondsRemaining
                ))
            } ?: pressure)
    }

    return getMaxPressure()
}

private fun part1(input: List<Room>): Int {
    val rooms = input.associateBy { it.id }
    val roomsWithValves = input.filter { it.valvePressure > 0 }.map { it.id }.toSet()
    val paths: Map<Pair<String, String>, Int> = findPaths(input)

    val combinations = combinations(listOf("AA"), roomsWithValves, paths)

    val maxPath = combinations.maxByOrNull { calculatePath(it, paths, rooms) }!!

    return calculatePath(maxPath, paths, rooms)
}

private fun part2(input: List<Room>): Int {
    val rooms = input.associateBy { it.id }
    val roomsWithValves = input.filter { it.valvePressure > 0 }.map { it.id }.toSet()
    val paths: Map<Pair<String, String>, Int> = findPaths(input)

    val humanCombinations = combinations(listOf("AA"), roomsWithValves, paths, 26)

    var max = 0
    humanCombinations.forEachIndexed { index, humanPath ->
        println("progress ${index.toFloat() / humanCombinations.size}")
        val elephantCombinations = combinations(
            path = listOf("AA"),
            rooms = roomsWithValves - humanPath.toSet(),
            paths = paths,
            timeLeft = 26
        )
        elephantCombinations.forEach { elephantPath ->
            val steam = calculatePath(humanPath, paths, rooms, 26) +
                    calculatePath(elephantPath, paths, rooms, 26)
            max = max(max, steam)
        }
    }

    return max
}

private fun combinations(
    path: List<String>,
    rooms: Set<String>,
    paths: Map<Pair<String, String>, Int>,
    timeLeft: Int = 30,
): List<List<String>> {
    if (rooms.isEmpty()) return listOf(path)
    if (timeLeft <= 0) return listOf(path)

    val from = path.last()
    return rooms
        //enough time to reach and open
        .filter { paths.getValue(from to it) < timeLeft - 1 }
        .flatMap { room ->
            val newTime = timeLeft - (paths[from to room]!! + 1)
            combinations(path + room, rooms - room, paths, newTime).plusElement(path)
        }
        //no possible paths
        .ifEmpty { listOf(path) }
}

private fun calculatePath(
    path: List<String>,
    paths: Map<Pair<String, String>, Int>,
    rooms: Map<String, Room>,
    timer: Int = 30
): Int {
    var timeLeft = timer
    var pressure = 0
    path.windowed(2).forEach { (from, to) ->
        val newTime = timeLeft - (paths[from to to]!! + 1)
        if (newTime <= 0) return pressure
        timeLeft = newTime
        pressure += rooms[to]!!.valvePressure * timeLeft
    }
    return pressure
}

private fun List<String>.parse(): List<Room> = map { line ->
    Room(
        id = line.substring(6..7),
        valvePressure = line.substringAfter("=").substringBefore(";").toInt(),
        connectedRooms = line
            .substringAfter(
                if (line.contains("valves")) "valves " else "valve "
            )
            .split(", ")
            .toSet()
    )
}

private fun findPaths(rooms: List<Room>): Map<Pair<String, String>, Int> {
    val paths: MutableMap<Pair<String, String>, Int> = mutableMapOf()

    fun findPath(from: String, to: String, rooms: Map<String, Room>, visited: Set<String> = emptySet()): Int {
        if (from == to) return visited.size
        val currentRoom = rooms.getValue(from)
        return currentRoom.connectedRooms
            .filter { !visited.contains(it) }
            .map { next ->
                findPath(next, to, rooms, visited + from)
            }
            .filter { it >= 0 }
            .minOrNull() ?: -1
    }

    val roomsById = rooms.associateBy { it.id }
    fun fillPath(from: String, to: String) {
        if (from == to) return
        val path = findPath(from, to, roomsById)
        if (path != -1) {
            paths[from to to] = path
            paths[to to from] = path
        }
    }

    val roomsWithValves = rooms.filter { it.valvePressure > 0 }

    roomsWithValves.forEach { room -> fillPath("AA", room.id) }
    roomsWithValves.forEach { from ->
        roomsWithValves.forEach { to ->
            if (!paths.containsKey(from.id to to.id)) fillPath(from.id, to.id)
        }
    }

    return paths
}

private data class Room(
    val id: String,
    val valvePressure: Int,
    val connectedRooms: Set<String>
)

