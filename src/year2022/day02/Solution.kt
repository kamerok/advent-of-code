package year2022.day02

import readInput

fun main() {
    val testInput = readInput(2022, 2, "test")
    val input = readInput(2022, 2)

    check(part1(testInput.parse()) == 15)
    println(part1(input.parse()))

    check(part2(testInput.parse()) == 12)
    println(part2(input.parse()))
}

private fun List<String>.parse(): List<UnclearRound> =
    map { line ->
        val words = line.split(" ")
        UnclearRound(
            opponent = when (words.first()) {
                "A" -> Shape.Rock
                "B" -> Shape.Paper
                else -> Shape.Scissors
            },
            you = RoundVariable.valueOf(words.last())
        )
    }

private fun part1(input: List<UnclearRound>): Int {
    val guess = mapOf(
        RoundVariable.X to Shape.Rock,
        RoundVariable.Y to Shape.Paper,
        RoundVariable.Z to Shape.Scissors
    )
    val rounds = input.map { Round(guess.getValue(it.you), it.opponent) }
    return rounds.sumOf { it.getScore() }
}

private fun part2(input: List<UnclearRound>): Int {
    val resultMap: Map<RoundVariable, RoundResult> = mapOf(
        RoundVariable.X to RoundResult.Loose,
        RoundVariable.Y to RoundResult.Draw,
        RoundVariable.Z to RoundResult.Win
    )
    val rounds = input.map {
        val outcome = resultMap.getValue(it.you).flip()
        val you = Shape.values()[resultTable[it.opponent.ordinal].indexOf(outcome)]
        Round(you, it.opponent)
    }
    return rounds.sumOf { it.getScore() }
}

private enum class Shape {
    Rock, Paper, Scissors
}

private enum class RoundVariable {
    X, Y, Z
}

private data class Round(
    val you: Shape,
    val opponent: Shape
) {
    fun getResult(): RoundResult {
        return resultTable[you.ordinal][opponent.ordinal]
    }

    fun getScore(): Int {
        return when (you) {
            Shape.Rock -> 1
            Shape.Paper -> 2
            Shape.Scissors -> 3
        } + when (getResult()) {
            RoundResult.Win -> 6
            RoundResult.Draw -> 3
            RoundResult.Loose -> 0
        }
    }
}

private data class UnclearRound(
    val opponent: Shape,
    val you: RoundVariable
)

private enum class RoundResult {
    Win, Draw, Loose;

    fun flip(): RoundResult = when (this) {
        Win -> Loose
        Loose -> Win
        Draw -> Draw
    }
}

private val resultTable: Array<Array<RoundResult>> = arrayOf(
    arrayOf(RoundResult.Draw, RoundResult.Loose, RoundResult.Win),
    arrayOf(RoundResult.Win, RoundResult.Draw, RoundResult.Loose),
    arrayOf(RoundResult.Loose, RoundResult.Win, RoundResult.Draw)
)
