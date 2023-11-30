package aoc2020

import Solver

private const val EXPECTED_1 = 514579
private const val EXPECTED_2 = 241861950

private class Day01(isTest: Boolean) : Solver(isTest, "/2020") {
    fun part1(): Any {
        val seen = mutableSetOf<Int>()
        readAsLines().forEach {
            val value = it.toInt()

            val remainder = 2020 - value
            if (remainder in seen) {
                return remainder * value
            }

            seen.add(value)
        }
        check(false)
        return 0
    }
    fun part2(): Any {
        val list = readAsLines().map { it.toInt() }.sorted()
        val numberSet = mutableSetOf<Int>()
        for (i in 0 until list.size) {
            for (j in i+1 until list.size) {
                val remainder = 2020 - list[i] - list[j]
                if (remainder in numberSet) {
                    return remainder * list[i] * list[j]
                }
            }
            numberSet.add(list[i])
        }

        check(false)
        return 0
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
