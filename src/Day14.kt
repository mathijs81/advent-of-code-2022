private const val EXPECTED_1 = 136
private const val EXPECTED_2 = 64

private class Day14(isTest: Boolean) : Solver(isTest) {
    fun part1(): Any {
        val field = readAsLines()
        var sum = 0
        fun add(row: Int, count: Int) {
            for (i in 0 until count) {
                sum += field.size - row - i
            }
        }

        for (x in 0 until field[0].length) {
            var rockCount = 0
            for (y in field.size - 1 downTo 0) {
                if (field[y][x] == 'O') {
                    rockCount++
                }
                if (field[y][x] == '#') {
                    add(y + 1, rockCount)
                    rockCount = 0
                }
            }
            add(0, rockCount)
        }
        return sum
    }

    fun load(field: List<CharArray>): Int {
        var sum = 0
        for (y in field.indices) {
            for (x in field[0].indices) {
                if (field[y][x] == 'O') {
                    sum += field.size - y
                }
            }
        }
        return sum
    }

    fun part2(): Any {
        var field = readAsLines().map { it.toCharArray() }
        fun add(row: Int, count: Int, field: List<CharArray>, x: Int) {
            for (i in 0 until count) {
                field[row + i][x] = 'O'
            }
        }

        fun rotate(field: List<CharArray>): List<CharArray> {
            val res = List(field[0].size) { CharArray(field.size) }
            for (y in res.indices) {
                for (x in res[0].indices) {
                    res[y][x] = field[field[0].size - x - 1][y]
                }
            }
            return res
        }

        val seen = mutableMapOf<String, Long>()
        var roundsLeft = 1000000000L

        while (roundsLeft > 0) {
            repeat(4) {
                for (x in 0 until field[0].size) {
                    var rockCount = 0
                    for (y in field.size - 1 downTo 0) {
                        if (field[y][x] == 'O') {
                            rockCount++
                            field[y][x] = '.'
                        }
                        if (field[y][x] == '#') {
                            add(y + 1, rockCount, field, x)
                            rockCount = 0
                        }
                    }
                    add(0, rockCount, field, x)
                }
                field = rotate(field)
            }
            roundsLeft--
            val key = field.map { it.joinToString("") }.joinToString("")
            val lastIndex = seen[key]
            if (lastIndex != null) {
                val add = lastIndex - roundsLeft
                if (roundsLeft >= add) {
                    roundsLeft %= add
                }
            }
            seen[key] = roundsLeft
        }
        return load(field)
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
