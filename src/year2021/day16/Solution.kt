package year2021.day16

import readInput

fun main() {
    val input = readInput(2021, 16).first()

    check(part1("8A004A801A8002F478") == 16)
    check(part1("620080001611562C8802118E34") == 12)
    check(part1("C0015000016115A2E0802F182340") == 23)
    check(part1("A0016C880162017C3686B18A3D4780") == 31)

    println(part1(input))

    check(part2("C200B40A82") == 3L)
    check(part2("04005AC33890") == 54L)
    check(part2("880086C3E88112") == 7L)
    check(part2("CE00C43D881120") == 9L)
    check(part2("D8005AC2A8F0") == 1L)
    check(part2("F600BC2D8F") == 0L)
    check(part2("9C005AC2F8F0") == 0L)
    check(part2("9C0141080250320F1802104A08") == 1L)

    println(part2(input))
}

private fun part1(input: String): Int =
    input.convert().parsePacket(0).first.sumOf { it.version }

private fun part2(input: String): Long = input.convert().parsePacket(0).first.value

private fun String.parsePacket(start: Int): Pair<Packet, Int> {
    val version = substring(start, start + 3).toInt(2)
    val typeId = substring(start + 3, start + 6).toInt(2)
    return if (typeId == 4) {
        val (value, newStart) = parseLiteralValue(start + 6)
        LiteralValue(version, typeId, value) to newStart
    } else {
        val (packets, newStart) = parseOperatorPackets(start + 6)
        Operation(version, typeId, packets) to newStart
    }
}

private fun String.parseLiteralValue(startIndex: Int): Pair<Long, Int> {
    val packets = mutableListOf<String>()
    var start = startIndex
    while (this[start] == '1') {
        packets.add(substring(start + 1, start + 5))
        start += 5
    }
    packets.add(substring(start + 1, start + 5))
    start += 5
    return packets.joinToString(separator = "").toLong(2) to start
}

private fun String.parseOperatorPackets(startIndex: Int): Pair<List<Packet>, Int> {
    val totalLengthType = this[startIndex] == '0'
    val packets = mutableListOf<Packet>()
    var localStartIndex = startIndex + 1
    if (totalLengthType) {
        val totalLength = substring(localStartIndex, localStartIndex + 15).toInt(2)
        localStartIndex += 15
        val estimatedNewIndex = localStartIndex + totalLength
        while (localStartIndex != estimatedNewIndex) {
            val (packet, index) = parsePacket(localStartIndex)
            packets.add(packet)
            localStartIndex = index
        }
    } else {
        val numberOfSubPackets = substring(localStartIndex, localStartIndex + 11).toInt(2)
        localStartIndex += 11
        repeat(numberOfSubPackets) {
            val (packet, index) = parsePacket(localStartIndex)
            packets.add(packet)
            localStartIndex = index
        }
    }
    return packets to localStartIndex
}

private fun String.convert(): String = buildString {
    this@convert.forEach {
        append(it.digitToInt(16).toString(2).padStart(4, '0'))
    }
}

private fun Packet.sumOf(getValue: (Packet) -> Int): Int =
    when (this) {
        is LiteralValue -> getValue(this)
        is Operation -> this.packets.sumOf { packet -> packet.sumOf(getValue) } + getValue(this)
    }

private sealed class Packet(
    open val version: Int,
    open val typeId: Int
) {
    abstract val value: Long
}

private data class Operation(
    override val version: Int,
    override val typeId: Int,
    val packets: List<Packet>
) : Packet(version, typeId) {
    override val value: Long
        get() = when (typeId) {
            0 -> packets.sumOf { it.value }
            1 -> packets.fold(1) { acc, packet -> acc * packet.value }
            2 -> packets.minOf { it.value }
            3 -> packets.maxOf { it.value }
            5 -> if (packets[0].value > packets[1].value) 1 else 0
            6 -> if (packets[0].value < packets[1].value) 1 else 0
            else -> if (packets[0].value == packets[1].value) 1 else 0
        }
}

private data class LiteralValue(
    override val version: Int = 0,
    override val typeId: Int = 0,
    override val value: Long
) : Packet(version, typeId)