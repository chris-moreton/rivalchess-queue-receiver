package com.netsensia.rivalchess.player

import com.google.gson.Gson
import com.netsensia.rivalchess.service.cuteChess
import com.netsensia.rivalchess.utils.JmsReceiver
import com.netsensia.rivalchess.utils.JmsSender
import com.netsensia.rivalchess.utils.getFile
import com.netsensia.rivalchess.vie.model.EngineSettings
import com.netsensia.rivalchess.vie.model.MatchResult
import java.io.File

fun getEngineS3Name(engineVersion: String) =
    if (engineVersion.replace(".", "").toIntOrNull() != null)
        "rivalchess-${engineVersion}-1.jar" else "$engineVersion.jar"

fun getEngine(engineVersion: String) {

    val s3Name = getEngineS3Name(engineVersion)
    val filePath = "/tmp/$s3Name"
    getFile("rivalchess-jars", s3Name, filePath)
}

fun getOpeningBook(openingBook: String) {
    val s3Name = "${openingBook}.bin"
    val filePath = "/tmp/$s3Name"
    getFile("rivalchess-openings", s3Name, filePath)
}

fun getEngines(engine1: String, engine2: String) {
    getEngine(engine1)
    getEngine(engine2)
}

fun game(matchRequest: EngineSettings): Boolean {
    if (matchRequest.engine1.maxNodes < 100 || matchRequest.engine2.maxNodes < 100) {
        println("Dodgy request ${matchRequest}")
        return true
    }
    val engine1 = matchRequest.engine1.version
    val engine2 = matchRequest.engine2.version
    getEngines(engine1, engine2)
    getOpeningBook(matchRequest.engine1.openingBook)
    println("Files retrieved, starting match")
    val result = cuteChess(matchRequest)
    println(result)
    val pgn = File("/tmp/out.pgn").readText()
    val matchResult = MatchResult(matchRequest, pgn)
    JmsSender.send("MatchResulted", matchResult)
    return true
}

fun main() {
    do {
        val gson = Gson()
        val message = JmsReceiver.receive("MatchRequested")
        val matchRequest = gson.fromJson(message, EngineSettings::class.java)
        println("Starting match ${matchRequest.engine1} v ${matchRequest.engine2}")
    } while (game(matchRequest))
}
