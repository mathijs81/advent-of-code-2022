private const val EXPECTED_1 = 110
private const val EXPECTED_2 = 20

private class Day23(isTest: Boolean) : Solver(isTest) {
    val dirs = listOf(0 to -1, 1 to -1, 1 to 0, 1 to 1, 0 to 1, -1 to 1, -1 to 0, -1 to -1)
    val startCoords = readAsLines().withIndex().flatMap { (y, line) ->
        line.withIndex().filter { it.value == '#' }.map { (x, value) -> x to y }
    }.toSet()

    fun surroundDirs(index: Int) = listOf(dirs[(index - 1).mod(dirs.size)], dirs[index], dirs[(index + 1).mod(dirs.size)])

    operator fun Pair<Int, Int>.plus(other: Pair<Int, Int>) = first + other.first to second + other.second

    fun draw(c: Set<Pair<Int, Int>>) {
        val field = Array<Array<Boolean>>(12) { Array<Boolean>(12) { false } }

        for ((x, y) in c) {
            field[y + 2][x + 2] = true
        }

        for (y in 0 until field.size) {
            println(field[y].joinToString("") { if (it) "#" else "." })
        }
        println()
    }

    fun part1(): Any {
        val dirIndex = mutableListOf(0, 4, 6, 2)
        var coords = startCoords
        //  draw(coords)
        repeat(10) {
            coords = simulate(coords, dirIndex)
            dirIndex.add(dirIndex.removeFirst())
        }
        return (coords.maxOf { it.first } - coords.minOf { it.first } + 1) *
                (coords.maxOf { it.second } - coords.minOf { it.second } + 1) - coords.size
    }

    private fun simulate(coords: Set<Pair<Int, Int>>, dirIndex: MutableList<Int>): Set<Pair<Int, Int>> {
        var proposedCoords = mutableMapOf<Pair<Int, Int>, Pair<Int, Int>>()
        for (elf in coords) {
            val allEmpty = dirs.all { (elf + it) !in coords }
            if (!allEmpty) {
                var ok = false
                for (index in dirIndex) {
                    if (surroundDirs(index).none { (elf + it) in coords }) {
                        proposedCoords[elf] = elf + dirs[index]
                        ok = true
                        break
                    }
                }
                if (!ok) {
                    proposedCoords[elf] = elf
                }
            } else {
                proposedCoords[elf] = elf
            }
        }

        val coordCount = proposedCoords.values.groupBy { it }
        return proposedCoords.map { (key, value) ->
            if (coordCount[value]!!.size == 1) {
                value
            } else key
        }.toSet()
    }

    fun part2(): Any {
        var dirIndex = mutableListOf(0, 4, 6, 2)

        var coords = startCoords
        repeat(Int.MAX_VALUE) { round ->
            coords = simulate(coords, dirIndex).also {
                if (it == coords) {
                    return round + 1
                }
            }
            dirIndex.add(dirIndex.removeFirst())
        }
        error("")
    }
}


fun main() {
    val testInstance = Day23(true)
    val instance = Day23(false)

    testInstance.part1().let { check(it == EXPECTED_1) { "part1: $it != $EXPECTED_1" } }
    println("part1 ANSWER: ${instance.part1()}")
    testInstance.part2().let {
        check(it == EXPECTED_2) { "part2: $it != $EXPECTED_2" }
        println("part2 ANSWER: ${instance.part2()}")
    }
}
