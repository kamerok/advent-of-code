package year2019.day01

import readInput

fun calculateFuel(mass: Long): Long = mass / 3 - 2

fun calculateTotalFuel(mass: Long): Long {
    var totalFuel = 0L
    var nextMass = mass
    while (true) {
        val fuel = calculateFuel(nextMass)
        if (fuel <= 0) break
        totalFuel += fuel
        nextMass = fuel
    }
    return totalFuel
}

private val testCases1 = mapOf(
    12L to 2L,
    14L to 2L,
    1969L to 654L,
    100756L to 33583L
)

private fun test1() {
    testCases1.forEach { (mass, fuel) ->
        check(fuel == calculateFuel(mass))
    }
}

private val testCases2 = mapOf(
    14L to 2L,
    1969L to 966L,
    100756L to 50346L
)

private fun test2() {
    testCases2.forEach { (mass, fuel) ->
        check(fuel == calculateTotalFuel(mass))
    }
}

fun main() {
    test1()
    test2()

    val mass = readInput(2019, 1).map { it.toLong() }

    println(mass.sumOf { calculateFuel(it) })
    println(mass.sumOf { calculateTotalFuel(it) })
}