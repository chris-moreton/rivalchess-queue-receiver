package com.netsensia.rivalchess.service

import com.netsensia.rivalchess.player.getEngineS3Name
import com.netsensia.rivalchess.vie.model.EngineMatch
import java.io.File
import java.io.IOException
import java.text.DecimalFormat
import java.util.concurrent.TimeUnit
import kotlin.system.exitProcess

fun getJavaCommand(engineVersion: String): String {
    if (engineVersion.contains("cuckoo"))
        return "cmd=java -jar ${getEngineS3Name(engineVersion)} uci"

    return "cmd=java -jar ${getEngineS3Name(engineVersion)}"
}

fun cuteChess(matchRequest: EngineMatch): String? {
    try {
        val decimalFormat = DecimalFormat("#.##")
        val seconds1 = decimalFormat.format(matchRequest.engine1.maxMillis / 1000)
        val seconds2 = decimalFormat.format(matchRequest.engine1.maxMillis / 1000)
        val parts = listOf(
                "/cutechess-cli/cutechess-cli",
                "-engine",
                getJavaCommand(matchRequest.engine1.version),
                "nodes=${matchRequest.engine1.maxNodes}",
                "book=/tmp/${matchRequest.engine1.openingBook}.bin",
                "st=${seconds1}",
                "-engine",
                getJavaCommand(matchRequest.engine2.version),
                "nodes=${matchRequest.engine2.maxNodes}",
                "book=/tmp/${matchRequest.engine2.openingBook}.bin",
                "st=${seconds2}",
                "-each",
                "proto=uci",
                "timemargin=1500",
                "-resign",
                "movecount=10",
                "score=600",
                "-rounds",
                "1",
                "-pgnout",
                "/tmp/out.pgn"
        )

        println("Executing command: " + parts.joinToString (separator = " ") { it })

        File("/tmp/out.pgn").delete()

        val proc = ProcessBuilder(parts)
                .directory(File("/tmp"))
                .redirectOutput(ProcessBuilder.Redirect.PIPE)
                .redirectError(ProcessBuilder.Redirect.PIPE)
                .start()

        proc.waitFor(120, TimeUnit.MINUTES)

        if (proc.exitValue() != 0) {
            println(proc.errorStream.bufferedReader().readText())
            exitProcess(proc.exitValue())
        } else {
            println("That seemed to work")
            val output = proc.inputStream.bufferedReader().readText()
            return output
        }
    } catch (e: IOException) {
        e.printStackTrace()
        return null
    }
}

