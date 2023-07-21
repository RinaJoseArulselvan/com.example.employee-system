package com.example.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class EmployeeWithManager(val id:Int,val name:String,val age:Int,val managerName:String ,val experience:Int)
