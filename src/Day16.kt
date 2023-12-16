import kotlin.collections.ArrayDeque
import kotlin.math.max

private const val EXPECTED_1 = 46
private const val EXPECTED_2 = 51

private class Day16(isTest: Boolean) : Solver(isTest) {
    data class Point(val y: Int, val x: Int, val dir: Int)

    val dirs = listOf(0 to -1, 1 to 0, 0 to 1, -1 to 0)
    val field = readAsLines().map { it.toCharArray() }
    val Y = field.size
    val X = field[0].size

    fun energized(startPoint: Point): Int {
        val seen = mutableSetOf<Point>()
        var points = ArrayDeque<Point>()
        points.add(startPoint)

        fun newDir(ch: Char, dir: Int): List<Int> {
            return when (ch) {
                '.' -> listOf(dir)
                '|' -> if (dir == 1 || dir == 3) {
                    listOf(0, 2)
                } else {
                    listOf(dir)
                }
                '-' -> if (dir == 0 || dir == 2) {
                    listOf(1, 3)
                } else {
                    listOf(dir)
                }
                '/' -> listOf(
                    when (dir) {
                        0 -> 1
                        1 -> 0
                        2 -> 3
                        3 -> 2
                        else -> error("wrong dir: $dir")
                    }
                )
                '\\' -> listOf(
                    when (dir) {
                        0 -> 3
                        1 -> 2
                        2 -> 1
                        3 -> 0
                        else -> error("wrong dir: $dir")
                    }
                )
                else -> error("wrong ch: $ch")
            }
        }

        while (points.isNotEmpty()) {
            val p = points.removeFirst()
            val nx = p.x + dirs[p.dir].first
            val ny = p.y + dirs[p.dir].second
            if (nx in 0..<X && ny in 0..<Y) {
                for (newDir in newDir(field[ny][nx], p.dir)) {
                    val np = Point(ny, nx, newDir)
                    if (np !in seen) {
                        seen.add(np)
                        points.add(np)
                    }
                }
            }
        }

        return seen.distinctBy { it.y to it.x }.size
    }

    fun part1() = energized(Point(0, -1, 1))

    fun part2(): Any {
        var result = 0
        for (y in 0..<Y) {
            result = max(result, energized(Point(y, -1, 1)))
            result = max(result, energized(Point(y, X, 3)))
        }
        for (x in 0..<X) {
            result = max(result, energized(Point(-1, x, 2)))
            result = max(result, energized(Point(Y, x, 0)))
        }
        return result
    }
}


fun main() {
    val testInstance = Day16(true)
    val instance = Day16(false)

    testInstance.part1().let { check(it == EXPECTED_1) { "part1: $it != $EXPECTED_1" } }
    println("part1 ANSWER: ${instance.part1()}")
    testInstance.part2().let {
        check(it == EXPECTED_2) { "part2: $it != $EXPECTED_2" }
        println("part2 ANSWER: ${instance.part2()}")
    }
}
