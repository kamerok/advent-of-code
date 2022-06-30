package year2021.day19

import readInput

fun main() {
    val simpleInput = readInput(2021, 19, "simple").parse()
    val testInput = readInput(2021, 19, "test").parse()
    val input = readInput(2021, 19).parse()

    check(part1(testInput) == 79)
    println(part1(input))

//    check(part2(testInput) == 3993)
//    println(part2(input))
}

private fun part1(scanners: List<Scanner>): Int {
    val knownScanners = mutableListOf(scanners.first())
    val unknownScanners = scanners.drop(1).toMutableList()
    while (unknownScanners.isNotEmpty()) {
        val known = knownScanners.toList()
        val unknown = unknownScanners.toList()
        unknown.forEach { scanner ->
            known.forEach { reference ->
                scanner.normalizeOrNull(reference)?.let { normalized ->
                    unknownScanners.remove(scanner)
                    knownScanners.add(normalized)
                }
            }
        }
        println("${unknownScanners.size} -> ${knownScanners.size}")
    }
    return knownScanners.allPoints().size
}

private fun part2(scanners: List<Scanner>): Int {

    return 0
}

private fun List<String>.parse(): List<Scanner> {
    val result = mutableListOf<Scanner>()
    val iterator = iterator()
    var scannerPoints = mutableListOf<Point>()
    var id = 0
    while (iterator.hasNext()) {
        val line = iterator.next()
        when {
            line.isEmpty() -> {
                result.add(Scanner("$id", scannerPoints))
                scannerPoints = mutableListOf()
            }
            line.startsWith("---") -> {
                id = line.split(' ')[2].toInt()
            }
            !line.startsWith("---") -> {
                val (x, y, z) = line.split(',').map { it.toInt() }
                scannerPoints.add(Point(x, y, z))
            }
        }
    }
    result.add(Scanner("$id", scannerPoints))
    return result
}

private fun Scanner.normalizeOrNull(reference: Scanner): Scanner? {
    everyOrientation().forEach { orientedScanner ->
        val translation = orientedScanner.findTranslationOrNull(reference)
        if (translation != null) {
            return orientedScanner.translate(translation)
        }
    }
    return null
}

private fun Scanner.findTranslationOrNull(reference: Scanner): Translation? {
    val translationMap = mutableMapOf<Translation, Int>()
    points.forEach { point ->
        reference.points.forEach { refPoint ->
            val translation = Translation(
                refPoint.x - point.x,
                refPoint.y - point.y,
                refPoint.z - point.z
            )
            translationMap[translation] = translationMap.getOrDefault(translation, 0) + 1
        }
    }
    val maxTranslation = translationMap.maxByOrNull { (_, points) -> points }
    return if (maxTranslation == null || maxTranslation.value < 12) null else maxTranslation.key
}

private fun Collection<Scanner>.allPoints(): Set<Point> = fold(mutableSetOf()) { acc, scanner ->
    acc.also { acc.addAll(scanner.points) }
}

private data class Point(val x: Int, val y: Int, val z: Int) {
    override fun toString(): String = "[$x,$y,$z]"
}

private data class Scanner(
    val id: String,
    val points: List<Point>
) {
    fun everyOrientation(): List<Scanner> = CoordinateSystem.all.map { coordinateSystem ->
        copy(points = points.map { point ->
            Point(
                x = point.get(coordinateSystem.x),
                y = point.get(coordinateSystem.y),
                z = point.get(coordinateSystem.z),
            )
        })
    }

    fun translate(translation: Translation): Scanner = copy(points =
    points.map { point ->
        Point(
            point.x + translation.x,
            point.y + translation.y,
            point.z + translation.z
        )
    }
    )
}

private data class Translation(
    val x: Int,
    val y: Int,
    val z: Int
)

private data class CoordinateSystem(
    val x: Direction,
    val y: Direction,
    val z: Direction
) {
    companion object {
        val all = listOf(
            //z is up
            CoordinateSystem(Direction.X, Direction.Y, Direction.Z),
            CoordinateSystem(Direction.NEGATIVE_Y, Direction.X, Direction.Z),
            CoordinateSystem(Direction.NEGATIVE_X, Direction.NEGATIVE_Y, Direction.Z),
            CoordinateSystem(Direction.Y, Direction.NEGATIVE_X, Direction.Z),
            //z is down
            CoordinateSystem(Direction.Y, Direction.X, Direction.NEGATIVE_Z),
            CoordinateSystem(Direction.NEGATIVE_X, Direction.Y, Direction.NEGATIVE_Z),
            CoordinateSystem(Direction.NEGATIVE_Y, Direction.NEGATIVE_X, Direction.NEGATIVE_Z),
            CoordinateSystem(Direction.X, Direction.NEGATIVE_Y, Direction.NEGATIVE_Z),
            //x is up
            CoordinateSystem(Direction.Y, Direction.Z, Direction.X),
            CoordinateSystem(Direction.NEGATIVE_Z, Direction.Y, Direction.X),
            CoordinateSystem(Direction.NEGATIVE_Y, Direction.NEGATIVE_Z, Direction.X),
            CoordinateSystem(Direction.Z, Direction.NEGATIVE_Y, Direction.X),
            //x is down
            CoordinateSystem(Direction.Y, Direction.Z, Direction.NEGATIVE_X),
            CoordinateSystem(Direction.NEGATIVE_Z, Direction.Y, Direction.NEGATIVE_X),
            CoordinateSystem(Direction.NEGATIVE_Y, Direction.NEGATIVE_Z, Direction.NEGATIVE_X),
            CoordinateSystem(Direction.Z, Direction.NEGATIVE_Y, Direction.NEGATIVE_X),
            //y is up
            CoordinateSystem(Direction.Z, Direction.X, Direction.Y),
            CoordinateSystem(Direction.NEGATIVE_X, Direction.Z, Direction.Y),
            CoordinateSystem(Direction.NEGATIVE_Z, Direction.NEGATIVE_X, Direction.Y),
            CoordinateSystem(Direction.X, Direction.NEGATIVE_Z, Direction.Y),
            //y is down
            CoordinateSystem(Direction.Z, Direction.X, Direction.NEGATIVE_Y),
            CoordinateSystem(Direction.NEGATIVE_X, Direction.Z, Direction.NEGATIVE_Y),
            CoordinateSystem(Direction.NEGATIVE_Z, Direction.NEGATIVE_X, Direction.NEGATIVE_Y),
            CoordinateSystem(Direction.X, Direction.NEGATIVE_Z, Direction.NEGATIVE_Y),
        )
    }
}

private enum class Direction {
    X, NEGATIVE_X, Y, NEGATIVE_Y, Z, NEGATIVE_Z
}

private fun Point.get(direction: Direction) = when (direction) {
    Direction.X -> x
    Direction.NEGATIVE_X -> -x
    Direction.Y -> y
    Direction.NEGATIVE_Y -> -y
    Direction.Z -> z
    Direction.NEGATIVE_Z -> -z
}
