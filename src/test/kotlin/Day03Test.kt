import Day03.*
import io.kotlintest.data.forall
import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec
import io.kotlintest.tables.row

class Day03Test : StringSpec({
    val day = Day03(emptyList())

    "pointsFromPath test" {
        forall(
               row(listOf("U2"), setOf(Point(0,1), Point(0,2))),
               row(listOf("D2"), setOf(Point(0,-1), Point(0,-2))),
               row(listOf("L3"), setOf(Point(-1,0), Point(-2,0), Point(-3,0))),
               row(listOf("R3"), setOf(Point(1,0), Point(2,0), Point(3,0))),
                row(listOf("U2","R3"), setOf(Point(0,1), Point(0,2),Point(1,2), Point(2,2), Point(3,2)))
        ) { input, result ->
            day.pointsFromPath(input).keys shouldBe result
        }
    }

    "pointToManhattanDistance test" {
        forall(
                row(Point(0,2), 2),
                row(Point(2,0), 2),
                row(Point(0,-2), 2),
                row(Point(-2,0), 2),
                row(Point(-2,-3), 5),
                row(Point(2,3), 5),
                row(Point(-2,3), 5)
        ) { input, result ->
            day.pointToManhattanDistance(input) shouldBe result
        }
    }


    "test part one exemples are validated" {
        forall(
                row(listOf("R8,U5,L5,D3", "U7,R6,D4,L4"), 6),
                row(listOf("R75,D30,R83,U83,L12,D49,R71,U7,L72", "U62,R66,U55,R34,D71,R55,D58,R83"), 159),
                row(listOf("R98,U47,R26,D63,R33,U87,L62,D20,R33,U53,R51", "U98,R91,D20,R16,D67,R40,U7,R15,U6,R7"), 135)
        ) { input, result ->
            day.partOneMinimalDistance(input) shouldBe result
        }
    }

    "test part two exemples are validated" {
        forall(
                row(listOf("R8,U5,L5,D3", "U7,R6,D4,L4"), 30),
                row(listOf("R75,D30,R83,U83,L12,D49,R71,U7,L72", "U62,R66,U55,R34,D71,R55,D58,R83"), 610),
                row(listOf("R98,U47,R26,D63,R33,U87,L62,D20,R33,U53,R51", "U98,R91,D20,R16,D67,R40,U7,R15,U6,R7"), 410)
        ) { input, result ->
            day.partTwoMinimalIntersection(input) shouldBe result
        }
    }
})