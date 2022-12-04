private const val EXPECTED_1 = 2
private const val EXPECTED_2 = 4

private class Day04(isTest: Boolean) : Solver(isTest) {
    fun part1(): Any {
        return readAsLines().sumInt {
            val parts = it.split(",")
            var elf1 = parts[0].split("-").map { it.toInt() }
            var elf2 = parts[1].split("-").map { it.toInt() }
            if (elf1[0] > elf2[0] || (elf1[0] == elf2[0] && elf1[1] < elf2[1])) {
                elf1 = elf2.also { elf2 = elf1 }
            }
            if (elf2[1] <= elf1[1]) 1 else 0
        }
    }

    fun part2(): Any {
        return readAsLines().sumInt {
            val parts = it.split(",")
            val elf1 = parts[0].split("-").map { it.toInt() }
            val elf2 = parts[1].split("-").map { it.toInt() }
            val maxBegin = elf1[0].coerceAtLeast(elf2[0])
            val minEnd = elf1[1].coerceAtMost(elf2[1])

            if (maxBegin <= minEnd) 1 else 0
        }
    }
}


fun main() {
    val testInstance = Day04(true)
    val instance = Day04(false)

    testInstance.part1().let { check(it == EXPECTED_1) { "part1: $it != $EXPECTED_1" } }
    println(instance.part1())
    testInstance.part2().let { check(it == EXPECTED_2) { "part2: $it != $EXPECTED_2" } }
    println(instance.part2())
}
