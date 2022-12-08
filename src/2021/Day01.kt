package aoc2021

import Solver

private const val EXPECTED_1 = 7
private const val EXPECTED_2 = 5

private class Day01(isTest: Boolean) : Solver(isTest, "/2021") {
    fun part1() = readAsLines().map { it.toInt() }.windowed(2).map { if (it[1] > it[0]) 1 else 0 }.sum()

    fun part2(): Any {
        return readAsLines().map { it.toInt() }.windowed(3).map { it.sum() }.windowed(2)
                .map {
                    if (it[1] > it[0]) 1 else 0
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
