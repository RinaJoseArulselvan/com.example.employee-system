package com.example.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Credential(val userId: Int, val username:String, val password:String )
