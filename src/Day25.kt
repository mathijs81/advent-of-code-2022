private const val EXPECTED_1 = 54
private const val EXPECTED_2 = 0

private class Day25(isTest: Boolean) : Solver(isTest) {
    fun flowCount(conn: MutableMap<String, MutableSet<String>>, src: String, dest: String): Pair<Int, List<Pair<String, String>>?> {
        val cap = mutableMapOf<Pair<String, String>, Int>()

        for ((s, dests) in conn.entries) {
            for(d in dests) {
                cap[s to d] = 1
            }
        }

        var n = 0

        val seen = mutableSetOf<String>()
        fun augmentFlow(p: String): List<Pair<String, String>>? {
            if (p == dest) {
                return listOf()
            }
            seen.add(p)
            for (d in conn[p]!!) {
                if (d in seen) {
                    continue
                }
                val e = p to d
                if (cap[e]!! > 0) {
                    cap.merge(e, -1) { a,b -> a+b}
                    cap.merge(d to p, 1) { a, b -> a + b}
                    augmentFlow(d)?.let {
                        return it.toMutableList().apply { add(0, p to d) }
                    }
                    cap.merge(e, 1) { a,b -> a+b}
                    cap.merge(d to p, -1) { a, b -> a + b}
                }
            }
            return null
        }

        var lastpath: List<Pair<String, String>>? = null
        while(true) {
            seen.clear()
            lastpath = augmentFlow(src) ?: break
            n++

        }

        return n to lastpath
    }


    fun removeEdge(conn: MutableMap<String, MutableSet<String>>, n: Int) {
        val target = 3 - n - 1
        println("Removing edge from ${conn.size} to reach target $target")
        while(true) {
            val a = (0..2).map { conn.keys.shuffled().first() }
            val b = (0..2).map { conn.keys.shuffled().first() }

            if ((0..2).any {a[it] == b[it] }) {
                continue
            }

            fun score(): Pair<Int, List<Pair<String, String>>?> {
                val r = (0..2).map { flowCount(conn, a[it], b[it]) }
                return if (r.map { it.first }.distinct().size == 1) {
                    r.first()
                } else {
                    -1 to null
                }
            }

            val (count, path) = score()
            if (count != target + 1) {
                continue
            }

            for (edge in path!!) {
                conn[edge.first]!!.remove(edge.second)
                conn[edge.second]!!.remove(edge.first)
                if (score().first == target) {
                    println("removed $edge, which reduces flow from $a to $b")
                    return
                } else {
                    //println("Couldn't remove $edge")
                }
                conn[edge.first]!!.add(edge.second)
                conn[edge.second]!!.add(edge.first)
            }
        }
    }

    fun part1(): Any {
        val conn = readAsLines().map {
            val parts = it.split(": ")
            parts[0] to parts[1].split(" ").toMutableSet()
        }.toMap().toMutableMap()

        for ((a,b) in conn.entries.toList()) {
            for(d in b) {
                conn.getOrPut(d) { mutableSetOf() }.add(a)
            }
        }

        repeat(3) {
            removeEdge(conn, it)
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
