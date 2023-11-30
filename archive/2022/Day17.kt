private const val EXPECTED_1 = 3068L
private const val EXPECTED_2 = 1514285714288

/**
 * Slightly cleaned up from my submission with better naming and removing stuff that wasn't
 * needed (I had a bad dataIndex / shapeIndex mixup that the testdata didn't catch because
 * the data length of 40 was divisible by N(shapes) of 5, and kept adding other stuff that
 * I thought could be the problem)
 */
private class Day17(isTest: Boolean) : Solver(isTest) {
    class MyField {
        val internal = Array(5000) { BooleanArray(7) }
        var index = 0

        operator fun get(y: Int): BooleanArray {
            if (y > index + 4500) {
                while (y > index + 4500) {
                    internal[index++ % internal.size].fill(false)
                }
            }
            return internal[y % internal.size]
        }

        fun depthProfile(y: Int) = (0..6).map {
                var dy = 0
                while (dy < 100 && !internal[(y + internal.size - dy) % internal.size][it]) dy++
                check(dy < 100)
                dy
        }
    }

    fun part1() = calc(2022)
    fun part2() = calc(1000000000000)

    fun calc(count: Long): Any {
        var highest = 0L
        val field = MyField()
        val shapes = listOf(
                listOf(0 to 0, 1 to 0, 2 to 0, 3 to 0),
                listOf(0 to -1, 1 to 0, 1 to -1, 1 to -2, 2 to -1),
                listOf(0 to 0, 1 to 0, 2 to 0, 2 to -1, 2 to -2),
                listOf(0 to 0, 0 to -1, 0 to -2, 0 to -3),
                listOf(0 to 0, 1 to 0, 0 to -1, 1 to -1)
        )
        val widths = shapes.map { it.maxOf { it.first + 1 } }
        val data = readAsString()
        var dataIndex = 0

        val lastSeen = mutableMapOf<List<Int>, Pair<Int, Long>>()
        var need = count

        var shapeIndex = 0
        var addHighest = 0L

        while (need > 0) {
            if (highest > 150) {
                    val state = listOf(dataIndex % data.length, shapeIndex % 5, *
                            field.depthProfile(highest.toInt()).toTypedArray())
                    if (state in lastSeen) {
                        val addTo = highest - lastSeen[state]!!.second
                        val shapes = shapeIndex - lastSeen[state]!!.first
                        if (need - 1 > shapes) {
                            //println("Found ${lastSeen[state]} -->${shapeIndex}  ${highest}")

                            val handleShapes = ((need - 1) / shapes) * shapes
                            need -= handleShapes
                            addHighest += addTo * (handleShapes / shapes)
                        }
                    } else {
                        lastSeen[state] = shapeIndex to highest
                    }
            }

            val shape = shapes[(shapeIndex % 5)]
            val width = widths[(shapeIndex % 5)]
            var pos = 2 to highest + 3
            do {
                val newPos = when (data[dataIndex++ % data.length]) {
                    '<' -> maxOf(0, pos.first - 1) to pos.second
                    '>' -> minOf(7 - width, pos.first + 1) to pos.second
                    else -> error("")
                }
                if (shape.none { newPos.second - it.second < 0 || field[(newPos.second - it.second).toInt()][newPos.first + it.first] }) {
                    pos = newPos
                }
                val rest = shape.any { pos.second - it.second - 1 < 0 || field[(pos.second - it.second - 1).toInt()][pos.first + it.first] }
                if (rest) {
                    shape.forEach {
                        field[(pos.second - it.second).toInt()][pos.first + it.first] = true
                        highest = maxOf(highest, pos.second - it.second + 1)
                    }
                } else {
                    pos = pos.first to pos.second - 1
                }
            } while (!rest)
            shapeIndex++
            need--
        }
        return highest + addHighest
    }
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
