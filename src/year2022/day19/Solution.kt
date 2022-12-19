package year2022.day19

import readInput
import kotlin.math.pow

fun main() {
    val testInput = readInput(2022, 19, "test").parse()
    val input = readInput(2022, 19).parse()

    check(part1(testInput) == 33)
    println(part1(input))

    check(State(testInput.first(), 32).maxGeodes() == 56)
    check(State(testInput[1], 32).maxGeodes() == 62)
    println(part2(input))
}

private fun part1(input: List<Blueprint>): Int {
    return input.sumOf { blueprint ->
        val maxGeodes = State(blueprint).maxGeodes()
        maxGeodes * blueprint.id
    }
}

private fun part2(input: List<Blueprint>): Int {
    return input
        .take(3)
        .map {
            State(it, 32).maxGeodes()
        }
        .fold(1) { acc, value -> acc * value }
}

private fun List<String>.parse(): List<Blueprint> = map { line ->
    val words = line.split(" ")
    Blueprint(
        id = words[1].dropLast(1).toInt(),
        oreCostOre = words[6].toInt(),
        clayCostOre = words[12].toInt(),
        obsidianCostOre = words[18].toInt(),
        obsidianCostClay = words[21].toInt(),
        geodeCostOre = words[27].toInt(),
        geodeCostObsidian = words[30].toInt()
    )
}

private data class Blueprint(
    val id: Int,
    val oreCostOre: Int,
    val clayCostOre: Int,
    val obsidianCostOre: Int,
    val obsidianCostClay: Int,
    val geodeCostOre: Int,
    val geodeCostObsidian: Int
)

private val takes = mutableMapOf<Int, Int>()

private data class State(
    val blueprint: Blueprint,
    val timeLeft: Int = 24,
    val oreRobots: Int = 1,
    val clayRobots: Int = 0,
    val obsidianRobots: Int = 0,
    val geodeRobots: Int = 0,
    val ore: Int = 0,
    val clay: Int = 0,
    val obsidian: Int = 0,
    val geode: Int = 0
) {

    private fun getGeodePossible(steps: Int = 0) =
        obsidian + obsidianRobots * steps >= blueprint.geodeCostObsidian && ore + oreRobots * steps >= blueprint.geodeCostOre

    private fun getObsidianPossible(steps: Int = 0) =
        clay + clayRobots * steps >= blueprint.obsidianCostClay && ore + oreRobots * steps >= blueprint.obsidianCostOre

    private fun getClayPossible(steps: Int = 0) = blueprint.clayCostOre <= ore + oreRobots * steps
    private fun getOrePossible(steps: Int = 0) = blueprint.oreCostOre <= ore + oreRobots * steps

    fun nextSteps(): List<State> {
        if (timeLeft == 0) return listOf()
        val result = mutableListOf<State>()

        val baseState = wait()

        if (getGeodePossible()) {
            result.add(
                baseState.copy(
                    ore = baseState.ore - blueprint.geodeCostOre,
                    obsidian = baseState.obsidian - blueprint.geodeCostObsidian,
                    geodeRobots = geodeRobots + 1
                )
            )
        }

        if (result.size < 2 && getObsidianPossible()) {
            result.add(
                baseState.copy(
                    ore = baseState.ore - blueprint.obsidianCostOre,
                    clay = baseState.clay - blueprint.obsidianCostClay,
                    obsidianRobots = obsidianRobots + 1
                )
            )
        }

        if (result.size < 2 && getClayPossible()) {
            result.add(
                baseState.copy(
                    ore = baseState.ore - blueprint.clayCostOre,
                    clayRobots = clayRobots + 1
                )
            )
        }

        if (result.size < 2 && getOrePossible()) {
            result.add(
                baseState.copy(
                    ore = baseState.ore - blueprint.oreCostOre,
                    oreRobots = oreRobots + 1
                )
            )
        }

        if (result.size < 2) {
            result.add(baseState)
        }

        return result
    }

    fun maxGeodes(variants: List<Int> = emptyList()): Int {
        takes[blueprint.id] = takes.getOrDefault(blueprint.id, 0) + 1
        val newProgress = takes[blueprint.id]!! / total
        if (newProgress - progress.getOrDefault(blueprint.id, 0.0) > 0.01) {
            progress[blueprint.id] = newProgress
            println("${blueprint.id} ${(progress[blueprint.id]!! * 100).toInt()}%")
        }
        val options = nextSteps()
//        if (options.isEmpty()) println(variants.joinToString(""))
        if (options.isEmpty()) return this.geode
        return options.maxOf {
            it.maxGeodes(variants.plusElement(options.size))
        }
    }

    fun wait(): State = copy(
        timeLeft = timeLeft - 1,
        ore = ore + oreRobots,
        clay = clay + clayRobots,
        obsidian = obsidian + obsidianRobots,
        geode = geode + geodeRobots
    )

}

private val total = 2.0.pow(32.0)
private var progress = mutableMapOf<Int, Double>()
