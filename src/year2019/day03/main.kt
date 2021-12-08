package year2019.day03

import readInput
import kotlin.math.abs


private fun calculateDistance(wire1: String, wire2: String): Int {
    val points = mutableSetOf<Pair<Int, Int>>()
    var x = 0
    var y = 0
    wire1.split(',').forEach { instruction ->
        repeat(instruction.substring(1).toInt()) {
            when (instruction[0]) {
                'R' -> x++
                'L' -> x--
                'U' -> y++
                'D' -> y--
            }
            points += x to y
        }
    }
    x = 0
    y = 0
    var distance: Int? = null
    wire2.split(',').forEach { instruction ->
        repeat(instruction.substring(1).toInt()) {
            when (instruction[0]) {
                'R' -> x++
                'L' -> x--
                'U' -> y++
                'D' -> y--
            }
            if (points.contains(x to y)) {
                val newDistance = abs(x) + abs(y)
                if (distance == null || newDistance < distance!!) {
                    distance = newDistance
                }
            }
        }
    }
    return distance!!
}

private fun calculateLength(wire1: String, wire2: String): Int {
    val points = mutableMapOf<Pair<Int, Int>, Int>()
    var x = 0
    var y = 0
    var length = 0
    wire1.split(',').forEach { instruction ->
        repeat(instruction.substring(1).toInt()) {
            when (instruction[0]) {
                'R' -> x++
                'L' -> x--
                'U' -> y++
                'D' -> y--
            }
            length++
            points += (x to y) to length
        }
    }
    x = 0
    y = 0
    length = 0
    var finalLength: Int? = null
    wire2.split(',').forEach { instruction ->
        repeat(instruction.substring(1).toInt()) {
            when (instruction[0]) {
                'R' -> x++
                'L' -> x--
                'U' -> y++
                'D' -> y--
            }
            length++
            if (points.containsKey(x to y)) {
                val newLength = length + points[x to y]!!
                if (finalLength == null || newLength < finalLength!!) {
                    finalLength = newLength
                }
            }
        }
    }
    return finalLength!!
}

private val testCases1 = mapOf(
    ("R8,U5,L5,D3" to "U7,R6,D4,L4") to 6,
    ("R75,D30,R83,U83,L12,D49,R71,U7,L72" to "U62,R66,U55,R34,D71,R55,D58,R83") to 159,
    ("R98,U47,R26,D63,R33,U87,L62,D20,R33,U53,R51" to "U98,R91,D20,R16,D67,R40,U7,R15,U6,R7") to 135
)

private fun test1() {
    testCases1.forEach { (input, output) ->
        check(output == calculateDistance(input.first, input.second))
    }
}

private val testCases2 = mapOf(
    ("R8,U5,L5,D3" to "U7,R6,D4,L4") to 30,
    ("R75,D30,R83,U83,L12,D49,R71,U7,L72" to "U62,R66,U55,R34,D71,R55,D58,R83") to 610,
    ("R98,U47,R26,D63,R33,U87,L62,D20,R33,U53,R51" to "U98,R91,D20,R16,D67,R40,U7,R15,U6,R7") to 410
)

private fun test2() {
    testCases2.forEach { (input, output) ->
        check(output == calculateLength(input.first, input.second))
    }
}

fun main() {
    test1()
    test2()

    val input = readInput(2019, 3)
    println(calculateDistance(input[0], input[1]))
    println(calculateLength(input[0], input[1]))
}