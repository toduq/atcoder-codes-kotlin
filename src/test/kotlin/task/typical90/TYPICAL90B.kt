package task.typical90

fun main() {
    val t = Thread(null, TYPICAL90B::solve, "s", 64_000_000)
    t.setUncaughtExceptionHandler { _, e -> e.printStackTrace(); kotlin.system.exitProcess(1) }
    t.apply { start() }.join()
}

/** https://atcoder.jp/contests/typical90/tasks/typical90_b **/ // REMOVE_LINE_ON_SUBMIT
object TYPICAL90B {
    fun solve() {
        val cached = mutableMapOf<Int, MutableSet<String>>()
        (0..20).forEach { cached[it] = mutableSetOf() }
        cached[2]?.add("()")

        fun gen(len: Int) {
            if (len >= 3) {
                cached[len - 2]?.forEach {
                    cached[len]?.add("($it)")
                }
            }
            (1 until len).forEach { left ->
                val right = len - left
                cached[left]?.forEach { leftChar ->
                    cached[right]?.forEach { rightChar ->
                        cached[len]?.add(leftChar + rightChar)
                    }
                }
            }
        }

        (0..20).forEach { gen(it) }
        val n = long().toInt()
        cached[n]?.toList()?.sorted()?.forEach { println(it) }
        if (cached[n].isNullOrEmpty()) println()
    }


    fun line(): String = readLine()!!.trimEnd()
    fun long(): Long = line().toLong()
    fun longs(): List<Long> = line().split(" ").map { it.toLong() }
}

// REMOVE_BELOW_ON_SUBMIT

class TYPICAL90BTest {
    @org.junit.jupiter.api.TestFactory
    fun test() = lib.runTest(testCase, TYPICAL90B::solve)
}

private val testCase = """
2

()

3



4

(())
()()

10

((((()))))
(((()())))
(((())()))
(((()))())
(((())))()
((()(())))
((()()()))
((()())())
((()()))()
((())(()))
((())()())
((())())()
((()))(())
((()))()()
(()((())))
(()(()()))
(()(())())
(()(()))()
(()()(()))
(()()()())
(()()())()
(()())(())
(()())()()
(())((()))
(())(()())
(())(())()
(())()(())
(())()()()
()(((())))
()((()()))
()((())())
()((()))()
()(()(()))
()(()()())
()(()())()
()(())(())
()(())()()
()()((()))
()()(()())
()()(())()
()()()(())
()()()()()
""".trimIndent()
