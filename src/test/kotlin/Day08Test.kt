import io.kotlintest.data.forall
import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec
import io.kotlintest.tables.row

class Day08Test : StringSpec({

    "test values partOne" {
        forall(
                row("123456789012", 1),
                row("123456121012", 6)
        ) { input, result ->
            val day = Day08(input, 3, 2)
            day.partOne(input) shouldBe result
        }
    }

    "f:test values partTwo" {
        forall(
                row("0222112222120000", listOf(0,1,1,0))
        ) { input, result ->
            val day = Day08(input, 2, 2)
            day.partTwo(input) shouldBe result
        }
    }
})