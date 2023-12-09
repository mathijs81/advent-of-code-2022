private const val EXPECTED_1 = 114
private const val EXPECTED_2 = 2

private class Day09(isTest: Boolean) : Solver(isTest) {
    fun part1() = readAsLines().sumOf { line ->
        val numbers = line.splitInts()
        val lineLists = mutableListOf(numbers)
        while (true) {
            val newLine = lineLists.last().windowed(2).map { it[1] - it[0] }
            lineLists.add(newLine)
            if (newLine.all { it == 0 }) {
                break
            }
        }
        var value = 0
        for (i in lineLists.indices.reversed()) {
            value = value + lineLists[i].last()
        }
        value
    }

    fun part2() = readAsLines().sumOf { line ->
        val numbers = line.splitInts()
        val lineLists = mutableListOf(numbers)
        while (true) {
            val newLine = lineLists.last().windowed(2).map { it[1] - it[0] }
            lineLists.add(newLine)
            if (newLine.all { it == 0 }) {
                break
            }
        }
        var value = 0
        for (i in lineLists.indices.reversed()) {
            value = lineLists[i].first() - value
        }
        value
    }

}


fun main() {
    val testInstance = Day09(true)
    val instance = Day09(false)

    testInstance.part1().let { check(it == EXPECTED_1) { "part1: $it != $EXPECTED_1" } }
    println("part1 ANSWER: ${instance.part1()}")
    testInstance.part2().let {
        check(it == EXPECTED_2) { "part2: $it != $EXPECTED_2" }
        println("part2 ANSWER: ${instance.part2()}")
    }
}
