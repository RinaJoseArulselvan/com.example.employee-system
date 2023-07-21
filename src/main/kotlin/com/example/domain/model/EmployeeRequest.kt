package com.example.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class EmployeeRequest(val name:String , val age:Int , val mid:Int)
