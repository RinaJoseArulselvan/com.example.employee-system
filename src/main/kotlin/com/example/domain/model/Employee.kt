package com.example.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Employee(val id:Int , val name:String , val age:Int ,  val mid:Int)
