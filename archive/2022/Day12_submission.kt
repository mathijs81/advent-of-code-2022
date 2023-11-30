private const val EXPECTED_1 = 31
private const val EXPECTED_2 = 29

/**
 * Pretty horrible code written during the race. Rushed too quickly to dfs before I
 * realized that that won't work without revisiting nodes when you see that they can be reached
 * in fewer steps than before. Fortunately it could be patched up and was still fast enough to
 * complete the challenge
 */
private class Day12_submission(isTest: Boolean) : Solver(isTest) {
    val MAX = 100000000
    var X = 0
    var Y = 0
    var targ = 0 to 0
    lateinit var best: List<MutableList<Int>>

    val dirs = listOf(0 to 1, 0 to -1, 1 to 0, -1 to 0)
    var reverse = false
    fun dfs(loc: Pair<Int, Int>, heights: List<List<Int>>, steps: Int) {
        if (loc.second !in 0 until X || loc.first !in 0 until Y) {
            return
        }
        if (best[loc.first][loc.second] <= steps) {
            return
        }
        best[loc.first][loc.second] = steps
        for (dir in dirs) {
            val n = (loc.first + dir.first) to (loc.second + dir.second)
            if (n.first in 0 until Y && n.second in 0 until X) {
                if ((if (reverse) -1 else 1) * (heights[n.first][n.second] - heights[loc.first][loc.second]) <= 1) {
                    dfs(n, heights, steps + 1)
                }
            }
        }
    }

    fun part1(): Any {
        var start = 0 to 0
        val heights = readAsLines().withIndex().map { (y, line) ->
            line.toCharArray().toList().withIndex().map { (x, value) ->
                if (value == 'S') {
                    start = y to x
                    0
                } else if (value == 'E') {
                    targ = y to x
                    'z' - 'a'
                } else value - 'a'
            }
        }
        Y = heights.size
        X = heights[0].size

        best = heights.map { IntArray(it.size) { MAX }.toMutableList() }
        dfs(start, heights, 0)
        return best[targ.first][targ.second]

    }

    fun part2(): Any {
        var start = 0 to 0
        val heights = readAsLines().withIndex().map { (y, line) ->
            line.toCharArray().toList().withIndex().map { (x, value) ->
                if (value == 'S') {
                    start = y to x
                    0
                } else if (value == 'E') {
                    targ = y to x
                    'z' - 'a'
                } else value - 'a'
            }
        }
        Y = heights.size
        X = heights[0].size

        best = heights.map { IntArray(it.size) { MAX }.toMutableList() }
        reverse = true
        dfs(targ, heights, 0)
        var answer = MAX
        for (y in 0 until Y)
            for (x in 0 until X) {
                if (heights[y][x] == 0) {
                    answer = answer.coerceAtMost(best[y][x])
                }
            }
        return answer
    }
}

fun main() {
    val testInstance = Day12_submission(true)
    val instance = Day12_submission(false)

    testInstance.part1().let { check(it == EXPECTED_1) { "part1: $it != $EXPECTED_1" } }
    println("part1 ANSWER: ${instance.part1()}")
    testInstance.part2().let {
        check(it == EXPECTED_2) { "part2: $it != $EXPECTED_2" }
        println("part2 ANSWER: ${instance.part2()}")
    }
}
