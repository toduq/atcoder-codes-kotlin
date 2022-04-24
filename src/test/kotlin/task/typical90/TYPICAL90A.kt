package task.typical90

fun main() {
    val t = Thread(null, TYPICAL90A::solve, "s", 64_000_000)
    t.setUncaughtExceptionHandler { _, e -> e.printStackTrace(); kotlin.system.exitProcess(1) }
    t.apply { start() }.join()
}

/** https://atcoder.jp/contests/typical90/tasks/typical90_a **/ // REMOVE_LINE_ON_SUBMIT
object TYPICAL90A {
    fun solve() {
        val (_, l) = longs()
        val k = long()
        val aList = listOf(0L) + longs() + listOf(l)
        val lenList = aList.zipWithNext().map { it.second - it.first }

        fun canBeSolvedAt(score: Long): Boolean {
            var count = 0L
            var remain = 0L
            lenList.forEach { len ->
                if (remain + len >= score) {
                    count += 1
                    remain = 0
                } else {
                    remain += len
                }
            }
            return count > k
        }

        println(bSearch(0, l, ::canBeSolvedAt))
    }

    /** 条件を満たす最大の値を求める */
    private fun bSearch(min: Long, max: Long, predicate: (Long) -> Boolean): Long {
        if (max - min == 1L) {
            return if (predicate(max)) {
                max
            } else {
                min
            }
        }
        val mid = min + (max - min) / 2
        return if (predicate(mid)) {
            bSearch(mid, max, predicate)
        } else {
            bSearch(min, mid, predicate)
        }
    }

    fun line(): String = readLine()!!.trimEnd()
    fun long(): Long = line().toLong()
    fun longs(): List<Long> = line().split(" ").map { it.toLong() }
}

// REMOVE_BELOW_ON_SUBMIT

class TYPICAL90ATest {
    @org.junit.jupiter.api.TestFactory
    fun test() = lib.runTest(testCase, TYPICAL90A::solve)
}

private val testCase = """
3 34
1
8 13 26

13

7 45
2
7 11 16 20 28 34 38

12

3 100
1
28 54 81

46

3 100
2
28 54 81

26

20 1000
4
51 69 102 127 233 295 350 388 417 466 469 523 553 587 720 739 801 855 926 954

170
""".trimIndent()
