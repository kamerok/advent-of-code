package year2015.day12

import readInput

fun main() {
    val input = readInput(2015, 12).first()

    check(part1("[1,2,3]") == 6)
    check(part1("{\"a\":2,\"b\":4}") == 6)
    check(part1("6") == 6)
    check(part1("\"a\"") == 0)
    check(part1("[[[3]]]") == 3)
    check(part1("{\"a\":{\"b\":4},\"c\":-1}") == 3)
    check(part1("{\"a\":[-1,1]}") == 0)
    check(part1("[-1,{\"a\":1}]") == 0)
    check(part1("[]") == 0)
    check(part1("{}") == 0)

    println(part1(input))

    check(part2("[1,2,3]") == 6)
    check(part2("[1,{\"c\":\"red\",\"b\":2},3]") == 4)
    check(part2("{\"d\":\"red\",\"e\":[1,2,3,4],\"f\":5}") == 0)
    check(part2("[1,\"red\",5]") == 6)

    println(part2(input))
}

private fun part1(input: String): Int = input.parse().sumNumbers()

private fun part2(input: String): Int = input.parse().sumNumbers { element ->
    element !is Element.Object || !element.map.containsValue(Element.Text("red"))
}

private fun String.parse(): Element = parse(0).first

private fun String.parse(startIndex: Int): Pair<Element, Int> = when {
    get(startIndex) == '[' -> parseArray(startIndex)
    get(startIndex) == '{' -> parseObject(startIndex)
    get(startIndex) == '"' -> parseText(startIndex)
    else -> parseNumber(startIndex)
}

private fun String.parseArray(startIndex: Int): Pair<Element.Array, Int> {
    var index = startIndex + 1
    val result = mutableListOf<Element>()
    while (get(index) != ']') {
        val (element, advance) = parse(index)
        result.add(element)
        index += advance
        if (get(index) != ']') {
            index++//comma
        }
    }
    return Element.Array(result) to index - startIndex + 1
}

private fun String.parseObject(startIndex: Int): Pair<Element.Object, Int> {
    var index = startIndex + 1
    val result = mutableMapOf<String, Element>()
    while (get(index) != '}') {
        val (nameElement, nameAdvance) = parseText(index)
        index += nameAdvance
        index += 1 //colon
        val (element, advance) = parse(index)
        index += advance
        if (get(index) == ',') {
            index++//comma
        }
        result[nameElement.value] = element
    }
    return Element.Object(result) to index - startIndex + 1
}

private fun String.parseText(startIndex: Int): Pair<Element.Text, Int> {
    var index = startIndex + 1
    val string = buildString {
        while (this@parseText[index] != '"') {
            append(this@parseText[index])
            index++
        }
    }
    return Element.Text(string) to string.length + 2
}

private fun String.parseNumber(startIndex: Int): Pair<Element.Number, Int> {
    var index = startIndex
    val string = buildString {
        if (this@parseNumber.getOrNull(index) == '-') {
            append(this@parseNumber[index])
            index++
        }
        while (this@parseNumber.getOrNull(index)?.isDigit() == true) {
            append(this@parseNumber[index])
            index++
        }
    }
    return Element.Number(string.toInt()) to string.length
}

private fun Element.sumNumbers(predicate: (Element) -> Boolean = { true }): Int {
    if (!predicate(this)) return 0
    return when (this) {
        is Element.Array -> sumNumbers(predicate)
        is Element.Object -> sumNumbers(predicate)
        is Element.Number -> value
        is Element.Text -> 0
    }
}

private fun Element.Array.sumNumbers(predicate: (Element) -> Boolean): Int =
    elements.sumOf { it.sumNumbers(predicate) }

private fun Element.Object.sumNumbers(predicate: (Element) -> Boolean): Int =
    map.values.sumOf { it.sumNumbers(predicate) }


sealed class Element {
    data class Array(val elements: List<Element>) : Element()
    data class Object(val map: Map<String, Element>) : Element()
    data class Number(val value: Int) : Element()
    data class Text(val value: String) : Element()
}
