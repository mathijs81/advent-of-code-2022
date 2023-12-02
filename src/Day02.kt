import kotlin.math.max

private const val EXPECTED_1 = 8
private const val EXPECTED_2 = 2286

private class Day02(isTest: Boolean) : Solver(isTest) {
    fun part1(): Any {
        var sum = 0
        var id = 1
        for (line in readAsLines()) {
            val parts = line.substringAfter(": ").split(";").map { it.trim() }
            var possible = true

            for (set in parts) {
                set.split(",").map { it.trim() }.forEach {
                    val count = it.substringBefore(" ").toInt()
                    val color = it.substringAfter(" ")
                    when(color) {
                        "red" -> possible = possible && count <= 12
                        "green" -> possible = possible && count <= 13
                        "blue" -> possible = possible && count <= 14
                    }
                }
            }
            if (possible) {
                sum += id
            }
            id++
        }
        return sum
    }

    fun part2(): Any {
        var sum = 0
        var id = 1
        for (line in readAsLines()) {
            val parts = line.substringAfter(": ").split(";").map { it.trim() }
            var minBlue = 0
            var minRed = 0
            var minGreen = 0

            for (set in parts) {
                set.split(",").map { it.trim() }.forEach {
                    val count = it.substringBefore(" ").toInt()
                    val color = it.substringAfter(" ")
                    when(color) {
                        "red" -> minRed = max(minRed, count)
                        "green" -> minGreen = max(minGreen, count)
                        "blue" -> minBlue = max(minBlue, count)
                    }
                }
            }
            println("$minBlue $minRed $minGreen")
            sum += minBlue * minRed * minGreen
            id++
        }
        return sum
    }
}


fun main() {
    val testInstance = Day02(true)
    val instance = Day02(false)

    testInstance.part1().let { check(it == EXPECTED_1) { "part1: $it != $EXPECTED_1" } }
    println("part1 ANSWER: ${instance.part1()}")
    testInstance.part2().let {
        check(it == EXPECTED_2) { "part2: $it != $EXPECTED_2" }
        println("part2 ANSWER: ${instance.part2()}")
    }
}
