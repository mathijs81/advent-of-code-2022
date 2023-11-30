package aoc2022
fun main() {
    fun part1(input: List<String>): Int {
        var sum = 0
        var max = 0
        for (line in input) {
            if (line.trim().isEmpty()) {
                max = max.coerceAtLeast(sum)
                sum = 0
            } else {
                sum += line.toInt()
            }
        }
        max = max.coerceAtLeast(sum)
        return max
    }

    fun part2(input: List<String>): Int {
        val list = mutableListOf<Int>()
        var sum = 0
        for (line in input) {
            if (line.trim().isEmpty()) {
                list.add(sum)
                sum = 0
            } else {
                sum += line.toInt()
            }
        }
        list.add(sum)

        list.sortWith(Comparator.naturalOrder<Int>().reversed())
        return list.subList(0, 3).sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readAsLines("Day01_test")
    check(part1(testInput) == 24000)
    check(part2(testInput) == 45000)

    val input = readAsLines("Day01")
    println(part1(input))
    println(part2(input))
}
