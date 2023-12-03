import kotlin.math.max
import kotlin.math.min

private const val EXPECTED_1 = 4361
private const val EXPECTED_2 = 467835

private class Day03(isTest: Boolean) : Solver(isTest) {
    fun part1(): Any {
        val field = readAsLines()
        var sum = 0
        for (y in field.indices) {
            var x = 0
            while (x < field[y].length) {
                if (field[y][x].isDigit()) {
                    val startX = x
                    while (x < field[y].length && field[y][x].isDigit()) {
                        x++
                    }
                    var nextToSymbol = false
                    for (checkx in max(0, startX-1)..min(field[y].length-1,x)) {
                        if (y < field.size - 2 && !".0123456789".contains(field[y+1][checkx]))
                            nextToSymbol = true
                        if (y > 0 && !".0123456789".contains(field[y-1][checkx]))
                            nextToSymbol = true
                    }
                    if (startX > 0 && !".0123456789".contains(field[y][startX-1]))
                        nextToSymbol = true
                    if (x < field[y].length - 1 && !".0123456789".contains(field[y][x]))
                        nextToSymbol = true

                    if (nextToSymbol) {
                        sum += field[y].substring(startX, x).toInt()
                    }
                } else {
                    x++
                }
            }
        }
        return sum
    }

    fun part2(): Any {
        val field = readAsLines()
        val gears = mutableMapOf<Pair<Int, Int>, MutableList<Int>>()
        for (y in field.indices) {
            var x = 0
            while (x < field[y].length) {
                if (field[y][x].isDigit()) {
                    val startX = x
                    while (x < field[y].length && field[y][x].isDigit()) {
                        x++
                    }

                    fun addGear(ax: Int, ay: Int) {
                        val key = Pair(ax, ay)
                        val gearList = gears[key] ?: mutableListOf<Int>().also { gears[key] = it }
                        gearList.add(field[y].substring(startX, x).toInt())
                    }
                    
                    for (checkx in max(0, startX-1)..min(field[y].length-1,x)) {
                        if (y < field.size - 2 && field[y+1][checkx] == '*')
                            addGear(checkx, y+1)
                        if (y > 0 && '*' == field[y-1][checkx])
                            addGear(checkx, y-1)
                    }
                    if (startX > 0 && '*' == field[y][startX-1])
                        addGear(startX-1, y)
                    if (x < field[y].length - 1 && '*' == field[y][x])
                        addGear(x, y)
                } else {
                    x++
                }
            }
        }
        return gears.filter { it.value.size == 2 }.map {it.value[0] * it.value[1] }.sum()
    }
}


fun main() {
    val testInstance = Day03(true)
    val instance = Day03(false)

    testInstance.part1().let { check(it == EXPECTED_1) { "part1: $it != $EXPECTED_1" } }
    println("part1 ANSWER: ${instance.part1()}")
    testInstance.part2().let {
        check(it == EXPECTED_2) { "part2: $it != $EXPECTED_2" }
        println("part2 ANSWER: ${instance.part2()}")
    }
}
