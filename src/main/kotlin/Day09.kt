import InitCodeComputerDay09.OperationType.*
import java.io.File
import kotlin.system.measureTimeMillis

fun main() {
    val inputData = Day09.inputDataFromFile("day09.txt")
    val service = Day09(inputData)

    val duration = measureTimeMillis {
        service.printResutPartOne()
        service.printResutPartTwo()
    }
    println("Duration: $duration ms")
}

class Day09(private val data: List<Long>, private val prefix: String = "") {

    fun partOne(data: List<Long>): String {
        val computerDay09 = InitCodeComputerDay09(data)
        val inputQueue = InitCodeComputerDay09.AdventQueue<Long>()
        inputQueue.addAll(listOf(1L))
        val result = computerDay09.runProgram(inputQueue, debug = false)
        return result.output.toString()
    }

    fun partTwo(data: List<Long>): String {
        val computerDay09 = InitCodeComputerDay09(data)
        val inputQueue = InitCodeComputerDay09.AdventQueue<Long>()
        inputQueue.addAll(listOf(2L))
        val result = computerDay09.runProgram(inputQueue)
        return result.output.toString()
    }

    fun printResutPartOne() {
        println("${prefix}Result part one is: ${partOne(data)}")
    }

    fun printResutPartTwo() {
        println("${prefix}Result part two is: ${partTwo(data)}")
    }

    companion object {
        val fileDir = "src/main/resources/"

        fun inputDataFromFile(fileName: String): List<Long> = loadDataFromFile(fileName)
                .flatMap { it.split(",") }
                .map { it.toLong() }

        fun loadDataFromFile(fileName: String): List<String> {
            val inputData: MutableList<String> = mutableListOf()
            File(fileDir + fileName).forEachLine {
                inputData.add(it)
            }
            return inputData.toList()
        }
    }
}


class InitCodeComputerDay09(private val program: List<Long>) {
    private val internalProgram: MutableList<Long> = mutableListOf()
    private val output = mutableListOf<Long>()
    var instructionPointer = 0L
    var relativeBase = 0L
    var state = State.RUNNING

    fun runProgram(input: AdventQueue<Long>, resetProgram: Boolean = true, debug: Boolean = false): ProgramResult {
        if (internalProgram.isEmpty() || resetProgram) {
            internalProgram.addAll(program)
        }

        val debugHistory = mutableListOf<DebugInfos>()
        if (debug) {
            println("Start with input: $input - program: $program")
        }

        while (state == State.RUNNING) {
            val instructionCode = internalProgram[instructionPointer.toInt()]
            val operation = destructureCommand(instructionCode)


            if (operation.type == INPUT && input.isEmpty()) {
                state = State.PAUSE
            } else {
                val instruction = buildInstruction(internalProgram, instructionPointer, operation, input, relativeBase)
                if (debug) {
                    debugHistory.add(instruction.debug(instructionPointer.toString(), relativeBase.toString()))
                }

                if (operation.type == OUTPUT) {
                    output.add(instruction.result.value)
                }
                if (operation.type == ADJUST) {
                    relativeBase += instruction.result.value
                }

                if (operation.type == HALT) {
                    state = State.HALT
                } else {
                    // update with new value if applicable
                    with(instruction.result) {
                        val resultPosition = when (position.type) {
                            PositionType.ABSOLUTE -> internalProgram.getOrElse(position.value.toInt()) { 0L }
                            PositionType.RELATIVE -> internalProgram.getOrElse((instructionPointer + position.value).toInt()) { 0L }
                            PositionType.EXACT -> position.value
                            PositionType.NONE -> null
                        }
                        resultPosition?.let {
                            if (resultPosition >= internalProgram.size) {
                                // uprise the program memory
                                // (internalProgram.size..resultPosition).forEach { internalProgram.add(it.toInt(), 0) }
                                (internalProgram.size..10000).forEach { internalProgram.add(it.toInt(), 0) }
                            }
                            internalProgram[resultPosition.toInt()] = value
                            // println("$instructionPointer: Update value: @${resultPosition.toInt()} = $value")
                        }
                    }
                    // increment pointer
                    with(instruction.positionAfter) {
                        when (type) {
                            PositionType.ABSOLUTE -> instructionPointer = value
                            PositionType.RELATIVE -> instructionPointer += value
                            PositionType.EXACT -> Unit
                            PositionType.NONE -> Unit
                        }
                    }
                }
            }
        }

        if (debug) {
            prettyPrint(debugHistory)
        }
        return ProgramResult(output = output.toList(), memory = internalProgram, pause = state == State.PAUSE)
    }

    fun buildInstruction(res: MutableList<Long>, ip: Long, operation: Operation, input: AdventQueue<Long>, base: Long): Instruction {

        val instruction = when (operation.type) {
            ADD -> { // 1
                val param1 = getParam(ip, res, 1, operation, base)
                val param2 = getParam(ip, res, 2, operation, base)
                val resPosition = getPosition(ip, res, 3, operation, base)
                val result = Result(param1.final + param2.final, Position(PositionType.EXACT, resPosition))
                val positionAfter = Position(PositionType.RELATIVE, 4)
                Instruction(operation.code, operation.type, param1, param2, ip, result, positionAfter)
            }
            MULTIPLY -> { // 2
                val param1 = getParam(ip, res, 1, operation, base)
                val param2 = getParam(ip, res, 2, operation, base)
                val resPosition = getPosition(ip, res, 3, operation, base)
                val result = Result(param1.final * param2.final, Position(PositionType.EXACT, resPosition))
                val positionAfter = Position(PositionType.RELATIVE, 4)
                Instruction(operation.code, operation.type, param1, param2, ip, result, positionAfter)
            }
            INPUT -> { // 3
                val inputValue = input.peek()
                if (inputValue != null) {
                    val param1 = Parameter(ParamMode.IMMEDIATE, inputValue, inputValue)
                    val position = getPosition(ip, res, 1, operation, base)
                    val result = Result(inputValue, Position(PositionType.EXACT, position))
                    val positionAfter = Position(PositionType.RELATIVE, 2)
                    Instruction(operation.code, operation.type, param1, null, ip, result, positionAfter)
                } else {
                    throw PauseException()
                }

            }
            OUTPUT -> { // 4
                val param1 = getParam(ip, res, 1, operation, base)
                val result = Result(param1.final, Position(PositionType.NONE, 0))
                val positionAfter = Position(PositionType.RELATIVE, 2)
                Instruction(operation.code, operation.type, param1, null, ip, result, positionAfter)
            }
            JUMPTRUE -> { // 5
                val param1 = getParam(ip, res, 1, operation, base)
                val param2 = getParam(ip, res, 2, operation, base)
                val result = Result(0, Position(PositionType.NONE, 0))
                val positionAfter = if (param1.final != 0L) Position(PositionType.ABSOLUTE, param2.final) else Position(PositionType.RELATIVE, 3)
                Instruction(operation.code, operation.type, param1, param2, ip, result, positionAfter)
            }
            JUMPFALSE -> { // 6
                val param1 = getParam(ip, res, 1, operation, base)
                val param2 = getParam(ip, res, 2, operation, base)
                val result = Result(0, Position(PositionType.NONE, 0))
                val positionAfter = if (param1.final == 0L) Position(PositionType.ABSOLUTE, param2.final) else Position(PositionType.RELATIVE, 3)
                Instruction(operation.code, operation.type, param1, param2, ip, result, positionAfter)
            }
            LESSTHAN -> { // 7
                val param1 = getParam(ip, res, 1, operation, base)
                val param2 = getParam(ip, res, 2, operation, base)
                val position = getPosition(ip, res, 3, operation, base)
                val result = Result(if (param1.final < param2.final) 1 else 0, Position(PositionType.EXACT, position))
                val positionAfter = Position(PositionType.RELATIVE, 4)
                Instruction(operation.code, operation.type, param1, param2, ip, result, positionAfter)
            }
            EQUALS -> { // 8
                val param1 = getParam(ip, res, 1, operation, base)
                val param2 = getParam(ip, res, 2, operation, base)
                val position = getPosition(ip, res, 3, operation, base)
                val result = Result(if (param1.final == param2.final) 1 else 0, Position(PositionType.EXACT, position))
                val positionAfter = Position(PositionType.RELATIVE, 4)
                Instruction(operation.code, operation.type, param1, param2, ip, result, positionAfter)
            }
            ADJUST -> { // 9
                val param1 = getParam(ip, res, 1, operation, base)
                val result = Result(param1.final, Position(PositionType.NONE, 0))
                val positionAfter = Position(PositionType.RELATIVE, 2)
                Instruction(operation.code, operation.type, param1, null, ip, result, positionAfter)
            }
            HALT -> { // 99
                val result = Result(0, Position(PositionType.NONE, 0))
                val positionAfter = Position(PositionType.NONE, 0)
                Instruction(operation.code, operation.type, null, null, ip, result, positionAfter)
            }
        }
        return instruction
    }

    fun destructureCommand(input: Long): Operation {
        val commands = mutableListOf<Long>()
        var nb = input
        while (nb > 0) {
            val n = nb % 10
            commands.add(n)
            nb /= 10
        }
        val op = commands.getOrElse(0) { 0 } + 10 * commands.getOrElse(1) { 0 }

        val params = if (commands.size > 2) commands.subList(2, commands.size).map { it.toInt() }.toList() else emptyList()

        return Operation(input, OperationType.fromCode(op.toInt()), params)
    }

    private fun prettyPrint(data: List<DebugInfos>) {
        val maxPointer = data.map { it.pointer.count() }.max() ?: 0
        val maxCode = data.map { it.code.count() }.max() ?: 0
        val maxOp = data.map { it.operation.count() }.max() ?: 0
        val maxP1 = data.map { it.param1.count() }.max() ?: 0
        val maxP2 = data.map { it.param2.count() }.max() ?: 0
        val maxRes = data.map { it.result.count() }.max() ?: 0
        val maxPos = data.map { it.positionAfter.count() }.max() ?: 0

        data.forEach {
            println("${it.pointer.padRight(maxPointer)} | ${it.base.padRight(maxCode)} | ${it.code.padRight(maxCode)} | ${it.operation.padRight(maxOp)} | ${it.param1.padRight(maxP1)} | ${it.param2.padRight(maxP2)} | ${it.result.padRight(maxRes)} | ${it.positionAfter.padRight(maxPos)}")
        }
    }

    fun String.padRight(len: Int) = if (len > this.length) {
        "${" ".repeat(len - this.length)}$this"
    } else this

    class HaltException : Exception()
    class PauseException : Exception()

    private fun getParam(pos: Long, data: List<Long>, paramNb: Long, operation: Operation, base: Long): Parameter {
        val position = getPosition(pos, data, paramNb, operation, base)
        val value = data.getOrElse(position.toInt()) { 0 }
        val mode = operation.getModeForParamIdx(paramNb)
        return Parameter(mode, position, value)
    }

    private fun getPosition(pos: Long, data: List<Long>, paramNb: Long, operation: Operation, base: Long): Long {
        val mode = operation.getModeForParamIdx(paramNb)
        val memValue = pos + paramNb
        return when (mode) {
            ParamMode.IMMEDIATE -> memValue
            ParamMode.RELATIVE -> base + data.getOrElse(memValue.toInt()) { 0 }
            ParamMode.MEMORY -> data.getOrElse(memValue.toInt()) { 0 }
        }
    }

    data class ProgramResult(val output: List<Long>, val memory: List<Long>, val pause: Boolean = false)

    data class Instruction(
            val code: Long,
            val operationType: OperationType,
            val param1: Parameter?,
            val param2: Parameter?,
            val position: Long,
            val result: Result,
            val positionAfter: Position
    ) {
        fun debug(pointer: String, base: String): DebugInfos {
            return DebugInfos(pointer, base, code.toString(), operationType.toString(), param1?.debug()
                    ?: "null", param2?.debug() ?: "null", result.debug(), positionAfter.debug())
        }
    }

    data class DebugInfos(val pointer: String, val base: String, val code: String, val operation: String,
                          val param1: String, val param2: String, val result: String, val positionAfter: String)

    data class Parameter(val mode: ParamMode, val initial: Long, val final: Long) {
        fun debug(): String {
            val prefix = when (mode) {
                ParamMode.MEMORY -> "$"
                ParamMode.IMMEDIATE -> ""
                ParamMode.RELATIVE -> "+$"
            }
            return "$prefix$initial ($final)"
        }
    }

    data class Result(val value: Long, val position: Position) {
        fun debug(): String {
            return "$value (${position.debug()})"
        }
    }

    data class Position(val type: PositionType, val value: Long) {
        fun debug(): String {
            val prefix = when (type) {
                PositionType.RELATIVE -> "+"
                PositionType.ABSOLUTE -> ""
                PositionType.EXACT -> "="
                PositionType.NONE -> "-"
            }
            return "$prefix$value"
        }
    }

    data class Operation(val code: Long, val type: OperationType, private val paramsMode: List<Int>) {
        fun getModeForParamIdx(idx: Long): ParamMode = ParamMode.fromCode(paramsMode.getOrElse(idx.toInt() - 1) { 0 })
    }

    enum class PositionType {
        RELATIVE, ABSOLUTE, EXACT, NONE
    }

    enum class OperationType(val code: Int) {
        ADD(1), MULTIPLY(2), INPUT(3), OUTPUT(4), JUMPTRUE(5),
        JUMPFALSE(6), LESSTHAN(7), EQUALS(8), ADJUST(9), HALT(99);

        companion object {
            fun fromCode(code: Int) = values().firstOrNull { it.code == code }
                    ?: throw UnexpectedValueException("Operation: $code unsupported")
        }
    }

    enum class ParamMode(val code: Int) {
        MEMORY(0), IMMEDIATE(1), RELATIVE(2);

        companion object {
            fun fromCode(code: Int) = values().firstOrNull { it.code == code }
                    ?: throw UnexpectedValueException("Parameter mode: $code unsupported")
        }
    }

    enum class State {
        RUNNING, PAUSE, HALT
    }

    class UnexpectedValueException(msg: String? = null) : Exception(msg)

    class AdventQueue<T> {
        private val items: MutableList<T> = mutableListOf()
        fun contains(element: T): Boolean = items.contains(element)
        fun addAll(elements: Collection<T>): Boolean = items.addAll(elements)
        fun clear() = items.clear()
        fun isEmpty(): Boolean = items.isEmpty()
        fun isNotEmpty(): Boolean = items.isNotEmpty()
        fun peek(): T? {
            return if (items.isNotEmpty()) {
                val element = items.first()
                items.drop(0)
                element
            } else null
        }

        constructor(vararg element: T) {
            items.addAll(element)
        }

        fun add(element: T): Boolean = items.add(element)
    }

}

