package com.example.domain.validation

import com.example.domain.model.*
import io.ktor.server.application.*
import io.ktor.server.plugins.requestvalidation.*
import kotlin.reflect.typeOf

fun Application.configureEmployeeValidation(){
    install(RequestValidation){
        validate<EmployeeRequest> {
            if(it.age<=130 ){
                ValidationResult.Valid
            }else{
                ValidationResult.Invalid("Invalid customer age or give the id in numbers")
            }

        }
        validate<CredentialsRequest>{
            if (it.username.length > 3  && it.password.length > 6){
                ValidationResult.Valid
            }else{
                ValidationResult.Invalid("The username should be greater than 3 and password should be greater than 6")
            }
        }
        validate<ManagerRequest>{
            if (it.yearsOfExperience<50){
                ValidationResult.Valid
            }else
                ValidationResult.Invalid("It is an invalid years of experience")
        }
        validate<JobRequest>{
            if ((it.name is String)&&(it.eid is Int)){
                ValidationResult.Valid
            }else
                ValidationResult.Invalid("Give the input properly")
        }

    }
}

//fun Application.configurecredential(){
//    install(RequestValidation){
//        validate<Credentials>{
//            if (it.username.length > 3  && it.password.length > 6){
//                ValidationResult.Valid
//            }else{
//                ValidationResult.Invalid("The username should be greater than 3 and password should be greater than 6")
//            }
//        }
//    }
//}