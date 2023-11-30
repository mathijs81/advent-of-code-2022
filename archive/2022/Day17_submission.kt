private const val EXPECTED_1 = 3068L
private const val EXPECTED_2 = 1514285714288

private class Day17_submission(isTest: Boolean) : Solver(isTest) {
    fun state(dataIndex: Int, depthHash: Long): List<Number> {
        return listOf(dataIndex, depthHash)
    }

    fun ok(firstLine: BooleanArray): Boolean {
        var inRow = 0
        for (value in firstLine) {
            if (value) {
                inRow = 0
            } else {
                inRow ++
                if (inRow >= 4){
                    return false
                }
            }
        }
        return true
    }

    class MyField {
        val internal = Array(500000) { BooleanArray(7) }
        var index = 0

        operator fun get(y: Int): BooleanArray {
            if (y > index + 490000) {
                while (y > index + 490000) {
                    internal[index % internal.size].let { row ->
                        (0..6).forEach { row[it] = false }
                    }
                    index++
                 //   println(index)
                }
            }
            return internal[y % internal.size]
        }

        fun depthHash(y : Int): Long {
            var value = 123L
            var multiplier = 123L
            var max = 100L
            (0..6).map {
                var dy = 0
                while(dy < max && !internal[(y + internal.size - dy) % internal.size][it]) dy++
                if (internal[(y + internal.size - dy - 2) % internal.size][it]) {
                    value += it
                }
                if (dy >= max) {
                    println(y)
                    error("depth wrong ")
                }
                value += dy * multiplier
                multiplier *= max
            }

            for (dy in 0..10) {
                for (x in 0..6) {
                    if (internal[(y + internal.size - dy) % internal.size][x]) {
                        value += multiplier
                    }
                    multiplier *= 2
                }
            }
            return value
        }
    }

    fun part1() = calc(2022)
    fun part2() = calc(1000000000000)

    fun calc(count: Long, doFast: Boolean = true): Any {
        var highest = 0L
        val field = MyField()//Array<BooleanArray>(50) { BooleanArray(7) }

        val shapes = listOf(
                listOf(0 to 0, 1 to 0, 2 to 0, 3 to 0),
                listOf(0 to -1, 1 to 0, 1 to -1, 1 to -2, 2 to -1),
                listOf(0 to 0, 1 to 0, 2 to 0, 2 to -1, 2 to -2),
                listOf(0 to 0, 0 to -1, 0 to -2, 0 to -3),
                listOf(0 to 0, 1 to 0, 0 to -1, 1 to -1)
        )
        val widths = shapes.map { it.maxOf { it.first + 1 } }
        val data = readAsString()
        var index = 0

        val lastSeen = mutableMapOf<List<Number>, Pair<Long, Long>>()
        var need = count
        var shapeIndex = 0L
        var addHighest = 0L

        while(need > 0) {
            if (doFast && need > 14000 && shapeIndex % 5 == 0L && highest > 1000) {//1e7) {
                if (ok(field[(highest - 1).toInt()])) {
                    val state = state((index % data.length).toInt(),
                            field.depthHash(highest.toInt()))
                            //field[(highest-1).toInt()])
                    if (state in lastSeen) {
                        val addTo = highest - lastSeen[state]!!.second
                        val shapes = shapeIndex - lastSeen[state]!!.first

                        if (need > shapes) {

                            println("Found ${lastSeen[state]} -->${shapeIndex}  ${highest}")

                            val handleShapes = (need / shapes) * shapes
                            println("Adding $handleShapes, $need --> ${addTo * (handleShapes / shapes)}")
                            need -= handleShapes
                            shapeIndex += handleShapes
                            addHighest += addTo * (handleShapes / shapes)
                            println("New values $need $addHighest")
                        }
                        //error("$highest ${lastSeen[state]}")
                    } else {
                        lastSeen[state] = shapeIndex to highest
                    }
                }
            }

            val shape = shapes[(shapeIndex % 5).toInt()]
            val width = widths[(shapeIndex % 5).toInt()]
            var pos = 2 to highest + 3
            do {
                val dir = data[index % data.length]
                var newPos: Pair<Int, Long>
                when (dir) {
                    '<' -> newPos = maxOf(0, pos.first - 1) to pos.second
                    '>' -> newPos = minOf(7 - width, pos.first + 1) to pos.second
                    else -> error("")
                }
                if (shape.none { newPos.second-it.second < 0 || field[(newPos.second - it.second).toInt()][newPos.first + it.first] }) {
                    pos = newPos
                }
                index ++
                val rest = shape.any { pos.second-it.second-1 < 0 || field[(pos.second - it.second - 1).toInt()][pos.first + it.first] }
                if (rest) {
                    shape.forEach {
                        field[(pos.second - it.second).toInt()][pos.first + it.first] = true
                        highest = maxOf(highest, pos.second - it.second + 1)
                    }
                } else {
                    pos = pos.first to pos.second - 1
                }
//                println("$it: (${it % 5}) $pos")

            } while(!rest)

            shapeIndex++
            need--
        }
        return highest + addHighest
    }
}


fun main() {
    val testInstance = Day17_submission(true)
    val instance = Day17_submission(false)


//    var amount = 10L
//    while(true ) {
//        println(amount)
//        val a = instance.calc(amount, false)
//        val b = instance.calc(amount, true)
//        println("$a -- $b")
//        check(a==b)
//        amount *= 5
//    }

    testInstance.part1().let { check(it == EXPECTED_1) { "part1: $it != $EXPECTED_1" } }
    println("part1 ANSWER: ${instance.part1()}")
    testInstance.part2().let {
        check(it == EXPECTED_2) { "part2: $it != $EXPECTED_2" }
        println("test done")
        println("part2 ANSWER: ${instance.part2()}")
    }
}
