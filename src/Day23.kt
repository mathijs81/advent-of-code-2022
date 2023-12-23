import kotlin.math.absoluteValue
import kotlin.math.max
import kotlin.time.measureTime

private const val EXPECTED_1 = 94
private const val EXPECTED_2 = 154

private class Day23(isTest: Boolean) : Solver(isTest) {
    val field = readAsLines().map { it.toCharArray() }
    val Y = field.size
    val X = field[0].size
    data class Point(val y : Int, val x: Int)

    fun part1(): Any {
        val sx = 1
        val sy = 0

        fun canMove(dir: Pair<Int, Int>, ch: Char): Boolean {
            return when(ch) {
                '#' -> false
                '.' -> true
                '^' -> dir.first == -1
                '>' -> dir.second == 1
                'v' -> dir.first == 1
                '<' -> dir.second == -1
                else -> error("")
            }
        }
        fun mod(x: Point, ch: Char): Point {
            return when(ch) {
                '#' -> error("")
                '.' -> x
                '^' -> x.copy(y=x.y - 1)
                '>' -> x.copy(x=x.x + 1)
                'v' -> x.copy(y=x.y + 1)
                '<' -> x.copy(x=x.x - 1)
                else -> error("")
            }
        }

        val steps = mutableMapOf<Point, Pair<Int, Set<Point>>>()
        val q = ArrayDeque<Point>()
        q.add(Point(sy, sx).also {steps[it] = 0 to setOf(it)})
        var best  = 0
        while(!q.isEmpty()) {
            val p = q.removeFirst()
            val (step, visited) = steps[p]!!
            //println("$p $step")
            for (dir in listOf(1 to 0, 0 to 1, 0 to -1, -1 to 0)) {
                val p2 = Point(p.y + dir.first, p.x + dir.second)
                if (p2.x in 0..<X && p2.y in 0..<Y && canMove(dir, field[p2.y][p2.x])) {
                    val m = mod(p2, field[p2.y][p2.x])
                    if (m !in visited) {
                        if ((steps[p2]?.first ?: 0) < step + 1) {
                            q.add(m)
                            steps[m] = (step + if (m == p2) 1 else 2) to (visited + m)
                            if (m.y == Y - 1) {
                                best = max(best, steps[m]!!.first)
                            }
                        }
                    }
                }
            }
        }
        return best
    }

    fun part2(): Int {
        for (y in 0..<Y) {
            for (x in 0..<X) {
                if (field[y][x] !in setOf('#', '.')) {
                    field[y][x] = '.'
                }
            }

        }
        val graph = mutableMapOf<Point, MutableList<Pair<Point, Int>>>()
        fun addLink(a: Point, b: Point, steps: Int) {
            graph.getOrPut(a) { mutableListOf() }.let {
                if ((b to steps) !in it) {
                    it.add(b to steps)
                }
            }
            graph.getOrPut(b) { mutableListOf() }.let {
                if ((a to steps) !in it) {
                    it.add(a to steps)
                }
            }
        }

        fun buildGraph(sourceP: Point, comeFrom_: Point, p_: Point, steps_: Int) {
            var comeFrom = comeFrom_
            var p = p_
            var steps = steps_
            while(true) {
                if (graph.contains(p)) {
                    addLink(p, sourceP, steps)
                    return
                }
                if (p.y == Y - 1) {
                    addLink(p, sourceP, steps)
                    return
                }

                val okDirs = listOf(1 to 0, 0 to 1, 0 to -1, -1 to 0).filter { dir ->
                    val p2 = Point(p.y + dir.first, p.x + dir.second)
                    if (p2 == comeFrom) {
                        false
                    } else if (p2.x in 0..<X && p2.y in 0..<Y && field[p2.y][p2.x] == '.') {
                        true
                    } else {
                        false
                    }
                }


                if (okDirs.size > 1) {
                    // This is a new sourceP
                    addLink(p, sourceP, steps)
                    okDirs.forEach { dir ->
                        val p2 = Point(p.y + dir.first, p.x + dir.second)
                        buildGraph(p, p, p2, 1)
                    }
                    return
                }
                val dir = okDirs.single()
                //buildGraph(sourceP, p, Point(p.y + dir.first, p.x + dir.second), steps + 1)
                comeFrom = p
                p = Point(p.y + dir.first, p.x + dir.second)
                steps++
            }
        }
        buildGraph(Point(0, 1), Point(0, 1), Point(1, 1), 1)

        fun longestPath(visited: Set<Point>): Int {
            if (visited.last() == Point(Y-1, X-2)) {
                return 0
            }

            return graph[visited.last()]!!.mapNotNull { (point, dist) ->
                if (point !in visited) {
                    longestPath(visited + point) + dist
                } else {
                    null
                }
            }.maxOrNull() ?: -100000
        }

        return longestPath(setOf(Point(0, 1)))
    }
}


fun main() {
    val testInstance = Day23(true)
    val instance = Day23(false)

    testInstance.part1().let { check(it == EXPECTED_1) { "part1: $it != $EXPECTED_1" } }
    println("part1 ANSWER: ${instance.part1()}")
    testInstance.part2().let {
        check(it == EXPECTED_2) { "part2: $it != $EXPECTED_2" }
        measureTime {
            println("part2 ANSWER: ${instance.part2()}")
        }.also { println(it) }
    }
}
