package com.netsensia.rivalchess.service

import java.io.File
import java.io.IOException
import java.util.concurrent.TimeUnit

val parts = listOf(
        "cutechess-cli",
        "-engine",
        "cmd=java -jar rivalchess-33.0.1-1.jar",
        "-engine",
        "cmd=java -jar rivalchess-33.0.2-1.jar",
        "-each",
        "nodes=1000",
        "proto=uci",
        "book=\"/home/chrismoreton/Chess/ProDeo.bin\"",
        "timemargin=1500",
        "st=0.25",
        "-resign",
        "movecount=10",
        "score=600",
        "-rounds",
        "1",
        "-pgnout",
        "out.pgn"
)

fun cuteChess(engine1: String, engine2: String, nodes: Int): String? {
    try {
        val proc = ProcessBuilder(parts)
                .directory(File("/tmp"))
                .redirectOutput(ProcessBuilder.Redirect.PIPE)
                .redirectError(ProcessBuilder.Redirect.PIPE)
                .start()

        proc.waitFor(5, TimeUnit.MINUTES)

        if (proc.exitValue() != 0) {
            val output = proc.errorStream.bufferedReader().readText()
            return output
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

