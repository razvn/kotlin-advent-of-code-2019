import java.io.File
import kotlin.system.measureTimeMillis

fun main() {
    val inputData = Day02.inputDataFromFile("day02.txt")

    val service = Day02(inputData)
    val duration = measureTimeMillis {
        service.printResultPartOne()
        service.printResultPartTwo()
    }
    println("Duration: $duration ms")
}

class Day02(private val data: List<Int>, private val prefix: String = "") {

    fun analyzeCommand(command: List<Int>): List<Int> {
        val res = command.toMutableList()
        var exit = false
        var i = 0
        while (!exit) {
            try {
                val newValue = calcValue(i, res)
                res[newValue.first] = newValue.second
                i += 4
            } catch (e: HaltException) {
                exit = true
            }
        }
        return res.toList()
    }

    fun findNounAndVerbeForOutput(output: Int, data: List<Int>): Pair<Int, Int> {
        var noun = 0
        var verbe = 0

        var exit = false
        while (!exit) {
            val newProgram = data.toMutableList()
            newProgram[1] = noun
            newProgram[2] = verbe
            val res = analyzeCommand(newProgram)
            // println(res)
            if (res[0] == output) exit = true
            if (!exit) {
                noun++
                if (noun > 99) {
                    noun = 0
                    verbe++
                    if (verbe > 99) throw Exception("No valid value found")
                }
            }
        }

        return noun to verbe
    }

    fun printResultPartOne() {
        val originalProgram = data.toMutableList()
        originalProgram[1] = 12
        originalProgram[2] = 2

        println("${prefix}Result part one is: ${analyzeCommand(originalProgram)}")
    }

    fun printResultPartTwo() {
        val (noun, verbe) = findNounAndVerbeForOutput(19690720, data)

        println("${prefix}Result part two is: ${100 * noun + verbe}")
    }

    private fun calcValue(opIndex: Int, data: List<Int>): Pair<Int, Int> {
        return when (data[opIndex]) {
            99 -> throw HaltException()
            1 -> operateValues(Operation.ADD, opIndex, data)
            2 -> operateValues(Operation.MULTIPLY, opIndex, data)
            else -> throw UnexpectedValueException()
        }
    }

    private fun operateValues(op: Operation, opIndex: Int, data: List<Int>): Pair<Int, Int> {
        val val1Index = try {
            data[opIndex + 1]
        } catch (e: Exception) {
            throw OutOfBoundsException()
        }
        val val2Index = try {
            data[opIndex + 2]
        } catch (e: Exception) {
            throw OutOfBoundsException()
        }
        val resPosition = try {
            data[opIndex + 3]
        } catch (e: Exception) {
            throw OutOfBoundsException()
        }

        return when (op) {
            Operation.ADD -> resPosition to data[val1Index] + data[val2Index]
            Operation.MULTIPLY -> resPosition to data[val1Index] * data[val2Index]
        }
    }

    enum class Operation {
        ADD, MULTIPLY;
    }

    class HaltException : Exception()
    class UnexpectedValueException : Exception()
    class OutOfBoundsException : Exception()

    companion object {
        val fileDir = "src/main/resources/"

        fun inputDataFromFile(fileName: String): List<Int> = loadDataFromFile(fileName)
                .flatMap { it.split(',') }
                .map { it.toInt() }

        private fun loadDataFromFile(fileName: String): List<String> {
            val inputData: MutableList<String> = mutableListOf()
            File(fileDir+fileName).forEachLine {
                inputData.add(it)
            }
            return inputData.toList()
        }
    }
}


