private const val EXPECTED_1 = 8
private const val EXPECTED_2 = 10

// Way too complicated with trying all 4 directions from the start point, but it works
private class Day10(isTest: Boolean) : Solver(isTest) {
    val connections = mapOf(
        '|' to listOf(0 to -1, 0 to 1),
        '-' to listOf(-1 to 0, 1 to 0),
        'L' to listOf(0 to -1, 1 to 0),
        'J' to listOf(0 to -1, -1 to 0),
        '7' to listOf(0 to 1, -1 to 0),
        'F' to listOf(0 to 1, 1 to 0),
        '.' to listOf(),
        'S' to listOf(0 to 1, 0 to -1, 1 to 0, -1 to 0)
    )

    val dirs = listOf(0 to 1, 0 to -1, 1 to 0, -1 to 0)


    val field = readAsLines().map { it.toCharArray() }
    val X = field[0].size
    val Y = field.size
    var startPoint = 0 to 0
    val allPoints = (0 until Y).flatMap { y ->
        (0 until X).map { x ->
            x to y
        }
    }

    init {
        for (y in 0 until Y) {
            for (x in 0 until X) {
                if (field[y][x] == 'S') {
                    startPoint = x to y
                }
            }
        }
    }

    fun createMap(dir: Pair<Int, Int>): Map<Pair<Int, Int>, Int> {
        val map = mutableMapOf<Pair<Int, Int>, Int>()
        val queue = mutableListOf(startPoint)
        var step = 0
        while (queue.isNotEmpty()) {
            val newQueue = mutableListOf<Pair<Int, Int>>()
            for (point in queue) {
                if (map.containsKey(point)) continue
                map[point] = step
                val conns = if (field[point.second][point.first] == 'S')
                    listOf(dir)
                else
                    connections[field[point.second][point.first]]!!
                for (d in conns) {
                    val newPoint = point.first + d.first to point.second + d.second
                    if (newPoint.first !in 0 until X || newPoint.second !in 0 until Y) continue
                    if (!connections[field[newPoint.second][newPoint.first]]!!.contains(-d.first to -d.second)) continue
                    newQueue.add(newPoint)
                }
            }
            queue.clear()
            queue.addAll(newQueue)
            step++
        }
        return map
    }

    fun part1(): Any {
        val maps = dirs.map { createMap(it) }
        var best = 0
        for (a in 0..3)
            for (b in (a + 1)..3) {
                val f1 = maps[a]
                val f2 = maps[b]
                for (point in allPoints) {
                    if (point !in f1 || point !in f2) continue
                    best = maxOf(best, minOf(f1[point]!!, f2[point]!!))
                }
            }

        return best
    }

    fun part2(): Any {
        val maps = dirs.map { createMap(it) }

        var best = 0
        var bestParams = listOf(0, 0, 0, 0)
        for (a in 0..3)
            for (b in (a + 1)..3) {
                val f1 = maps[a]
                val f2 = maps[b]
                for (point in allPoints) {
                    if (point !in f1 || point !in f2) continue
                    if (minOf(f1[point]!!, f2[point]!!) > best) {
                        best = minOf(f1[point]!!, f2[point]!!)
                        bestParams = listOf(a, b, point.first, point.second)
                    }
                }
            }

        val inEdge: MutableSet<Pair<Int, Int>> = mutableSetOf()

        tailrec fun walkBack(p: Pair<Int, Int>, map: Map<Pair<Int, Int>, Int>) {
            inEdge.add(p)
            if (map[p] == 0) return
            var nextPoint = -1 to -1
            for (d in connections[field[p.second][p.first]]!!) {
                val newPoint = p.first + d.first to p.second + d.second
                if (newPoint.first !in 0 until X || newPoint.second !in 0 until Y) continue
                if (!connections[field[newPoint.second][newPoint.first]]!!.contains(-d.first to -d.second)) continue
                if (map[newPoint]!! < map[p]!!) {
                    nextPoint = newPoint
                }
            }
            if (nextPoint == -1 to -1) throw Exception("No next point")
            walkBack(nextPoint, map)
        }

        fun findConnection(a: Int, b: Int): Char {
            val d1 = dirs[a]
            val d2 = dirs[b]
            return connections.entries.filter { it.value.contains(d1) && it.value.contains(d2) }
                .first().key
        }


        walkBack(bestParams[2] to bestParams[3], maps[bestParams[0]])
        walkBack(bestParams[2] to bestParams[3], maps[bestParams[1]])
        field[startPoint.second][startPoint.first] = findConnection(bestParams[0], bestParams[1])

        var sum = 0

        for (y in 0 until Y) {
            var inside = false
            for (x in 0 until X) {
                if (x to y in inEdge) {
                    val conn = connections[field[y][x]]!!
                    val yMovement = conn.maxOf { it.second }
                    if (yMovement > 0) {
                        inside = !inside
                    }
                } else {
                    if (inside) {
                        sum++
                    }
                }
            }
        }

        return sum
    }
}


fun main() {
    val testInstance = Day10(true)
    val instance = Day10(false)

//    testInstance.part1().let { check(it == EXPECTED_1) { "part1: $it != $EXPECTED_1" } }
//    println("part1 ANSWER: ${instance.part1()}")
    testInstance.part2().let {
        check(it == EXPECTED_2) { "part2: $it != $EXPECTED_2" }
        println("part2 ANSWER: ${instance.part2()}")
    }
}
