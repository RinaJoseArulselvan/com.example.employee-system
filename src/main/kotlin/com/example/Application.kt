package com.example

import com.example.domain.exception.configureException
import com.example.domain.usecase.configureAuthentication
import com.example.domain.validation.configureEmployeeValidation
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import com.example.plugins.*


fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    configureAuthentication()
    configureSerialization()
    configureRouting()
    configureEmployeeValidation()
    configureException()

}
