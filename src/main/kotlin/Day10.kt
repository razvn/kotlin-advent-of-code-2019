import java.io.File
import kotlin.math.abs
import kotlin.math.atan2

fun main() {
    val inputData = Day10.inputDataFromFile("day10.txt")
    val service = Day10(inputData)

    service.printResultPartOne()
    // couldn't figure out
    // service.printResultPartTwo()

}

class Day10(data: List<List<String>>, private val prefix: String = "") {

    val asteroidList: List<Asteroid> = data.mapIndexed { y, list ->
        list.mapIndexedNotNull { x, value ->
            if (value != ".") Asteroid(x, y) else null
        }
    }.flatten()

    fun partOne(data: List<Asteroid>): Int {
        return asteroidBestInSight(data)?.inSight ?: 0
    }

    fun asteroidBestInSight(data: List<Asteroid>): Asteroid? {
        data.forEach {
            it.inSight = inSightFor(it, data).size
        }

        return  data.maxBy { it.inSight }
    }

    fun inSightFor(asteroid: Asteroid, list: List<Asteroid>): List<Asteroid> {
        val visibles = list.filter { it != asteroid }
                .filter { a ->
                    val listToTest = mutableListOf<Asteroid>()
                    listToTest.addAll(list - listOf(asteroid, a))
                    val alignedAndCloser = listToTest.firstOrNull { it.isAllignedAndCloser(asteroid, a) }
                    alignedAndCloser == null
        }
        return visibles
    }

    fun partTwo(data: List<Asteroid>): Int {
        val location = asteroidBestInSight(data) ?: return 0

        val currentAsteroids = data.toMutableList()
        currentAsteroids.forEach {

            it.angle = atan2((it.y - location.y).toDouble(), (it.x - location.x).toDouble())
        }
        currentAsteroids.remove(location)
        val maxX = data.maxBy { it.x }?.x ?: 0
        val maxY = data.maxBy { it.y }?.y ?: 0

        println("Start for location: $location - maxX: $maxX, maxY: $maxY")
        var position = location.x to 0
        val removedAsteroids  = mutableListOf<Asteroid>()

        var i = 0
        while (removedAsteroids.size < 200  && currentAsteroids.isNotEmpty()) {
            println("Position: $position, Current: ${currentAsteroids.size}, removed: ${removedAsteroids.size}")
            // get the astero at the max position if exists
            val maxLocation = closestAsteroidToMax(position.first, position.second, location, currentAsteroids)
            //move forward if none found
            if (maxLocation == null) {
                position = moveTarget(position.first, position.second, maxX, maxY)
            } else {
                println("\tMax: ${maxLocation}")
                // asteroids visible by the base
                val asteroidsInSight = inSightFor(location, currentAsteroids)
                println("\tInsight: ${asteroidsInSight.size}")
                // find the one aligned with the current position
                val destroyed = asteroidsInSight.firstOrNull {
                    it.isAllignedAndCloser(location, maxLocation)
                }
                println("\tdestroyed: ${destroyed}")

                destroyed?.let {
                    // remove it from current astero and add it to distoyed ones
                    currentAsteroids.remove(destroyed)
                    removedAsteroids.add(destroyed)
                    position = moveTarget(position.first, position.second, maxX, maxY)
                }
            }

           // printBattlefild(maxX, maxY, currentAsteroids, removedAsteroids, location, maxLocation)

            //if (removedAsteroids.size == 117 ) i++
            //if (i == 1) throw Exception("Debug")
        }

        val lastAsteroidDestroyed = removedAsteroids.last()
        return lastAsteroidDestroyed.x * 100 + lastAsteroidDestroyed.y
    }

    fun closestAsteroidToMax(x: Int, y: Int, base: Asteroid, asteroids: List<Asteroid>): Asteroid? {
        var value: Asteroid? = null
        var goon = true
        while (value == null && goon) {
            value = when {
                y <= base.y -> asteroids.filter { it.x == x }.minBy { it.y }
                y > base.y -> asteroids.filter { it.x == x }.maxBy { it.y }
                else -> null
            }

            if (value == null) {

            }
        }

        return value
    }

    fun printBattlefild(maxX: Int, maxY: Int, alive: List<Asteroid>, dead: List<Asteroid>, location: Asteroid, maxLocation: Asteroid?) {
        val battleField = MutableList(maxY+1) { MutableList(maxX+1) { "-" } }

        alive.forEach {
            battleField[it.y][it.x] = "o"
        }
        dead.forEach {
            battleField[it.y][it.x] = "x"
        }
        maxLocation?.let {
            battleField[it.y][it.x] = "M"
        }

        battleField[location.y][location.x] = "#"
        battleField.forEach { yLine -> println(yLine.joinToString("")) }

    }

    fun moveTarget(x: Int, y: Int, maxX: Int, maxY: Int): Pair<Int, Int> {
        return when {
            // increment x if it's not yet max
            y == 0 && x < maxX -> x + 1 to y
            x == maxX && y < maxY -> x to y + 1
            y == maxY && x != 0 -> x - 1 to y
            x== 0 && y > 0 -> x to y - 1
            else -> 0 to 0
        }
    }

    fun printResultPartOne() {
        println("${prefix}Result part one is: ${partOne(asteroidList)}")
    }

    fun printResultPartTwo() {
        println("${prefix}Result part two is: Need some more type to figure out}")
        // println("${prefix}Result part two is: ${partTwo(asteroidList)}")
    }

    companion object {
        val fileDir = "src/main/resources/"

        fun inputDataFromFile(fileName: String): List<List<String>> = loadDataFromFile(fileName)
                .map { it.chunked(1) }

        fun loadDataFromFile(fileName: String): List<String> {
            val inputData: MutableList<String> = mutableListOf()
            File(fileDir + fileName).forEachLine {
                inputData.add(it)
            }
            return inputData.toList()
        }
    }
}

data class Asteroid(val x: Int, val y: Int,
                    var inSight: Int = 0, var angle: Double? = null ) {
    override fun equals(other:Any?): Boolean {
        return when {
            other == null -> return false
            other !is Asteroid -> return false
            else -> (this.x == other.x && this.y == other.y)
        }
    }

    override fun hashCode(): Int {
        return super.hashCode()
    }
}

fun Asteroid.isAllignedAndCloser(a: Asteroid, b: Asteroid): Boolean {
    val dax = this.x - a.x
    val day = this.y - a.y

    val dbx = b.x - a.x
    val dby = b.y - a.y

    val isAlligned = dax * dby == day * dbx
    val resp = if (!isAlligned) false
    else {
        //is it beteen a and b
        if (abs(dbx) >= abs(dby)) {
            if (dax > 0) a.x <= this.x && this.x <= b.x
            else b.x <= this.x && this.x <= a.x
        } else {
            if (dby > 0) a.y <= this.y && this.y <= b.y
            else b.y <= this.y && this.y <= a.y
        }
    }

    // println("astero: $this : isAllignedAndCloser: $resp : a: $a, b: $b")
    return resp
}
