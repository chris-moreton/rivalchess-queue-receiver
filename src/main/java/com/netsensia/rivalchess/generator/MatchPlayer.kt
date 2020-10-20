package com.netsensia.rivalchess.generator

import com.netsensia.rivalchess.service.JmsReceiver
import com.netsensia.rivalchess.service.cuteChess
import com.netsensia.rivalchess.service.getEngines

fun game(engine1: String, engine2: String, nodes: Int) {
    getEngines(engine1, engine2)
    val result = cuteChess(engine1, engine2, nodes)
    println(result)
}

fun main() {
        //val message = JmsReceiver.receive()
        game("33.0.1", "33.0.2", 1000)
}
