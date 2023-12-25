private const val EXPECTED_1 = 54
private const val EXPECTED_2 = 0

private class Day25(isTest: Boolean) : Solver(isTest) {
    fun flowCount(
        conn: MutableMap<String, MutableSet<String>>,
        src: String,
        dest: String
    ): Pair<Int, List<Pair<String, String>>?> {
        val allEdges = conn.flatMap { (src, dests) -> dests.map { src to it } }
        val cap = allEdges.associateWith { 1 }.toMutableMap()

        var n = 0

        val seen = mutableSetOf<String>()
        fun augmentFlow(p: String): Boolean {
            if (p == dest) {
                return true
            }
            seen.add(p)
            for (d in conn[p]!!) {
                if (d in seen) {
                    continue
                }
                val e = p to d
                if (cap[e]!! > 0) {
                    cap.merge(e, -1) { a, b -> a + b }
                    cap.merge(d to p, 1) { a, b -> a + b }
                    if (augmentFlow(d)) {
                        return true
                    }
                    cap.merge(e, 1) { a, b -> a + b }
                    cap.merge(d to p, -1) { a, b -> a + b }
                }
            }
            return false
        }

        while (augmentFlow(src)) {
            seen.clear()
            n++
        }

        // Return fully saturated edges
        return n to allEdges.filter { cap[it] == 0 }
    }

    fun minCutEdges(conn: MutableMap<String, MutableSet<String>>): List<Pair<String, String>> {
        val nodes = conn.keys.toList()
        for (i in nodes.indices) {
            for (j in i + 1..<nodes.size) {
                val (cut, edges) = flowCount(conn, nodes[i], nodes[j])
                if (cut == 3) {
                    println("Cut between ${nodes[i]} and ${nodes[j]} -- $edges")
                    return edges!!
                }
            }
        }
        error("no min cut 3")
    }

    fun part1(): Any {
        val conn = readAsLines().map {
            val parts = it.split(": ")
            parts[0] to parts[1].split(" ").toMutableSet()
        }.toMap().toMutableMap()

        for ((a, b) in conn.entries.toList()) {
            for (d in b) {
                conn.getOrPut(d) { mutableSetOf() }.add(a)
            }
        }

        for ((src, dest) in minCutEdges(conn)) {
            conn[src]!!.remove(dest)
        }

        val seen = mutableSetOf<String>()
        fun visit(n: String) {
            for (d in conn[n]!!) {
                if (d !in seen) {
                    seen.add(d)
                    visit(d)
                }
            }
        }
        visit(conn.keys.first())
        return (conn.size - seen.size) * seen.size
    }

    fun part2(): Any {
        return 0
    }
}


fun main() {
    val testInstance = Day25(true)
    val instance = Day25(false)

    testInstance.part1().let { check(it == EXPECTED_1) { "part1: $it != $EXPECTED_1" } }
    println("part1 ANSWER: ${instance.part1()}")
    testInstance.part2().let {
        check(it == EXPECTED_2) { "part2: $it != $EXPECTED_2" }
        println("part2 ANSWER: ${instance.part2()}")
    }
}
