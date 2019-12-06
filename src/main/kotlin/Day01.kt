import java.io.File
import kotlin.math.max
import kotlin.system.measureTimeMillis

fun main() {
    val inputData = Day01.inputDataFromFile("day01.txt")

    val service = Day01(inputData)
    val duration = measureTimeMillis {
        service.printResutPartOne()
        service.printResutPartTwo()
    }
    println("Duration: $duration ms")
}

class Day01(private val data: List<Int>, private val prefix: String = "") {

    fun getFuel(mass: Int) = max((mass / 3) - 2, 0)

    tailrec fun getTotalModuleFuel(mass: Int, accum: Int = 0): Int {
        val newMass = getFuel(mass)
        val currentMass = accum + newMass
        return if (newMass == 0) currentMass else getTotalModuleFuel(newMass, currentMass)
    }

    fun getPartOneFuel(masses: List<Int>) = masses.map { getFuel(it) }.sum()

    fun getPartTwoFuel(masses: List<Int>) = masses
            .map { getTotalModuleFuel(it) }
            .sum()

    fun printResutPartOne() {
        println("${prefix}Result part one is: ${getPartOneFuel(data)}")
    }

    fun printResutPartTwo() {
        println("${prefix}Result part two is: ${getPartTwoFuel(data)}")
    }

    companion object {
        val fileDir = "src/main/resources/"

        fun inputDataFromFile(fileName: String): List<Int> = loadDataFromFile(fileName)
                .map { it.toInt() }

        private fun loadDataFromFile(fileName: String): List<String> {
            val inputData: MutableList<String> = mutableListOf()
            File(fileDir + fileName).forEachLine {
                inputData.add(it)
            }
            return inputData.toList()
        }
    }
}


