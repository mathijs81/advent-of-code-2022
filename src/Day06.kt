private const val EXPECTED_1 = 288L
private const val EXPECTED_2 = 71503L

private class Day06(isTest: Boolean) : Solver(isTest) {
    fun part1(): Any {
        val lines = readAsLines()
        val times = lines[0].splitInts()
        val distance = lines[1].splitInts()
        var sum = 1L
        for (i in 0 until times.size) {
            val time = times[i]
            val dist = distance[i]
            var t = 0
            for (press in 0..time) {
                val speed = press
                val mydist = speed * (time - press)
                if (mydist > dist)
                    t++

            }
            sum *= t
        }
        return sum
    }

    fun part2(): Any {
        val lines = readAsLines().map { it.filter { it.isDigit() }.toLong() }
        val time = lines[0]
        val dist = lines[1]
        println("$time $dist")
        var t = 0L
        for (press in 0..time) {
            val speed = press
            val mydist = speed * (time - press)
            if (mydist > dist)
                t++
        }
        return t
    }
}


fun main() {
    val testInstance = Day06(true)
    val instance = Day06(false)

    testInstance.part1().let { check(it == EXPECTED_1) { "part1: $it != $EXPECTED_1" } }
    println("part1 ANSWER: ${instance.part1()}")
    testInstance.part2().let {
        check(it == EXPECTED_2) { "part2: $it != $EXPECTED_2" }
        println("part2 ANSWER: ${instance.part2()}")
    }
}
