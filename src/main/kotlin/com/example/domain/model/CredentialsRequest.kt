package com.example.domain.model

import kotlinx.serialization.Serializable
import org.mindrot.jbcrypt.BCrypt

@Serializable
data class CredentialsRequest(val username: String, val password: String){

    fun hashedPassword():String{
        return BCrypt.hashpw(password,BCrypt.gensalt())
    }
}
