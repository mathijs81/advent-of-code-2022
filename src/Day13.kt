private const val EXPECTED_1 = 405
private const val EXPECTED_2 = 400

private class Day13(isTest: Boolean) : Solver(isTest) {
    fun List<String>.transpose(): List<String> {
        val Y = this.size
        val X = this[0].length
        val res = List(X) { CharArray(Y) }
        for (y in 0 until Y) {
            for (x in 0 until X) {
                res[x][y] = this[y][x]
            }
        }
        return res.map { it.joinToString("") }

    }

    fun findSymmetry(field: List<String>, unequalTarget: Int = 0): Int {
        val Y = field.size
        val X = field[0].length
        for (i in 0 until X - 1) {
            var unequal = 0
            for (a in 0..i) {
                val other = (i - a) + i + 1
                if (other >= X) {
                    continue
                }
                unequal += (0 until Y).count { field[it][a] != field[it][other] }
            }
            if (unequal == unequalTarget) {
                return i + 1
            }
        }
        return -1
    }

    fun part1(): Any {
        return readAsString().split("\n\n").map { it.split("\n") }.sumOf { field ->
            val col = findSymmetry(field)
            val row = findSymmetry(field.transpose())
            check(col != -1 || row != -1)
            if (col == -1) {
                100 * row
            } else {
                col
            }
        }
    }

    fun part2(): Any {
        return readAsString().split("\n\n").map { it.split("\n") }.sumOf { field ->
            val col = findSymmetry(field, 1)
            val row = findSymmetry(field.transpose(), 1)
            check(col != -1 || row != -1)
            if (col == -1) {
                100 * row
            } else {
                col
            }
        }
    }
}


fun main() {
    val testInstance = Day13(true)
    val instance = Day13(false)

    testInstance.part1().let { check(it == EXPECTED_1) { "part1: $it != $EXPECTED_1" } }
    println("part1 ANSWER: ${instance.part1()}")
    testInstance.part2().let {
        check(it == EXPECTED_2) { "part2: $it != $EXPECTED_2" }
        println("part2 ANSWER: ${instance.part2()}")
    }
}
