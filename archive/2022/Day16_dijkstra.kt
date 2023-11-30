import java.util.*
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

private const val EXPECTED_1 = 1651
private const val EXPECTED_2 = 1707

/**
 * Reworked solution: use Dijkstra's to find the highest value path through the state space
 * for part1.
 *
 * Actually a bit slower than the dp solution.
 */

private class Day16(isTest: Boolean) : Solver(isTest) {
    private val edges = mutableMapOf<String, List<String>>()
    private val num = mutableMapOf<String, Int>()
    private val flowList = mutableListOf<Int>()

    init {
        readAsLines().withIndex().forEach { (index, line) ->
            val parts = line.split(" ")
            val key = parts[1]
            flowList.add(parts[4].removeSurrounding("rate=", ";").toInt())
            val parts2 = line.split(", ")
            edges[key] = parts2.map { it.substringAfterLast(' ', it) }
            num[key] = index
        }
    }

    data class State(val open: Long, val loc: String, val timeLeft: Int)

    // Return the best score from start, and the map of end states
    fun dijkstra(start: State): Pair<Int, Map<State, Int>> {
        val best = mutableMapOf<State, Int>()
        val queue = TreeSet(compareBy<State>({ best[it] }, { it.timeLeft }, { it.loc }, { it.open }).reversed())
        var result = 0
        best[start] = 0
        queue.add(start)
        while (!queue.isEmpty()) {
            val state = queue.pollFirst()!!
            val score = best[state]!!
            result = maxOf(result, score)

            if (state.timeLeft == 0) {
                // Special case: store the best score at any location in state 'any' so we
                // can look it up fast after partitioning
                best.compute(state.copy(loc = "any")) { key, prev ->
                    maxOf(score, prev ?: 0)
                }
                continue
            }
            val locNum = num[state.loc]!!

            if (flowList[locNum] > 0 && (state.open and (1L shl locNum)) == 0L) {
                val newScore = score + (state.timeLeft - 1) * flowList[locNum];
                val newState = state.copy(open = state.open or (1L shl locNum), timeLeft = state.timeLeft - 1)
                if (newScore > (best[newState] ?: -1)) {
                    queue.remove(newState)
                    best[newState] = newScore
                    queue.add(newState)
                }
            }

            for (dest in edges[state.loc]!!) {
                val newState = state.copy(loc = dest, timeLeft = state.timeLeft - 1)
                if (score > (best[newState] ?: -1)) {
                    queue.remove(newState)
                    best[newState] = score
                    queue.add(newState)
                }
            }
        }
        return result to best
    }

    fun part1() = dijkstra(State(0, "AA", 30)).first

    fun partition(startIndex: Int, assignment1: Long, assignment2: Long, best: Map<State, Int>): Int {
        var index = startIndex
        while (index < flowList.size && flowList[index] == 0) index++
        return if (index < flowList.size) {
            // This valve gets opened either by user1, by user2 or by neither
            maxOf(
                    partition(index + 1, assignment1 or (1L shl index), assignment2, best),
                    partition(index + 1, assignment1, assignment2 or (1L shl index), best),
                    partition(index + 1, assignment1, assignment2, best)
            )
        } else {
            // Evaluate: just look up what score we can get by opening the valves as assigned
            (best[State(assignment1, "any", 0)] ?: 0) + (best[State(assignment2, "any", 0)]
                    ?: 0)
        }
    }

    fun part2(): Any {
        val bestMap = dijkstra(State(0, "AA", 26)).second
        return partition(0, 0L, 0L, bestMap)
    }
}

fun main() {
    val testInstance = Day16(true)
    val instance = Day16(false)

    testInstance.part1().let { check(it == EXPECTED_1) { "part1: $it != $EXPECTED_1" } }
    println("part1 ANSWER: ${instance.part1()}")
    testInstance.part2().let {
        check(it == EXPECTED_2) { "part2: $it != $EXPECTED_2" }
        println("part2 ANSWER: ${instance.part2()}")
    }
}
