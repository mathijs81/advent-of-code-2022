private const val EXPECTED_1 = 7
private const val EXPECTED_2 = 19

private class Day06(isTest: Boolean) : Solver(isTest) {
    fun firstUniqueSubstring(data: String, len: Int): Int {
        val lastSeen = IntArray(26) { -100 }
        var candidateStart = 0
        data.forEachIndexed { index, char ->
            val code = char - 'a'
            candidateStart = candidateStart.coerceAtLeast(lastSeen[code] + 1)
            lastSeen[code] = index
            if (index - candidateStart + 1 >= len) {
                return index + 1
            }
        }
        return -1
    }

    fun part1() = firstUniqueSubstring(readAsString(), 4)
    fun part2() = firstUniqueSubstring(readAsString(), 14)
}


fun main() {
    val testInstance = Day06(true)
    val instance = Day06(false)

    testInstance.part1().let { check(it == EXPECTED_1) { "part1: $it != $EXPECTED_1" } }
    println(instance.part1())
    testInstance.part2().let { check(it == EXPECTED_2) { "part2: $it != $EXPECTED_2" } }
    println(instance.part2())
}
