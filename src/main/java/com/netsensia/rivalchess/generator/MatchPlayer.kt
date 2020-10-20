package com.netsensia.rivalchess.generator

import com.google.gson.Gson
import com.netsensia.rivalchess.service.JmsReceiver
import com.netsensia.rivalchess.service.cuteChess
import com.netsensia.rivalchess.service.getEngines

fun game(matchRequest: MatchRequestPayload): Boolean {
    val engine1 = matchRequest.engine1.version
    val engine2 = matchRequest.engine2.version
    getEngines(engine1, engine2)
    val result = cuteChess(matchRequest)
    println(result)
    return true
}

fun main() {
    do {
        val gson = Gson()
        val message = JmsReceiver.receive()
        val matchRequest = gson.fromJson(message, MatchRequestPayload::class.java)
        println("Match between ${matchRequest.engine1} and ${matchRequest.engine2}")
    } while (game(matchRequest))
}
