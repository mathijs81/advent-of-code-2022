private const val EXPECTED_1 = 1320
private const val EXPECTED_2 = 145

private class Day15(isTest: Boolean) : Solver(isTest) {
    fun String.hash(): Int {
        var v = 0
        for (ch in this) {
            v += ch.code
            v = (v * 17) % 256
        }
        return v
    }

    fun part1(): Any {
        return readAsString().split(",").sumOf { it.hash() }
    }

    fun part2(): Any {
        val boxes = mutableMapOf<Int, MutableList<Pair<String, Int>>>()
        readAsString().split(",").forEach { cmd ->
            if (cmd.endsWith("-")) {
                val lensName = cmd.removeSuffix("-")
                boxes[lensName.hash()]?.removeIf { it.first == lensName }
            } else {
                val lens = cmd.split("=").let { it[0] to it[1].toInt() }
                val list = boxes.getOrPut(lens.first.hash()) { mutableListOf() }
                val index = list.indexOfFirst { it.first == lens.first }
                if (index == -1) {
                    list.add(lens)
                } else {
                    list[index] = lens
                }
            }
        }

        var result = 0
        for ((index, list) in boxes.entries) {
            for ((lIndex, lens) in list.withIndex()) {
                result += (1 + index) * (1 + lIndex) * lens.second
            }
        }
        return result
    }
}


fun main() {
    val testInstance = Day15(true)
    val instance = Day15(false)

    testInstance.part1().let { check(it == EXPECTED_1) { "part1: $it != $EXPECTED_1" } }
    println("part1 ANSWER: ${instance.part1()}")
    testInstance.part2().let {
        check(it == EXPECTED_2) { "part2: $it != $EXPECTED_2" }
        println("part2 ANSWER: ${instance.part2()}")
    }
}
