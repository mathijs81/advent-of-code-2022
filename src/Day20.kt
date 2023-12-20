private const val EXPECTED_1 = 11687500L
private const val EXPECTED_2 = 0

private class Day20(isTest: Boolean) : Solver(isTest) {
    data class Module(
        val type: Char, val outputs: List<String>, var state: Boolean = false,
        var inputCount: Int = 0, val highPulses: MutableSet<String> = mutableSetOf()
    )
    data class Pulse(val destination: String, val isHigh: Boolean, val from: String = "")

    val map = mutableMapOf<String, Module>()

    init {
        for (line in readAsLines()) {
            val parts = line.split("->")
            val name = parts[0].trim().removePrefix("%").removePrefix("&")
            val outputs = parts[1].split(",").map { it.trim() }
            val type = parts[0][0]
            map.put(name, Module(type, outputs))
        }
        for ((name, module) in map.entries) {
            for (output in module.outputs) {
                val m = map[output]
                if (m != null) {
                    m.inputCount++
                }
            }
        }
    }

    fun part1(): Any {
        var lowPulses = 0L
        var highPulses = 0L

        repeat(1000) {
            val pulses = ArrayDeque<Pulse>()
            fun addPulse(d: String, isHigh: Boolean, from: String) {
                if (isHigh) {
                    highPulses++
                } else {
                    lowPulses++
                }
//                println("Pulse (${if (isHigh) "hi" else "lo"}) from $from to $d")
                pulses.add(Pulse(d, isHigh, from))
            }
//            println()
            addPulse("broadcaster", false, "")
            //pulses.add(Pulse("broadcaster", false, ""))
            while (!pulses.isEmpty()) {
                val next = pulses.removeFirst()
                val module = map[next.destination]
                if (module != null) {
                    if (module.type == '%') {
                        if (!next.isHigh) {
                            module.state = !module.state
                            for (output in module.outputs) {
                                addPulse(output, module.state, next.destination)
                            }
                        }
                    } else if (module.type == '&') {
                        if (next.isHigh) {
                            module.highPulses.add(next.from)
                        } else {
                            module.highPulses.remove(next.from)
                        }
                        val myPulseHigh = module.highPulses.size != module.inputCount
//                        println(module)

                        for (output in module.outputs) {
                            addPulse(output, myPulseHigh, next.destination)
                        }
                    } else {
                        check(next.destination == "broadcaster")
                        for (output in module.outputs) {
                            addPulse(output, next.isHigh, next.destination)
                        }
                    }
                }
            }
        }

        return lowPulses * highPulses
    }

    fun part2(): Any {

//        println("digraph {")
//        for ((name, module) in map.entries) {
//            for (output in module.outputs) {
//                println("$name -> $output;")
//            }
//        }
//
//        println("}")

        val preDestination = map.entries.filter {
            "rx" in it.value.outputs
        }.single().key

        val cycleDestinations = map.entries.filter {
            preDestination in it.value.outputs
        }.map { it.key }

        var ans = 1L

        for (to in cycleDestinations) {
            var N = 0
            var lastN = 0
            var count = 0
            println(to)
            l@while (N < 1_0000_000_00) {
                val pulses = ArrayDeque<Pulse>()
                fun addPulse(d: String, isHigh: Boolean, from: String) {
                    pulses.add(Pulse(d, isHigh, from))
                }
                addPulse("broadcaster", false, "")
                N++
                while (!pulses.isEmpty()) {
                    val next = pulses.removeFirst()
                    if (next.destination == to && !next.isHigh) {
                        println(N.toString() + ", " + (N - lastN))
                        count +=1
                        if (count > 5) {
                            ans *= (N - lastN)
                            break@l
                        }
                        lastN = N
                    }

                    val module = map[next.destination]
                    if (module != null) {
                        if (module.type == '%') {
                            if (!next.isHigh) {
                                module.state = !module.state
                                for (output in module.outputs) {
                                    addPulse(output, module.state, next.destination)
                                }
                            }
                        } else if (module.type == '&') {
                            if (next.isHigh) {
                                module.highPulses.add(next.from)
                            } else {
                                module.highPulses.remove(next.from)
                            }
                            val myPulseHigh = module.highPulses.size != module.inputCount

                            for (output in module.outputs) {
                                addPulse(output, myPulseHigh, next.destination)
                            }
                        } else {
                            check(next.destination == "broadcaster")
                            for (output in module.outputs) {
                                addPulse(output, next.isHigh, next.destination)
                            }
                        }
                    }
                }
            }
        }

        return ans
    }
}


fun main() {
    val testInstance = Day20(true)
    val instance = Day20(false)

    testInstance.part1().let { check(it == EXPECTED_1) { "part1: $it != $EXPECTED_1" } }
    println("part1 ANSWER: ${instance.part1()}")
//    testInstance.part2().let {
//        check(it == EXPECTED_2) { "part2: $it != $EXPECTED_2" }
    println("part2 ANSWER: ${instance.part2()}")
//    }
}
