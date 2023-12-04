private const val EXPECTED_1 = 13
private const val EXPECTED_2 = 30L

private class Day04(isTest: Boolean) : Solver(isTest) {
    fun part1(): Any {
        var sum = 0
        for (line in readAsLines()) {
            val parts = line.split(" | ")
            val winning = parts[0].substringAfter(": ").splitInts().toSet()
            val myNumbers = parts[1].splitInts().toSet()
            val winCount = winning.intersect(myNumbers).count()
            if (winCount > 0) {
                sum += 1 shl (winCount - 1)
            }
        }
        return sum
    }

    fun part2(): Any {
        val lines = readAsLines()
        val wins = LongArray(lines.size) { 1 }
        for ((index, line) in lines.withIndex()) {
            val current = wins[index]
            val parts = line.split(" | ")
            val winning = parts[0].substringAfter(": ").splitInts().toSet()
            val myNumbers = parts[1].splitInts().toSet()
            val winCount = winning.intersect(myNumbers).count()
            for (i in 0 until winCount) {
                wins[index + i + 1] += current
            }
        }

        return wins.sum()
    }
}


fun main() {
    val testInstance = Day04(true)
    val instance = Day04(false)

    testInstance.part1().let { check(it == EXPECTED_1) { "part1: $it != $EXPECTED_1" } }
    println("part1 ANSWER: ${instance.part1()}")
    testInstance.part2().let {
        check(it == EXPECTED_2) { "part2: $it != $EXPECTED_2" }
        println("part2 ANSWER: ${instance.part2()}")
    }
}
