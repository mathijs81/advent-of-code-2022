import kotlin.streams.toList

private const val EXPECTED_1 = 21
private const val EXPECTED_2 = 8

inline fun <T> List<List<Int>>.map (action: (Int, Int, Int) -> T): List<T> {
    val result = mutableListOf<T>()
    for ((y, row) in this.withIndex()) {
        for ((x, value) in row.withIndex()) {
            result.add(action(x, y, value))
        }
    }
    return result
}

private class Day08(isTest: Boolean) : Solver(isTest) {
    fun heightMap() = readAsLines().map { line -> line.map { it - '0' } }

    val dirs = arrayOf(
            -1 to 0,
            1 to 0,
            0 to -1,
            0 to 1
    )

    fun part1(): Any {
        val heights = heightMap()
        return heights.map { startX, startY, height ->
            for (dir in dirs) {
                var x = startX
                var y = startY
                while (true) {
                    y += dir.first
                    x += dir.second
                    if (y < 0 || y >= heights.size || x < 0 || x >= heights[y].size) {
                        return@map 1
                    } else if (heights[y][x] >= height) {
                        break
                    }
                }
            }
            return@map 0
        }.sum()
    }

    fun part2(): Any {
        val heights = heightMap()
        return heights.map { startX, startY, height ->
            var result = 1
            for (dir in dirs) {
                if (result == 0) break
                var x = startX
                var y = startY
                var viewScore = 0
                while (true) {
                    y += dir.first
                    x += dir.second
                    if (y < 0 || y >= heights.size || x < 0 || x >= heights[y].size) {
                        break
                    }
                    viewScore++
                    if (heights[y][x] >= height) {
                        break
                    }
                }
                result *= viewScore
            }
            result
        }.max()
    }
}


fun main() {
    val testInstance = Day08(true)
    val instance = Day08(false)

    testInstance.part1().let { check(it == EXPECTED_1) { "part1: $it != $EXPECTED_1" } }
    println(instance.part1())
    testInstance.part2().let { check(it == EXPECTED_2) { "part2: $it != $EXPECTED_2" } }
    println(instance.part2())
}
