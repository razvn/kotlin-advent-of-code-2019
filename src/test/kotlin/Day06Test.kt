import io.kotlintest.data.forall
import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec
import io.kotlintest.tables.row

class Day06Test : StringSpec({


    "test values part1" {
        val inputData = listOf(
            "COM)B",
            "B)C",
            "C)D",
            "D)E",
            "E)F",
            "B)G",
            "G)H",
            "D)I",
            "E)J",
            "J)K",
            "K)L"
    )
        val day = Day06(inputData)
        forall(
                row(42)
        ) { result ->
            day.partOne() shouldBe result
        }
    }

    "f:test values part2" {
        val inputData = listOf(
                "COM)B",
                "B)C",
                "C)D",
                "D)E",
                "E)F",
                "B)G",
                "G)H",
                "D)I",
                "E)J",
                "J)K",
                "K)L",
                "K)YOU",
                "I)SAN"
        )
        val day = Day06(inputData)
        forall(
                row("YOU", "SAN",4)
        ) { a, b, result ->
            day.partTwo(a, b) shouldBe result
        }
    }
})