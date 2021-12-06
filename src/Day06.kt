fun main() {
    fun calculateFishCount(timers: List<Int>, days: Int): Long {
        val timerCounts = LongArray(9)
        timers.forEach { timerCounts[it]++ }
        repeat(days) {
            val first = timerCounts[0]
            (0..7).forEach { i ->
                timerCounts[i] = timerCounts[i + 1]
            }
            timerCounts[6] += first
            timerCounts[8] = first
        }
        return timerCounts.sum()
    }

    fun part1(input: List<String>): Long =
        calculateFishCount(
            timers = input.first().split(",").map { it.toInt() },
            days = 80
        )

    fun part2(input: List<String>): Long =
        calculateFishCount(
            timers = input.first().split(",").map { it.toInt() },
            days = 256
        )

    val testInput = readInput("Day06_test")
    val input = readInput("Day06")

    check(part1(testInput) == 5934L)
    println(part1(input))

    check(part2(testInput) == 26984457539L)
    println(part2(input))
}
