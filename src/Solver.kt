import java.io.File

open class Solver(val isTest: Boolean, val extraPath: String = "") {
    private val filename
        get() = "${this.javaClass.simpleName.let { it.substringBefore('_', it) }}${if (isTest) "_test" else ""}.txt"

    fun readAsLines() = File("src$extraPath", filename).readLines()
    fun readAsString() = File("src$extraPath", filename).readText()
}