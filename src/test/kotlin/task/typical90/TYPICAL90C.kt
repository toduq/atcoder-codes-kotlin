package task.typical90

fun main() {
    val t = Thread(null, TYPICAL90C::solve, "s", 64_000_000)
    t.setUncaughtExceptionHandler { _, e -> e.printStackTrace(); kotlin.system.exitProcess(1) }
    t.apply { start() }.join()
}

/** https://atcoder.jp/contests/typical90/tasks/typical90_c **/ // REMOVE_LINE_ON_SUBMIT
object TYPICAL90C {
    fun solve() {
        val n = long()
        val nodes = (0 until n).map { mutableSetOf<Int>() }
        val edges = (1 until n).map { _ -> longs().map { it.toInt() - 1 } }
        edges.forEach { (from, to) ->
            nodes[from].add(to)
            nodes[to].add(from)
        }

        data class NodeWithLen(val i: Int, var len: Int)

        fun findFarthestNode(index: Int, from: Int?): NodeWithLen {
            val children = nodes[index].filter { it != from }
            if (children.isEmpty()) return NodeWithLen(index, 0)
            return children.map { child -> findFarthestNode(child, index) }
                .maxBy { it.len }!!.let { NodeWithLen(it.i, it.len + 1) }
        }

        val farthestFromZero = findFarthestNode(0, null)
        val farthestFromFarthest = findFarthestNode(farthestFromZero.i, null)
        println(farthestFromFarthest.len + 1)
    }

    fun line(): String = readLine()!!.trimEnd()
    fun long(): Long = line().toLong()
    fun longs(): List<Long> = line().split(" ").map { it.toLong() }
}

// REMOVE_BELOW_ON_SUBMIT

class TYPICAL90CTest {
    @org.junit.jupiter.api.TestFactory
    fun test() = lib.runTest(testCase, TYPICAL90C::solve)
}

private val testCase = """
3
1 2
2 3

3

5
1 2
2 3
3 4
3 5

4

10
1 2
1 3
2 4
4 5
4 6
3 7
7 8
8 9
8 10

8

31
1 2
1 3
2 4
2 5
3 6
3 7
4 8
4 9
5 10
5 11
6 12
6 13
7 14
7 15
8 16
8 17
9 18
9 19
10 20
10 21
11 22
11 23
12 24
12 25
13 26
13 27
14 28
14 29
15 30
15 31

9
""".trimIndent()
