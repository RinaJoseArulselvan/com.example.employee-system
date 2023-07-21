package com.example.plugins

import com.example.routes.authenticateRoutes
import com.example.routes.employeeRoutes
import com.example.routes.jobRoutes
import com.example.routes.managerRoutes
import io.ktor.server.routing.*
import io.ktor.server.response.*
import io.ktor.server.application.*

fun Application.configureRouting() {
    routing {
        authenticateRoutes()
        employeeRoutes()
        managerRoutes()
        jobRoutes()
    }
}
