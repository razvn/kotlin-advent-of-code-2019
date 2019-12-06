import java.io.File
import kotlin.math.abs
import kotlin.math.min
import kotlin.system.measureTimeMillis

fun main() {
    val inputData = Day03.inputDataFromFile("day03.txt")
    val service = Day03(inputData)
    val duration = measureTimeMillis {
        service.printResutPartOne()
        service.printResutPartTwo()
    }
    println("Duration: $duration ms")
}

class Day03(private val data: List<String>, private val prefix: String = "") {

    fun partOneMinimalDistance(data: List<String>): Int {
        val firstWirePoints = pointsFromPath(data.first().split(","))
        val secondWirePoints = pointsFromPath(data.last().split(","))

        return shortestDistanceFromPoints(firstWirePoints.keys, secondWirePoints.keys)
    }

    fun partTwoMinimalIntersection(data: List<String>): Int {
        val firstWirePoints = pointsFromPath(data.first().split(","))
        val secondWirePoints = pointsFromPath(data.last().split(","))

        return shortestIntersectionFromPoints(firstWirePoints, secondWirePoints)
    }

    fun printResutPartOne() {
        println("${prefix}Result part one is: ${partOneMinimalDistance(data)}")
    }

    fun printResutPartTwo() {
        println("${prefix}Result part two is: ${partTwoMinimalIntersection(data)}")
    }

    fun pointsFromPath(path: List<String>): Map<Point, Int> {
        var x = 0
        var y = 0
        var stepsTillPoint = 0
        val points = mutableMapOf<Point,Int>()

        path.forEach {
            val (direction, steps) = decodeMovement(it)

            repeat(steps) {
                when(direction) {
                    Direction.UP -> y++
                    Direction.DOWN -> y--
                    Direction.LEFT -> x--
                    Direction.RIGHT -> x++
                }
                stepsTillPoint++
                if (x != 0 || y != 0) {
                    val point = Point(x, y)
                    val t = points.getOrDefault(point, stepsTillPoint)
                    points[point] = min(t, stepsTillPoint)
                }
            }
        }

        return points.toMap()
    }

    fun pointToManhattanDistance(point: Point): Int = abs(point.x) + abs(point.y)

    fun shortestDistanceFromPoints(firstWire: Set<Point>, secondWire: Set<Point>): Int {
        val intersection = firstWire.intersect(secondWire)
        val distance = if (intersection.isNotEmpty()) {
            intersection.map { pointToManhattanDistance(it) }.min() ?: throw Exception("Minimal distatnce not found")
        } else {
            throw Exception("No interesection found")
        }
        return distance
    }

    fun shortestIntersectionFromPoints(firstWire: Map<Point, Int>, secondWire: Map<Point, Int>): Int {
        val intersection = firstWire.keys.intersect(secondWire.keys)

        val shortestIntersection = intersection.map {
            val firstWireSteps = firstWire[it] ?: throw Exception("Interesection point: $it not found in first wire path")
            val secondWireSteps = secondWire[it] ?: throw Exception("Interesection point: $it not found in second wire path")
            firstWireSteps + secondWireSteps
        }.min() ?: throw Exception("Minimal not found")

        return shortestIntersection
    }

    private fun decodeMovement(code: String): Pair<Direction, Int> {
        val direction = when (code.first()) {
            'R' -> Direction.RIGHT
            'L' -> Direction.LEFT
            'U' -> Direction.UP
            'D' -> Direction.DOWN
            else -> throw Exception("Unknown direction: ${code.first()}")
        }

        val steps = try {
            code.substring(1).toInt()
        } catch (e: Exception) {
            throw Exception("Steps are not a number: $code")
        }

        return direction to steps
    }

    data class Point(val x: Int, val y: Int)

    enum class Direction {
        RIGHT, UP, DOWN, LEFT;
    }

    companion object {
        val fileDir = "src/main/resources/"

        fun inputDataFromFile(fileName: String): List<String> = loadDataFromFile(fileName)

        private fun loadDataFromFile(fileName: String): List<String> {
            val inputData: MutableList<String> = mutableListOf()
            File(fileDir+fileName).forEachLine {
                inputData.add(it)
            }
            return inputData.toList()
        }
    }
}


