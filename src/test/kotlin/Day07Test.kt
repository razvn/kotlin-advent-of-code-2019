import io.kotlintest.data.forall
import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec
import io.kotlintest.tables.row

class Day07Test : StringSpec({
    val day = Day07(emptyList())

    "test combinationsFor" {
        forall(
                row(listOf(0, 1, 2, 3), 1, setOf(listOf(0, 2, 1, 3), listOf(0, 3, 2, 1))),
                row(listOf(0, 1, 2, 3), 2, setOf(listOf(0, 1, 3, 2)))
        ) { input, index, result ->
            day.combinationsFor(input, index) shouldBe result
        }
    }

    "test partOne" {
        forall(
                row(0, listOf(3, 15, 3, 16, 1002, 16, 10, 16, 1, 16, 15, 15, 4, 15, 99, 0, 0), 43210, listOf(4, 3, 2, 1, 0)) //,
                //row(0, listOf(3, 23, 3, 24, 1002, 24, 10, 24, 1002, 23, -1, 23, 101, 5, 23, 23, 1, 24, 23, 23, 4, 23, 99, 0, 0), 54321, listOf(0, 1, 2, 3, 4)),
                //row( 0, listOf(3, 31, 3, 32, 1002, 32, 10, 32, 1001, 31, -2, 31, 1007, 31, 0, 33, 1002, 33, 7, 33, 1, 33, 31, 31, 1, 32, 31, 31, 4, 31, 99, 0, 0, 0), 65210, listOf(1, 0, 4, 3, 2))
        ) { start, program, result, seq ->
            val myDay = Day07(program)
            myDay.partOne() shouldBe result
        }
    }
})