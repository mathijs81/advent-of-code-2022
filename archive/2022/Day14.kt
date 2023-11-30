private const val EXPECTED_1 = 24
private const val EXPECTED_2 = 93

private class Day14(isTest: Boolean) : Solver(isTest) {
    private val dirs = listOf(0 to 1, -1 to 1, 1 to 1)

    private fun parseField(): Pair<List<BooleanArray>, Int> {
        val field = (0..1000).map { BooleanArray(1000) { false } }
        var maxY = 0
        for (line in readAsLines()) {
            line.split(" -> ").map { pt -> pt.split(",").let { it[0].toInt() to it[1].toInt() } }.windowed(2).forEach { (a, b) ->
                drawLine(a, b, field)
                maxY = maxOf(a.second, b.second, maxY)
            }
        }
        return Pair(field, maxY)
    }

    private fun drawLine(a: Pair<Int, Int>, b: Pair<Int, Int>, field: List<BooleanArray>) {
        if (a.first == b.first) {
            for (y in minOf(a.second, b.second)..maxOf(a.second, b.second)) {
                field[a.first][y] = true
            }
        } else {
            for (x in minOf(a.first, b.first)..maxOf(a.first, b.first)) {
                field[x][a.second] = true
            }
        }
    }

    private fun simulate(field: List<BooleanArray>, maxY: Int): Int {
        var n = 0
        while (!field[500][0]) {
            var pos = 500 to 0
            while (pos.second < maxY) {
                pos = dirs.map { (dx, dy) -> pos.first + dx to pos.second + dy }.firstOrNull { !field[it.first][it.second] }
                        ?: break
            }
            if (pos.second >= maxY) {
                break
            }
            field[pos.first][pos.second] = true
            n++
        }
        return n
    }

    fun part1(): Any {
        val (field, maxY) = parseField()
        return simulate(field, maxY)
    }

    fun part2(): Any {
        val (field, maxY) = parseField()
        drawLine(0 to maxY + 2, 999 to maxY + 2, field)
        return simulate(field, maxY + 2)
    }

}


fun main() {
    val testInstance = Day14(true)
    val instance = Day14(false)

    testInstance.part1().let { check(it == EXPECTED_1) { "part1: $it != $EXPECTED_1" } }
    println("part1 ANSWER: ${instance.part1()}")
    testInstance.part2().let {
        check(it == EXPECTED_2) { "part2: $it != $EXPECTED_2" }
        println("part2 ANSWER: ${instance.part2()}")
    }
}
