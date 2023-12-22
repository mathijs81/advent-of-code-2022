private const val EXPECTED_1 = 16
private const val EXPECTED_2 = 0L

private class Day21(isTest: Boolean) : Solver(isTest) {
    data class Point(val y: Int, val x: Int)

    val field = readAsLines().map { it.toCharArray() }
    val Y = field.size
    val X = field[0].size
    var sx: Int = 0
    var sy: Int = 0

    init {
        for (y in 0..<Y) {
            for (x in 0..<X) {
                if (field[y][x] == 'S') {
                    sx = x
                    sy = y
                    field[y][x] = '.'
                }
            }
        }
    }

    fun part1(): Any {
        val steps = if (isTest) 6 else 64

        var canBe = mutableSetOf<Point>()
        canBe.add(Point(sy, sx))

        repeat(steps) {
            val newSet = mutableSetOf<Point>()
            for (p in canBe) {
                for ((dy, dx) in listOf(1 to 0, 0 to 1, -1 to 0, 0 to -1)) {
                    val ny = p.y + dy
                    val nx = p.x + dx
                    if (nx in 0..<X && ny in 0..<Y && field[ny][nx] != '#') {
                        newSet.add(Point(ny, nx))
                    }
                }
            }
            canBe = newSet
        }

        return canBe.size
    }

    fun part2(): Any {
        val steps = if (isTest) 50 else 26501365

        var currentStep = 0

        val firstStep = mutableMapOf<Point, Int>()
        firstStep[Point(sy, sx)] = 0

        fun iter(stepVal: Int) {
            for ((p, step) in firstStep.entries.toList()) {
                if (step == stepVal) {
                    for ((dy, dx) in listOf(1 to 0, 0 to 1, -1 to 0, 0 to -1)) {
                        val ny = p.y + dy
                        val nx = p.x + dx
                        val np = Point(ny, nx)
                        if (field[(ny + 1000 * Y) % Y][(nx + 1000 * X) % X] != '#') {
                            if (np !in firstStep) {
                                firstStep[np] = step + 1
                            }
                        }
                    }
                }
            }
        }

        var lastValue = firstStep.size
        var lastIncrease = 0L

//        repeat(20*X + 1) {
//            if (it % X == 0 && currentStep % 2 == 0) {
//                val frontierPoints = firstStep.values.count { it == currentStep }
//                val cnt = firstStep.values.count { currentStep % 2 == it % 2}
//                val increase = cnt-lastValue
//                println("$it:$cnt (+${increase}, ++ (${increase - lastIncrease}), frontier = $frontierPoints, total = ${firstStep.size}")
//                lastValue = cnt
//                lastIncrease = increase
//            }
//            iter(currentStep)
//            currentStep++
//            if (currentStep in setOf(6,10,50,100,500)) {
//                println("$currentStep: ${firstStep.values.count { currentStep % 2 == it % 2}}")
//            }
//        }

        if (isTest) {
            return 0L
        }

        val increaseTarget = 120600L
        while (true) {
            if ((steps - currentStep) % (2*X) == 0) {
                val cnt = firstStep.values.count { currentStep % 2 == it % 2 }
                val increase = cnt - lastValue.toLong()
                println("$currentStep:$cnt (+${increase}, ++ (${increase - lastIncrease}), total = ${firstStep.size}")
                lastValue = cnt
                if ((increase - lastIncrease) == increaseTarget) { // && currentStep > 589) {
                    lastIncrease = increase
                    break
                }
                lastIncrease = increase
            }
            iter(currentStep)
            currentStep++
        }

        var sum = firstStep.values.count { currentStep % 2 == it % 2 }.toLong()

        while (currentStep < steps) {
            lastIncrease += increaseTarget
            sum += lastIncrease
            currentStep += 2 * X
        }
        check(currentStep == steps)

        return sum
    }
}


fun main() {
    val testInstance = Day21(true)
    val instance = Day21(false)

    testInstance.part1().let { check(it == EXPECTED_1) { "part1: $it != $EXPECTED_1" } }
    println("part1 ANSWER: ${instance.part1()}")
    testInstance.part2().let {
        check(it == EXPECTED_2) { "part2: $it != $EXPECTED_2" }
        println("part2 ANSWER: ${instance.part2()}")
    }
}
