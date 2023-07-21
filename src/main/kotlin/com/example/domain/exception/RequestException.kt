package com.example.domain.exception

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.requestvalidation.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import java.sql.SQLDataException

fun Application.configureException(){
    install(StatusPages) {
        exception<Throwable> { call, cause ->
            if (cause is IllegalArgumentException) {
                call.respond("give the input properly")
            }else if (cause is NullPointerException){
                call.respond("Database error while inserting")
            }else {
                call.respondText(text = "500: $cause", status = HttpStatusCode.InternalServerError)
            }
        }
        exception<RequestValidationException> { call, cause ->
            call.respond(HttpStatusCode.BadRequest, cause.reasons.joinToString())
        }
        exception<SQLDataException> { call, sqlDataException ->
            call.respond(HttpStatusCode.BadRequest,sqlDataException.toString())
        }
    }
}