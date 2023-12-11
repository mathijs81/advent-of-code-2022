import kotlin.math.max
import kotlin.math.min

private const val EXPECTED_1 = 374L
private const val EXPECTED_2 = 1030L

private class Day11(isTest: Boolean) : Solver(isTest) {
    val field = readAsLines().map { it.toCharArray() }
    val Y = field.size
    val X = field[0].size
    var mul = 2L

    fun part1(): Any {
        val rows = (0..<Y).filter { y -> (0..<X).all { x -> field[y][x] == '.' } }.toSet()
        val columns = (0..<X).filter { x -> (0..<Y).all { y -> field[y][x] == '.' } }.toSet()

        val galaxies = mutableListOf<Pair<Int, Int>>()
        for (y in 0 until Y) {
            for (x in 0 until X) {
                if (field[y][x] == '#') {
                    galaxies.add(Pair(y, x))
                }
            }
        }

        fun dist(a: Int, b: Int): Long {
            val y1 = min(galaxies[a].first, galaxies[b].first)
            val y2 = max(galaxies[a].first, galaxies[b].first)
            val x1 = min(galaxies[a].second, galaxies[b].second)
            val x2 = max(galaxies[a].second, galaxies[b].second)

            val add = (x1..x2).intersect(columns).count() + (y1..y2).intersect(rows).count()

            return (add * (mul - 1) + y2-y1 + x2-x1)
        }

        var sum = 0L
        for (a  in galaxies.indices) {
            for (b in a + 1 until galaxies.size) {
                sum += dist(a, b)
            }
        }
        return sum
    }

    fun part2(): Any {
        return part1()
    }
}


fun main() {
    val testInstance = Day11(true)
    val instance = Day11(false)


    testInstance.part1().let { check(it == EXPECTED_1) { "part1: $it != $EXPECTED_1" } }
    println("part1 ANSWER: ${instance.part1()}")
    testInstance.mul = 10L
    testInstance.part2().let {
        check(it == EXPECTED_2) { "part2: $it != $EXPECTED_2" }
        instance.mul = 1000000L
        println("part2 ANSWER: ${instance.part2()}")
    }
}
