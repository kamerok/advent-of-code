fun main() {
    fun part1(input: List<String>): Int {
        val depth = input.map { it.toInt() }
        return depth.zipWithNext().count { it.second > it.first }
    }

    fun part2(input: List<String>): Int {
        val depth = input.map { it.toInt() }
        return depth.windowed(3).zipWithNext().count { it.second.sum() > it.first.sum() }
    }

    check(part1(readInput("Day01_test")) == 7)
    println(part1(readInput("Day01")))

    check(part2(readInput("Day01_test")) == 5)
    println(part2(readInput("Day01")))
}
