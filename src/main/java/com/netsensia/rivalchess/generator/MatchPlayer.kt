package com.netsensia.rivalchess.generator

import com.netsensia.rivalchess.service.JmsReceiver

fun main() {
    do {
        val message = JmsReceiver.receive()
        println(message)
    } while (!message.equals("ERROR"))
}
