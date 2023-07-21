package com.example.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class JobRequest(val eid:Int , val name:String )
