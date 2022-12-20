private const val EXPECTED_1 = 3L
private const val EXPECTED_2 = 1623178306L

/**
 * Looks like a simple problem once it's done, but I lost a lot of time before I realized that
 * the modulo should be N-1 instead of N and (for part 2) that there are duplicate numbers so
 * you need to store the original index in addition to the number itself.
 */
private class Day20(isTest: Boolean) : Solver(isTest) {
    fun part1() = decrypt(1, 1)
    fun part2() = decrypt(10, 811589153L)

    fun decrypt(rounds: Int, multiplier: Long): Any {
        val list = readAsLines().map { it.toInt() * multiplier }.withIndex().toMutableList()
        repeat(rounds) {
            for (originalIndex in list.indices) {
                val i = list.indexOfFirst { it.index == originalIndex }
                val newIndex = (i + list[i].value).mod(list.size - 1)
                list.add(newIndex, list.removeAt(i))
            }
        }
        val index = list.withIndex().single { it.value.value == 0L }.index
        return listOf(1000, 2000, 3000).sumOf { list[(index + it) % list.size].value }
    }
}


fun main() {
    val testInstance = Day20(true)
    val instance = Day20(false)

    testInstance.part1().let { check(it == EXPECTED_1) { "part1: $it != $EXPECTED_1" } }
    println("part1 ANSWER: ${instance.part1()}")
    testInstance.part2().let {
        check(it == EXPECTED_2) { "part2: $it != $EXPECTED_2" }
        println("part2 ANSWER: ${instance.part2()}")
    }
}
