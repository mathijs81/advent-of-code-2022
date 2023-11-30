package aoc2019

import Solver

private const val EXPECTED_1 = 656
private const val EXPECTED_2 = 968

private class Day01(isTest: Boolean) : Solver(isTest, "/2019") {
    fun part1(): Any {
        return readAsLines().map { it.toInt() / 3 - 2 }.sum()
    }
    fun part2(): Any {
        return readAsLines().map {
            var current = it.toInt() / 3 - 2
            var sum = 0
            while (current > 0) {
                sum += current
                current = current / 3 - 2
            }
            sum
        }.sum()
    }
}


fun main() {
    val testInstance = Day01(true)
    val instance = Day01(false)

    testInstance.part1().let { check(it == EXPECTED_1) { "part1: $it != $EXPECTED_1" } }
    println(instance.part1())
    testInstance.part2().let { check(it == EXPECTED_2) { "part2: $it != $EXPECTED_2" } }
    println(instance.part2())
}
