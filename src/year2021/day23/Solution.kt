package year2021.day23

import readInput
import java.util.ArrayDeque

fun main() {
    val testInput = readInput(2021, 23, "test").parse()
    val input = readInput(2021, 23).parse()

    check(part1(testInput).also { println(it) } == 12521)
    println("check")
    println(part1(input))

//    check(part2(testInput) == 2758514936282235)
//    println(part2(input))
}

private fun part1(input: State): Int = getMinRemainingScore(input) ?: 0

private fun part2(input: State): Int {
    return 0
}

var count = 0

private fun getMinRemainingScore(
    state: State,
    depth: Int = 0,
    cache: MutableMap<State, Int?> = mutableMapOf()
): Int? {
    cache[state]?.let { return it }
    if (state.isFinal()) return 0
    val possibleMoves = state.possibleMoves()
    return possibleMoves.also {
        if (count < 20) {
            count++
//            state.prettyPrint()
//            println(it)
        }
    }.mapIndexedNotNull { index, (from, to) ->
        if (depth <= 1) {
            println("depth: $depth progress: $index of ${possibleMoves.size}")
        }
        val (newState, cost) = state.performMove(from, to)
        val minScore = getMinRemainingScore(newState, depth + 1, cache)
        minScore?.let { it + cost }
    }.minOrNull().also { cache[state] = it }
}

private fun List<String>.parse(): State = State(
    state = listOf(
        Box(),
        Box(),
        Room(
            target = Amphipod.A,
            stack = ArrayDeque<Amphipod>(2).apply {
                push(Amphipod.valueOf(this@parse[3][3].toString()))
                push(Amphipod.valueOf(this@parse[2][3].toString()))
            }
        ),
        Box(),
        Room(
            target = Amphipod.B,
            stack = ArrayDeque<Amphipod>(2).apply {
                push(Amphipod.valueOf(this@parse[3][5].toString()))
                push(Amphipod.valueOf(this@parse[2][5].toString()))
            }),
        Box(),
        Room(
            target = Amphipod.C,
            stack = ArrayDeque<Amphipod>(2).apply {
                push(Amphipod.valueOf(this@parse[3][7].toString()))
                push(Amphipod.valueOf(this@parse[2][7].toString()))
            }),
        Box(),
        Room(
            target = Amphipod.D,
            stack = ArrayDeque<Amphipod>(2).apply {
                push(Amphipod.valueOf(this@parse[3][9].toString()))
                push(Amphipod.valueOf(this@parse[2][9].toString()))
            }),
        Box(),
        Box()
    )
)

private val amphipodToRoomMap = mapOf(
    Amphipod.A to 2,
    Amphipod.B to 4,
    Amphipod.C to 6,
    Amphipod.D to 8
)

private data class State(val state: List<Element>) {
    fun possibleStart(): List<Int> = state.indices.filter {
        when (val element = state[it]) {
            is Room -> element.stack.isNotEmpty()
            is Box -> element.amphipod != null
        }
    }

    fun possibleFinish(start: Int): List<Int> {
        val element = state[start]
        val amphipod = when (element) {
            is Box -> element.amphipod!!
            is Room -> element.stack.peek()
        }
        val targetIndex = amphipodToRoomMap.getValue(amphipod)
        val targetElement = state[targetIndex] as Room
        if (targetIndex == start) {
            if (targetElement.stack.size == 1) {
                return emptyList()
            }
            if (targetElement.stack.size == 2) {
                val copy = ArrayDeque(targetElement.stack)
                copy.pop()
                if (copy.peek() == amphipod) {
                    return emptyList()
                }
            }
        }
        if (element is Box) {
            val path = if (targetIndex > start) {
                state.subList(start + 1, targetIndex + 1)
            } else {
                state.subList(targetIndex, start).reversed()
            }
            val isPathAvailable = path.all { element ->
                when (element) {
                    is Box -> element.amphipod == null
                    is Room -> element.target != amphipod ||
                            element.stack.isEmpty() ||
                            (element.stack.size == 1 && element.stack.peek() == element.target)
                }
            }
            if (isPathAvailable) return listOf(targetIndex)
        } else {
            return state.indices.filter { possibleTarget ->
                val targetElement = state[possibleTarget]
                if (targetElement is Box) {
                    val path = if (possibleTarget > start) {
                        state.subList(start + 1, possibleTarget + 1)
                    } else {
                        state.subList(possibleTarget, start).reversed()
                    }
                    val isPathAvailable = path.all {
                        it is Room || (it is Box && it.amphipod == null)
                    }
                    isPathAvailable
                } else {
                    false
                }
            }
        }
        return emptyList()
    }

    fun possibleMoves(): List<Pair<Int, Int>> = possibleStart().flatMap { start ->
        possibleFinish(start).map { start to it }
    }

    fun performMove(from: Int, to: Int): Pair<State, Int> {
        val element = state[from]
        val (amphipod, isBox) = when (element) {
            is Box -> element.amphipod!! to true
            is Room -> element.stack.peek() to false
        }
        val path = if (to > from) {
            state.subList(from + 1, to + 1)
        } else {
            state.subList(to, from).reversed()
        }
        var points = 0
        //walk out of room
        if (element is Room) {
            val steps = if (element.stack.size == 1) 2 else 1
            points += steps * amphipod.weight
        }
        //walk
        points += path.size * amphipod.weight
        //walk in the room
        val targetElement = state[to]
        if (targetElement is Room) {
            val steps = if (targetElement.stack.size == 1) 1 else 2
            points += steps * amphipod.weight
        }
        return State(
            state = state.mapIndexed { index, e ->
                when {
                    index == from && e is Box -> Box()
                    index == from && e is Room -> Room(
                        e.target,
                        ArrayDeque(e.stack).apply { pop() })
                    index == to && e is Box -> Box(amphipod)
                    index == to && e is Room -> Room(
                        e.target,
                        ArrayDeque(e.stack).apply { push(amphipod) }
                    )
                    else -> e
                }
            }
        ) to points
    }

    fun prettyPrint() {
        println("#############")

        print("#")
        print(state.map {
            when (it) {
                is Box -> if (it.amphipod == null) "." else it.amphipod.name
                is Room -> "."
            }
        }.joinToString(separator = ""))
        println("#")

        print("###")
        val stackCopy1 = ArrayDeque((state[2] as Room).stack)
        print(if (stackCopy1.size == 2) stackCopy1.pop().name else ".")
        print("#")
        val stackCopy2 = ArrayDeque((state[4] as Room).stack)
        print(if (stackCopy2.size == 2) stackCopy2.pop().name else ".")
        print("#")
        val stackCopy3 = ArrayDeque((state[6] as Room).stack)
        print(if (stackCopy3.size == 2) stackCopy3.pop().name else ".")
        print("#")
        val stackCopy4 = ArrayDeque((state[8] as Room).stack)
        print(if (stackCopy4.size == 2) stackCopy4.pop().name else ".")
        println("###")

        print("  #")
        print(if (stackCopy1.size == 1) stackCopy1.pop().name else ".")
        print("#")
        print(if (stackCopy2.size == 1) stackCopy2.pop().name else ".")
        print("#")
        print(if (stackCopy3.size == 1) stackCopy3.pop().name else ".")
        print("#")
        print(if (stackCopy4.size == 1) stackCopy4.pop().name else ".")
        println("#")

        println("  #########")
    }

    fun isFinal(): Boolean = state.all { it.isFinal }
}

private sealed class Element {
    abstract val isFinal: Boolean
}

private data class Box(
    val amphipod: Amphipod? = null
) : Element() {
    override val isFinal: Boolean = amphipod == null
}

private data class Room(
    val target: Amphipod,
    val stack: ArrayDeque<Amphipod>
) : Element() {
    override val isFinal: Boolean = ArrayDeque(stack).let {
        it.size == 2 && it.pop() == target && it.pop() == target
    }
}

private enum class Amphipod(val weight: Int) {
    A(1), B(10), C(100), D(1000)
}
