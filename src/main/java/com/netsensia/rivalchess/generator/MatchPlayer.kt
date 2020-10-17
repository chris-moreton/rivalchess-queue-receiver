package com.netsensia.rivalchess.generator

import com.netsensia.rivalchess.service.JmsReceiver

fun main() {
    println(JmsReceiver.receive())
}
