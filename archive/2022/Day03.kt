package aoc2022

private const val EXPECTED_1 = 157
private const val EXPECTED_2 = 70

private fun Set<Char>.score(): Int {
    check(size == 1)
    val value = first()
    return if (value in 'a'..'z')
        value - 'a' + 1
    else
        value - 'A' + 27
}

private class Day03(isTest: Boolean) : Solver(isTest) {
    fun part1(): Any {
        return readAsLines().sumOf { line ->
            val left = line.subSequence(0, line.length / 2).toSet()
            val right = line.subSequence(line.length / 2, line.length).toSet()

            left.intersect(right).score()
        }
    }

    fun part2(): Any {
        return readAsLines().chunked(3).sumOf { group ->
            check(group.size == 3)
            val sets = group.map { it.toSet() }

            sets[0].intersect(sets[1]).intersect(sets[2]).score()
        }
    }
}


fun main() {
    val testInstance = Day03(true)
    val instance = Day03(false)

    testInstance.part1().let { check(it == EXPECTED_1) { "part1: $it != $EXPECTED_1" } }
    println(instance.part1())
    testInstance.part2().let { check(it == EXPECTED_2) { "part2: $it != $EXPECTED_2" } }
    println(instance.part2())
}
