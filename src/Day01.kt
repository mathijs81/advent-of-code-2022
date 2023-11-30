private const val EXPECTED_1 = 0
private const val EXPECTED_2 = 0

private class Day01(isTest: Boolean) : Solver(isTest) {
    fun part1(): Any {
        return 0
    }

    fun part2(): Any {
        return 0
    }
}


fun main() {
    val testInstance = Day01(true)
    val instance = Day01(false)

    testInstance.part1().let { check(it == EXPECTED_1) { "part1: $it != $EXPECTED_1" } }
    println("part1 ANSWER: ${instance.part1()}")
    testInstance.part2().let {
        check(it == EXPECTED_2) { "part2: $it != $EXPECTED_2" }
        println("part2 ANSWER: ${instance.part2()}")
    }
}
