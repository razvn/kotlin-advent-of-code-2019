import java.io.File
import kotlin.system.measureTimeMillis

fun main() {
    val inputData = Day06.inputDataFromFile("day06.txt")
    val service = Day06(inputData)

    val duration = measureTimeMillis {
        service.printResutPartOne()
        service.printResutPartTwo()
    }
    println("Duration: $duration ms")
}

class Day06(data: List<String>, private val prefix: String = "") {

    val com = TreeNode("COM")
    val treeMap: MutableMap<String, TreeNode> = mutableMapOf("COM" to com)

    init {
        data.forEach {
            val (static, orbit) = it.split(')')
            val currentSun = treeMap.getOrDefault(static, TreeNode(static))
            val currentPlanet = treeMap.getOrDefault(orbit, TreeNode(orbit))
            currentSun.addChild(currentPlanet)
            currentPlanet.parent = currentSun
            treeMap[static] = currentSun
            treeMap[orbit] = currentPlanet
        }

        com.depth = 0
    }

    fun partOne(): Int {
        val rep = treeMap.asSequence().sumBy { it.value.depth }
        return rep
    }

    fun partTwo(you: String, san: String): Int {
        val youNode = treeMap[you] ?: throw Exception("Node: $you does not exists")
        val sanNode = treeMap[san] ?: throw Exception("Node: $you does not exists")
        val youNodeParents = getParents(youNode)
        val sanNodeParents = getParents(sanNode)

        val communNode = youNodeParents.intersect(sanNodeParents).maxBy { it.depth } ?: throw Exception("No common node")
        val nb = (youNode.depth - communNode.depth - 1) + (sanNode.depth - communNode.depth - 1)
        return nb
    }

    fun printResutPartOne() {
        println("${prefix}Result part one is: ${partOne()}")
    }

    fun printResutPartTwo() {
        println("${prefix}Result part two is: ${partTwo("YOU", "SAN")}")
    }

    fun getParents(node: TreeNode?): List<TreeNode> {
        if (node == null) return emptyList()
        val parents = mutableListOf<TreeNode>()
        var curremtNode = node.parent
        while(curremtNode != null) {
            parents.add(curremtNode)
            curremtNode = curremtNode.parent
        }
        return parents
    }

    class TreeNode(val value: String, var parent: TreeNode? = null, val children: MutableList<TreeNode> = mutableListOf()) {
        var depth: Int = 0
            set(value) {
                children.forEach { it.depth = value + 1 }
                field = value
            }

        fun addChild(node: TreeNode) {
            children.add(node)
            node.parent = this
        }

        override fun toString(): String {
            return "${value} [depth: $depth - children: ${children.count()}]"
        }
    }

    companion object {

        fun inputDataFromFile(fileName: String) = loadDataFromFile(fileName)

        val fileDir = "src/main/resources/"
        private fun loadDataFromFile(fileName: String): List<String> {
                val inputData: MutableList<String> = mutableListOf()
            File(fileDir+fileName).forEachLine {
                inputData.add(it)
            }
            return inputData.toList()
        }
    }
}




