import io.kotlintest.data.forall
import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec
import io.kotlintest.tables.row

class Day02Test : StringSpec({
    val day02 = Day02()

    "test values are validated" {
        forall(
                row(listOf(1, 9, 10, 3, 2, 3, 11, 0, 99, 30, 40, 50), listOf(3500, 9, 10, 70, 2, 3, 11, 0, 99, 30, 40, 50)),
                row(listOf(1, 0, 0, 0, 99), listOf(2, 0, 0, 0, 99)),
                row(listOf(2, 3, 0, 3, 99), listOf(2, 3, 0, 6, 99)),
                row(listOf(2, 4, 4, 5, 99, 0), listOf(2, 4, 4, 5, 99, 9801)),
                row(listOf(1, 1, 1, 4, 99, 5, 6, 0, 99), listOf(30, 1, 1, 4, 2, 5, 6, 0, 99))
        ) { input, result ->
            day02.analyzeCommand(input) shouldBe result
        }
    }
})