import Day12.*
import java.io.File
import kotlin.math.abs
import kotlin.math.sign

fun main() {
    val inputData = listOf(
            Moon("Io", Coordinates(12, 0, -15)),
            Moon("Europa", Coordinates(-8, -5, -10)),
            Moon("Ganymede", Coordinates(7, -17, 1)),
            Moon("Callisto", Coordinates(2, -11, -6))
    )
    val service = Day12(inputData)

    service.printResultPartOne()
    service.printResultPartTwo()
}

class Day12(private val data: List<Moon>, private val prefix: String = "") {

    fun partOne(data: List<Moon>): Int {
        val partOneData = data.map { it.copy() }
        repeat(1000) {
            applyGravity(partOneData)
            applyVelocity(partOneData)
        }

        return totalEnergy(partOneData)
    }

    fun partTwo(data: List<Moon>): String {
        var i = 0L
        val initalData = data.map {
            it.name to it.copy(position = it.position.copy(), velocity = it.velocity.copy())
        }.toMap()

        val seenX: MutableMap<String, Long> = mutableMapOf()
        val seenY: MutableMap<String, Long> = mutableMapOf()
        val seenZ: MutableMap<String, Long> = mutableMapOf()

        while (seenX.size < initalData.size || seenY.size < initalData.size || seenZ.size < initalData.size) {
            applyGravity(data)
            applyVelocity(data)
            i++

            data.filter { m ->
                val init = initalData[m.name] ?: throw Exception("Unknown moon")
                m.position.x == init.position.x && m.velocity.x == init.velocity.x
            }.forEach {
                println("$i: added: $it to x")
                seenX[it.name] = i
            }
            data.filter { m ->
                val init = initalData[m.name] ?: throw Exception("Unknown moon")
                m.position.y == init.position.y && m.velocity.y == init.velocity.y
            }.forEach {
                println("$i: added: $it to y")
                seenY[it.name] = i
            }
            data.filter { m ->
                val init = initalData[m.name] ?: throw Exception("Unknown moon")
                m.position.z == init.position.z && m.velocity.z == init.velocity.z
            }.forEach {
                println("$i: added: $it to z")
                seenZ[it.name] = i
            }
        }

        println("X: $seenX")
        println("Y: $seenY")
        println("Z: $seenZ")

        val finalValues = seenX.map {
            val name = it.key
            val x = it.value
            val y = seenY[name] ?: throw Exception("No y found for $name" )
            val z = seenZ[name] ?: throw Exception("No z found for $name" )
            name to lcm(x, lcm(y, z))
        }
        println(finalValues)
        println(lcm(lcm(finalValues[0].second, finalValues[1].second), lcm(finalValues[2].second, finalValues[3].second)))
        return i.toString()
    }

    fun printResultPartOne() {
        println("${prefix}Result part one is: ${partOne(data)}")
    }

    fun printResultPartTwo() {
        // println("${prefix}Result part two is: ${partTwo(data)}")
        println("${prefix}Result part two is: Not yet figured out}")
    }

    companion object {
        val fileDir = "src/main/resources/"

        fun inputDataFromFile(fileName: String): List<Int> = loadDataFromFile(fileName)
                .map { it.toInt() }

        fun loadDataFromFile(fileName: String): List<String> {
            val inputData: MutableList<String> = mutableListOf()
            File(fileDir + fileName).forEachLine {
                inputData.add(it)
            }
            return inputData.toList()
        }
    }

    fun applyGravity(data: List<Moon>) {
        val pairs = allPairs(data)
        pairs.forEach {
            it.first.applyGravity(it.second)
        }
    }

    fun applyVelocity(data: List<Moon>) {
        data.forEach {
            it.applyVelocity()
        }
    }

    fun totalEnergy(data: List<Moon>): Int {
        return data.sumBy { it.energy }
    }

    fun allPairs(data: List<Moon>): List<Pair<Moon, Moon>> {
        val moons = mutableListOf<Moon>()
        val res = mutableListOf<Pair<Moon, Moon>>()
        moons.addAll(data)
        while (moons.size > 1) {
            // take 1st
            val moon = moons.removeAt(0)
            // build all pairs involving this one
            moons.forEach { res.add(moon to it) }
        }
        return res
    }

    data class Coordinates(var x: Int, var y: Int, var z: Int) {
        operator fun minus(param: Coordinates): Coordinates {
            return Coordinates(this.x - param.x, this.y - param.y, this.z - param.z)
        }

        operator fun plus(param: Coordinates): Coordinates {
            return Coordinates(this.x + param.x, this.y + param.y, this.z + param.z)
        }

        fun absSum() = abs(x) + abs(y) + abs(z)
    }

    data class Moon(val name: String, var position: Coordinates, var velocity: Coordinates = Coordinates(0, 0, 0)) {
        val energy
            get() = position.absSum() * velocity.absSum()

        fun applyVelocity() {
            with(this.position) {
                x += velocity.x
                y += velocity.y
                z += velocity.z
            }
        }

        fun applyGravity(from: Moon) {
            val coord = (from.position - this.position).apply {
                x = x.sign
                y = y.sign
                z = z.sign
            }
            this.velocity += coord
            from.velocity -= coord
        }
    }

    private fun lcm(x: Long, y: Long): Long {
        var a = x
        var b = y
        while (a != 0L) {
            a = (b % a).also { b = a }
        }
        return x / b * y
    }
}


