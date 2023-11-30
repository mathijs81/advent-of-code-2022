import java.util.*

private const val EXPECTED_1 = 31
private const val EXPECTED_2 = 29

private const val MAX = 100000
private val DIRS = listOf(1 to 0, -1 to 0, 0 to 1, 0 to -1)

/**
 * Day12 implementation using Dijkstra's
 */
private class Day12(isTest: Boolean) : Solver(isTest) {
    var start = 0 to 0
    var end = 0 to 0
    var X = 0
    var Y = 0
    lateinit var best: List<MutableList<Int>>
    val heights: List<List<Int>> = readAsLines().withIndex().map { (y, row) ->
        row.withIndex().map { (x, value) ->
            when (value) {
                'S' -> {
                    start = y to x
                    0
                }
                'E' -> {
                    end = y to x
                    26
                }
                else -> value - 'a'
            }
        }
    }

    init {
        Y = heights.size
        X = heights[0].size
    }

    fun dijkstra(start: Pair<Int, Int>, reverse: Boolean) {
        val queue = TreeSet<Pair<Int, Int>>(compareBy(
                        { best[it.first][it.second] },
                        { it.first },
                        { it.second }
                )
        )
        best[start.first][start.second] = 0
        queue.add(start)
        while (!queue.isEmpty()) {
            val pos = queue.pollFirst()!!
            for (dir in DIRS) {
                val n = (pos.first + dir.first) to (pos.second + dir.second)
                if (n.first in 0 until Y && n.second in 0 until X) {
                    if ((if (reverse) -1 else 1) * (heights[n.first][n.second] - heights[pos.first][pos.second]) <= 1) {
                        if (best[n.first][n.second] > best[pos.first][pos.second] + 1) {
                            queue.remove(n)
                            best[n.first][n.second] = best[pos.first][pos.second] + 1
                            queue.add(n)
                        }
                    }
                }
            }
        }
    }

    fun part1(): Any {
        best = (0 until Y).map {
            (0 until X).map { MAX }.toMutableList()
        }
        dijkstra(start, false)

        return best[end.first][end.second]
    }

    fun part2(): Any {
        best = (0 until Y).map {
            (0 until X).map { MAX }.toMutableList()
        }
        dijkstra(end, true)

        return (0 until Y).minOf { y ->
            (0 until X).minOf { x ->
                if (heights[y][x] == 0) best[y][x] else MAX
            }
        }

    }
}

fun main() {
    val testInstance = Day12(true)
    val instance = Day12(false)

    testInstance.part1().let { check(it == EXPECTED_1) { "part1: $it != $EXPECTED_1" } }
    println("part1 ANSWER: ${instance.part1()}")
    testInstance.part2().let {
        check(it == EXPECTED_2) { "part2: $it != $EXPECTED_2" }
        println("part2 ANSWER: ${instance.part2()}")
    }
}
