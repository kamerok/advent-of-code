package year2015.day11

fun main() {
    val input = "vzbxkghb"

    check("hijklmmn".isCorrect().not())
    check("abbceffg".isCorrect().not())
    check("abbcegjk".isCorrect().not())
    check(part1("abcdefgh") == "abcdffaa")
    check(part1("ghijklmn") == "ghjaabcc")

    val newPassword = part1(input)
    println(newPassword)
    println(part1(newPassword))
}

private fun part1(input: String): String {
    var newPassword = input.increment()
    while (newPassword.isCorrect().not()) {
        newPassword = newPassword.increment()
    }
    return newPassword
}

private fun String.increment(): String {
    var index = lastIndex
    var result: Pair<String, Boolean> = incrementDigit(index)
    while (result.second) {
        index--
        result = result.first.incrementDigit(index)
    }
    return result.first
}

private fun String.incrementDigit(index: Int): Pair<String, Boolean> {
    if (index < 0) return buildString {
        append('a')
        append(this)
    } to false
    val currentDigit = this[index]
    var overflow = false
    val newString: String
    if (currentDigit == 'z') {
        overflow = true
        newString = String(toCharArray().apply { this[index] = 'a' })
    } else {
        newString = String(toCharArray().apply { this[index] = currentDigit + 1 })
    }
    return newString to overflow
}

private fun String.isCorrect(): Boolean =
    isAllCharsAllowed() && hasIncreasingChars() && containsTwoPairs()

private fun String.hasIncreasingChars(): Boolean {
    if (length < 3) return false
    (0..lastIndex - 2).forEach { i ->
        val firstChar = get(i)
        if (get(i + 1) == firstChar + 1 && get(i + 2) == firstChar + 2) return true
    }
    return false
}

private fun String.isAllCharsAllowed(): Boolean = !contains(Regex("[iol]"))

private fun String.containsTwoPairs(): Boolean {
    val pairsMap = mutableMapOf<Char, Int>()
    var index = 0
    while (index <= lastIndex - 1) {
        val current = get(index)
        val next = get(index + 1)
        if (current == next) {
            pairsMap[current] = pairsMap.getOrDefault(current, 0) + 1
            index++
        }
        index++
    }
    return pairsMap.values.sum() >= 2
}
