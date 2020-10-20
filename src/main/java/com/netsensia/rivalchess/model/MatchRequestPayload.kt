package com.netsensia.rivalchess.generator

data class EngineSettings(
        val version: String,
        val maxNodes: Int,
        val maxMillis: Int,
        val openingBook: String
)

data class MatchRequestPayload(
        val engine1: EngineSettings,
        val engine2: EngineSettings
)
