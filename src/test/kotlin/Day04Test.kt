import io.kotlintest.data.forall
import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec
import io.kotlintest.tables.row

class Day04Test : StringSpec({
    val day = Day04(0 to 0)

    "test valid numbers part one" {
        forall(
                row(2222222, true),
                row(2234567, true),
                row(2334567, true),
                row(223450, false),
                row(123789, false),
                row(111111, true)
        ) { input, result ->
            // day.partOne(input) shouldBe result
            day.validNumber(input) shouldBe result
        }
    }

    "test valid numbers part two" {
        forall(
                row(222222, false),
                row(223456, true),
                row(233456, true),
                row(223450, false),
                row(123789, false),
                row(123799, true),
                row(111111, false),
                row(112233, true),
                row(123444, false),
                row(111122, true),
                row(112222, true),
                row(111222, false),
                row(113322, false),
                row(111339, true)
        ) { input, result ->
            // day.partOne(input) shouldBe result
            day.validNumber(input, true) shouldBe result
        }
    }

    "test IntToListInt" {
        forall(
                row(123456, listOf(1, 2, 3, 4, 5, 6)),
                row(220011, listOf(2, 2, 0, 0, 1, 1))

        ) { input, result ->
            day.intToListInt(input) shouldBe result
        }
    }

    "test checkRepeatableValue part one" {
        forall(
                row(listOf(1, 2, 3, 4, 5, 6), false),
                row(listOf(2, 2, 0, 0, 1, 1), true),
                row(listOf(1, 1, 1, 2, 2, 2), true)
        ) { input, result ->
            day.checkRepeatableValue(input, false) shouldBe result
        }
    }

    "test checkRepeatableValue part two" {
        forall(
                row(listOf(1, 2, 3, 4, 5, 6), false),
                row(listOf(2, 2, 0, 0, 1, 1), true),
                row(listOf(1, 1, 1, 2, 2, 2), false),
                row(listOf(1, 1, 2, 2, 2, 2), true),
                row(listOf(1, 1, 2, 2, 2, 4), true),
                row(listOf(1, 2, 3, 3, 3, 4), false),
                row(listOf(1, 2, 2, 3, 3, 3), true)
        ) { input, result ->
            day.checkRepeatableValue(input, true) shouldBe result
        }
    }

})