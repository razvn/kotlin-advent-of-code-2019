import io.kotlintest.data.forall
import io.kotlintest.matchers.collections.shouldContainExactly
import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec
import io.kotlintest.tables.row

class Day10Test : StringSpec({

    "test initOk" {
        forall(
                row(listOf(
                        listOf(".", "#", ".", ".", "#"),
                        listOf(".", ".", "#", "#", ".")
                ), listOf(
                        Asteroid(1, 0), Asteroid(4, 0),
                        Asteroid(2, 1), Asteroid(3, 1)

                ))
        ) { input, result ->
            val day = Day10(input)
            day.asteroidList shouldContainExactly result
        }
    }


    "test alignement" {
        forall(
                row(Asteroid(1, 1), Asteroid(3, 3), Asteroid(7, 7), true),
                row(Asteroid(1, 1), Asteroid(3, 1), Asteroid(7, 7), false),
                row(Asteroid(1, 0), Asteroid(2, 2), Asteroid(3, 4), true),
                row(Asteroid(8, 3), Asteroid(9, 1), Asteroid(9, 0), false)
        ) { a, b, c, result ->

            b.isAllignedAndCloser(a, c) shouldBe result
            a.isAllignedAndCloser(b, c) shouldBe false
        }
    }

    "test inSight" {
        forall(
                row(Asteroid(1, 1), listOf(Asteroid(3, 3), Asteroid(7, 7)), listOf(Asteroid(3, 3))),
                row(Asteroid(8, 3), listOf(Asteroid(9, 0), Asteroid(8, 1)), listOf(Asteroid(9, 0)))
        ) { a, b, result ->
            val day = Day10(emptyList())
            day.inSightFor(a, b) shouldContainExactly  result
        }
    }

    "test moveTarget" {
        forall(
            row(2, 0, 5, 5, 3 to 0),
            row(4, 0, 5, 5, 5 to 0),
            row(5, 0, 5, 5, 5 to 1),
            row(5, 4, 5, 5, 5 to 5),
            row(5, 5, 5, 5, 4 to 5),
            row(1, 5, 5, 5, 0 to 5),
            row(0, 5, 5, 5, 0 to 4),
            row(0, 2, 5, 5, 0 to 1),
            row(0, 1, 5, 5, 0 to 0),
            row(0, 0, 5, 5, 1 to 0)
        ) { x, y, maxX, maxY, result ->
            val day = Day10(emptyList())
            day.moveTarget(x, y, maxX, maxY) shouldBe result
        }
    }

    "test exData" {
        val input = """
        .#....#####,...#..
        ##...##.#####..##
        ##...#...#.#####.
        ..#.....X...###..
        ..#.#.....#....##
        """.trimIndent()

        val data = input.split("\n").map { it.trim().chunked(1) }
        val day = Day10(data)
        val maxY = data.size
        val maxX = data.maxBy { it.size }?.size ?: 0
        val loc = day.asteroidBestInSight(day.asteroidList) ?: throw Exception("no best")
        day.printBattlefild(maxX, maxY, day.asteroidList, emptyList(), loc, null)

        // day.partTwo(day.asteroidList)
    }
})