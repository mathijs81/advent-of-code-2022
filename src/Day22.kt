import kotlin.math.max
import kotlin.math.min

private const val EXPECTED_1 = 5
private const val EXPECTED_2 = 7

data class Brick(val coord: MutableList<Int>, val size: MutableList<Int>) {
    fun maxFall(other: Brick): Int {
        if (other.coord[2] > coord[2]) {
            return Int.MAX_VALUE
        }

        for (i in 0..1) {
            val a = max(coord[i], other.coord[i])
            val b = min(coord[i] + size[i], other.coord[i] + other.size[i])

            if (b <= a) {
                return Int.MAX_VALUE
            }
        }
        return coord[2] - (other.coord[2] + other.size[2])
    }
}

private class Day22(isTest: Boolean) : Solver(isTest) {
    val bricks = readAsLines().map {
        it.split("~").let {
            val coord = it[0].splitInts().toMutableList()
            val endCoord = it[1].splitInts()
            val size = (coord zip endCoord).map { it.second - it.first + 1 }
            Brick(coord, size.toMutableList())
        }
    }

    fun fallAll(bricks: List<Brick>) {
        while (true) {
            var change = false
            outer@ for (brick in bricks) {
                var maxFall = brick.coord[2] - 1
                if (maxFall == 0) {
                    continue
                }
                for (other in bricks) {
                    if (brick.coord[2] > other.coord[2]) {
                        maxFall = min(maxFall, brick.maxFall(other))
                        if (maxFall <= 0) {
                            continue@outer
                        }
                    }
                }
                if (maxFall > 0) {
                    //println("$brick falls by $maxFall")
                    brick.coord[2] -= maxFall
                    change = true
                }
            }

            if (!change) {
                break
            }
        }
    }

    fun part1(): Any {
        fallAll(bricks)

        var ans = 0
        for ((index, removeBrick) in bricks.withIndex()) {
            //println("$index ${bricks.size}")
            val newList = bricks.toMutableList().apply { remove(removeBrick) }
            var ok = true
            outer@ for (brick in newList) {
                var maxFall = brick.coord[2] - 1
                for (other in newList) {
                    if (brick.coord[2] > other.coord[2]) {
                        maxFall = min(maxFall, brick.maxFall(other))
                        if (maxFall <= 0) {
                            continue@outer
                        }
                    }
                }
                if (maxFall > 0) {
                    ok = false
                    break
                }
            }
            if (ok) {
                ans++
            }
        }

        return ans
    }

    fun part2(): Any {
        fallAll(bricks)
        val brickBlockedBy = mutableMapOf<Int, MutableList<Int>>()
        for (index in bricks.indices) {
            for (j in bricks.indices) {
                if (index != j && bricks[j].coord[2] > bricks[index].coord[2]) {
                    val fall = bricks[j].maxFall(bricks[index])
                    if (fall == 0) {
                        brickBlockedBy.getOrPut(j) { mutableListOf() }.add(index)
                    }
                }
            }
        }
        var ans = 0
        for (index in bricks.indices) {
            val removed = mutableSetOf<Int>()

            fun remove(i: Int) {
                if (i in removed) {
                    return
                }
                removed.add(i)
                val candidates =
                    brickBlockedBy.entries.filter { it.key !in removed && it.value.contains(i) }.map { it.key }
                for (k in candidates) {
                    if (brickBlockedBy[k]!!.all { it in removed }) {
                        remove(k)
                    }
                }
            }
            remove(index)
            ans += removed.size - 1
        }
        return ans
    }
}


fun main() {
    val testInstance = Day22(true)
    val instance = Day22(false)

    testInstance.part1().let { check(it == EXPECTED_1) { "part1: $it != $EXPECTED_1" } }
    println("part1 ANSWER: ${instance.part1()}")
    testInstance.part2().let {
        check(it == EXPECTED_2) { "part2: $it != $EXPECTED_2" }
        println("part2 ANSWER: ${instance.part2()}")
    }
}
