package lib

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DynamicTest
import java.io.ByteArrayOutputStream
import java.io.PrintStream

fun runTest(testCase: String, solve: () -> Unit): List<DynamicTest> {
    val cases = testCase.split("\n\n").windowed(2, 2).map { "${it[0]}\n" to "${it[1]}\n" }
    return cases.mapIndexed { index, case ->
        DynamicTest.dynamicTest("Test Case #${index + 1}") {
            val reader = case.first.byteInputStream()
            val writer = ByteArrayOutputStream()
            System.setIn(reader)
            System.setOut(PrintStream(writer))
            solve()
            Assertions.assertEquals(case.second, writer.toString())
        }
    }
}
