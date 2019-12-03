import Day03.Point
import io.kotlintest.data.forall
import io.kotlintest.matchers.maps.shouldContainExactly
import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec
import io.kotlintest.tables.row

class Day03Test : StringSpec({
    val day = Day03(emptyList())

    "pointsFromPath test" {
        forall(
                row(listOf("U2"), mapOf(Point(0, 1) to 1, Point(0, 2) to 2)),
                row(listOf("D2"), mapOf(Point(0, -1) to 1, Point(0, -2) to 2)),
                row(listOf("L3"), mapOf(Point(-1, 0) to 1, Point(-2, 0) to 2, Point(-3, 0) to 3)),
                row(listOf("R3"), mapOf(Point(1, 0) to 1, Point(2, 0) to 2, Point(3, 0) to 3)),
                row(listOf("U2", "R3"), mapOf(Point(0, 1) to 1, Point(0, 2) to 2, Point(1, 2) to 3, Point(2, 2) to 4, Point(3, 2) to 5))
        ) { input, result ->
            day.pointsFromPath(input) shouldContainExactly result
        }
    }

    "pointToManhattanDistance test" {
        forall(
                row(Point(0, 2), 2),
                row(Point(2, 0), 2),
                row(Point(0, -2), 2),
                row(Point(-2, 0), 2),
                row(Point(-2, -3), 5),
                row(Point(2, 3), 5),
                row(Point(-2, 3), 5)
        ) { input, result ->
            day.pointToManhattanDistance(input) shouldBe result
        }
    }

    "shortestDistanceFromPoints test" {
        forall(
                row(setOf(Point(0, 2), Point(3,5), Point(7, 8)),
                        setOf(Point(1,2), Point(7,8), Point(0,1), Point(3,5)), 8),
                row(setOf(Point(0, -2), Point(1, 3)),
                        setOf(Point(0,-2), Point(1, 3)), 2)
        ) { first, second, result ->
            day.shortestDistanceFromPoints(first, second) shouldBe result
        }
    }

    "shortestIntersectionFromPoints test" {
        forall(
                row(mapOf(Point(0, 2) to 2, Point(3,5) to 8, Point(7, 8) to 10),
                        mapOf(Point(1,2) to 3, Point(7,8) to 16, Point(0,1) to 2, Point(3,5) to 19), 26),
                row(mapOf(Point(0, -2)  to 2, Point(1, 3) to 6),
                        mapOf(Point(0,-2) to 20, Point(1, 3) to 9), 15)
        ) { first, second, result ->
            day.shortestIntersectionFromPoints(first, second) shouldBe result
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