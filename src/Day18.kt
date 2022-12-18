import kotlin.collections.ArrayDeque

private const val EXPECTED_1 = 64
private const val EXPECTED_2 = 58

private class Day18(isTest: Boolean) : Solver(isTest) {
    val data = readAsLines().map { line ->
        line.split(",").map { it.toInt() }
    }.toSet()
    val dirs = listOf(
            listOf(1, 0, 0),
            listOf(-1, 0, 0),
            listOf(0, 1, 0),
            listOf(0, -1, 0),
            listOf(0, 0, 1),
            listOf(0, 0, -1),
    )

    operator fun List<Int>.plus(dir: List<Int>) = listOf(this[0] + dir[0], this[1] + dir[1], this[2] + dir[2])
    val bounds = (0..2).map { coord -> data.minOf { it[coord] } to data.maxOf { it[coord] } }

    fun part1() = data.sumOf { point -> dirs.count { dir -> point + dir !in data } }

    fun isInside(point: List<Int>, result: MutableMap<List<Int>, Boolean>, seen: MutableSet<List<Int>>): Boolean {
        if(point in data || point in seen) return true
        if((point zip bounds).any { (p, b) -> p !in b.first..b.second }) return false
        result[point]?.let { return it }
        seen.add(point)
        return if (dirs.all { isInside(point + it, result, seen) }) {
            seen.forEach { result[it] = true }
            true
        } else {
            seen.forEach { result[it] = false }
            false
        }
    }

    val insidePoints = mutableMapOf<List<Int>, Boolean>()
    fun part2() = data.sumOf { point ->
        dirs.count { dir -> !isInside(point + dir, insidePoints, mutableSetOf()) }
    }
}


fun main() {
    val testInstance = Day18(true)
    val instance = Day18(false)

    testInstance.part1().let { check(it == EXPECTED_1) { "part1: $it != $EXPECTED_1" } }
    println("test part1 correct")
    println("part1 ANSWER: ${instance.part1()}")
    testInstance.part2().let {
        check(it == EXPECTED_2) { "part2: $it != $EXPECTED_2" }
        println("test part2 correct")
        println("part2 ANSWER: ${instance.part2()}")
    }
}
