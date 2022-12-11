private const val EXPECTED_1 = 10605L
private const val EXPECTED_2 = 2713310158L

private class Monkey {
    val items = mutableListOf<Long>()
    var testDiv = 0L
    var trueMonk = 0
    var falseMonk = 0
    lateinit var operation: (Long) -> Long
    var inspectCount = 0L
}

private class Day11(isTest: Boolean) : Solver(isTest) {
    fun parseMonkeys(): List<Monkey> {
        val monkeys = mutableListOf<Monkey>()
        var currentMonkey = Monkey()
        readAsLines().map { it.trim() }.forEach { line ->
            if (line.startsWith("Monkey")) {
                currentMonkey = Monkey()
                monkeys.add(currentMonkey)
            } else if (line.startsWith("Starting")) {
                currentMonkey.items.addAll(line.substringAfter(":").split(",").map { it.trim().toLong() })
            } else if (line.startsWith("Operation")) {
                val expression = line.substringAfter("= ").split(" ")
                currentMonkey.operation = { old ->
                    val arg1 = if (expression[0] == "old") old else expression[0].toLong()
                    val arg2 = if (expression[2] == "old") old else expression[2].toLong()
                    if (expression[1] == "*") arg1 * arg2 else arg1 + arg2
                }
            } else if (line.startsWith("Test")) {
                currentMonkey.testDiv = line.substringAfter("by ").toLong()
            } else if (line.startsWith("If true")) {
                currentMonkey.trueMonk = line.substringAfter("monkey ").toInt()
            } else if (line.startsWith("If false")) {
                currentMonkey.falseMonk = line.substringAfter("monkey ").toInt()
            } else if(line.isNotEmpty()) error("unexpected input: $line")
        }
        return monkeys
    }

    fun simulate(monkeys: List<Monkey>, postOperation: (Long) -> Long) {
        for (monkey in monkeys) {
            val items = monkey.items.toList().also { monkey.inspectCount += it.size }
            monkey.items.clear()
            for (item in items) {
                val newItem = postOperation(monkey.operation(item))
                if (newItem % monkey.testDiv == 0L) {
                    monkeys[monkey.trueMonk].items.add(newItem)
                } else {
                    monkeys[monkey.falseMonk].items.add(newItem)
                }
            }
        }
    }

    fun part1(): Any {
        val monkeys = parseMonkeys()
        repeat(20) {
            simulate(monkeys) { it / 3 }
        }
        return monkeys.map { it.inspectCount }.sortedDescending().let { it[0] * it[1] }
    }

    fun part2(): Any {
        val monkeys = parseMonkeys()
        val commonProduct = monkeys.fold(1L) { value, monkey -> value * monkey.testDiv }
        repeat(10000) {
            simulate(monkeys) { it % commonProduct }
        }
        return monkeys.map { it.inspectCount }.sortedDescending().let { it[0] * it[1] }
    }
}


fun main() {
    val testInstance = Day11(true)
    val instance = Day11(false)

    testInstance.part1().let { check(it == EXPECTED_1) { "part1: $it != $EXPECTED_1" } }
    println("part1 ANSWER: ${instance.part1()}")
    testInstance.part2().let {
        check(it == EXPECTED_2) { "part2: $it != $EXPECTED_2" }
        println("part2 ANSWER: ${instance.part2()}")
    }
}
