fun main() {
    val service = Day02()
    val inputData = listOf(
            1, 0, 0, 3,
            1, 1, 2, 3,
            1, 3, 4, 3,
            1, 5, 0, 3,
            2, 1, 10, 19,
            1, 19, 5, 23,
            1, 6, 23, 27,
            1, 27, 5, 31,
            2, 31, 10, 35,
            2, 35, 6, 39,
            1, 39, 5, 43,
            2, 43, 9, 47,
            1, 47, 6, 51,
            1, 13, 51, 55,
            2, 9, 55, 59,
            1, 59, 13, 63,
            1, 6, 63, 67,
            2, 67, 10, 71,
            1, 9, 71, 75,
            2, 75, 6, 79,
            1, 79, 5, 83,
            1, 83, 5, 87,
            2, 9, 87, 91,
            2, 9, 91, 95,
            1, 95, 10, 99,
            1, 9, 99, 103,
            2, 103, 6, 107,
            2, 9, 107, 111,
            1, 111, 5, 115,
            2, 6, 115, 119,
            1, 5, 119, 123,
            1, 123, 2, 127,
            1, 127, 9, 0,
            99, 2, 0, 14, 0
    )

    val originalProgram = inputData.toMutableList()
    originalProgram[1] = 12
    originalProgram[2] = 2

    val resultPartOne = service.analyzeCommand(originalProgram)
    println()
    println("Result part one is: $resultPartOne")

    // part two

    val (noun, verbe) = service.findNounAndVerbeForOutput(19690720, inputData)

    println("Result part two is: ${100 * noun + verbe}")
}

class Day02 {
    enum class Operation {
        ADD, MULTIPLY;
    }

    class HaltException : Exception()
    class UnexpectedValueException : Exception()
    class OutOfBoundsException : Exception()

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
}


