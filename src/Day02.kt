fun main() {
    fun parseCommands(input: List<String>) = input.map {
        val words = it.split(' ')
        val direction = words.first().let { word ->
            when (word) {
                "up" -> Direction.UP
                "down" -> Direction.DOWN
                else -> Direction.FORWARD
            }
        }
        val distance = words[1].toInt()
        Command(direction, distance)
    }

    fun part1(input: List<String>): Int {
        val commands: List<Command> = parseCommands(input)
        var horizontal = 0
        var depth = 0
        for (command in commands) {
            when (command.direction) {
                Direction.UP -> depth -= command.value
                Direction.DOWN -> depth += command.value
                Direction.FORWARD -> horizontal += command.value
            }
        }
        return depth * horizontal
    }

    fun part2(input: List<String>): Int {
        val commands: List<Command> = parseCommands(input)
        var aim = 0
        var horizontal = 0
        var depth = 0
        for (command in commands) {
            when (command.direction) {
                Direction.UP -> aim -= command.value
                Direction.DOWN -> aim += command.value
                Direction.FORWARD -> {
                    horizontal += command.value
                    depth += aim * command.value
                }
            }
        }
        return depth * horizontal
    }

    check(part1(readInput("Day02_test")) == 150)
    println(part1(readInput("Day02")))

    check(part2(readInput("Day02_test")) == 900)
    println(part2(readInput("Day02")))
}

data class Command(
    val direction: Direction,
    val value: Int
)

enum class Direction {
    UP, DOWN, FORWARD
}
