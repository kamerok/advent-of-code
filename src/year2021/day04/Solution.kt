package year2021.day04

import readInput

fun main() {
    fun readNumbersAndCards(input: List<String>): Pair<List<Int>, List<Card>> {
        val numbers = input.first().split(",").map { it.toInt() }
        val cards: List<Card> = input.drop(1).chunked(6) { chunk ->
            //skip new line
            val cardData = (1..5).map { i ->
                Regex("\\d+").findAll(chunk[i]).map { it.value.toInt() }.toMutableList()
            }
            Card(cardData)
        }
        return Pair(numbers, cards)
    }

    fun part1(input: List<String>): Int {
        val (numbers, cards) = readNumbersAndCards(input)
        numbers.forEach { number ->
            cards.forEach { card ->
                if (card.applyNumberAndCheck(number)) {
                    return card.uncheckedSum() * number
                }
            }
        }
        return 0
    }

    fun part2(input: List<String>): Int {
        val (numbers, cards) = readNumbersAndCards(input)
        val remainingCards = cards.toMutableList()
        var lastWin: Pair<Card, Int>? = null
        numbers.forEach { number ->
            remainingCards.removeIf { card ->
                val cardComplete = card.applyNumberAndCheck(number)
                if (cardComplete) {
                    lastWin = card to number
                }
                cardComplete
            }
        }
        return lastWin?.let { it.first.uncheckedSum() * it.second } ?: 0
    }

    val testInput = readInput(2021, 4, "test")
    val input = readInput(2021, 4)

    check(part1(testInput) == 4512)
    println(part1(input))

    check(part2(testInput) == 1924)
    println(part2(input))
}

class Card(
    private val numbers: List<MutableList<Int>>
) {
    private var sum = numbers.sumOf { it.sum() }

    fun applyNumberAndCheck(number: Int): Boolean {
        numbers.forEachIndexed { i, row ->
            row.forEachIndexed { j, x ->
                if (x == number) {
                    sum -= number
                    numbers[i][j] = -1
                    if (checkForWin(i, j)) return true
                }
            }
        }
        return false
    }

    fun uncheckedSum(): Int = sum

    private fun checkForWin(i: Int, j: Int): Boolean = checkRowForWin(i) || checkColumnForWin(j)

    private fun checkRowForWin(i: Int): Boolean = numbers[i].find { it != -1 } == null

    private fun checkColumnForWin(j: Int): Boolean = numbers.find { it[j] != -1 } == null

}