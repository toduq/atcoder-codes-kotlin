package task.abc247

fun main() {
    val t = Thread(null, ABC247B::solve, "s", 64_000_000)
    t.setUncaughtExceptionHandler { _, e -> e.printStackTrace(); kotlin.system.exitProcess(1) }
    t.apply { start() }.join()
}

/** https://atcoder.jp/contests/abc247/tasks/abc247_b **/ // REMOVE_LINE_ON_SUBMIT
object ABC247B {
    fun solve() {
        val n = long()
        val names = (1..n).map { line().split(" ") }
        val cardinalities = names.flatten().groupingBy { it }.eachCount()
        fun card(s: String) = cardinalities.getOrDefault(s, 0)
        var ok = true
        names.forEach { (first, last) ->
            if ((first == last && card(first) > 2) || (first != last && card(first) > 1 && card(last) > 1)) {
                ok = false
                return@forEach
            }
        }
        println(
            if (ok) {
                "Yes"
            } else {
                "No"
            }
        )
    }

    fun line(): String = readLine()!!.trimEnd()
    fun long(): Long = line().toLong()
    fun longs(): List<Long> = line().split(" ").map { it.toLong() }
}

// REMOVE_BELOW_ON_SUBMIT

class ABC247BTest {
    @org.junit.jupiter.api.TestFactory
    fun test() = lib.runTest(testCase, ABC247B::solve)
}

private val testCase = """
3
tanaka taro
tanaka jiro
suzuki hanako

Yes

3
aaa bbb
xxx aaa
bbb yyy

No

2
tanaka taro
tanaka taro

No

3
takahashi chokudai
aoki kensho
snu ke

Yes
""".trimIndent()
