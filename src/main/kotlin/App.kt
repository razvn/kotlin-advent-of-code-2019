import Day12.Coordinates
import Day12.Moon
import kotlin.system.measureTimeMillis

class App {
    val greeting: String
        get() {
            return "Welcome to Advent of Code 2019"
        }
}

fun main() {
    println(App().greeting)
    println("~".repeat(App().greeting.length))

    val day01InputData = Day01.inputDataFromFile("day01.txt")
    val day01 = Day01(day01InputData)
    val durationDay1 = measureTimeMillis {
        day01.printResultPartOne()
        day01.printResultPartTwo()
    }
    println("Duration Day 1 : $durationDay1 ms")
    println("=".repeat(100))

    val day02InputData = Day02.inputDataFromFile("day02.txt")

    val day02 = Day02(day02InputData, "Day 2 : ")
    val durationDay2 = measureTimeMillis {
        day02.printResultPartOne()
        day02.printResultPartTwo()
    }
    println("Duration Day 2 : $durationDay2 ms")
    println("=".repeat(100))

    val day03InputData = Day03.inputDataFromFile("day03.txt")

    val day03 = Day03(day03InputData, "Day 3 : ")
    val durationDay3 = measureTimeMillis {
        day03.printResultPartOne()
        day03.printResultPartTwo()
    }
    println("Duration Day 3 : $durationDay3 ms")
    println("=".repeat(100))

    val day04InputData = 152085 to 670283
    val day04 = Day04(day04InputData, "Day 4 : ")
    val durationDay4 = measureTimeMillis {
        day04.printResultPartOne()
        day04.printResultPartTwo()
    }

    println("Duration Day 4 : $durationDay4 ms")
    println("=".repeat(100))

    val day05InputData = Day05.inputDataFromFile("day05.txt")
    val day05InputValues = listOf(1, 5)
    val day05 = Day05(data = day05InputData, input = day05InputValues, prefix = "Day 5 : ")
    val durationDay5 = measureTimeMillis {
        day05.printResultPartOne()
        day05.printResultPartTwo()
    }

    println("Duration Day 5 : $durationDay5 ms")
    println("=".repeat(100))

    val day06InputData = Day06.inputDataFromFile("day06.txt")
    val day06 = Day06(day06InputData, "Day 6 : ")
    val durationDay6 = measureTimeMillis {
        day06.printResultPartOne()
        day06.printResultPartTwo()
    }

    println("Duration Day 6 : $durationDay6 ms")
    println("=".repeat(100))

    val durationDay7 = measureTimeMillis {
        println("Day 7 : Coming soon")
    }
    println("Duration Day 7 : $durationDay7 ms")
    println("=".repeat(100))

    val day08InputData = Day08.inputDataFromFile("day08.txt")
    val day08 = Day08(day08InputData, 25, 6,"Day 8 : ")
    val durationDay8 = measureTimeMillis {
        day08.printResultPartOne()
        day08.printResultPartTwo()
    }

    println("Duration Day 8 : $durationDay8 ms")
    println("=".repeat(100))


    val day09InputData = Day09.inputDataFromFile("day09.txt")
    val day09 = Day09(day09InputData,"Day 9 : ")
    val durationDay9 = measureTimeMillis {
        day09.printResultPartOne()
        day09.printResultPartTwo()
    }

    println("Duration Day 9 : $durationDay9 ms")
    println("=".repeat(100))

    val day10InputData = Day10.inputDataFromFile("day10.txt")
    val day10 = Day10(day10InputData,"Day 10 : ")
    val durationDay10 = measureTimeMillis {
        day10.printResultPartOne()
        day10.printResultPartTwo()
    }
    println("Duration Day 10 : $durationDay10 ms")
    println("=".repeat(100))

    val day11InputData = Day11.inputDataFromFile("day11.txt")
    val day11 = Day11(day11InputData,"Day 11 : ")
    val durationDay11 = measureTimeMillis {
        day11.printResultPartOne()
        day11.printResultPartTwo()
    }

    println("Duration Day 11 : $durationDay11 ms")
    println("=".repeat(100))

    val day12InputData = listOf(
            Moon("Io", Coordinates(12, 0, -15)),
            Moon("Europa", Coordinates(-8, -5, -10)),
            Moon("Ganymede", Coordinates(7, -17, 1)),
            Moon("Callisto", Coordinates(2, -11, -6))
    )
    val day12 = Day12(day12InputData,"Day 12 : ")
    val durationDay12 = measureTimeMillis {
        day12.printResultPartOne()
        day12.printResultPartTwo()
    }

    println("Duration Day 12 : $durationDay12 ms")
    println("=".repeat(100))

    val day13InputData = Day13.inputDataFromFile("day13.txt")
    val day13 = Day13(day13InputData, false, "Day 13 : ")
    val durationDay13 = measureTimeMillis {
        day13.printResults()
    }

    println("Duration Day 13 : $durationDay13 ms")
    println("=".repeat(100))
}

