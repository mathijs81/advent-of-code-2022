private const val EXPECTED_1 = 13
private const val EXPECTED_2 = 140

fun parse(data: String): Any {
    if (data[0] != '[') {
        return data.toInt()
    }
    var nesting = 0
    var lastIndex = 1
    return buildList {
        for ((i, ch) in data.withIndex()) {
            when (ch) {
                '[' -> nesting++
                ']' -> nesting--
                ',' -> if (nesting == 1) {
                    add(parse(data.substring(lastIndex, i)))
                    lastIndex = i + 1
                }
            }
        }
        check(nesting == 0)
        if (lastIndex < data.length - 1) {
            add(parse(data.substring(lastIndex, data.length - 1)))
        }
    }
}

tailrec fun compare(a: Any?, b: Any?): Int {
    if (a is Int && b is Int) {
        return a - b
    }
    if (a is List<*> && b is List<*>) {
        for (i in 0 until minOf(a.size, b.size)) {
            compare(a[i], b[i]).let { if (it != 0) return it }
        }
        return a.size - b.size
        // Functional with "firstNotNullOfOrNull" but that seems too contrived to me:
        //        return (0 until minOf(a.size, b.size)).firstNotNullOfOrNull { index ->
        //            compare(a[index], b[index]).let { if (it == 0) null else it }
        //        } ?: (a.size - b.size)
    }
    if (a is Int) {
        return compare(listOf(a), b)
    }
    if (b is Int) {
        return compare(a, listOf(b))
    }
    error("unexpected $a $b")
}

private class Day13(isTest: Boolean) : Solver(isTest) {
    fun part1(): Any {
        val parts = readAsString().split("\n\n").map { it.split("\n") }
        return parts.withIndex().sumOf { (index, lines) ->
            val (a, b) = lines.map(::parse)
            if (compare(a, b) < 0) index + 1 else 0
        }
    }

    fun part2(): Any {
        val a = listOf(listOf(2))
        val b = listOf(listOf(6))
        val all = (listOf(a, b) + readAsLines().filter { it.trim().isNotEmpty() }.map(::parse)).sortedWith(::compare)
        return (all.indexOf(a) + 1) * (all.indexOf(b) + 1)
    }
}


fun main() {
    val testInstance = Day13(true)
    val instance = Day13(false)

    testInstance.part1().let { check(it == EXPECTED_1) { "part1: $it != $EXPECTED_1" } }
    println("part1 ANSWER: ${instance.part1()}")
    testInstance.part2().let {
        check(it == EXPECTED_2) { "part2: $it != $EXPECTED_2" }
        println("part2 ANSWER: ${instance.part2()}")
    }
}
