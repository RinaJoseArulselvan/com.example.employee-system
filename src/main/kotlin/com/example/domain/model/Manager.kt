package com.example.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Manager(val id:Int , val name: String , val yearsOfExperience:Int)
