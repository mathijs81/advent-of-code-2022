private const val EXPECTED_1 = 6440
private const val EXPECTED_2 = 5905

private class Day07(isTest: Boolean) : Solver(isTest) {
    var withJokers = false

    val order
        get() = if(withJokers) listOf("A", "K", "Q", "T", "9", "8", "7", "6", "5", "4", "3", "2", "J")
        else listOf("A", "K", "Q", "J", "T", "9", "8", "7", "6", "5", "4", "3", "2")

    inner class Hand(val cards: String) {
        fun typeScore(): Int {
            val same = if (withJokers) {
                val jokerCount = cards.count { it == 'J' }
                val same = cards.filter { it != 'J' }.groupBy { it }.map { it.value.size }
                    .sortedDescending().toMutableList()
                if (same.size == 0) {
                    listOf(jokerCount)
                } else {
                    same.also { it[0] += jokerCount }
                }
            } else {
                cards.groupBy { it }.map { it.value.size }.sortedDescending()
            }
            return if (same.size == 1) {
                20
            } else if (same.size == 2) {
                if (same[0] == 4) {
                    19
                } else {
                    18
                }
            } else if (same.size == 3) {
                if (same[0] == 3) {
                    17 // 3 of a k
                } else {
                    16 // 2 pair
                }
            } else if (same.size ==4) {
                15 // pair
            } else {
                14 // high card
            }
        }

        val orderScore by lazy {
            var sum = 0
            for (ch in cards) {
                sum *= 20
                sum += 20 - order.indexOf(ch.toString())
            }
            sum + typeScore() * 5_000_000
        }
    }

    fun rankScore(): Int {
        val hands = readAsLines().map { it.split(" ").let { Hand(it[0]) to it[1].toInt()  } }
        val sorted = hands.sortedBy { it.first.orderScore }
        var sum = 0
        for ((index, hb) in sorted.withIndex()) {
            sum += hb.second * (index+1)
        }
        return sum
    }

    fun part1(): Any {
        withJokers = false
        return rankScore()
    }

    fun part2(): Any {
        withJokers = true
        return rankScore()
    }
}


fun main() {
    val testInstance = Day07(true)
    val instance = Day07(false)

    testInstance.part1().let { check(it == EXPECTED_1) { "part1: $it != $EXPECTED_1" } }
    println("part1 ANSWER: ${instance.part1()}")
    testInstance.part2().let {
        check(it == EXPECTED_2) { "part2: $it != $EXPECTED_2" }
        println("part2 ANSWER: ${instance.part2()}")
    }
}
