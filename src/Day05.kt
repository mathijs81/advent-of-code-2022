private const val EXPECTED_1 = "CMZ"
private const val EXPECTED_2 = "MCD"

private class Day05(isTest: Boolean) : Solver(isTest) {
    fun part1(): Any {
        val data = readAsString()
        val parts = data.split("\n\n")
        val stacks = readInitialState(parts[0])

        applyCommands(stacks, parts[1], true)
        return stacks.map { it.last() }.joinToString("")
    }

    fun part2(): Any {
        val data = readAsString()
        val parts = data.split("\n\n")
        val stacks = readInitialState(parts[0])

        applyCommands(stacks, parts[1], false)
        return stacks.map { it.last() }.joinToString("")
    }

    private fun readInitialState(initialState: String): MutableList<MutableList<Char>> {
        val stacks = mutableListOf<MutableList<Char>>()

        val lines = initialState.split("\n")
        val N = (lines[0].length + 3) / 4
        for (i in 0 until N) {
            stacks.add(mutableListOf<Char>())
        }

        for (line in lines.subList(0, lines.size - 1)) {
            for (i in 0 until N) {
                if (line[i * 4 + 1] != ' ') {
                    stacks[i].add(line[i * 4 + 1])
                }
            }
        }
        stacks.forEach { it.reverse() }
        return stacks
    }

    private fun applyCommands(stacks: MutableList<MutableList<Char>>, commands: String, shouldReverse: Boolean) {
        for (line in commands.split('\n')) {
            if (line.trim().isEmpty()) {
                continue
            }
            val parts = line.split(" ")
            val count = parts[1].toInt()
            val from = stacks[parts[3].toInt() - 1]
            val to = stacks[parts[5].toInt() - 1]

            to.addAll(from.subList(from.size - count, from.size).let { if(shouldReverse) it.reversed() else it })
            from.subList(from.size - count, from.size).clear()
        }
    }
}

fun main() {
    val testInstance = Day05(true)
    val instance = Day05(false)

    testInstance.part1().let { check(it == EXPECTED_1) { "part1: $it != $EXPECTED_1" } }
    println(instance.part1())
    testInstance.part2().let { check(it == EXPECTED_2) { "part2: $it != $EXPECTED_2" } }
    println(instance.part2())
}
