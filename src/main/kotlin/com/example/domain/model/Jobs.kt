package com.example.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Jobs(val id:Int, val eid : Int, val name: String)
