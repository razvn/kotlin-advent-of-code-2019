import Day05.Operation.*
import java.io.File
import kotlin.system.measureTimeMillis

fun main() {
    val inputData = Day05.inputDataFromFile("day05.txt")

    val inputValues = listOf(1, 5)
    val service = Day05(inputData, inputValues)
    val duration = measureTimeMillis {
        service.printResultPartOne()
        service.printResultPartTwo()
    }
    println("Duration: $duration ms")
}

class Day05(private val data: List<Int>, private var input: List<Int>, private val prefix: String = "") {
    fun analyzeProgram(command: List<Int>, inputValues: List<Int> = emptyList(), debug: Boolean = true): ProgramResult {
        var inputReadIndex = 0
        val res = command.toMutableList()
        val output = mutableListOf<Int>()
        var instructionPointer = 0
        while (true) {
            val instruction = buildInstruction(res, instructionPointer, inputValues, inputReadIndex)
            if (instruction.operation == OUTPUT) {
                output.add(instruction.result.value)
            }
            if (instruction.operation == INPUT) {
                inputReadIndex++
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


    fun printResultPartOne() {
        val originalProgram = data.toMutableList()
        println("${prefix}Result part one is: ${analyzeProgram(originalProgram, listOf(input.first()), false).output}")
    }

    fun printResultPartTwo() {
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

    companion object {
        val fileDir = "src/main/resources/"

        fun inputDataFromFile(fileName: String): List<Int> = loadDataFromFile(fileName)
                .flatMap { it.split(",") }
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


