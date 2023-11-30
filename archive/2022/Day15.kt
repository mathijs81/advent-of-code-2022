import kotlin.Int.Companion.MAX_VALUE
import kotlin.math.absoluteValue

private const val EXPECTED_1 = 26
private const val EXPECTED_2 = 56000011L

private class Day15(isTest: Boolean) : Solver(isTest) {
    val data = readAsLines().map { line ->
        val (sensorStr, closestStr) = line.split("beacon")
        fun parsePoint(str: String) = str.split(",").let { (xStr, yStr) -> xStr.filter(Char::isDigit).toInt() to yStr.filter(Char::isDigit).toInt() }
        parsePoint(sensorStr) to parsePoint(closestStr)
    }

    fun ranges(y: Int) = buildList {
        data.forEach { (sensor, closest) ->
            val xWidth = (sensor.first - closest.first).absoluteValue + (sensor.second - closest.second).absoluteValue - (sensor.second - y).absoluteValue
            if (xWidth >= 0) {
                add(sensor.first - xWidth..sensor.first + xWidth)
            }
        }
    }.sortedBy { it.first }

    fun part1(): Any {
        val y = if (isTest) 10 else 2000000
        val ranges = ranges(y)
        val xSet = data.mapNotNull { (_, beacon) -> if (beacon.second == y) beacon.first else null }.toSet()

        var result = 0
        (ranges + listOf(MAX_VALUE..MAX_VALUE)).reduce { currentRange, next ->
            if (next.first > currentRange.last) {
                result += currentRange.last - currentRange.first + 1
                next
            } else {
                currentRange.first..maxOf(next.last, currentRange.last)
            }
        }
        return result - xSet.size
    }

    fun part2(): Any {
        val size = if (isTest) 20 else 4000000
        val beaconSet = data.map { (_, beacon) -> beacon }.toSet()

        for (y in 0 until size) {
            (ranges(y) + listOf(MAX_VALUE..MAX_VALUE)).reduce { currentRange, next ->
                if (next.first > currentRange.last + 1 && currentRange.last + 1 in 0 until size && currentRange.last + 1 to y !in beaconSet) {
                    return y + (currentRange.last + 1) * 4000000L
                }
                currentRange.first..maxOf(next.last, currentRange.last)
            }
        }
        error("unexpected")
    }
}

fun main() {
    val testInstance = Day15(true)
    val instance = Day15(false)

    testInstance.part1().let { check(it == EXPECTED_1) { "part1: $it != $EXPECTED_1" } }
    println("part1 ANSWER: ${instance.part1()}")
    testInstance.part2().let {
        check(it == EXPECTED_2) { "part2: $it != $EXPECTED_2" }
        println("part2 ANSWER: ${instance.part2()}")
    }
}
