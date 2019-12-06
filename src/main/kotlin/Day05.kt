import Day05.Operation.*
import kotlin.system.measureTimeMillis

fun main() {
    val inputData = listOf(
            3, 225,
            1, 225, 6, 6,
            1100, 1, 238, 225,
            104, 0,
            1002, 92, 42, 224,
            1001, 224, -3444, 224,
            4, 224,
            102, 8, 223, 223,
            101, 4, 224, 224,
            1, 224, 223, 223,
            1102, 24, 81, 225,
            1101, 89, 36, 224,
            101, -125, 224, 224,
            4, 224,
            102, 8, 223, 223,
            101, 5, 224, 224,
            1, 224, 223, 223,
            2, 118, 191, 224,
            101, -880, 224, 224,
            4, 224,
            1002, 223, 8, 223,
            1001, 224, 7, 224,
            1, 224, 223, 223,
            1102, 68, 94, 225,
            1101, 85, 91, 225,
            1102, 91, 82, 225,
            1102, 85, 77, 224,
            101, -6545, 224, 224,
            4, 224,
            1002, 223, 8, 223,
            101, 7, 224, 224,
            1, 223, 224, 223,
            1101, 84, 20, 225,
            102, 41, 36, 224,
            101, -3321, 224, 224,
            4, 224,
            1002, 223, 8, 223,
            101, 7, 224, 224,
            1, 223, 224, 223,
            1, 188, 88, 224,
            101, -183, 224, 224,
            4, 224,
            1002, 223, 8, 223,
            1001, 224, 7, 224,
            1, 224, 223, 223,
            1001, 84, 43, 224,
            1001, 224, -137, 224,
            4, 224,
            102, 8, 223, 223,
            101, 4, 224, 224,
            1, 224, 223, 223,
            1102, 71, 92, 225,
            1101, 44, 50, 225,
            1102, 29, 47, 225,
            101, 7, 195, 224,
            101, -36, 224, 224,
            4, 224,
            102, 8, 223, 223,
            101, 6, 224, 224,
            1, 223, 224, 223,
            4, 223,
            99,
            0, 0, 0, 677, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1105, 0, 99999, 1105, 227, 247, 1105, 1, 99999, 1005, 227, 99999, 1005, 0, 256, 1105, 1, 99999, 1106, 227, 99999, 1106, 0, 265, 1105, 1, 99999, 1006, 0, 99999, 1006, 227, 274, 1105, 1, 99999, 1105, 1, 280, 1105, 1, 99999, 1, 225, 225, 225, 1101, 294, 0, 0, 105, 1, 0, 1105, 1, 99999, 1106, 0, 300, 1105, 1, 99999, 1, 225, 225, 225, 1101, 314, 0, 0, 106, 0, 0, 1105, 1, 99999, 107, 677, 677, 224, 1002, 223, 2, 223, 1006, 224, 329, 1001, 223, 1, 223, 1108, 226, 677, 224, 102, 2, 223, 223, 1006, 224, 344, 101, 1, 223, 223, 1107, 226, 226, 224, 1002, 223, 2, 223, 1006, 224, 359, 101, 1, 223, 223, 8, 677, 226, 224, 1002, 223, 2, 223, 1006, 224, 374, 1001, 223, 1, 223, 1107, 677, 226, 224, 102, 2, 223, 223, 1005, 224, 389, 1001, 223, 1, 223, 1008, 677, 677, 224, 1002, 223, 2, 223, 1006, 224, 404, 1001, 223, 1, 223, 108, 677, 677, 224, 102, 2, 223, 223, 1005, 224, 419, 1001, 223, 1, 223, 1107, 226, 677, 224, 102, 2, 223, 223, 1006, 224, 434, 101, 1, 223, 223, 1008, 226, 226, 224, 1002, 223, 2, 223, 1006, 224, 449, 1001, 223, 1, 223, 107, 226, 226, 224, 102, 2, 223, 223, 1006, 224, 464, 1001, 223, 1, 223, 1007, 677, 226, 224, 1002, 223, 2, 223, 1006, 224, 479, 1001, 223, 1, 223, 1108, 226, 226, 224, 102, 2, 223, 223, 1006, 224, 494, 1001, 223, 1, 223, 8, 226, 226, 224, 1002, 223, 2, 223, 1005, 224, 509, 1001, 223, 1, 223, 7, 226, 677, 224, 102, 2, 223, 223, 1005, 224, 524, 101, 1, 223, 223, 1008, 677, 226, 224, 102, 2, 223, 223, 1005, 224, 539, 101, 1, 223, 223, 107, 226, 677, 224, 1002, 223, 2, 223, 1006, 224, 554, 1001, 223, 1, 223, 1108, 677, 226, 224, 102, 2, 223, 223, 1005, 224, 569, 101, 1, 223, 223, 108, 226, 226, 224, 1002, 223, 2, 223, 1005, 224, 584, 1001, 223, 1, 223, 7, 677, 226, 224, 1002, 223, 2, 223, 1005, 224, 599, 1001, 223, 1, 223, 108, 226, 677, 224, 1002, 223, 2, 223, 1006, 224, 614, 101, 1, 223, 223, 1007, 677, 677, 224, 1002, 223, 2, 223, 1006, 224, 629, 101, 1, 223, 223, 7, 677, 677, 224, 102, 2, 223, 223, 1005, 224, 644, 101, 1, 223, 223, 1007, 226, 226, 224, 1002, 223, 2, 223, 1006, 224, 659, 1001, 223, 1, 223, 8, 226, 677, 224, 102, 2, 223, 223, 1005, 224, 674, 1001, 223, 1, 223, 4, 223, 99, 226
    )

    val inputValues = listOf(1, 5)
    val service = Day05(inputData, inputValues)
    val duration = measureTimeMillis {
        service.printResutPartOne()
        service.printResutPartTwo()
    }
    println("Duration: $duration ms")
}

class Day05(private val data: List<Int>, private var input: List<Int>, private val prefix: String = "") {
    fun analyzeProgram(command: List<Int>, inputValues: List<Int> = emptyList(), debug: Boolean = true): ProgramResult {
        var inputReadIndex: Int = 0
        val res = command.toMutableList()
        val output = mutableListOf<Int>()
        var instructionPointer = 0
        while (true) {
            val instruction = buildInstruction(res, instructionPointer, inputValues, inputReadIndex)
            if (instruction.operation == OUTPUT) {
                output.add(instruction.result.value)
            }

            if (debug) println(":$instructionPointer:$instruction")

            if (instruction.operation == HALT) {
                break
            } else {
                // update with new value if applicable
                with(instruction.result) {
                    val resultPosition = when (position.type) {
                        PositionType.ABSOLUTE -> res[position.value]
                        PositionType.RELATIVE -> res[instructionPointer + position.value]
                        PositionType.NONE -> null
                    }
                    resultPosition?.let {
                        res[resultPosition] = value
                    }
                }
                // increment pointer
                with(instruction.positionAfter) {
                    when (type) {
                        PositionType.ABSOLUTE -> instructionPointer = value
                        PositionType.RELATIVE -> instructionPointer += value
                        PositionType.NONE -> Unit
                    }
                }
            }
            // println(res)
        }

        return ProgramResult(output = output.toList(), memory = res)
    }

    fun buildInstruction(res: MutableList<Int>, instructionPointer: Int, inputValues: List<Int>, inputReadIndex: Int): Instruction {
        val instructionCode = res[instructionPointer]
        val (operation, param1Mode, param2Mode) = destructureCommand(instructionCode)

        val instruction = when (operation) {
            ADD -> {
                val param1 = getParam(instructionPointer, res, 1, param1Mode)
                val param2 = getParam(instructionPointer, res, 2, param2Mode)
                val result = Result(param1.final + param2.final, Position(PositionType.RELATIVE, 3))
                val positionAfter = Position(PositionType.RELATIVE, 4)
                Instruction(instructionCode, operation, param1, param2, instructionPointer, result, positionAfter)
            }
            MULTIPLY -> {
                val param1 = getParam(instructionPointer, res, 1, param1Mode)
                val param2 = getParam(instructionPointer, res, 2, param2Mode)
                val result = Result(param1.final * param2.final, Position(PositionType.RELATIVE, 3))
                val positionAfter = Position(PositionType.RELATIVE, 4)
                Instruction(instructionCode, operation, param1, param2, instructionPointer, result, positionAfter)
            }
            INPUT -> {
                val result = Result(inputValues[inputReadIndex], Position(PositionType.RELATIVE, 1))
                val positionAfter = Position(PositionType.RELATIVE, 2)
                Instruction(instructionCode, operation, null, null, instructionPointer, result, positionAfter)
            }
            OUTPUT -> {
                val param1 = getParam(instructionPointer, res, 1, param1Mode)
                val result = Result(param1.final, Position(PositionType.NONE, 0))
                val positionAfter = Position(PositionType.RELATIVE, 2)
                Instruction(instructionCode, operation, param1, null, instructionPointer, result, positionAfter)
            }
            JUMPTRUE -> {
                val param1 = getParam(instructionPointer, res, 1, param1Mode)
                val param2 = getParam(instructionPointer, res, 2, param2Mode)
                val result = Result(0, Position(PositionType.NONE, 0))
                val positionAfter = if (param1.final != 0) Position(PositionType.ABSOLUTE, param2.final) else Position(PositionType.RELATIVE, 3)
                Instruction(instructionCode, operation, param1, param2, instructionPointer, result, positionAfter)
            }
            JUMPFALSE -> {
                val param1 = getParam(instructionPointer, res, 1, param1Mode)
                val param2 = getParam(instructionPointer, res, 2, param2Mode)
                val result = Result(0, Position(PositionType.NONE, 0))
                val positionAfter = if (param1.final == 0) Position(PositionType.ABSOLUTE, param2.final) else Position(PositionType.RELATIVE, 3)
                Instruction(instructionCode, operation, param1, param2, instructionPointer, result, positionAfter)
            }
            LESSTHAN -> {
                val param1 = getParam(instructionPointer, res, 1, param1Mode)
                val param2 = getParam(instructionPointer, res, 2, param2Mode)
                val result = Result(if (param1.final < param2.final) 1 else 0, Position(PositionType.RELATIVE, 3))
                val positionAfter = Position(PositionType.RELATIVE, 4)
                Instruction(instructionCode, operation, param1, param2, instructionPointer, result, positionAfter)
            }
            EQUALS -> {
                val param1 = getParam(instructionPointer, res, 1, param1Mode)
                val param2 = getParam(instructionPointer, res, 2, param2Mode)
                val result = Result(if (param1.final == param2.final) 1 else 0, Position(PositionType.RELATIVE, 3))
                val positionAfter = Position(PositionType.RELATIVE, 4)
                Instruction(instructionCode, operation, param1, param2, instructionPointer, result, positionAfter)
            }
            HALT -> {
                val result = Result(0, Position(PositionType.NONE, 0))
                val positionAfter = Position(PositionType.NONE, 0)
                Instruction(instructionCode, operation, null, null, instructionPointer, result, positionAfter)
            }
        }
        return instruction
    }


    fun printResutPartOne() {
        val originalProgram = data.toMutableList()
        println("${prefix}Result part one is: ${analyzeProgram(originalProgram, listOf(input.first()), false).output}")
    }

    fun printResutPartTwo() {
        val originalProgram = data.toMutableList()
        println("${prefix}Result part two is: ${analyzeProgram(originalProgram, listOf(input.last()), false).output}")
    }

    fun destructureCommand(input: Int): Triple<Operation, ParamMode, ParamMode> {
        val commands = mutableListOf<Int>()
        var nb = input
        while (nb > 0) {
            val n = nb % 10
            commands.add(n)
            nb /= 10
        }
        val op = commands.getOrElse(0) { 0 } + 10 * commands.getOrElse(1) { 0 }
        val mode1 = commands.getOrElse(2) { 0 }
        val mode2 = commands.getOrElse(3) { 0 }
        return Triple(Operation.fromCode(op), ParamMode.fromCode(mode1), ParamMode.fromCode(mode2))
    }

    private fun getParam(pos: Int, data: List<Int>, paramNb: Int, mode: ParamMode): Parameter {
        val p1 = data[pos + paramNb]
        val final = if (mode == ParamMode.IMMEDIATE) p1 else data[p1]
        return Parameter(mode, p1, final)
    }

    data class ProgramResult(val output: List<Int>, val memory: List<Int>)

    data class Instruction(
            val code: Int,
            val operation: Operation,
            val param1: Parameter?,
            val param2: Parameter?,
            val position: Int,
            val result: Result,
            val positionAfter: Position
    )

    data class Parameter(val mode: ParamMode, val initial: Int, val final: Int)

    data class Result(val value: Int, val position: Position)

    data class Position(val type: PositionType, val value: Int)

    enum class PositionType {
        RELATIVE, ABSOLUTE, NONE
    }
    enum class Operation(val code: Int) {
        ADD(1), MULTIPLY(2), INPUT(3), OUTPUT(4), JUMPTRUE(5), JUMPFALSE(6), LESSTHAN(7), EQUALS(8), HALT(99);

        companion object {
            fun fromCode(code: Int) = values().firstOrNull { it.code == code }
                    ?: throw UnexpectedValueException("Operation: $code unsupported")
        }
    }

    enum class ParamMode(val code: Int) {
        MEMORY(0), IMMEDIATE(1);

        companion object {
            fun fromCode(code: Int) = values().firstOrNull { it.code == code }
                    ?: throw UnexpectedValueException("Parameter mode: $code unsupported")
        }
    }

    class UnexpectedValueException(msg: String? = null) : Exception(msg)
}


