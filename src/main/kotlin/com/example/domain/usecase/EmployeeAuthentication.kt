package com.example.domain.usecase

import com.example.utils.TokenManager
import com.typesafe.config.ConfigFactory
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.config.*

fun Application.configureAuthentication(){
    val config = HoconApplicationConfig(ConfigFactory.load())
    val tokenManager = TokenManager(config)
    install(Authentication){
        jwt {
            verifier(tokenManager.verifyJWTToken())
            realm = config.property("realm").getString()
            validate {jwtCredential ->
                if (jwtCredential.payload.getClaim("username").toString().isNotEmpty()){
                    JWTPrincipal(jwtCredential.payload)
                }else{
                    null
                }
            }
        }
    }
}

/*
 val config = HoconApplicationConfig(ConfigFactory.load())
    val tokenManager = TokenManager(config)
    install(Authentication){
        jwt {
            verifier(tokenManager.verifyJWTToken())
            realm = config.property("realm").getString()
            validate { jwtCredential ->
                if (jwtCredential.payload.getClaim("username").toString().isNotEmpty()){
                    JWTPrincipal(jwtCredential.payload)
                }else{
                    null
                }

            }
        }
    }
 */