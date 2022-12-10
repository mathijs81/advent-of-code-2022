private const val EXPECTED_1 = 13140
private const val EXPECTED_2 = 0

private class Day10(isTest: Boolean) : Solver(isTest) {
    fun generateX(): List<Pair<Int, Int>> {
        var cycleNo = 1
        var x = 1
        return readAsLines().flatMap {
            val parts = it.split(" ")
            when (parts[0]) {
                "noop" -> {
                    listOf(cycleNo++ to x)
                }

                "addx" -> {
                    try {
                        listOf(cycleNo to x, cycleNo + 1 to x)
                    } finally {
                        cycleNo += 2
                        x += parts[1].toInt()
                    }
                }

                else -> error("")
            }
        }
    }

    fun part1() = generateX().filter { (it.first - 20) % 40 == 0 }.sumOf { it.first * it.second }

    fun part2(): Any {
        generateX().chunked(40).forEach { row ->
            for ((index, pair) in row.withIndex()) {
                if (index in (pair.second - 1..pair.second + 1))
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