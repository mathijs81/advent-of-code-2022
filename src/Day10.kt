private const val EXPECTED_1 = 13140
private const val EXPECTED_2 = 0

private class Day10(isTest: Boolean) : Solver(isTest) {
    fun generateX() = readAsLines().flatMap {
        val parts = it.split(" ")
        when (parts[0]) {
            "noop" -> listOf(0)
            "addx" -> listOf(0, parts[1].toInt())
            else -> error("")
        }
    }.runningFold(1, Int::plus)

    fun part1() = (listOf(0) + generateX()).withIndex().filter { (index, _) -> (index - 20) % 40 == 0 }.sumOf { (index, value) -> (index * value) }

    fun part2(): Any {
        generateX().chunked(40).forEach { row ->
            for ((index, x) in row.withIndex()) {
                if (index in x - 1..x + 1)
                    print('#')
                else print(" ")
            }
            println()
        }
        println()
        return 0
    }
}

fun main() {
    val testInstance = Day10(true)
    val instance = Day10(false)

    testInstance.part1().let { check(it == EXPECTED_1) { "part1: $it != $EXPECTED_1" } }
    println("part1 ANSWER: ${instance.part1()}")
    testInstance.part2().let {
        check(it == EXPECTED_2) { "part2: $it != $EXPECTED_2" }
        println("part2 ANSWER: ${instance.part2()}")
    }
}