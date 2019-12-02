import io.kotlintest.data.forall
import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec
import io.kotlintest.tables.row

open class Day01Test : StringSpec({
    val day01 = Day01()

    "test values are validate part one" {
        forall(
                row(12, 2),
                row(14, 2),
                row(1969, 654),
                row(100756, 33583)
        ) { mass, fuel ->
            day01.getFuel(mass.toLong()) shouldBe fuel.toLong()
        }
    }

    "test total fuel part one" {
        val input = listOf<Long>(12, 14, 1969, 100756)
        val result = 2 + 2 + 654 + 33583
        day01.getPartOneFuel(input) shouldBe result.toLong()
    }

    "test values are validate part two" {
        forall(
                row(12,2),
                row(14,2),
                row(1969,966),
                row(100756,50346)
        ) { mass, fuel ->
            day01.getTotalModuleFuel(mass.toLong()) shouldBe fuel.toLong()
        }
    }

    "test total fuel part two" {
        val input = listOf<Long>(12, 14, 1969, 100756)
        val result = 2 + 2 + 966 + 50346
        day01.getPartTwoFuel(input) shouldBe result.toLong()
    }

})