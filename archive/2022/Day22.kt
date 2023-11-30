private const val EXPECTED_1 = 6032
private const val EXPECTED_2 = 5031

/**
 * Very hard problem. I ended up hardcoding the cube face linking edges. Didn't have time anymore
 * to clean up.
 */
private class Day22(isTest: Boolean) : Solver(isTest) {
    val map = readAsString().split("\n\n")[0].split("\n").let {
        val maxWidth = it.maxOf { it.length }
        it.map { it.padEnd(maxWidth, ' ') }
    }

    val Y = map.size
    val X = map[0].length
    val instructions = readAsString().split("\n\n")[1].let {
        it.splitInts() zip it.split("\\d+".toRegex()).drop(1)
    }

    val dirs = listOf(1 to 0, 0 to 1, -1 to 0, 0 to -1)

    fun part1(): Any {
        var pos = Triple(map[0].indexOf('.'), 0, 0)

        fun Triple<Int, Int, Int>.nextStep() = copy(first = (first + dirs[third].first).mod(X), (second + dirs[third].second).mod(Y))
        fun Triple<Int, Int, Int>.next(): Triple<Int, Int, Int> {
            check(map[second][first] != ' ')
            var next = nextStep()
            while (map[next.second][next.first] == ' ') next = next.nextStep()
            return next
        }

        for (inst in instructions) {
            for (i in 0 until inst.first) {
                var newpos = pos.next()
                if (map[newpos.second][newpos.first] == '#') {
                    break
                }
                pos = newpos
            }

            when (inst.second) {
                "L" -> pos = pos.copy(third = (pos.third - 1).mod(dirs.size))
                "R" -> pos = pos.copy(third = (pos.third + 1).mod(dirs.size))
                else -> {}
            }
        }

        return (pos.second + 1) * 1000 + (pos.first + 1) * 4 + pos.third
    }


    fun part2(): Any {
        val cubesize = if (isTest) 4 else 50
        var pos = Triple(map[0].indexOf('.'), 0, 0)

        var connections =
                if (isTest) {
                    mapOf(
                            Triple(2, 1, 0) to Triple(3, 2, 3),
                            Triple(2, 2, 1) to Triple(0, 1, 1),
                            Triple(0, 1, 3) to Triple(2, 0, 2),
                            Triple(1, 1, 3) to Triple(2, 0, 2),
                    )
                } else {
                    mapOf(
                            Triple(1, 0, 3) to Triple(0, 3, 2),
                            Triple(1, 0, 2) to Triple(0, 2, 2),
                            Triple(0, 3, 1) to Triple(2, 0, 3),
                            Triple(2, 0, 1) to Triple(1, 1, 0),
                            Triple(1, 2, 0) to Triple(2, 0, 0),
                            Triple(1, 2, 1) to Triple(0, 3, 0),
                            Triple(0, 2, 3) to Triple(1, 1, 2),
                    )
                }
        connections = connections + connections.map { it.value to it.key }.toMap()

        fun Triple<Int, Int, Int>.rotate() = Triple(cubesize - second - 1, first, (third + 1).mod(4))

        fun connectedSide(lastPos: Triple<Int, Int, Int>): Triple<Int, Int, Int> {
            val cube = Triple(lastPos.first / cubesize, lastPos.second / cubesize, lastPos.third)
            val newcube = connections[cube] ?: error("$cube $lastPos")
            var nextFirst = (lastPos.first + dirs[lastPos.third].first).mod(cubesize)
            var nextSecond = (lastPos.second + dirs[lastPos.third].second).mod(cubesize)

            var newpos = Triple(nextFirst, nextSecond, lastPos.third)
            while (newpos.third != (newcube.third + 2).mod(4)) {
                newpos = newpos.rotate()
            }
            return Triple(newpos.first + cubesize * newcube.first, newpos.second + cubesize * newcube.second, (newcube.third + 2).mod(4))
        }

        fun Triple<Int, Int, Int>.nextStep(): Triple<Int, Int, Int> {
            val nextFirst = first + dirs[third].first
            val nextSecond = second + dirs[third].second
            if (nextFirst !in 0 until X || nextSecond !in 0 until Y || map[nextSecond][nextFirst] == ' ') {
                return connectedSide(this)
            } else {
                return copy(nextFirst, nextSecond)
            }
        }

        fun Triple<Int, Int, Int>.next(): Triple<Int, Int, Int> {
            check(map[second][first] != ' ')
            var next = nextStep()
            while (map[next.second][next.first] == ' ') {
                next = next.nextStep()
            }
            return next
        }

        for (inst in instructions) {
            for (i in 0 until inst.first) {
                var newpos = pos.next()
                if (map[newpos.second][newpos.first] == '#') {
                    break
                }
                pos = newpos
            }

            when (inst.second) {
                "L" -> pos = pos.copy(third = (pos.third - 1).mod(dirs.size))
                "R" -> pos = pos.copy(third = (pos.third + 1).mod(dirs.size))
                else -> {}
            }
        }

        return (pos.second + 1) * 1000 + (pos.first + 1) * 4 + pos.third
    }
}


fun main() {
    val testInstance = Day22(true)
    val instance = Day22(false)

    testInstance.part1().let { check(it == EXPECTED_1) { "part1: $it != $EXPECTED_1" } }
    println("part1 ANSWER: ${instance.part1()}")
    testInstance.part2().let {
        check(it == EXPECTED_2) { "part2: $it != $EXPECTED_2" }
        println("part2 ANSWER: ${instance.part2()}")
    }
}

