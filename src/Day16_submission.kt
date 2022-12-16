/**
 * Pretty terrible code hacked together during submission.
 * I missed that lots of valves are not useful to open (flow = 0) and tried a walking-time solution
 * optimized to limit to the 500K top states at every minute
 */

private const val EXPECTED_1 = 1651
private const val EXPECTED_2 = 1707

private val edges = mutableMapOf<String, List<String>>()
private val flow = mutableMapOf<String, Int>()
private val num = mutableMapOf<String, Int>()

class Day16_submission(isTest: Boolean) : Solver(isTest) {
    data class State(val open: Long, val loc: String) {
        fun calcflow() =
            flow.entries.sumOf { if (open and (1L shl num[it.key]!!) != 0L ) flow[it.key]!! else 0 }
    }

    data class State2(val open: Long, val loc: String, val loc2: String) {
        fun calcflow() =
                flow.entries.sumOf { if (open and (1L shl num[it.key]!!) != 0L ) flow[it.key]!! else 0 }
    }

    fun part1(): Any {
        var index = 0
        edges.clear()
        flow.clear()
        num.clear()
        readAsLines().forEach{  line ->
            val parts = line.split(" ")
            val key = parts[1]
            flow[key] = parts[4].removeSurrounding("rate=", ";").toInt()
            val parts2 = line.split(", ")
            edges[key] = parts2.map { it.substringAfterLast(' ', it) }
            num[key] = index++
        }
        var states = mapOf(State(0, "AA") to 0)

        repeat(30) {
            var newStates = mutableMapOf<State, Int>()
            for ((state, cflow) in states.entries) {
                val locNum = num[state.loc]!!
                val newflow = cflow + state.calcflow()
                if ((state.open and (1L shl locNum)) == 0L) {
                    newStates.compute(state.copy(open = state.open or (1L shl locNum))) {
                        key, prev ->
                            if (prev != null) maxOf(prev, newflow) else newflow
                    }
                }
                for (dest in edges[state.loc]!!) {
                    newStates.compute(state.copy(loc = dest)) {
                        key, prev ->
                        if (prev != null) maxOf(prev, newflow) else newflow
                    }
                }
            }
            states = newStates.entries.sortedByDescending { it.value }.take(500_000).associate { it.key to it.value }
            //println("${states.size} ${states.entries.maxOf { it.value }}")
        }

        return states.maxOf { state -> state.value  }
    }

    fun part2(): Any {
        var index = 0
        edges.clear()
        flow.clear()
        num.clear()
        readAsLines().forEach{  line ->
            val parts = line.split(" ")
            val key = parts[1]
            flow[key] = parts[4].removeSurrounding("rate=", ";").toInt()
            val parts2 = line.split(", ")
            edges[key] = parts2.map { it.substringAfterLast(' ', it) }
            num[key] = index++
        }
        var states = mapOf(State2(0, "AA", "AA") to 0)

        repeat(26) {
            var newStates = mutableMapOf<State2, Int>()
            for ((state, cflow) in states.entries) {
                val locNum = num[state.loc]!!
                val locNum2 = num[state.loc2]!!
                val newflow = cflow + state.calcflow()
                //if (newflow < currentMin) continue
                val newstate1 = mutableSetOf<State2>()
                if ((state.open and (1L shl locNum)) == 0L) {
                    newstate1.add(state.copy(open = state.open or (1L shl locNum)))
                }
                for (dest in edges[state.loc]!!) {
                    newstate1.add(state.copy(loc = dest))
                }
                //println("new1 ${newstate1.size}")
                val newstate2 = mutableSetOf<State2>()
                for (state in newstate1) {
                    if ((state.open and (1L shl locNum2)) == 0L) {
                        newstate2.add(state.copy(open = state.open or (1L shl locNum2)))
                    }
                    for (dest in edges[state.loc2]!!) {
                        newstate2.add(state.copy(loc2 = dest))
                    }
                }

                for (state in newstate2) {
                    if (state.loc > state.loc2) {
                        newStates.compute(state.copy(loc=state.loc2, loc2=state.loc)) {
                            key, prev ->
                            if (prev != null) maxOf(prev, newflow) else newflow
                        }
                    } else
                    newStates.compute(state) {
                        key, prev ->
                        if (prev != null) maxOf(prev, newflow) else newflow
                    }
                }

            }
            states = newStates.entries.sortedByDescending { it.value }.take(500_000).associate { it.key to it.value }
            println("${states.size} ${states.entries.maxOf { it.value }}")
        }

        return states.maxOf { state -> state.value  }
    }
}


fun main() {
    val testInstance = Day16_submission(true)
    val instance = Day16_submission(false)

    testInstance.part1().let { check(it == EXPECTED_1) { "part1: $it != $EXPECTED_1" } }
    println("part1 ANSWER: ${instance.part1()}")
    testInstance.part2().let {
        check(it == EXPECTED_2) { "part2: $it != $EXPECTED_2" }
        println("part2 ANSWER: ${instance.part2()}")
    }
}
