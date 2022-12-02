private const val EXPECTED_1 = 15
private const val EXPECTED_2 = 12

private val SCORES = listOf(
        3, 0, 6,
        6, 3, 0,
        0, 6, 3,
)
private class Day02(isTest: Boolean) : Solver(isTest) {
    fun part1(): Any {
        var sum = 0
        for (line in readAsLines()) {
            val player2 = line[0] - 'A'
            val player1 = line[2] - 'X'
            sum += player1 + 1 + SCORES[player1 * 3 + player2]
        }
        return sum
    }

    fun part2(): Any {
        var sum = 0
        for (line in readAsLines()) {
            val player2 = line[0] - 'A'
            val outcome = line[2] - 'X'
            val intendedScore = outcome * 3
            var playSymbol = 0
            var index = player2
            while (SCORES[index] != intendedScore) {
                index += 3
                playSymbol ++
            }
            sum += playSymbol + 1 + intendedScore
        }
        return sum
    }
}


fun main() {
    val testInstance = Day02(true)
    val instance = Day02(false)

    testInstance.part1().let { check(it == EXPECTED_1) { "part1: $it != $EXPECTED_1" } }
    println(instance.part1())
    testInstance.part2().let { check(it == EXPECTED_2) { "part2: $it != $EXPECTED_1" } }
    println(instance.part2())
}
