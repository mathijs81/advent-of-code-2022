private const val EXPECTED_1 = 2
private const val EXPECTED_2 = 6L

private class Day08(isTest: Boolean) : Solver(isTest) {
    fun part1(): Any {
        val lines = readAsLines()
        val instructions = lines[0].trim()
        val edges = mutableMapOf<String, MutableList<String>>()
        for (edge in lines.drop(2)) {
            val parts = edge.replace("(", "").replace(")", "").split("=").flatMap { it.trim().split(",")}.map { it.trim() }
            check(parts.size == 3) { "Invalid edge: $edge, $parts" }
            edges.put(parts[0], mutableListOf(parts[1], parts[2]))
        }

        var step = 0
        var pos = "AAA"
        while (pos != "ZZZ") {
            println(pos)
            val next = instructions[step % instructions.length]
            if (next == 'L') {
                pos = edges[pos]!![0]
            } else {
                pos = edges[pos]!![1]
            }
            step++
        }

        return step
    }
    fun part2(): Any {
        val lines = readAsLines()
        val instructions = lines[0].trim()
        val edges = mutableMapOf<String, MutableList<String>>()
        for (edge in lines.drop(2)) {
            val parts = edge.replace("(", "").replace(")", "").split("=").flatMap { it.trim().split(",")}.map { it.trim() }
            check(parts.size == 3) { "Invalid edge: $edge, $parts" }
            edges.put(parts[0], mutableListOf(parts[1], parts[2]))
        }

        var result = 1L
        for (p in edges.keys.filter { it.endsWith("A") } ) {
            var pos = p
            var step = 0
            var instructIndex = 0
            val seen = mutableMapOf<Pair<String, Int>, Int>()

            var cycle = 0 to 0
            do {
                seen[pos to instructIndex] = step

                val next = instructions[instructIndex]
                pos = if (next == 'L') {
                    edges[pos]!![0]
                } else {
                    edges[pos]!![1]
                }
                step++
                instructIndex = step % instructions.length
                if (pos.endsWith("Z") && seen.containsKey(pos to instructIndex)) {
                    val firstStep = seen[pos to instructIndex]!!
                    cycle = firstStep to (step - firstStep)
                    break
                }
            } while (step < 5000000)
            println("$p $cycle $step")
            val mul = cycle.first
            result = result * mul / gcd(result, (mul).toLong())
        }
        return result

    }
}


fun main() {
    val testInstance = Day08(true)
    val instance = Day08(false)

//    testInstance.part1().let { check(it == EXPECTED_1) { "part1: $it != $EXPECTED_1" } }
//    println("part1 ANSWER: ${instance.part1()}")
    testInstance.part2().let {
        check(it == EXPECTED_2) { "part2: $it != $EXPECTED_2" }
        println("part2 ANSWER: ${instance.part2()}")
    }
}
