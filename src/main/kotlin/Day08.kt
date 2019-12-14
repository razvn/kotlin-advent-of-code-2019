import java.io.File
import kotlin.system.measureTimeMillis

fun main() {
    val inputData = Day08.inputDataFromFile("day08.txt")
    val service = Day08(inputData, 25, 6)

    val duration = measureTimeMillis {
        service.printResultPartOne()
        service.printResultPartTwo()
    }
    println("Duration: $duration ms")
}

class Day08(private val data: String, private val wide: Int, private val tall: Int, private val prefix: String = "") {

    fun partOne(data: String): Int {
        val layers: Map<String, Int> = data.chunked(wide * tall).map {
            it to it.chunked(1).filter { ch -> ch == "0" }.count()
        }.toMap()

        // layers.forEach { println(it.key + " : " + it.key.length + " : " + it.value) }
        val layerFewer0 = layers.minBy { it.value }
        // println(layerFewer0)
        val listAsInt = layerFewer0?.key?.chunked(1)?.map { it.toInt() } ?: emptyList()
        val nb1 = listAsInt.filter { it == 1 }.count()
        val nb2 = listAsInt.filter { it == 2 }.count()
        return nb1 * nb2
    }

    fun partTwo(data: String): String {
        val layers: List<List<Int>> = data.chunked(wide * tall).map {
            it.chunked(1).map { ch -> ch.toInt() }
        }

        val result = layers.first().mapIndexed { idx, _ ->
            calcPixel(idx, layers)
        }
        val res = result.joinToString("").chunked(wide)
        // res.forEach { println(it.replace('0', ' ')) }
        return "\n" + res.joinToString("\n").replace('0', ' ').replace('1', '■')
    }

    fun calcPixel(idx: Int, layers: List<List<Int>>): Int {
        var resp = 2
        layers.reversed().forEach { list ->
            val listValue = list[idx]
            if (listValue != 2) resp = listValue
            // get out
            if (resp == 0) return@forEach
        }
        return resp
    }

    fun printResultPartOne() {
        println("${prefix}Result part one is: ${partOne(data)}")
    }

    fun printResultPartTwo() {
        println("${prefix}Result part two is: ${partTwo(data)}")
    }

    companion object {
        val fileDir = "src/main/resources/"

        fun inputDataFromFile(fileName: String): String = loadDataFromFile(fileName).first()

        fun loadDataFromFile(fileName: String): List<String> {
            val inputData: MutableList<String> = mutableListOf()
            File(fileDir + fileName).forEachLine {
                inputData.add(it)
            }
            return inputData
        }
    }
}


