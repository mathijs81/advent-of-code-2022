import kotlin.math.absoluteValue
import kotlin.math.max
import kotlin.math.sign

private const val EXPECTED_1 = 13
private const val EXPECTED_2 = 1

private fun Pair<Int, Int>.update(other: Pair<Int, Int>): Pair<Int, Int> {
    val d = other.first - first to other.second - second
    if (max(d.first.absoluteValue, d.second.absoluteValue) <= 1) {
        return this
    }
    return (this.first + d.first.sign to this.second + d.second.sign)
}

private operator fun Pair<Int, Int>.plus(other: Pair<Int, Int>) = first + other.first to second + other.second

private class Day09(isTest: Boolean) : Solver(isTest) {
    val dMap = mapOf(
            'R' to (1 to 0),
            'L' to (-1 to 0),
            'U' to (0 to 1),
            'D' to (0 to -1)
    )

    fun part1(): Any {
        var pos = 0 to 0
        var tailPos = 0 to 0
        val tailSet = mutableSetOf(tailPos)
        for (line in readAsLines()) {
            repeat(line.substring(2).toInt()) {
                pos += dMap[line[0]]!!
                tailPos = tailPos.update(pos)
                tailSet.add(tailPos)
            }
        }
        return tailSet.size
    }

    fun part2(): Any {
        val pos = (0..9).map { 0 to 0 }.toMutableList()
        val tailSet = mutableSetOf(pos[9])
        for (line in readAsLines()) {
            repeat(line.substring(2).toInt()) {
                pos[0] += dMap[line[0]]!!
                for (i in 1..9) {
                    pos[i] = pos[i].update(pos[i - 1])
                }
                tailSet.add(pos[9])
            }
        }
        return tailSet.size
    }
}


fun main() {
    val testInstance = Day09(true)
    val instance = Day09(false)

    testInstance.part1().let { check(it == EXPECTED_1) { "part1: $it != $EXPECTED_1" } }
    println(instance.part1())
    testInstance.part2().let { check(it == EXPECTED_2) { "part2: $it != $EXPECTED_2" } }
    println(instance.part2())
}
