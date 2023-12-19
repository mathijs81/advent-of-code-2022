import kotlin.math.absoluteValue
import kotlin.math.max
import kotlin.math.min

private const val EXPECTED_1 = 62L
private const val EXPECTED_2 = 952408144115L

private class Day18(isTest: Boolean) : Solver(isTest) {
    data class Point(val y: Int, val x: Int)

    fun part1Bfs(): Any {
        val walls = mutableSetOf<Point>()

        var y = 0
        var x = 0

        var Y = 0
        var X = 0
        var mx = 0
        var my = 0

        for (line in readAsLines()) {
            val dir = when(line[0]) {
                'R' -> 0 to 1
                'L' -> 0 to -1
                'U' -> -1 to 0
                'D' -> 1 to 0
                else -> error(line)
            }
            val len = line.split(" ")[1].toInt()
            repeat(len) {
                y += dir.first
                x += dir.second

                mx = min(x, mx)
                my = min(y, my)

                Y = max(y, Y)
                X = max(x, X)
                walls.add(Point(y,x))
            }
        }

        val outside = mutableSetOf<Point>()
        val q = ArrayDeque<Point>()
        q.add(Point(my - 1, 0).also { outside.add(it) })

        while (!q.isEmpty()) {
            val p = q.removeFirst()

            for (d in listOf(0 to 1, 0 to -1, -1 to 0, 1 to 0, 1 to -1, 1 to 1, -1 to -1, -1 to 1, 1 to -1)) {
                val p2 = Point(p.y + d.first, p.x + d.second)
//                println("Trying $p2 -- ${walls.contains(p2)}")
                if (p2.x in (mx - 1)..(X+1) && p2.y in (my-1)..(Y+1) && !walls.contains(p2)) {
                    if (outside.add(p2)) {
//                        println("Adding $p2")
                        q.add(p2)
                    }
                }
            }
        }
        return (X - mx + 2 + 1) * (Y-my+2 + 1) - outside.size
    }

    fun part1(): Any {
        val walls = mutableListOf<Pair<Point, Point>>()

        var y = 0
        var x = 0

        for (line in readAsLines()) {
            val len = line.split(" ")[1].toInt()
            val oy = y
            val ox = x

            when (line[0]) {
                'R' -> x += len
                'L' -> x -= len
                'U' -> y -= len
                'D' -> y += len
                else -> error(line)
            }
            walls.add(Point(oy,ox) to Point(y, x))
        }

        return gridpoints(walls)
    }


    fun part2(): Long {
        val walls = mutableListOf<Pair<Point, Point>>()
        var x = 0
        var y = 0
        var maxY = 0
        for (line in readAsLines()) {
            val parts = line.split("#")
            val len = parts[1].substring(0, 5).toLong(radix = 16).toInt()
            val dir = parts[1][5]

            val ox = x
            val oy = y
            when (dir) {
                '0' -> x += len
                '1' -> y += len
                '2' -> x -= len
                '3' -> y -= len
            }
            walls.add(Point(oy, ox) to Point(y,x))
            maxY = max(y, maxY)
        }

        return gridpoints(walls)
    }

    fun gridpoints(walls: List<Pair<Point, Point>>): Long {
        var boundaryPoints = 0L
        var area = 0L
        for (w in walls) {
            boundaryPoints += (w.first.y - w.second.y).absoluteValue + (w.first.x - w.second.x).absoluteValue
            if (w.first.y == w.second.y) {
                area += w.first.y * (w.second.x - w.first.x.toLong())
            }
        }

        // Pick's theorem
        val interiorPoints = area.absoluteValue - boundaryPoints / 2 + 1
        return interiorPoints + boundaryPoints
    }
}


fun main() {
    val testInstance = Day18(true)
    val instance = Day18(false)

    testInstance.part1().let { check(it == EXPECTED_1) { "part1: $it != $EXPECTED_1" } }
    println("part1 ANSWER: ${instance.part1()}")
    testInstance.part2().let {
        check(it == EXPECTED_2) { "part2: $it != $EXPECTED_2 -- (${it - EXPECTED_2})" }
        println("part2 ANSWER: ${instance.part2()}")
    }
}
