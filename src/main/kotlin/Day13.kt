import Day13.TileType.*
import InitCodeComputerDay13.OperationType.*
import java.io.File
import kotlin.math.sign
import kotlin.system.measureTimeMillis

fun main() {
    val inputData = Day13.inputDataFromFile("day13.txt")
    val service = Day13(inputData, true)
    val duration = measureTimeMillis {
        service.printResults()
    }
    println("Duration: $duration ms")
}

class Day13(private val data: List<Long>, private val printPlayground: Boolean = false, private val prefix: String = "") {

    fun partOne(data: List<Long>): String {
        val computer = InitCodeComputerDay13(data)
        val inputQueue = InitCodeComputerDay13.AdventQueue<Long>()
        // inputQueue.addAll(listOf(1L))
        val result = computer.runProgram(inputQueue, debug = false)
        var score: Int = 0
        val game = result.output.chunked(3).mapNotNull {
            val x = it[0].toInt()
            val y = it[1].toInt()
            val z = it[2].toInt()

            if (x == -1 && y == 0) {
                score = z
                null
            } else {
                Tile(x, y, TileType.fromCode(z))
            }
        }.toSet()

        val nbBlockTiles = game.count { it.id == BLOCK }

        if (printPlayground) {
            printGame(game)
            printScore(score, 0, game)
        }
        //game.forEach { println(it) }
        return nbBlockTiles.toString()
    }

    fun partTwo(data: List<Long>): String {
        val newData = data.toMutableList().also { it[0] = 2 }.toList()
        val score = playGame(newData)
        return score.toString()
    }

    fun printResutPartOne(): String {
        return "${prefix}Result part one is: ${partOne(data)}"
    }

    fun printResutPartTwo(): String {
        return "${prefix}Result part two is: ${partTwo(data)}"
    }

    fun printResults() {
        val p1 = printResutPartOne()
        val p2 = printResutPartTwo()

        println(p1)
        println(p2)
    }

    private fun playGame(gameProgram: List<Long>): Int {
        val gameData: MutableSet<Tile> = mutableSetOf()
        val computer = InitCodeComputerDay13(gameProgram)
        val inputQueue = InitCodeComputerDay13.AdventQueue<Long>()
        var score = 0

        while (true) {
            val result = computer.runProgram(inputQueue, resetProgram = false, debug = false)

            val game = result.output.chunked(3).mapNotNull {
                val x = it[0].toInt()
                val y = it[1].toInt()
                val z = it[2].toInt()

                if (x == -1 && y == 0) {
                    score = z
                    null
                } else {
                    val tile = Tile(x, y, TileType.fromCode(z))
                    tile
                }

            }.toSet()

            var paddleDirection = 0
            if (result.pause) {
                // get ball position
                val ball = game.firstOrNull { it.id == BALL }
                val oldBall = gameData.firstOrNull { it.id == BALL }
                val paddle = game.firstOrNull { it.id == PADDLE }
                val oldPaddle = gameData.firstOrNull { it.id == PADDLE }

                ball?.let { b ->
                    // gameData.filter { it.id == BALL }.map { it.id = OLD_BALL }
                    gameData.filter { it.id == BALL }.forEach { gameData.remove(it) }
                }
                if (paddle != null) {
                    // gameData.filter { it.id == PADDLE }.map { it.id = OLD_PADDLE }
                    gameData.filter { it.id == PADDLE }.forEach { gameData.remove(it) }
                }

                val b = ball ?: oldBall
                val p = paddle ?: oldPaddle
                if (b != null && p != null && oldBall != null) {
                    val ballInpactPosition = (b.x - oldBall.x) * (p.y - b.y) + b.x - 1
                    paddleDirection = (ballInpactPosition - p.x).sign
                    // println("Old ball: ${oldBall.x}, oldPaddle: ${oldPaddle?.x}, ball: ${ball?.x}, paddle: ${paddle?.x}, inpact: $ballInpactPosition, direction: $paddleDirection")
                }

                // add new data to game
                game.forEach { gameData.add(it) }
                inputQueue.add(paddleDirection.toLong())
            }

            if (printPlayground) {
                printGame(gameData)
                printScore(score, paddleDirection, gameData)
            }

            if (!result.pause) {
                break
            }
        }
        return score
    }

    fun printGame(game: Set<Tile>) {
        val maxX = game.maxBy { it.x }?.x ?: 0
        val maxY = game.maxBy { it.y }?.y ?: 0

        var playground = arrayOf<Array<String>>()
        (0..maxY).forEach {
            var array = arrayOf<String>()
            (0..maxX).forEach { array += " " }
            playground += array
        }
        //clear console
        Runtime.getRuntime().exec("pwd")
        //System.out.print("\033\143")
        game.forEach {
            try {
                playground[it.y][it.x] = when (it.id) {
                    EMPTY -> " "
                    WALL -> "█"
                    BLOCK -> "▒"
                    PADDLE -> "═"
                    OLD_PADDLE -> "-"
                    BALL -> "°"
                    OLD_BALL -> "."
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        playground.forEach {
            println(it.joinToString(""))
        }

        println()
    }

    fun printScore(score: Int, paddleDirection: Int, gameData: Set<Tile>) {
        val blockTitles = gameData.filter { it.id == BLOCK }.toMutableList()
        gameData.filter { it.id == EMPTY }.forEach { e ->
            val block = blockTitles.firstOrNull { e.x == it.x && e.y == it.y }
            block?.let {
                blockTitles.remove(block)
            }
        }

        val nbBlockTiles = blockTitles.count() // gameData.count { it.id == BLOCK }
        println("= Score: $score (blocks: $nbBlockTiles - joystick: $paddleDirection) =")
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

    data class Tile(val x: Int, val y: Int, var id: TileType)

    enum class TileType(val value: Int) {
        EMPTY(0), WALL(1), BLOCK(2), PADDLE(3), BALL(4), OLD_PADDLE(33), OLD_BALL(44);

        companion object {
            fun fromCode(code: Int) = values().firstOrNull { it.value == code }
                    ?: throw Exception("Tile id: $code unsupported")
        }
    }
}

class InitCodeComputerDay13(private val program: List<Long>) {
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
        if (state == State.PAUSE) {
            state = State.RUNNING
            output.clear()
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
                                (internalProgram.size..resultPosition).forEach { internalProgram.add(it.toInt(), 0) }
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
                    throw Exception("Input value null")
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
                    ?: "-", param2?.debug() ?: "-", result.debug(), positionAfter.debug())
        }
    }

    data class DebugInfos(val pointer: String, val base: String, val code: String, val operation: String,
                          val param1: String, val param2: String, val result: String, val positionAfter: String)

    data class Parameter(val mode: ParamMode, val initial: Long, val final: Long) {
        fun debug(): String {
            val prefix = when (mode) {
                ParamMode.MEMORY -> "$"
                ParamMode.IMMEDIATE -> "="
                ParamMode.RELATIVE -> "+$"
            }
            return "$prefix$initial ($final)"
        }
    }

    data class Result(val value: Long, val position: Position) {
        fun debug(): String {
            return "${position.debug()} ${if (position.type != PositionType.NONE) "($value)" else ""}"
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
            return "$prefix${if (type != PositionType.NONE) value else ""}"
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
                items.removeAt(0)
                element
            } else null
        }

        constructor(vararg element: T) {
            items.addAll(element)
        }

        fun add(element: T): Boolean = items.add(element)
    }
}

