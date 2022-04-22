package task.abc247

fun main() {
    val t = Thread(null, ABC247A::solve, "s", 64_000_000)
    t.setUncaughtExceptionHandler { _, e -> e.printStackTrace(); kotlin.system.exitProcess(1) }
    t.apply { start() }.join()
}

/** https://atcoder.jp/contests/abc247/tasks/abc247_a **/ // REMOVE_LINE_ON_SUBMIT
object ABC247A {
    fun solve() {
        println("0" + line().dropLast(1))
    }

    fun line(): String = readLine()!!.trimEnd()
    fun long(): Long = line().toLong()
    fun longs(): List<Long> = line().split(" ").map { it.toLong() }
}

// REMOVE_BELOW_ON_SUBMIT

class ABC247ATest {
    @org.junit.jupiter.api.TestFactory
    fun test() = lib.runTest(testCase, ABC247A::solve)
}

private val testCase = """
1011

0101

0000

0000

1111

0111
""".trimIndent()
