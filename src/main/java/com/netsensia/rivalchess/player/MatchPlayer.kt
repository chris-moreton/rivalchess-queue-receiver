package com.netsensia.rivalchess.player

import com.google.gson.Gson
import com.netsensia.rivalchess.service.cuteChess
import com.netsensia.rivalchess.utils.JmsReceiver
import com.netsensia.rivalchess.utils.JmsSender
import com.netsensia.rivalchess.utils.getFile
import com.netsensia.rivalchess.vie.model.EngineSettings
import com.netsensia.rivalchess.vie.model.MatchResult
import java.io.File

fun getEngine(engineVersion: String) {
    val s3Name = "rivalchess-${engineVersion}-1.jar"
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
    val engine1 = matchRequest.engine1.version
    val engine2 = matchRequest.engine2.version
    getEngines(engine1, engine2)
    getOpeningBook(matchRequest.engine1.openingBook)
    getOpeningBook(matchRequest.engine2.openingBook)
    val result = cuteChess(matchRequest)
    println(result)
    val pgn = File("/tmp/out.pgn").readText()
    val matchResult = MatchResult(matchRequest, pgn)
    JmsSender.send("MatchResults", matchResult)
    return true
}

fun main() {
    do {
        val gson = Gson()
        val message = JmsReceiver.receive("MatchRequests")
        val matchRequest = gson.fromJson(message, EngineSettings::class.java)
        println("Match between ${matchRequest.engine1} and ${matchRequest.engine2}")
    } while (game(matchRequest))
}
