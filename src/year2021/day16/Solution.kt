package year2021.day16

import readInput

fun main() {
    val input = readInput(2021, 16).first()

    check(part1("8A004A801A8002F478") == 16)
    check(part1("620080001611562C8802118E34") == 12)
    check(part1("C0015000016115A2E0802F182340") == 23)
    check(part1("A0016C880162017C3686B18A3D4780") == 31)
    check(part1(input) == 877)

    println(part1(input))

    check(part2("C200B40A82") == 3L)
    check(part2("04005AC33890") == 54L)
    check(part2("880086C3E88112") == 7L)
    check(part2("CE00C43D881120") == 9L)
    check(part2("D8005AC2A8F0") == 1L)
    check(part2("F600BC2D8F") == 0L)
    check(part2("9C005AC2F8F0") == 0L)
    check(part2("9C0141080250320F1802104A08") == 1L)
    check(part2(input) == 194435634456L)

    println(part2(input))
}

private fun part1(input: String): Int =
    input.convert().iterator().readPacket().sumOf { it.version }

private fun part2(input: String): Long = input.convert().iterator().readPacket().value

private fun CharIterator.readPacket(): Packet {
    val version = readString(3).toInt(2)
    val typeId = readString(3).toInt(2)
    return if (typeId == 4) {
        val (value, size) = readLiteralValue()
        LiteralValue(version, typeId, size + 6, value)
    } else {
        val (packets, size) = readOperatorPackets()
        Operation(version, typeId, size + 6, packets)
    }
}

private fun CharIterator.readLiteralValue(): Pair<Long, Int> {
    val packets = mutableListOf<String>()
    var size = 1
    var flag = nextChar()
    while (flag == '1') {
        packets.add(readString(4))
        flag = nextChar()
        size += 5
    }
    packets.add(readString(4))
    size += 4
    return packets.joinToString(separator = "").toLong(2) to size
}

private fun CharIterator.readOperatorPackets(): Pair<List<Packet>, Int> {
    var size = 1
    val totalLengthType = nextChar() == '0'
    val packets = mutableListOf<Packet>()
    if (totalLengthType) {
        val totalPacketsLength = readString(15).toInt(2)
        size += 15
        var parsedSize = 0
        while (totalPacketsLength > parsedSize) {
            val packet = readPacket()
            packets.add(packet)
            size += packet.size
            parsedSize += packet.size
        }
    } else {
        val numberOfSubPackets = readString(11).toInt(2)
        size += 11
        repeat(numberOfSubPackets) {
            val packet = readPacket()
            packets.add(packet)
            size += packet.size
        }
    }
    return packets to size
}

private fun String.convert(): String = buildString {
    this@convert.forEach {
        append(it.digitToInt(16).toString(2).padStart(4, '0'))
    }
}

private fun CharIterator.readString(n: Int) = buildString { repeat(n) { append(nextChar()) } }

private fun Packet.sumOf(getValue: (Packet) -> Int): Int =
    when (this) {
        is LiteralValue -> getValue(this)
        is Operation -> this.packets.sumOf { packet -> packet.sumOf(getValue) } + getValue(this)
    }

private sealed class Packet(
    open val version: Int,
    open val typeId: Int,
    open val size: Int
) {
    abstract val value: Long
}

private data class Operation(
    override val version: Int,
    override val typeId: Int,
    override val size: Int,
    val packets: List<Packet>
) : Packet(version, typeId, size) {
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
    override val size: Int,
    override val value: Long
) : Packet(version, typeId, size)