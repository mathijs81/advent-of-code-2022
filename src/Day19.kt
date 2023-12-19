import kotlin.math.max
import kotlin.math.min

private const val EXPECTED_1 = 19114
private const val EXPECTED_2 = 167409079868000L

typealias XmasRange = List<Pair<Int, Int>>

private class Day19(isTest: Boolean) : Solver(isTest) {
    val desc: String
    val parts: String
    val indexStr = "xmas"

    init {
        readAsString().split("\n\n").let { (desc, parts) ->
            this.desc = desc
            this.parts = parts
        }
    }
    val rules = desc.split("\n").map {
        val (name, rest) = it.split("{")
        val orders = rest.removeSuffix("}").split(",")
        name to orders
    }.toMap()

    fun part1(): Any {
        fun List<Int>.match(s: String): Boolean {
            val instr = s.split(":")[0]
            val comp = instr.substring(2).toInt()

            val value = get(indexStr.indexOf(instr[0]))
            when (instr[1]) {
                '>' -> return value > comp
                '<' -> return value < comp
                else -> error("instr " + s)
            }
        }

        return parts.split("\n").sumOf { part ->
            val res = part.splitInts()

            var ruleName = "in"

            w@ while (ruleName != "A" && ruleName != "R") {
                val orders = rules[ruleName]!!
                for (i in 0..<orders.size - 1) {
                    if (res.match(orders[i])) {
                        ruleName = orders[i].substringAfter(":")
                        continue@w
                    }
                }
                ruleName = orders.last()
            }

            if (ruleName == "A")
                res.sum()
            else 0
        }
    }

    fun part2(): Any {

        fun XmasRange.count(): Long {
            var ans = 1L
            for (p in this) {
                ans *= if (p.second < p.first) 0 else (p.second - p.first)
            }
            return ans
        }
        fun XmasRange.empty(): Boolean = count() == 0L

        fun XmasRange.match(s: String): Pair<XmasRange, XmasRange> {
            val instr = s.split(":")[0]
            val comp = instr.substring(2).toInt()

            val ind = indexStr.indexOf(instr[0])
            val value = get(ind)
            when (instr[1]) {
                '>' -> {
                    return toMutableList().apply {
                        set(ind, max(value.first, comp + 1) to value.second)
                    } to toMutableList().apply {
                        set(ind, value.first to min(comp + 1, value.second))
                    }
                }

                '<' -> {
                    return toMutableList().apply {
                        set(ind, value.first to min(value.second, comp))
                    } to toMutableList().apply {
                        set(ind, max(comp, value.first) to value.second)
                    }
                }

                else -> error("instr " + s)
            }
        }

        var rangeSum = 0L

        fun process(range: List<Pair<Int, Int>>, ruleName: String) {
            if (range.empty() || ruleName == "R") {
                return
            }
            if (ruleName == "A") {
                rangeSum += range.count()
                return
            }

            val orders = rules[ruleName]!!

            var rangeLeft = range
            for (i in 0 ..< orders.size - 1) {
                val (match, noMatch) = rangeLeft.match(orders[i])
                if (!match.empty()) {
                    process(match, orders[i].substringAfter(":"))
                }
                rangeLeft = noMatch
            }
            process(rangeLeft,orders.last())
        }
        process(listOf(1 to 4001, 1 to 4001, 1 to 4001, 1 to 4001), "in")
        return rangeSum
    }
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
