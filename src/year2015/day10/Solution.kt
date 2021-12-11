package year2015.day10

fun main() {
    val input = "3113322113"

    println(part1(input, 40))
    println(part1(input, 50))
}

private fun part1(input: String, steps: Int): Int {
    var result = input
    repeat(steps) {
        result = transform(result)
    }
    return result.length
}

private fun transform(input: String): String = buildString {
    var countingChar = input.first()
    var count = 0
    input.forEach { c ->
        if (countingChar == c) {
            count++
        } else {
            append(count)
            append(countingChar)
            count = 1
            countingChar = c
        }
    }
    if (count != 0) {
        append(count)
        append(countingChar)
    }
}
