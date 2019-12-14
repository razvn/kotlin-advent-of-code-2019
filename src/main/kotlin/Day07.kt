import InitCodeComputerDay07.OperationType.*
import java.io.File
import kotlin.system.measureTimeMillis

fun main() {
    val inputData = Day07.inputDataFromFile("day07.txt")

    val service = Day07(inputData)
    val duration = measureTimeMillis {
        service.printResutPartOne()
        // service.printResutPartTwo()
    }
    println("Duration: $duration ms")
}

class Day07(private val data: List<Int>, private val prefix: String = "") {
    fun printResutPartOne() {
        //println("${prefix}Result part one is: ${partOne()}")
        println("${prefix}Result part one is: Not working")
    }

    fun partOne(): Int {
        val possibleSequences = allCombinations(0, 4)

        // println("${possibleSequences.count()} : $possibleSequences")
        val resp = possibleSequences.map {
            calculateValueForSequence(it, 0, data)
        }

        return resp.max() ?: 0
    }

    fun partTwo(): Int {
        val possibleSequences = allCombinations(5, 9)



        return 1

    }

    fun calculateValueForSequence(sequence: List<Int>, startValue: Int, program: List<Int>): Int {
        var inputSignal = startValue
        sequence.forEach { phase ->
            val computer = InitCodeComputerDay07(data)
            val inputQueue = InitCodeComputerDay07.AdventQueue<Int>().also { it.add(inputSignal) }
            val resp = computer.runProgram(inputQueue, resetProgram = false, debug = true)
            inputSignal = resp.output.first()
        }

        return inputSignal
    }

    fun allCombinations(start: Int, end: Int): Set<List<Int>> {
        val result = mutableSetOf<List<Int>>()

        (start..end).forEach { a ->
            (start..end).forEach { b ->
                (start..end).forEach { c ->
                    (start..end).forEach { d ->
                        (start..end).forEach { e ->
                            if (a != b && a != c && a != d && a != e &&
                                    b != c && b != d && b != e &&
                                    c != d && c != e &&
                                    d != e
                            ) result.add(listOf(a, b, c, d, e))
                        }
                    }
                }
            }
        }

        return result
    }

    fun combinationsFor(inputValues: List<Int>, index: Int): Set<List<Int>> {
        val result = mutableSetOf<List<Int>>()
        (index + 1 until inputValues.size).forEach {
            val newList = inputValues.toMutableList()
            val a = newList[index]
            val b = newList[it]
            newList[index] = b
            newList[it] = a
            result.add(newList.toList())
        }
        return result
    }

    fun printResutPartTwo() {
        // val originalProgram = data.toMutableList()
        // println("${prefix}Result part two is: ${analyzeProgram(originalProgram, listOf(input.last()), false).output}")
    }


    companion object {
        val fileDir = "src/main/resources/"

        fun inputDataFromFile(fileName: String): List<Int> = loadDataFromFile(fileName)
                .flatMap { it.split(",") }
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


class InitCodeComputerDay07(val initialMemory: List<Int>, val input: MutableList<Int> = mutableListOf()) {

    val memory: MutableList<Int> = initialMemory.toMutableList()
    val output = mutableListOf<Int>()
    var instructionPointer = 0
    var state = State.NOT_STARTED

    fun runProgram(input: AdventQueue<Int>, resetProgram: Boolean = true, debug: Boolean = true): ProgramResult {

        if (debug) {
            println("Start with input: $input - program: $initialMemory")
        }

        if (memory.isEmpty() || resetProgram) {
            memory.addAll(initialMemory)
        }

        if (state == State.WAITING_INPUT) {
            state = State.RUNNING
        }
        val debugHistory = mutableListOf<DebugInfos>()

        while (state == State.RUNNING) {
            val instructionCode = memory[instructionPointer]
            val operation = destructureCommand(instructionCode)


            if (operation.type == INPUT && input.isEmpty()) {
                state = State.WAITING_INPUT
            } else {
                val instruction = buildInstruction(memory, instructionPointer, operation, input)
                if (debug) {
                    debugHistory.add(instruction.debug(instructionPointer.toString()))
                }

                if (operation.type == OUTPUT) {
                    output.add(instruction.result.value)
                }

                if (operation.type == HALT) {
                    state = State.TERMINATED
                } else {
                    // update with new value if applicable
                    with(instruction.result) {
                        val resultPosition = when (position.type) {
                            PositionType.ABSOLUTE -> memory.getOrElse(position.value) { 0 }
                            PositionType.RELATIVE -> memory.getOrElse((instructionPointer + position.value)) { 0 }
                            PositionType.EXACT -> position.value
                            PositionType.NONE -> null
                        }
                        resultPosition?.let {
                            memory[resultPosition] = value
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

        return ProgramResult(output = output.toList(), memory = memory, state = state)
    }

    fun buildInstruction(res: MutableList<Int>, ip: Int, operation: Operation, input: AdventQueue<Int>): Instruction {
        val instruction = when (operation.type) {
            ADD -> { // 1
                val param1 = getParam(ip, res, 1, operation)
                val param2 = getParam(ip, res, 2, operation)
                val resPosition = getPosition(ip, res, 3, operation)
                val result = Result(param1.final + param2.final, Position(PositionType.EXACT, resPosition))
                val positionAfter = Position(PositionType.RELATIVE, 4)
                Instruction(operation.code, operation.type, param1, param2, ip, result, positionAfter)
            }
            MULTIPLY -> { // 2
                val param1 = getParam(ip, res, 1, operation)
                val param2 = getParam(ip, res, 2, operation)
                val resPosition = getPosition(ip, res, 3, operation)
                val result = Result(param1.final * param2.final, Position(PositionType.EXACT, resPosition))
                val positionAfter = Position(PositionType.RELATIVE, 4)
                Instruction(operation.code, operation.type, param1, param2, ip, result, positionAfter)
            }
            INPUT -> { // 3
                val inputValue = input.peek()
                if (inputValue != null) {
                    val param1 = Parameter(ParamMode.IMMEDIATE, inputValue, inputValue)
                    val position = getPosition(ip, res, 1, operation)
                    val result = Result(inputValue, Position(PositionType.EXACT, position))
                    val positionAfter = Position(PositionType.RELATIVE, 2)
                    Instruction(operation.code, operation.type, param1, null, ip, result, positionAfter)
                } else {
                    throw Exception("Input value null")
                }
            }
            OUTPUT -> { // 4
                val param1 = getParam(ip, res, 1, operation)
                val result = Result(param1.final, Position(PositionType.NONE, 0))
                val positionAfter = Position(PositionType.RELATIVE, 2)
                Instruction(operation.code, operation.type, param1, null, ip, result, positionAfter)
            }
            JUMPTRUE -> { // 5
                val param1 = getParam(ip, res, 1, operation)
                val param2 = getParam(ip, res, 2, operation)
                val result = Result(0, Position(PositionType.NONE, 0))
                val positionAfter = if (param1.final != 0) Position(PositionType.ABSOLUTE, param2.final) else Position(PositionType.RELATIVE, 3)
                Instruction(operation.code, operation.type, param1, param2, ip, result, positionAfter)
            }
            JUMPFALSE -> { // 6
                val param1 = getParam(ip, res, 1, operation)
                val param2 = getParam(ip, res, 2, operation)
                val result = Result(0, Position(PositionType.NONE, 0))
                val positionAfter = if (param1.final == 1) Position(PositionType.ABSOLUTE, param2.final) else Position(PositionType.RELATIVE, 3)
                Instruction(operation.code, operation.type, param1, param2, ip, result, positionAfter)
            }
            LESSTHAN -> { // 7
                val param1 = getParam(ip, res, 1, operation)
                val param2 = getParam(ip, res, 2, operation)
                val position = getPosition(ip, res, 3, operation)
                val result = Result(if (param1.final < param2.final) 1 else 0, Position(PositionType.EXACT, position))
                val positionAfter = Position(PositionType.RELATIVE, 4)
                Instruction(operation.code, operation.type, param1, param2, ip, result, positionAfter)
            }
            EQUALS -> { // 8
                val param1 = getParam(ip, res, 1, operation)
                val param2 = getParam(ip, res, 2, operation)
                val position = getPosition(ip, res, 3, operation)
                val result = Result(if (param1.final == param2.final) 1 else 0, Position(PositionType.EXACT, position))
                val positionAfter = Position(PositionType.RELATIVE, 4)
                Instruction(operation.code, operation.type, param1, param2, ip, result, positionAfter)
            }
            HALT -> { // 99
                val result = Result(0, Position(PositionType.NONE, 0))
                val positionAfter = Position(PositionType.NONE, 0)
                Instruction(operation.code, operation.type, null, null, ip, result, positionAfter)
            }
        }
        return instruction
    }

    fun destructureCommand(input: Int): Operation {
        val commands = mutableListOf<Int>()
        var nb = input
        while (nb > 0) {
            val n = nb % 10
            commands.add(n)
            nb /= 10
        }
        val op = commands.getOrElse(0) { 0 } + 10 * commands.getOrElse(1) { 0 }

        val params = if (commands.size > 2) commands.subList(2, commands.size).map { it }.toList() else emptyList()

        return Operation(input, OperationType.fromCode(op), params)
    }

    private fun getParam(pos: Int, data: List<Int>, paramNb: Int, operation: Operation): Parameter {
        val position = getPosition(pos, data, paramNb, operation)
        val value = data.getOrElse(position) { 0 }
        val mode = operation.getModeForParamIdx(paramNb)
        return Parameter(mode, position, value)
    }

    private fun getPosition(pos: Int, data: List<Int>, paramNb: Int, operation: Operation): Int {
        val mode = operation.getModeForParamIdx(paramNb)
        val memValue = pos + paramNb
        return when (mode) {
            ParamMode.IMMEDIATE -> memValue
            ParamMode.POSITION -> data.getOrElse(memValue) { 0 }
        }
    }

    data class ProgramResult(val output: List<Int>, val memory: List<Int>, val state: State)

    data class Instruction(
            val code: Int,
            val operationType: OperationType,
            val param1: Parameter?,
            val param2: Parameter?,
            val position: Int,
            val result: Result,
            val positionAfter: Position
    ){
        fun debug(pointer: String): DebugInfos {
            return DebugInfos(pointer, code.toString(), operationType.toString(), param1?.debug()
                    ?: "-", param2?.debug() ?: "-", result.debug(), positionAfter.debug())
        }
    }

    data class Parameter(val mode: ParamMode, val initial: Int, val final: Int) {
        fun debug(): String {
            val prefix = when (mode) {
                ParamMode.POSITION -> "$"
                ParamMode.IMMEDIATE -> "="
            }
            return "$prefix$initial ($final)"
        }
    }

    private operator fun MutableList<Int>.get(parmeter: Parameter): Int {
        return when (parmeter.mode) {
            ParamMode.POSITION -> this[parmeter.final]
            ParamMode.IMMEDIATE -> parmeter.final
        }
    }

    data class Result(val value: Int, val position: Position) {
        fun debug(): String {
            return "${position.debug()} ${if (position.type != PositionType.NONE) "($value)" else ""}"
        }
    }

    data class Position(val type: PositionType, val value: Int) {
        fun debug(): String {
            val prefix = when (type) {
                PositionType.RELATIVE -> "+"
                PositionType.ABSOLUTE -> ""
                PositionType.EXACT -> "="
                PositionType.NONE -> "-"
            }
            return "$prefix${if (type != PositionType.NONE) value else ""}"
        }
    }

    enum class PositionType {
        RELATIVE, ABSOLUTE, NONE, EXACT
    }

    data class DebugInfos(val pointer: String, val code: String, val operation: String,
                          val param1: String, val param2: String, val result: String, val positionAfter: String)

    data class Operation(val code: Int, val type: OperationType, private val paramsMode: List<Int>) {
        fun getModeForParamIdx(idx: Int): ParamMode = ParamMode.fromCode(paramsMode.getOrElse(idx - 1) { 0 })
    }

    enum class OperationType(val code: Int) {
        ADD(1), MULTIPLY(2), INPUT(3), OUTPUT(4), JUMPTRUE(5), JUMPFALSE(6), LESSTHAN(7), EQUALS(8), HALT(99);
        companion object {
            fun fromCode(code: Int) = values().firstOrNull { it.code == code }
                    ?: throw UnexpectedValueException("Operation: $code unsupported")
        }
    }

    enum class State {
        NOT_STARTED, RUNNING, WAITING_INPUT, TERMINATED
    }

    enum class ParamMode(val code: Int) {
        POSITION(0), IMMEDIATE(1);

        companion object {
            fun fromCode(code: Int) = values().firstOrNull { it.code == code }
                    ?: throw UnexpectedValueException("Parameter mode: $code unsupported")
        }
    }

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
                items.removeAt(0)
                element
            } else null
        }

        constructor(vararg element: T) {
            items.addAll(element)
        }

        fun add(element: T): Boolean = items.add(element)
    }

    class UnexpectedValueException(msg: String? = null) : Exception(msg)
}