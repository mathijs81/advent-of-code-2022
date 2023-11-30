private const val EXPECTED_1 = 4890L
private const val EXPECTED_2 = 0

val ch = mapOf('=' to -2, '-' to -1, '0' to 0, '1' to 1, '2' to 2)

fun decode(p: String): Long {
    var value = 0L
    var multiplier = 1L
    var index = 0
    while (index < p.length) {
        value += multiplier * ch[p[p.length -1 - index]]!!
        multiplier = Math.multiplyExact(5, multiplier)
        index++
    }
    return value
}

val ch2 = listOf('0', '1', '2', '=', '-')
val adjust = listOf(0, 1, 2, -2, -1)

fun encode(l: Long): String {
    if (l == 0L) return ""
    val rem = l.mod(5)
    val newVal = (l - adjust[rem]) / 5
    return encode(newVal ) + ch2[rem]
}

private class Day25(isTest: Boolean) : Solver(isTest) {
    fun part1(): Any {
        val sum = readAsLines().map { decode(it) }.sum()
        println(encode(sum))
        return sum
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
