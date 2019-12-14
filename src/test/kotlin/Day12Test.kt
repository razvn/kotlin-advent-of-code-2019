import io.kotlintest.data.forall
import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec
import io.kotlintest.tables.row

class Day12Test : StringSpec({
    val day = Day1x(emptyList())

    "test values are validated" {
        forall(
                row(listOf(1), "TODO")
        ) { input, result ->
            day.partOne(input) shouldBe result
        }
    }
})