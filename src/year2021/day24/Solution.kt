package year2021.day24

import readInput
import java.math.BigDecimal
import java.util.ArrayDeque

fun main() {
    val input = readInput(2021, 24).parse()

    println(part1(input))

//    println(part2(input))
}

private fun part1(input: List<Command>): String {
    val executor = Executor(input)
    var number: Long = 99999999999999
    while (number > 0) {
        val percent = BigDecimal.ONE - (number.toBigDecimal() / 99999999999999.toBigDecimal())
        println("${percent.setScale(5)}")
        val numberText = number.toString()
        if (numberText.none { it == '0' }) {
            var cursor = 0
            if (executor.execute { numberText[cursor].digitToInt().also { cursor++ } } == 0) {
                return numberText
            }
        }
        number--
    }
    return "not found"
}

private fun part2(input: List<Context>): Int {
    return 0
}

private fun List<String>.parse(): List<Command> =
    map { line ->
        if (line.startsWith("inp")) {
            Input(Variable.valueOf(line.last().uppercaseChar().toString()))
        } else {
            val (operationName, variable, value) = line.split(" ")
            Operation(
                on = VariableArgument(Variable.valueOf(variable.uppercase())),
                with = runCatching {
                    ValueArgument(value.toInt())
                }.getOrNull() ?: VariableArgument(Variable.valueOf(value.uppercase())),
                type = when (operationName) {
                    "add" -> OperationType.ADD
                    "mul" -> OperationType.MULTIPLY
                    "div" -> OperationType.DIVIDE
                    "mod" -> OperationType.MOD
                    else -> OperationType.EQUALS
                }
            )
        }
    }

private data class Executor(private val operations: List<Command>) {

    private val cache = mutableMapOf<Pair<Context, Int>, Int>()

    fun execute(readValue: () -> Int): Int {
        var context = Context()
        val contexts = mutableListOf(Context())
        operations.forEachIndexed { index, command ->
            cache[context to index]?.let { result ->
                println("hit")
                if (index > 0) {
                    contexts.forEachIndexed { i, c -> cache[c to i] = result }
                }
                return result
            }
            context = when (command) {
                is Input -> context.set(command.variable, readValue())
                is Operation -> command.execute(context)
            }
            contexts.add(context)
        }
        return (context.values[Variable.Z]
            ?: 0).also { contexts.forEachIndexed { i, c -> cache[c to i] = it } }
    }

    private fun Operation.execute(context: Context): Context =
        context.set(on.variable, performOperation(context.resolve(on), context.resolve(with)))

    private fun Operation.performOperation(a: Int, b: Int): Int = when (type) {
        OperationType.ADD -> a + b
        OperationType.MULTIPLY -> a * b
        OperationType.DIVIDE -> a / b
        OperationType.MOD -> a.mod(b)
        OperationType.EQUALS -> if (a == b) 1 else 0
    }
}

private data class Context(
    val values: Map<Variable, Int> = mapOf()
) {

    fun set(variable: Variable, value: Int): Context =
        Context(values.plus(variable to value))

    fun resolve(argument: Argument): Int = when (argument) {
        is ValueArgument -> argument.value
        is VariableArgument -> values.getOrDefault(argument.variable, 0)
    }
}

private sealed class Command

private data class Input(
    val variable: Variable
) : Command()

private data class Operation(
    val on: VariableArgument,
    val with: Argument,
    val type: OperationType
) : Command()

private enum class OperationType {
    ADD, MULTIPLY, DIVIDE, MOD, EQUALS
}

private sealed class Argument

private data class VariableArgument(
    val variable: Variable
) : Argument()

private data class ValueArgument(
    val value: Int
) : Argument()

private enum class Variable {
    W, X, Y, Z
}
