package com.harshcode

import com.harshcode.plugins.*
import com.harshcode.repository.DatabaseFactory
import io.ktor.server.application.*
import io.ktor.server.locations.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    DatabaseFactory.init()
    configureSerialization()
    configureSecurity()
    configureRouting()
}
