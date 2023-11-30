private const val EXPECTED_1 = 33
private const val EXPECTED_2 = 62 * 56

private class Day19(isTest: Boolean) : Solver(isTest) {
    // State: 8 ints
    // 0,1,2,3 = produced good
    // 4,5,6,7 = robots
    private fun MutableList<Int>.produceGoods() {
        (0..3).forEach { this[it] += this[it + 4] }
    }

    fun reduceSpace(states: Set<List<Int>>): Set<List<Int>> {
        //My submission had this criterion, which requires a bit of a wait for the program to finish:
        // return states.sortedByDescending { it[3] * 10000 + it[7] * 1000 + it[6] * 100 + it.sum() }.take(100_000).toSet()

        var result = mutableSetOf<List<Int>>()

        // Ideally we'd like to keep the convex hull of the 8D vector, but I don't know a
        // short solution to that
        // Alternative: just take the max 500 points in each single dimension, it makes
        // sense that at different stages of the puzzle, one metric is the one to maximize
        // (ore robots in the beginning, geode robots and/or geode count in the end)
        for (sortIndex in 0..7) {
            result.addAll(states.sortedByDescending { it[sortIndex] * 10000 + it[3] + it[7] }.take(200))

            // If we don't break ties on the geode + robots, then we have to take some more items:
            // result.addAll(states.sortedByDescending { it[sortIndex] }.take(1000))
        }

        // Alternatively, we could remove states that are useless (e.g. creating more ore every
        // timestep than needed to manufacture any of the robots)

        //println("Reduced from ${states.size} to ${result.size}")
        return result
    }

    fun addStates(states: MutableSet<List<Int>>, state: List<Int>, bp: List<Int>) {
        if (state[0] >= bp[1]) {
            states.add(state.toMutableList().apply {
                this[0] -= bp[1]
                produceGoods()
                this[4]++
            })
        }
        if (state[0] >= bp[2]) {
            states.add(state.toMutableList().apply {
                this[0] -= bp[2]
                produceGoods()
                this[5]++
            })
        }
        if (state[0] >= bp[3] && state[1] >= bp[4]) {
            states.add(state.toMutableList().apply {
                this[0] -= bp[3]
                this[1] -= bp[4]
                produceGoods()
                this[6]++
            })
        }
        if (state[0] >= bp[5] && state[2] >= bp[6]) {
            states.add(state.toMutableList().apply {
                this[0] -= bp[5]
                this[2] -= bp[6]
                produceGoods()
                this[7]++
            })
        }
        state.toMutableList().let {
            it.produceGoods()
            states.add(it)
        }
    }

    fun quality(bp: List<Int>, num: Int): Int {
        var states = setOf(listOf(0, 0, 0, 0, 1, 0, 0, 0))
        for (t in 1..num) {
            val newStates = mutableSetOf<List<Int>>().also {
                for (state in states) {
                    addStates(it, state, bp)
                }
            }
            //println("$t ${newstates.size}")
            states = reduceSpace(newStates)
        }
        return states.maxOf { it[3] }
    }

    val bluePrints = readAsLines().map {
        it.splitInts() as List<Int>
    }

    fun part1() = bluePrints.sumOf { it[0] * quality(it, 24) }
    fun part2() = bluePrints.take(3).map { quality(it, 32) }.reduce { a, b -> a * b }
}


fun main() {
    val testInstance = Day19(true)
    val instance = Day19(false)

    testInstance.part1().let { check(it == EXPECTED_1) { "part1: $it != $EXPECTED_1" } }
    println("part1 ANSWER: ${instance.part1()}")
    testInstance.part2().let {
        check(it == EXPECTED_2) { "part2: $it != $EXPECTED_2" }
        println("part2 ANSWER: ${instance.part2()}")
    }
}
