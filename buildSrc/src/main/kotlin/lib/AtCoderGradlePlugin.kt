package lib

import org.gradle.api.Plugin
import org.gradle.api.Project
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import kotlin.streams.toList

class AtCoderGradlePlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.task("download") {
            doLast {
                preCheck()
                generateCodeFiles(project)
            }
        }

        project.task("submit") {
            doLast {
                preCheck()
                submitCode(project)
            }
        }
    }

    private fun preCheck() {
        if ("oj --version".execCommand() != 0) {
            throw RuntimeException("please install oj command `pip3 install online-judge-tools`")
        }
        if ("oj login --check https://atcoder.jp/".execCommand() != 0) {
            throw RuntimeException("please login to AtCoder `oj login https://atcoder.jp/`")
        }
    }

    /**
     * task=abc100     -> abc100_a, abc100_b, ..., abc100_h
     * task=abc100_b   -> abc100_b
     * task=abc100_a,b -> abc100_a, abc100_b
     */
    private fun generateCodeFiles(project: Project) {
        val task = project.findProperty("task") as String?
            ?: throw RuntimeException("please specify task as ./gradlew downloadProblem -P task=abc100")
        val (contest, problems) = if (task.contains("_")) {
            task.split("_")[0] to task.split("_")[1].split(",")
        } else {
            task to "a,b,c,d,e,f,g,h".split(",")
        }

        problems.forEach { problem ->
            val taskVars = TaskVars(contest, problem)
            println(taskVars.url())

            val template =
                javaClass.getResourceAsStream("/templates/ProblemTemplate.txt")!!.bufferedReader().use { it.readText() }
            val sourceCode = template
                .replace("__REPLACE_WITH_TEST_CASE__", downloadTestCase(taskVars.url()))
                .replace("__REPLACE_WITH_PACKAGE_NAME__", taskVars.packageName())
                .replace("__REPLACE_WITH_CLASS__", taskVars.className())
                .replace("__REPLACE_WITH_URL__", taskVars.url())

            val destination = taskVars.dest()
            if (Files.exists(Path.of(destination))) {
                println("  $destination already exists")
            } else {
                File(taskVars.dir()).mkdirs()
                File(destination).writeText(sourceCode)
                println("  $destination generated")
            }
        }
    }

    private fun downloadTestCase(url: String): String {
        val tmpDir = Files.createTempDirectory("atcoder-oj-")
        try {
            if ("oj download $url -d ${tmpDir.toAbsolutePath()}".execCommand(true) != 0) {
                throw RuntimeException("`$url` is not found. Maybe the task is shared between abc/arc and has other URL.")
            }
            return Files.walk(tmpDir.toAbsolutePath())
                .toList()
                .filter { !Files.isDirectory(it) }
                .map { it.toAbsolutePath().toString() }
                .sorted()
                .joinToString("\n\n") { Files.readAllLines(Path.of(it)).joinToString("\n") }
        } finally {
            tmpDir.toFile().deleteOnExit()
        }
    }

    private fun submitCode(project: Project) {
        val task = project.findProperty("task") as String?
            ?: throw RuntimeException("please specify task as ./gradlew submitProblem -P task=abc100_a")
        val (contest, problem) = task.split("_")
        val taskVars = TaskVars(contest, problem)

        if (project.findProperty("with-test") != null) {
            val passed =
                "./gradlew test --tests ${taskVars.packageName()}.${taskVars.className()}Test".execCommand(true)
            if (passed != 0) {
                throw RuntimeException(
                    "**Test failed. Re-check your code, or remove with-test flag to ignore this error.**"
                )
            }
        }

        val destination = taskVars.dest()
        if (!Files.exists(Path.of(destination))) {
            throw RuntimeException("File not found : $destination")
        }
        val source = Files.readAllBytes(Path.of(destination)).toString(Charsets.UTF_8)
            .let { it.split("// REMOVE_BELOW_ON_SUBMIT")[0] }
            .replace("^package.+\\n\\n?".toRegex(RegexOption.MULTILINE), "")
            .replace("^.+// REMOVE_LINE_ON_SUBMIT\\n".toRegex(RegexOption.MULTILINE), "")

        val file = Files.createTempFile("atcoder-oj-", ".kt")
        try {
            file.toFile().writeText(source)
            println(source)
            "oj submit -l kotlin -y ${taskVars.url()} ${file.toAbsolutePath()}".execCommand(true)
        } finally {
            file.toFile().deleteOnExit()
        }
    }

    private fun String.execCommand(print: Boolean = false): Int {
        if (print) {
            println(this)
        }
        val proc = Runtime.getRuntime().exec(this)
        proc.waitFor()
        proc.destroy()
        return proc.exitValue()
    }

    data class TaskVars(private val contest: String, private val problem: String) {
        fun url() = "https://atcoder.jp/contests/$contest/tasks/${contest}_$problem"
        fun dir() = System.getProperty("user.dir") + "/src/test/kotlin/task/$contest/"
        fun fileName() = "${contest}${problem}".toUpperCase()
        fun dest() = dir() + fileName() + ".kt"
        fun className() = "${contest}${problem}".toUpperCase()
        fun packageName() = "task.${contest}"
    }
}
