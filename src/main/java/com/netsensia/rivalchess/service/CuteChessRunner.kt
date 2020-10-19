package com.netsensia.rivalchess.service

import java.io.File
import java.io.IOException
import java.util.concurrent.TimeUnit

const val COMMAND = "cutechess-cli -engine cmd=\"java -jar /tmp/%s.jar\" -engine cmd=\"java -jar /tmp/%s.jar\" -each proto=uci book=\"/home/chrismoreton/Chess/ProDeo.bin\" timemargin=1500 st=10 -resign movecount=10 score=600 -pgnout %s.pgn"

fun String.runCommand(workingDir: File): String? {
    try {
        val parts = this.split("\\s".toRegex())
        val proc = ProcessBuilder(*parts.toTypedArray())
                .directory(workingDir)
                .redirectOutput(ProcessBuilder.Redirect.PIPE)
                .redirectError(ProcessBuilder.Redirect.PIPE)
                .start()

        proc.waitFor(60, TimeUnit.MINUTES)
        return proc.inputStream.bufferedReader().readText()
    } catch(e: IOException) {
        e.printStackTrace()
        return null
    }
}

fun playGame(engine1: String, engine2: String, nodes: Integer) {

}