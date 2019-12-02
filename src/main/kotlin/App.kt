class App {
    val greeting: String
        get() {
            return "Welcome to Advent of Code 2019"
        }
}

fun main() {
    println(App().greeting)
    println("~".repeat(App().greeting.length))

    val day01 = Day01()
    val day01InputData = listOf<Long>(
            62259,75368,93740,119724,112546,137714,96999,130673,102398,73819,100734,85337,62764,82115,127696,54391,
            103213,77954,112513,112392,138404,92989,108521,83163,109720,91918,114443,54306,90623,66833,58505,85919,
            77539,149419,128385,66452,94677,109179,62072,137245,136226,145783,60689,103320,145931,101286,63458,122468,
            87858,105675,146185,57417,96883,70739,97494,140951,149416,83137,66122,134319,58511,139600,102929,112240,
            149634,64142,83332,129526,99058,148889,50087,74961,133606,143518,68849,97045,73920,61357,115941,56740,
            111773,77880,90792,77103,111355,125898,56547,84918,113822,74113,98557,80928,60519,146379,59354,102490,
            72584,59000,63151,114253
    )

    val day01ResultPartOne = day01.getPartOneFuel(day01InputData)
    println("Day 1 : Result part one is: $day01ResultPartOne")

    val day01ResultPartTwo = day01.getPartTwoFuel(day01InputData)
    println("Day 2 : Result part two is: $day01ResultPartTwo")

    println("=".repeat(200))

    val day02 = Day02()

    val day02InputData = listOf(
            1, 0, 0, 3,
            1, 1, 2, 3,
            1, 3, 4, 3,
            1, 5, 0, 3,
            2, 1, 10, 19,
            1, 19, 5, 23,
            1, 6, 23, 27,
            1, 27, 5, 31,
            2, 31, 10, 35,
            2, 35, 6, 39,
            1, 39, 5, 43,
            2, 43, 9, 47,
            1, 47, 6, 51,
            1, 13, 51, 55,
            2, 9, 55, 59,
            1, 59, 13, 63,
            1, 6, 63, 67,
            2, 67, 10, 71,
            1, 9, 71, 75,
            2, 75, 6, 79,
            1, 79, 5, 83,
            1, 83, 5, 87,
            2, 9, 87, 91,
            2, 9, 91, 95,
            1, 95, 10, 99,
            1, 9, 99, 103,
            2, 103, 6, 107,
            2, 9, 107, 111,
            1, 111, 5, 115,
            2, 6, 115, 119,
            1, 5, 119, 123,
            1, 123, 2, 127,
            1, 127, 9, 0,
            99, 2, 0, 14, 0
    )

    val day02OriginalProgram = day02InputData.toMutableList()
    day02OriginalProgram[1] = 12
    day02OriginalProgram[2] = 2

    val resultDay2PartOne = day02.analyzeCommand(day02OriginalProgram)

    println()
    println("Day 2 : Result part one is: $resultDay2PartOne")

    val (noun, verbe) = day02.findNounAndVerbeForOutput(19690720, day02InputData)
    println("Day 2 : Result part two is: ${100 * noun + verbe}")
}