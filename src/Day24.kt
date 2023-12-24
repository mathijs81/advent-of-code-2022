import java.math.BigInteger
import java.math.BigInteger.ONE
import java.math.BigInteger.ZERO
import kotlin.math.absoluteValue
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sign

private const val EXPECTED_1 = 2L
private const val EXPECTED_2 = 47L

fun List<Pair<BigInteger, BigInteger>>.chineseRemainderTheorem(): BigInteger {
    val product = fold(ONE) { acc, (number, _) -> acc * number }
    val result = map { (number, remainder) ->
        val pp = product / number
        remainder * pp.moduloInverse(number) * pp
    }.fold(ZERO, BigInteger::plus)
    var base = result.rem(product)
    while (base < ZERO) {
        base += product
    }
    return base
}

fun BigInteger.moduloInverse(modulo: BigInteger): BigInteger {
    if (modulo == ONE) return ZERO
    var m = modulo
    var a = this
    var x0 = ZERO
    var x1 = ONE
    var temp: BigInteger
    while (a > ONE) {
        val quotient = a / m
        temp = m
        m = a % m
        a = temp
        temp = x0
        x0 = x1 - quotient * x0
        x1 = temp
    }
    return x1
}

private class Day24(isTest: Boolean) : Solver(isTest) {

    fun part1(): Any {
        data class Stone(val pos: List<Double>, val speed: List<Double>) {
            val speedNorm by lazy {
                1.0 / Math.hypot(speed[0], speed[1])
            }

            val x1 = pos[0]
            val x2 = pos[0] + speed[0] / speedNorm
            val y1 = pos[1]
            val y2 = pos[1] + speed[1] / speedNorm

            fun intersect2(other: Stone): List<Double> {
                val x3 = other.x1
                val y3 = other.y1
                val x4 = other.x2
                val y4 = other.y2
                val div = (x1 - x2) * (y3 - y4) - (y1 - y2) * (x3 - x4)

                return listOf(
                    ((x1 * y2 - y1 * x2) * (x3 - x4) - (x1 - x2) * (x3 * y4 - y3 * x4)) / div,
                    ((x1 * y2 - y1 * x2) * (y3 - y4) - (y1 - y2) * (x3 * y4 - y3 * x4)) / div
                )
            }
        }
        val stones = readAsLines().map {
            val all = it.splitDoubles()
            Stone(all.subList(0, 3), all.subList(3, 6))
        }

        val rmin = if (isTest) 7.0 else 200000000000000.0
        val rmax = if (isTest) 27.0 else 400000000000000.0

        fun Double.inside(a: Double, b: Double): Boolean {
            return (a - this) <= 1e-9 && (this - b) <= 1e-9
        }

        var ans = 0L

        for ((a, sa) in stones.withIndex()) {
            for ((b, sb) in stones.withIndex()) {
                if (b > a) {
                    val p = sa.intersect2(sb)
                    if (p.any { !it.isFinite() }) {
                        continue
                    } else {
                        if (p[0].inside(rmin, rmax) && p[1].inside(rmin, rmax)) {
                            val t = p[0] - sa.pos[0]
                            val t2 = p[0] - sb.pos[0]
                            if (t.sign.toInt() == sa.speed[0].sign.toInt() && t2.sign.toInt() == sb.speed[0].sign.toInt()) {
                                ans++
                            }
                        }
                    }
                }
            }
        }
        return ans
    }

    fun part2(): Any {
        data class Stone(val pos: List<Long>, val speed: List<Long>) {
            fun posAt(t: Long): List<Long> {
                return (0..2).map { pos[it] + speed[it] * t }
            }
        }

        val stones = readAsLines().map {
            val all = it.splitLongs()
            Stone(all.subList(0, 3), all.subList(3, 6))
        }.sortedBy { it.pos[2] }

        println("test $isTest")

        var limit = if (isTest) 10L else 1000L

        zSpeed@ for (zSpeed in -limit..limit) {
            var minPos = Long.MIN_VALUE
            var maxPos = Long.MAX_VALUE

            val relSpeeds = stones.mapNotNull {
                val relSpeed = it.speed[2] - zSpeed
                if (relSpeed >= 0) {
                    minPos = max(it.pos[2], minPos)
                }
                if (relSpeed <= 0) {
                    maxPos = min(it.pos[2], maxPos)
                }
                if (relSpeed != 0L) {
                    relSpeed.absoluteValue to (it.pos[2] % relSpeed.absoluteValue)
                } else {
                    null
                }
            }

            if (minPos > maxPos) {
                continue
            }
            fun simplifySpeeds(l: List<Pair<Long, Long>>): List<Pair<Long, Long>>? {
                val modulosByPrime = mutableMapOf<Long, Long>()
                for ((prod, remain) in l) {
                    var prod2 = prod
                    val primes = mutableSetOf<Long>()
                    for (i in 2..Math.sqrt(prod.toDouble()).toLong()) {
                        while (prod2 % i == 0L) {
                            primes.add(i)
                            prod2 /= i
                        }
                    }
                    if (prod2 != 1L) {
                        primes.add(prod2)
                    }

                    for (p in primes) {
                        val current = modulosByPrime[p]
                        if (current != null && current != remain % p) {
                            return null
                        }
                        modulosByPrime[p] = remain % p
                    }
                }

                return modulosByPrime.toSortedMap().entries.map { it.key to it.value }
            }

            val simplified = simplifySpeeds(relSpeeds) ?: continue@zSpeed
            val rs = simplified.map { it.first.toBigInteger() to it.second.toBigInteger() }

            val crt = try {
                rs.chineseRemainderTheorem()
            } catch (e: Exception) {
                continue@zSpeed
            }

            fun tryZ(zSpeed: Long, startZ: Long): Long {
                val collideTime1 = (stones[0].pos[2] - startZ) / (zSpeed - stones[0].speed[2])
                val collideTime2 = (stones[1].pos[2] - startZ) / (zSpeed - stones[1].speed[2])

                check(stones[0].posAt(collideTime1)[2] == startZ + zSpeed * collideTime1)

                val dt = collideTime2 - collideTime1
                val c1 = stones[0].posAt(collideTime1)
                val c2 = stones[1].posAt(collideTime2)

                val xSpeed = (c2[0] - c1[0]) / dt
                check(dt * xSpeed == c2[0] - c1[0]) { "$c1 $c2 $dt --> $xSpeed (${dt * xSpeed}, ${c2[0] - c1[0]})"}
                val ySpeed = (c2[1] - c1[1]) / dt
                check(dt * ySpeed == c2[1] - c1[1])

                val xPos = c1[0] - collideTime1 * xSpeed
                val yPos = c1[1] - collideTime1 * ySpeed

                val me = Stone(listOf(xPos, yPos, startZ), listOf(xSpeed, ySpeed, zSpeed))

                for (stone in stones) {
                    val collideTime = (stone.pos[2] - startZ) / (zSpeed - stone.speed[2])
                    check(stone.posAt(collideTime) == me.posAt(collideTime))
                }
                return xPos + yPos + startZ
            }


            try {
                return tryZ(zSpeed, crt.toLong())
            } catch(e: Exception) {
                if(!isTest) {
                    e.printStackTrace()
                }
                println("speed $zSpeed @ $crt failed: $e")
            }
        }

        error("No answer")
    }
}

fun main() {
    val testInstance = Day24(true)
    val instance = Day24(false)

    testInstance.part1().let { check(it == EXPECTED_1) { "part1: $it != $EXPECTED_1" } }
    println("part1 ANSWER: ${instance.part1()}")
    testInstance.part2().let {
        check(it == EXPECTED_2) { "part2: $it != $EXPECTED_2" }
        println("part2 ANSWER: ${instance.part2()}")
    }
}
