import kotlin.system.measureTimeMillis

fun main() {
    val inputData = 152085 to 670283
    val service = Day04(inputData)
    val duration = measureTimeMillis {
        service.printResultPartOne()
        service.printResultPartTwo()
    }
    println("Duration: $duration ms")
}

class Day04(private val data: Pair<Int, Int>, private val prefix: String = "") {

    fun partOne(data: Pair<Int, Int>): Int {
        val (min, max) = data

        val possibilites = possibilites(min, max, false)
        // println(possibilites)
        return possibilites.size
    }

    fun partTwo(data: Pair<Int, Int>): Int {
        val (min, max) = data

        val possibilites = possibilites(min, max, true)
        // println(possibilites)
        return possibilites.size
    }

    fun printResultPartOne() {
        println("${prefix}Result part one is: ${partOne(data)}")
    }

    fun printResultPartTwo() {
        println("${prefix}Result part two is: ${partTwo(data)}")
    }

    fun possibilites(min: Int, max: Int, largerGroup: Boolean = false): List<Int> {
        return (min..max).filter {
            validNumber(it, largerGroup)
        }
    }

    fun validNumber(number: Int, largerGroupe: Boolean = false): Boolean {
        val tab = intToListInt(number)
        var prev: Int = tab.first()
        tab.forEach {
            if (prev > it) return false
            else prev = it
        }
        return checkRepeatableValue(tab, largerGroupe)
    }

    fun intToListInt(i: Int): List<Int> {
        val array = mutableListOf<Int>()
        var nb = i
        while (nb > 0) {
            val n = nb % 10
            array.add(n)
            nb /= 10
        }

        return array.reversed()
    }

    fun checkRepeatableValue(tab: List<Int>, largerGroup: Boolean): Boolean {
        val mapValues = mutableMapOf<Int, Int>()
        var prev: Int? = null
        tab.forEach {
            if (prev == null) prev = it
            else {
                if (prev == it) {
                    val count = mapValues.getOrDefault(it, 1)
                    mapValues[it] = count + 1
                } else {
                    prev = it
                }
            }
        }

        return if (largerGroup) mapValues.containsValue(2) else mapValues.isNotEmpty()
    }
}


