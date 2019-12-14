import Day12.Coordinates
import Day12.Moon
import io.kotlintest.data.forall
import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec
import io.kotlintest.tables.row

class Day11Test : StringSpec({

    val inputData = listOf(
            Moon("Io", Coordinates(-1, 0, 2)),
            Moon("Europa", Coordinates(2, -10, -7)),
            Moon("Ganymede", Coordinates(4, -8, 8)),
            Moon("Callisto", Coordinates(3, 5, -1))
    )
    val day = Day12(inputData)

    "test equals" {
        val m1 = Moon("Io", Coordinates(-1, 0, 2), Coordinates(3, 1, 2))
        val m2 = Moon("Io", Coordinates(-1, 0, 2), Coordinates(3, 1, 2))

        (m1 == m2) shouldBe true
    }

    "f:test partTwo" {
        val inputData2 = listOf(
                Moon("Io", Coordinates(-8, -10, 0)),
                Moon("Europa", Coordinates(5, 5, 10)),
                Moon("Ganymede", Coordinates(2, -7, 3)),
                Moon("Callisto", Coordinates(9, -8, -3))
        )
        day.partTwo(inputData2)
    }

    "test values are validated" {
        forall(
                row(listOf(1), "TODO")
        ) { input, result ->

            day.allPairs(inputData).forEach { println("${it.first.name} - ${it.second.name}") }

            println("0 step")
            inputData.forEach {
                println(it)
            }
            day.applyGravity(inputData)

            println("0 step - gravity")
            inputData.forEach {
                println(it)
            }

            day.applyVelocity(inputData)
            println("After 1 step : ")
            inputData.forEach {
                println(it)
            }

            //inputData.forEach {
            //    println(it)
            //}
            // day.partOne(input) shouldBe result
        }
    }
})