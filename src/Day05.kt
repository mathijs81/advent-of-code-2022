import java.lang.Long.min
import kotlin.math.max

private const val EXPECTED_1 = 35L
private const val EXPECTED_2 = 46L

private class Day05(isTest: Boolean) : Solver(isTest) {
    val transitions = listOf("seed-to-soil", "soil-to-fertilizer", "fertilizer-to-water", "water-to-light", "light-to-temperature", "temperature-to-humidity", "humidity-to-location")
    val seeds = mutableListOf<Long>()
    var mappings = mutableMapOf<String, MutableList<Triple<Long, Long, Long>>>()

    fun parse() {
        var mapName = ""

        for (line in readAsLines()) {
            if (line.startsWith("seeds:")) {
                seeds.addAll(line.splitLongs())
            } else {
                if (line.contains("map:")) {
                    mapName = line.substringBefore(" ")
                }

                if (line.any { it.isDigit() }) {
                    val parts = line.split(" ")
                    val map = mappings.getOrPut(mapName) { mutableListOf() }
                    map.add(Triple(parts[0].toLong(), parts[1].toLong(), parts[2].toLong()))
                }
            }
        }
    }

    fun part1(): Any {
        parse()

        var lowest = Long.MAX_VALUE
        for (seed in seeds) {
            var number = seed
            for (transition in transitions) {
                var newNumber = number
                val map = mappings[transition] ?: error("No mapping for $transition")
                for (t in map) {
                    if (number >= t.second && number < t.second + t.third) {
                        newNumber = t.first + (number - t.second)
                    }
                }
                number = newNumber
            }
            lowest = min(lowest, number)
        }
        return lowest
    }


    fun part2(): Any {
        parse()

        var lowest = Long.MAX_VALUE
        for ((seedStart, seedLen) in seeds.chunked(2)) {
            var seed = seedStart
            while (seed < seedStart + seedLen) {
                var maxProceed = Long.MAX_VALUE
                var number = seed
                for (transition in transitions) {
                    var newNumber = number
                    val map = mappings[transition] ?: error("No mapping for $transition")
                    for (t in map) {
                        if (number >= t.second && number < t.second + t.third) {
                            newNumber = t.first + (number - t.second)
                            maxProceed = min(maxProceed, t.second + t.third - number)
                        } else {
                            if (t.second > number) {
                                maxProceed = min(maxProceed, t.second - number)
                            }
                        }
                    }
                    number = newNumber
                }
                lowest = min(lowest, number)
                //println("$seed $maxProceed")
                seed += max(1, maxProceed)
            }
        }
        return lowest
    }
}


fun main() {
    val testInstance = Day05(true)
    val instance = Day05(false)

    testInstance.part1().let { check(it == EXPECTED_1) { "part1: $it != $EXPECTED_1" } }
    println("part1 ANSWER: ${instance.part1()}")
    testInstance.part2().let {
        check(it == EXPECTED_2) { "part2: $it != $EXPECTED_2" }
        println("part2 ANSWER: ${instance.part2()}")
    }
}
