package com.netsensia.rivalchess.generator

import com.netsensia.rivalchess.service.JmsReceiver
import com.netsensia.rivalchess.service.getEngines

fun game(engine1: String, engine2: String, nodes: Int) {
    getEngines(engine1, engine2)
}

@kotlin.ExperimentalUnsignedTypes
fun main() {
    do {
        val message = JmsReceiver.receive()
        game("33.0.1", "33.0.2", 1000)
    } while (!message.equals("ERROR"))
}
