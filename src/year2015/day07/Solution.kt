package year2015.day07

import readInput

fun main() {
    val input = readInput(2015, 7).map { line ->
        val (commandPart, resultWire) = line.split(" -> ")
        val command = commandPart.split(' ')
        when {
            command.size == 1 -> Command.Assign(command.first(), resultWire)
            command.size == 2 -> Command.Not(command[1], resultWire)
            command[1] == "AND" -> Command.And(command[0], command[2], resultWire)
            command[1] == "OR" -> Command.Or(command[0], command[2], resultWire)
            command[1] == "LSHIFT" -> Command.LShift(command[0], command[2].toInt(), resultWire)
            else -> Command.RShift(command[0], command[2].toInt(), resultWire)
        }
    }

    val signal = getSignal(input)
    println(signal)
    println(getSignal(input, "b" to signal))
}

private fun getSignal(input: List<Command>, override: Pair<String, Int>? = null): Int {
    val commands = input.toMutableList()

    val signals = mutableMapOf<String, Int>()
    fun String.isExecutable(): Boolean = toIntOrNull() != null || signals.contains(this)
    fun String.getInt(): Int = toIntOrNull() ?: signals.getValue(this)
    var lastSize = Int.MAX_VALUE
    while (commands.isNotEmpty() && lastSize != commands.size) {
        lastSize = commands.size
        commands.removeIf { command ->
            val executable = when (command) {
                is Command.Assign -> command.value.isExecutable()
                is Command.Not -> command.value.isExecutable()
                is Command.And -> command.a.isExecutable() && command.b.isExecutable()
                is Command.Or -> command.a.isExecutable() && command.b.isExecutable()
                is Command.LShift -> command.value.isExecutable()
                is Command.RShift -> command.value.isExecutable()
            }
            if (executable) {
                signals[command.resultWire] =
                    if (override != null && override.first == command.resultWire) {
                        override.second
                    } else {
                        when (command) {
                            is Command.Assign -> command.value.getInt()
                            is Command.Not -> command.value.getInt().inv()
                            is Command.And -> command.a.getInt() and command.b.getInt()
                            is Command.Or -> command.a.getInt() or command.b.getInt()
                            is Command.LShift -> command.value.getInt() shl command.bits
                            is Command.RShift -> command.value.getInt() shr command.bits
                        }
                    }
            }
            executable
        }
    }
    return signals.getValue("a")
}

private sealed class Command(open val resultWire: String) {
    data class Assign(
        val value: String,
        override val resultWire: String
    ) : Command(resultWire)

    data class Not(
        val value: String,
        override val resultWire: String
    ) : Command(resultWire)

    data class And(
        val a: String,
        val b: String,
        override val resultWire: String
    ) : Command(resultWire)

    data class Or(
        val a: String,
        val b: String,
        override val resultWire: String
    ) : Command(resultWire)

    data class LShift(
        val value: String,
        val bits: Int,
        override val resultWire: String
    ) : Command(resultWire)

    data class RShift(
        val value: String,
        val bits: Int,
        override val resultWire: String
    ) : Command(resultWire)
}
