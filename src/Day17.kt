import java.util.PriorityQueue
import kotlin.math.min

private const val EXPECTED_1 = 102
private const val EXPECTED_2 = 94

private class Day17(isTest: Boolean) : Solver(isTest) {
    val field = readAsLines().map { it.toCharArray() }
    val Y = field.size
    val X = field[0].size

    data class Point(val y: Int, val x: Int, val dir: Int, val sameDir: Int, val score: Int)
    val dirs = listOf(1 to 0, 0 to 1, -1 to 0, 0 to -1)

    fun bestPath(minLen: Int, maxLen: Int): Int {
        val best = mutableMapOf<Point, Int>()

        val q = PriorityQueue<Point>(Comparator.comparing { it.score })
        for (dir in 0..3) {
            val start = Point(0, 0, dir, 0, 0)
            q.add(start)
            best[start] = 0
        }

        var ans = Int.MAX_VALUE

        while (!q.isEmpty()) {
            val p = q.remove()
            if (p.score > ans) {
                break
            }

            if (p.sameDir < maxLen) {
                val nx = p.x + dirs[p.dir].second
                val ny = p.y + dirs[p.dir].first
                if (nx in 0 until X && ny in 0 until Y) {
                    val newScore = p.score + field[ny][nx].code - '0'.code
                    if (nx == X - 1 && ny == Y - 1) {
                        ans = min(ans, newScore)
                    }

                    fun tryAdd(np: Point) {
                        if (np.score > ans) {
                            return
                        }
                        val c = np.copy(score = 0)

                        val current = best[c]
                        if (current == null || current > newScore) {
                            best[c] = newScore
                            q.add(np)
                        }
                    }

                    tryAdd(Point(ny, nx, p.dir, p.sameDir + 1, newScore))

                    if (p.sameDir + 1 >= minLen) {
                        for (plusDir in listOf(1, 3)) {
                            tryAdd(Point(ny, nx, (p.dir + plusDir) % 4, 0, newScore))
                        }
                    }
                }
            }
        }

        return ans
    }

    fun part1() = bestPath(1, 3)

    fun part2() = bestPath(4, 10)
}


fun main() {
    val testInstance = Day17(true)
    val instance = Day17(false)

    testInstance.part1().let { check(it == EXPECTED_1) { "part1: $it != $EXPECTED_1" } }
    println("part1 ANSWER: ${instance.part1()}")
    testInstance.part2().let {
        check(it == EXPECTED_2) { "part2: $it != $EXPECTED_2" }
        println("part2 ANSWER: ${instance.part2()}")
    }
}
