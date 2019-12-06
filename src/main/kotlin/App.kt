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
        day01.printResutPartOne()
        day01.printResutPartTwo()
    }
    println("Duration Day 1 : $durationDay1 ms")
    println("=".repeat(100))

    val day02InputData = Day02.inputDataFromFile("day02.txt")

    val day02 = Day02(day02InputData, "Day 2 : ")
    val durationDay2 = measureTimeMillis {
        day02.printResutPartOne()
        day02.printResutPartTwo()
    }
    println("Duration Day 2 : $durationDay2 ms")
    println("=".repeat(100))

    val day03InputData = Day03.inputDataFromFile("day03.txt")

    val day03 = Day03(day03InputData, "Day 3 : ")
    val durationDay3 = measureTimeMillis {
        day03.printResutPartOne()
        day03.printResutPartTwo()
    }
    println("Duration Day 3 : $durationDay3 ms")
    println("=".repeat(100))

    val day04InputData = 152085 to 670283
    val day04 = Day04(day04InputData, "Day 4 : ")
    val durationDay4 = measureTimeMillis {
        day04.printResutPartOne()
        day04.printResutPartTwo()
    }

    println("Duration Day 4 : $durationDay4 ms")
    println("=".repeat(100))

    val day05InputData = Day05.inputDataFromFile("day05.txt")
    val day05InputValues = listOf(1, 5)
    val day05 = Day05(data = day05InputData, input = day05InputValues, prefix = "Day 5 : ")
    val durationDay5 = measureTimeMillis {
        day05.printResutPartOne()
        day05.printResutPartTwo()
    }

    println("Duration Day 5 : $durationDay5 ms")
    println("=".repeat(100))

    val day06InputData = Day06.inputDataFromFile("day06.txt")
    val day06 = Day06(day06InputData, "Day 6 : ")
    val durationDay6 = measureTimeMillis {
        day06.printResutPartOne()
        day06.printResutPartTwo()
    }

    println("Duration Day 6 : $durationDay6 ms")
    println("=".repeat(100))
}

