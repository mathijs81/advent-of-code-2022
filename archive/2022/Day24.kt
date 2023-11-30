private const val EXPECTED_1 = 18
private const val EXPECTED_2 = 54

private class Day24(isTest: Boolean) : Solver(isTest) {
    // 4 moves + stay in place
    val dirs = listOf(1 to 0, 0 to 1, -1 to 0, 0 to -1, 0 to 0)
    val dirMap = mapOf('>' to 0, 'v' to 1, '<' to 2, '^' to 3)

    val map = readAsLines()
    var startBlizzards = map.withIndex().flatMap { (y, row) ->
        row.withIndex().mapNotNull { (x, char) ->
            if (char in dirMap) {
                Triple(x, y, dirMap[char]!!)
            } else {
                check(char == '.' || char == '#')
                null
            }
        }
    }

    val Y = map.size
    val X = map[0].length

    fun part1(): Any {
        var blizzards = startBlizzards.toList()
        var t = 0
        var positions = setOf(map[0].indexOf('.') to 0)

        while (positions.maxOf { it.second } < Y - 1) {
            var newBlizzards = moveBlizzards(blizzards)
            val blizzardSet = newBlizzards.map { it.first to it.second }.toSet()

            var newPositions = mutableSetOf<Pair<Int, Int>>()
            for (pos in positions) {
                for (dir in dirs) {
                    val newPos = pos.first + dir.first to pos.second + dir.second
                    if (newPos !in blizzardSet && newPos.first in 0 until X && newPos.second in 0 until Y && map[newPos.second][newPos.first] != '#') {
                        newPositions.add(newPos)
                    }
                }
            }
            positions = newPositions
            blizzards = newBlizzards
            t++
        }
        return t
    }

    private fun moveBlizzards(blizzards: List<Triple<Int, Int, Int>>): List<Triple<Int, Int, Int>> {
        var newBlizzards = blizzards.map {
            var newX = it.first + dirs[it.third].first
            var newY = it.second + dirs[it.third].second
            if (map[newY][newX] == '#') {
                when (it.third) {
                    0 -> newX = 1
                    1 -> newY = 1
                    2 -> newX = X - 2
                    3 -> newY = Y - 2
                }
            }
            Triple(newX, newY, it.third)
        }
        return newBlizzards
    }

    fun part2(): Any {
        var blizzards = startBlizzards.toList()
        var t = 0

        // state: (position) to (leg of trip)
        var states = setOf((map[0].indexOf('.') to 0) to 0)

        while ((states.filter { it.second == 2 }.maxOfOrNull { it.first.second } ?: 0) < Y - 1) {
            var newBlizzards = moveBlizzards(blizzards)
            val blizzardSet = newBlizzards.map { it.first to it.second }.toSet()
            var newStates = mutableSetOf<Pair<Pair<Int, Int>, Int>>()

            fun Pair<Pair<Int, Int>, Int>.newState(pos: Pair<Int, Int>): Pair<Pair<Int, Int>, Int> {
                return if (second == 0 && pos.second == Y - 1) {
                    pos to 1
                } else if (second == 1 && pos.second == 0) {
                    pos to 2
                } else pos to second
            }

            for (state in states) {
                val pos = state.first
                for (dir in dirs) {
                    val newPos = pos.first + dir.first to pos.second + dir.second
                    if (newPos !in blizzardSet && newPos.first in 0 until X && newPos.second in 0 until Y && map[newPos.second][newPos.first] != '#') {
                        newStates.add(state.newState(newPos))
                    }
                }
            }
            states = newStates

            blizzards = newBlizzards
            t++
        }
        return t
    }
}


fun main() {
    val testInstance = Day24(true)
    val instance = Day24(false)

    testInstance.part1().let { check(it == EXPECTED_1) { "part1: $it != $EXPECTED_1" } }
    println("part1 ANSWER: ${instance.part1()}")
    testInstance.part2().let {
        check(it == EXPECTED_2) { "part2: $it != $EXPECTED_2" }
        println("part2 ANSWER: ${instance.part2()}")
    }
}
