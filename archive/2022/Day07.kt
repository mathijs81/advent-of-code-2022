private const val EXPECTED_1 = 95437L
private const val EXPECTED_2 = 24933642L

private class Day07(isTest: Boolean) : Solver(isTest) {
    fun constructDirSizes(): Map<String, Long> {
        val currentDir = mutableListOf<String>()
        val sizeMap = mutableMapOf<String, Long>()
        readAsLines().forEach { line ->
            if (line.startsWith("$ cd ")) {
                when (line) {
                    "$ cd /" -> {
                        currentDir.clear()
                    }
                    "$ cd .." -> {
                        currentDir.removeLast()
                    }
                    else -> {
                        currentDir.add(line.removePrefix("$ cd "))
                    }
                }
            } else if (line[0].isDigit()) {
                val size = line.substringBefore(' ').toLong()
                val dirCopy = currentDir.toMutableList()
                while(true) {
                    val key = dirCopy.joinToString("/")
                    sizeMap[key] = size + (sizeMap[key] ?: 0L)
                    if (dirCopy.isEmpty()) {
                        break
                    }
                    dirCopy.removeLast()
                }
            }
        }
        return sizeMap
    }

    fun part1(): Any {
        return constructDirSizes().values.filter { it <= 100000 }.sum()
    }

    fun part2(): Any {
        val sizeMap = constructDirSizes()
        val sizeNeeded = 30000000 - (70000000 - sizeMap[""]!!)
        return sizeMap.values.filter { it >= sizeNeeded }.min()
    }
}


fun main() {
    val testInstance = Day07(true)
    val instance = Day07(false)

    testInstance.part1().let { check(it == EXPECTED_1) { "part1: $it != $EXPECTED_1" } }
    println(instance.part1())
    testInstance.part2().let { check(it == EXPECTED_2) { "part2: $it != $EXPECTED_2" } }
    println(instance.part2())
}
