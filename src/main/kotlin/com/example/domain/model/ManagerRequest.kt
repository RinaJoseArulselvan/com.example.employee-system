package com.example.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class ManagerRequest(val name:String , val yearsOfExperience:Int)
