import java.io.File

fun main() {
    val inputData = Day1x.inputDataFromFile("day11.txt")
    val service = Day11(inputData)

    service.printResultPartOne()
    service.printResultPartTwo()
}

class Day11(private val data: List<Int>, private val prefix: String = "") {

    fun partOne(data: List<Int>): String {
        return "TODO"
    }

    fun partTwo(data: List<Int>): String {
        return "TODO"
    }

    fun printResultPartOne() {
        // println("${prefix}Result part one is: ${partOne(data)}")
        println("${prefix}Result part one is: Not done yet}")
    }

    fun printResultPartTwo() {
        // println("${prefix}Result part two is: ${partTwo(data)}")
        // println("${prefix}Result part two is: ${partTwo(data)}")
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
}


