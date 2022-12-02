import java.io.File

open class Solver(val isTest: Boolean) {
    private val filename
        get() = "${this.javaClass.name}${if (isTest) "_test" else ""}.txt"

    fun readAsLines() = File("src", filename).readLines()
    fun readAsString() = File("src", filename).readText()
}