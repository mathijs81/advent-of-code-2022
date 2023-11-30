import java.util.*
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

private const val EXPECTED_1 = 1651
private const val EXPECTED_2 = 1707

/**
 * Reworked solution: DP by treating 1 minute at a time (ignoring useless valves) to find the
 * highest value path through the state space for part1.
 *
 * The result of a single 26-round evaluation can be used for part2. Each valve gets opened by
 * one of the two agents (or neither) and then the result of the longest path can just
 * be looked up.
 */
private class Day16_alt(isTest: Boolean) : Solver(isTest) {
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

    data class State(val open: Long, val loc: String)

    fun dp(start: State, rounds: Int): Map<State, Int> {
        var states = mapOf(start to 0)
        repeat(rounds) { t ->
            val newStates = mutableMapOf<State, Int>()
            for ((state, score) in states.entries) {
                val locNum = num[state.loc]!!

                if (flowList[locNum] > 0 && (state.open and (1L shl locNum)) == 0L) {
                    val newScore = score + (rounds - t - 1) * flowList[locNum];
                    val newState = state.copy(open = state.open or (1L shl locNum))
                    newStates.compute(newState) { _, prev -> maxOf(newScore, prev ?: 0) }
                }
                for (dest in edges[state.loc]!!) {
                    val newState = state.copy(loc = dest)
                    newStates.compute(newState) { _, prev -> maxOf(score, prev ?: 0) }
                }
            }
            states = newStates
        }
        return states
    }

    fun part1() = dp(State(0, "AA"), 30).maxOf { it.value }

    fun partition(startIndex: Int, assignment1: Long, assignment2: Long, openToScore: Map<Long, Int>): Int {
        var index = startIndex
        while (index < flowList.size && flowList[index] == 0) index++
        return if (index < flowList.size) {
            // This valve gets opened either by user1, by user2 or by neither
            maxOf(
                    partition(index + 1, assignment1 or (1L shl index), assignment2, openToScore),
                    partition(index + 1, assignment1, assignment2 or (1L shl index), openToScore),
                    partition(index + 1, assignment1, assignment2, openToScore)
            )
        } else {
            // Evaluate: just look up what score we can get by opening the valves as assigned
            (openToScore[assignment1] ?: 0) + (openToScore[assignment2] ?: 0)
        }
    }

    fun part2(): Any {
        val bestMap = dp(State(0, "AA"), 26).entries.groupBy { it.key.open }.
            mapValues { (_, entryList) -> entryList.maxOf { it.value }}
        return partition(0, 0L, 0L, bestMap)
    }
}

@OptIn(ExperimentalTime::class)
fun main() {
    val testInstance = Day16_alt(true)
    val instance = Day16_alt(false)

    println(measureTime {
        testInstance.part1().let { check(it == EXPECTED_1) { "part1: $it != $EXPECTED_1" } }
        println("part1 ANSWER: ${instance.part1()}")
    })
    println(measureTime {
        testInstance.part2().let {
            check(it == EXPECTED_2) { "part2: $it != $EXPECTED_2" }
            println("part2 ANSWER: ${instance.part2()}")
        }
    })
}
