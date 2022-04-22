package task.abc246

fun main() {
    val t = Thread(null, ABC246A::solve, "s", 64_000_000)
    t.setUncaughtExceptionHandler { _, e -> e.printStackTrace(); kotlin.system.exitProcess(1) }
    t.apply { start() }.join()
}

/** https://atcoder.jp/contests/abc246/tasks/abc246_a **/ // REMOVE_LINE_ON_SUBMIT
object ABC246A {
    fun solve() {

    }

    fun line(): String = readLine()!!.trimEnd()
    fun long(): Long = line().toLong()
    fun longs(): List<Long> = line().split(" ").map { it.toLong() }
}

// REMOVE_BELOW_ON_SUBMIT

class ABC246ATest {
    @org.junit.jupiter.api.TestFactory
    fun test() = lib.runTest(testCase, ABC246A::solve)
}

private val testCase = """
-1 -1
-1 2
3 2

3 -1

-60 -40
-60 -80
-20 -80

-20 -40
""".trimIndent()
