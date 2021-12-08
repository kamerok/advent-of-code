package year2019.day04

private fun checkNumber(number: Long): Boolean {
    var hasDouble = false
    var previousInt = number.toString()[0].toString().toInt()
    number.toString().substring(1).forEach { char ->
        val int = char.toString().toInt()
        if (previousInt == int) hasDouble = true
        if (int < previousInt) {
            return false
        }
        previousInt = int
    }
    return hasDouble
}

private fun checkNumber2(number: Long): Boolean {
    var hasDouble = false
    var previousInt = number.toString()[0].toString().toInt()
    var count = 1
    number.toString().substring(1).forEach { char ->
        val int = char.toString().toInt()
        if (previousInt == int) {
            count++
        } else {
            if (count == 2) hasDouble = true
            count = 1
        }
        if (int < previousInt) {
            return false
        }
        previousInt = int
    }
    return hasDouble || count == 2
}

private val testCases1 = mapOf(
    111111L to true,
    223450L to false,
    123789L to false
)

private fun test1() {
    testCases1.forEach { (input, output) ->
        check(output == checkNumber(input))
    }
}

private val testCases2 = mapOf(
    112233L to true,
    123444L to false,
    111122L to true
)

private fun test2() {
    testCases2.forEach { (input, output) ->
        check(output == checkNumber2(input))
    }
}

fun main() {
    test1()
    test2()

    println((256310L..732736L).count { checkNumber(it) })
    println((256310L..732736L).count { checkNumber2(it) })
}