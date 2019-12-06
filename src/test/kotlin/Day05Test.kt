import Day05.*
import Day05.Operation.*
import Day05.ParamMode.*
import Day05.PositionType.RELATIVE
import io.kotlintest.data.forall
import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec
import io.kotlintest.tables.row

class Day05Test : StringSpec({
    val day05 = Day05(emptyList(), emptyList())

    "test destructureCommand" {
        forall(
                row(99, Triple(HALT, MEMORY,MEMORY)),
                row(1, Triple(ADD,MEMORY,MEMORY)),
                row(4, Triple(OUTPUT,MEMORY,MEMORY)),
                row(5, Triple(JUMPTRUE,MEMORY,MEMORY)),
                row(6, Triple(JUMPFALSE,MEMORY,MEMORY)),
                row(7, Triple(LESSTHAN,MEMORY,MEMORY)),
                row(8, Triple(EQUALS,MEMORY,MEMORY)),
                row(101, Triple(ADD,IMMEDIATE,MEMORY)),
                row(1002, Triple(MULTIPLY,MEMORY,IMMEDIATE)),
                row(1103, Triple(INPUT,IMMEDIATE,IMMEDIATE)),
                row(199, Triple(HALT,IMMEDIATE,MEMORY))
        ) { input, result ->
            day05.destructureCommand(input) shouldBe result
        }
    }


    "test buildInstruction" {
        forall(
                row(mutableListOf(1, 0, 0, 0, 99), 0, listOf(3), 0, Instruction(1, ADD, Parameter(MEMORY,0,1), Parameter(MEMORY,0,1), 0, Result(2, Position(RELATIVE, 3)), Position(RELATIVE, 4))),
                row(mutableListOf(1, 0, 0, 0, 107, 1, 4, 3, 99), 4, listOf(3), 0, Instruction(107, LESSTHAN, Parameter(IMMEDIATE,1,1), Parameter(MEMORY,4,107), 4, Result(1, Position(RELATIVE, 3)), Position(RELATIVE, 4)))
        ) { input, pointer, inputValues, inputIdx, result ->
            day05.buildInstruction(input, pointer, inputValues, inputIdx) shouldBe result
        }
    }

    "test values are validated" {
        forall(
                row(listOf(1, 9, 10, 3, 2, 3, 11, 0, 99, 30, 40, 50), listOf(3500, 9, 10, 70, 2, 3, 11, 0, 99, 30, 40, 50)),
                row(listOf(1, 0, 0, 0, 99), listOf(2, 0, 0, 0, 99)),
                row(listOf(2, 3, 0, 3, 99), listOf(2, 3, 0, 6, 99)),
                row(listOf(2, 4, 4, 5, 99, 0), listOf(2, 4, 4, 5, 99, 9801)),
                row(listOf(1, 1, 1, 4, 99, 5, 6, 0, 99), listOf(30, 1, 1, 4, 2, 5, 6, 0, 99))
        ) { input, result ->
            day05.analyzeProgram(input, emptyList()).memory shouldBe result
        }
    }

    "test values with operations validated" {
        forall(
                row(listOf(3,0,4,0,99), listOf(5), listOf<Int>(5), listOf(5,0,4,0,99)),
                row(listOf(103,5,4,5,99,0), listOf(5), listOf<Int>(5), listOf(103,5,4,5,99,5)),
                row(listOf(1101,100,-1,4,0), listOf<Int>(), listOf<Int>(), listOf(1101,100,-1,4,99))

        ) { inputCommands, inputValues, output, memory ->
            day05.analyzeProgram(inputCommands, inputValues, false).memory shouldBe memory
            day05.analyzeProgram(inputCommands, inputValues, false).output shouldBe output
        }
    }
})