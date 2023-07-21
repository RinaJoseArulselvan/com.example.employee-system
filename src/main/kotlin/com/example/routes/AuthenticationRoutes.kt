package com.example.routes

import com.example.data.CredentialsData
import com.example.data.DatabaseConnection.dbQuery
import com.example.domain.model.CredentialsRequest
import com.example.domain.model.Credential
import com.example.utils.TokenManager
import com.typesafe.config.ConfigFactory
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.config.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.mindrot.jbcrypt.BCrypt

fun Route.authenticateRoutes(){
    val tokenManager = TokenManager(HoconApplicationConfig(ConfigFactory.load()))
     post("/register") {
        val credential = call.receive<CredentialsRequest>()
        val username = credential.username.lowercase()
        val password = credential.hashedPassword()

        val checkUserExists = dbQuery {
            CredentialsData.select {
                CredentialsData.username eq username
            }.firstOrNull()
        }
         if (checkUserExists != null){
             call.respondText("Employee already exists", status = HttpStatusCode.BadRequest)
             return@post
         }
         val dbInsert = dbQuery{
                CredentialsData.insert {
                    it[this.username] = username
                    it[this.password] = password
                }[CredentialsData.userId]
        }
        if (dbInsert == 1){
            call.respondText("Employee registered successfully", status = HttpStatusCode.OK)
        }

     }
     post("/login") {
         val request = call.receive<CredentialsRequest>()
         val name = request.username.lowercase()
         val pass = request.password
         val result = dbQuery {
             CredentialsData.select {
                 CredentialsData.username eq name
             }.map {
               val userId = it[CredentialsData.userId]!!
               val username = it[CredentialsData.username]!!
               val password = it[CredentialsData.password]!!
                 Credential(userId,username,password)
             }.firstOrNull()
         }
        if (result == null){
            call.respondText("This username does not exists")
        }
         val doesPasswordMatch = BCrypt.checkpw(pass,result?.password)
         if (!doesPasswordMatch){
             call.respondText("The username or password is Invalid")
         }

         val token = result?.let { it1 -> tokenManager.generateJWTToken(it1) }
         call.respond("Login successfully  $token")
     }

     authenticate {
        get("/me") {
            val principle = call.principal<JWTPrincipal>()
            val username = principle!!.payload.getClaim("username").asString()
            val password = principle!!.payload.getClaim("password").asString()
            call.respondText("welcome $username to the Employee System")
        }
     }

}

