package year2021.day21

import readInput

fun main() {
    val testInput = readInput(2021, 21, "test").parse()
    val input = readInput(2021, 21).parse()

    check(part1(testInput) == 739785)
    println(part1(input))

    check(part2(testInput) == 444356092776315)
    println(part2(input))
}

private fun part1(input: List<Int>): Int {
    var players = input.mapIndexed { index, i ->
        Player(id = index, position = i, score = 0)
    }
    var dice = 0
    var rolls = 0
    while (true) {
        players = players.map { player ->
            var advance = 0
            repeat(3) {
                dice++
                if (dice > 100) {
                    dice = 1
                }
                rolls++
                advance += dice
            }
            val newPlayer = player.advance(advance)
            if (newPlayer.score >= 1000) {
                return players.minus(player).minOf { it.score } * rolls
            }
            newPlayer
        }
    }
}

private fun part2(input: List<Int>): Long {
    val players = input.mapIndexed { index, i ->
        Player(id = index, position = i, score = 0)
    }
    return findPlayerWins(GameState(true, players[0], players[1])).maxOf { it.value }
}

private fun List<String>.parse(): List<Int> = map { it.split(" ").last().toInt() }

private fun Player.advance(steps: Int): Player {
    var newPosition = position + steps
    if (newPosition > 10) {
        newPosition = (newPosition - 1).mod(10) + 1
    }
    val newScore = score + newPosition
    return copy(position = newPosition, score = newScore)
}

private val variants: List<Int> = listOf(1, 2, 3).flatMap { first ->
    listOf(1, 2, 3).map { it + first }
}.flatMap { first ->
    listOf(1, 2, 3).map { it + first }
}

private fun findPlayerWins(
    gameState: GameState,
    cache: MutableMap<GameState, Map<Int, Long>> = mutableMapOf()
): Map<Int, Long> {
    cache[gameState]?.let { return it }
    val winner = when {
        gameState.firstPlayer.score >= 21 -> gameState.firstPlayer
        gameState.secondPlayer.score >= 21 -> gameState.secondPlayer
        else -> null
    }
    if (winner != null) {
        return mapOf(winner.id to 1)
    }
    val result = mutableMapOf<Int, Long>()
    variants
        .asSequence()
        .map { roll ->
            if (gameState.isFirstPlayerMove) {
                gameState.copy(
                    isFirstPlayerMove = false,
                    firstPlayer = gameState.firstPlayer.advance(roll)
                )
            } else {
                gameState.copy(
                    isFirstPlayerMove = true,
                    secondPlayer = gameState.secondPlayer.advance(roll)
                )
            }
        }
        .map { findPlayerWins(it, cache) }
        .forEach {
            it.forEach { (playerId, wins) ->
                result[playerId] = result.getOrDefault(playerId, 0) + wins
            }
        }
    return result.also { cache[gameState] = it }
}

private data class Player(
    val id: Int,
    val position: Int,
    val score: Int
)

private data class GameState(
    val isFirstPlayerMove: Boolean,
    val firstPlayer: Player,
    val secondPlayer: Player
)
