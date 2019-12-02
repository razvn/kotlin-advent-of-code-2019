import kotlin.math.floor
import kotlin.math.max

fun main() {
    val service = Day01()
    val inputData = listOf<Long>(
            62259,75368,93740,119724,112546,137714,96999,130673,102398,73819,100734,85337,62764,82115,127696,54391,
            103213,77954,112513,112392,138404,92989,108521,83163,109720,91918,114443,54306,90623,66833,58505,85919,
            77539,149419,128385,66452,94677,109179,62072,137245,136226,145783,60689,103320,145931,101286,63458,122468,
            87858,105675,146185,57417,96883,70739,97494,140951,149416,83137,66122,134319,58511,139600,102929,112240,
            149634,64142,83332,129526,99058,148889,50087,74961,133606,143518,68849,97045,73920,61357,115941,56740,
            111773,77880,90792,77103,111355,125898,56547,84918,113822,74113,98557,80928,60519,146379,59354,102490,
            72584,59000,63151,114253
    )

    val resultPartOne = service.getPartOneFuel(inputData)
    println("Result part one is: $resultPartOne")

    val resultPartTwo = service.getPartTwoFuel(inputData)
    println("Result part two is: $resultPartTwo")
}

class Day01 {
    fun getFuel(mass: Long) = max((floor(mass.toDouble() / 3) - 2), 0.0).toLong()

    tailrec fun getTotalModuleFuel(mass: Long, accum: Long = 0): Long {
        val newMass = getFuel(mass)
        val currentMass = accum + newMass
        return if (newMass == 0L) currentMass else getTotalModuleFuel(newMass, currentMass)
    }

    fun getPartOneFuel(masses: List<Long>) = masses.map { getFuel(it) }.sum()

    fun getPartTwoFuel(masses: List<Long>) = masses
            .map { getTotalModuleFuel(it) }
            .sum()
}


