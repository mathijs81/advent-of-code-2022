private const val EXPECTED_1 = 21L
private const val EXPECTED_2 = 525152L

private class Day12(isTest: Boolean) : Solver(isTest) {
    val resultMap = mutableMapOf<Pair<Int, Int>, Long>()

    fun arrangements(strLeft: String, groupsLeft: List<Int>): Long {
        val damageCount = groupsLeft.sum()

        val minDamage = strLeft.count { it == '#' }
        val maxDamage = strLeft.count { it != '.' }

        if (damageCount == 0 && minDamage == 0) {
            return 1
        }
        if (minDamage > damageCount || damageCount > maxDamage) {
            return 0
        }

        if (strLeft[0] == '?') {
            val key = strLeft.length to groupsLeft.size
            resultMap[key]?.let { return it }

            var sum = 0L
            if (strLeft.substring(0, groupsLeft[0]).all { it != '.' } && strLeft[groupsLeft[0]] != '#') {
                sum += arrangements(strLeft.substring(groupsLeft[0] + 1), groupsLeft.drop(1))
            }
            sum += arrangements(strLeft.substring(1), groupsLeft)
            return sum.also { resultMap[key] = it }
        } else {
            if (strLeft[0] == '#') {
                if (strLeft.substring(0, groupsLeft[0]).all { it == '#' || it == '?' } && strLeft[groupsLeft[0]] != '#') {
                    return arrangements(strLeft.substring(groupsLeft[0] + 1), groupsLeft.drop(1))
                } else return 0
            } else {
                return arrangements(strLeft.substring(1), groupsLeft)
            }
        }
    }

    fun part1() = readAsLines().sumOf { line ->
        resultMap.clear()
        val parts = line.split(" ")
        val nums = parts[1].splitInts()

        arrangements(parts[0] + ".", nums)
    }

    fun part2() = readAsLines().sumOf { line ->
        resultMap.clear()
        val parts = line.split(" ")
        val nums = parts[1].splitInts()

        val newNums = mutableListOf<Int>()
        var newStr = ""
        repeat(5) {
            newNums.addAll(nums)
            newStr += parts[0]
            if (it != 4) {
                newStr += "?"
            }
        }

        arrangements(newStr + ".", newNums)
    }
}


fun main() {
    val testInstance = Day12(true)
    val instance = Day12(false)

    testInstance.part1().let { check(it == EXPECTED_1) { "part1: $it != $EXPECTED_1" } }
    println("part1 ANSWER: ${instance.part1()}")
    testInstance.part2().let {
        check(it == EXPECTED_2) { "part2: $it != $EXPECTED_2" }
        println("part2 ANSWER: ${instance.part2()}")
    }
}
