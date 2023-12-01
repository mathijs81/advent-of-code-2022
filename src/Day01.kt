private const val EXPECTED_1 = 142
private const val EXPECTED_2 = 281

private class Day01(isTest: Boolean) : Solver(isTest) {
    fun part1(): Any {
        return readAsLines().sumOf { line ->
            val first = line.first { it.isDigit() }
            val last = line.last { it.isDigit() }
            (first - '0') * 10 + (last - '0')
        }
    }

    fun part2(): Any {
        val digits = listOf(
            "one", "two", "three", "four", "five", "six", "seven", "eight", "nine"
        )
        return readAsLines().sumOf { line ->
            fun getDigitAt(index: Int): Int? {
                return if (line[index].isDigit()) {
                    line[index] - '0'
                } else {
                    digits.firstNotNullOfOrNull { digit ->
                        if (line.substring(index).startsWith(digit)) {
                            1 + digits.indexOf(digit)
                        } else {
                            null
                        }
                    }
                }
            }

            val first = line.indices.firstNotNullOf(::getDigitAt)
            val last = line.indices.reversed().firstNotNullOf(::getDigitAt)
            first * 10 + last
        }
    }
}


fun main() {
    val testInstance = Day01(true)
    val instance = Day01(false)

    // Commented out because testdata changed from part 1 to part 2
//    testInstance.part1().let { check(it == EXPECTED_1) { "part1: $it != $EXPECTED_1" } }
//    println("part1 ANSWER: ${instance.part1()}")
    testInstance.part2().let {
        check(it == EXPECTED_2) { "part2: $it != $EXPECTED_2" }
        println("part2 ANSWER: ${instance.part2()}")
    }
}
