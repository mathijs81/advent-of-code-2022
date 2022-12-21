import kotlin.math.sign

private const val EXPECTED_1 = 152L
private const val EXPECTED_2 = 301L

private class Day21(isTest: Boolean) : Solver(isTest) {
    fun Map<String, String>.solve(key: String): Long {
        key.toLongOrNull()?.let { return it }
        val parts = (this[key]!! + " XX").split(" ")
        return when (parts[1]) {
            "+" -> Math.addExact(solve(parts[0]), solve(parts[2]))
            "-" -> Math.subtractExact(solve(parts[0]), solve(parts[2]))
            "/" -> solve(parts[0]) / solve(parts[2])
            "*" -> Math.multiplyExact(solve(parts[0]), solve(parts[2]))
            "XX" -> parts[0].toLong()
            else -> error("$key ${this[key]}")
        }
    }

    val operations = readAsLines().associate { line -> line.split(": ").let { it[0] to it[1] } }.toMutableMap()

    fun part1() = operations.solve("root")

    fun part2(): Any {
        val items = operations["root"]!!.split(" ").let { listOf(it[0], it[2]) }

        fun diff(probe: Long): Long {
            operations["humn"] = probe.toString()
            return Math.subtractExact(operations.solve(items[0]), operations.solve(items[1]))
        }
        fun isOverflow(probe: Long) = try {
            diff(probe)
            false
        } catch (e: ArithmeticException) {
            true
        }

        var min = Long.MIN_VALUE / 2
        while (isOverflow(min)) min /= 2
        var max = Long.MAX_VALUE / 2
        while (isOverflow(max)) max /= 2

        val minSign = diff(min).sign
        val maxSign = diff(max).sign
        check(minSign != maxSign)

        while (max > min) {
            val probe = (min + max) / 2
            val value = diff(probe)
            if (value.sign == minSign) {
                min = probe + 1
            } else {
                max = probe
            }
        }
        return min
    }
}


fun main() {
    val testInstance = Day21(true)
    val instance = Day21(false)

    testInstance.part1().let { check(it == EXPECTED_1) { "part1: $it != $EXPECTED_1" } }
    println("part1 ANSWER: ${instance.part1()}")
    testInstance.part2().let {
        check(it == EXPECTED_2) { "part2: $it != $EXPECTED_2" }
        println("part2 ANSWER: ${instance.part2()}")
    }
}
