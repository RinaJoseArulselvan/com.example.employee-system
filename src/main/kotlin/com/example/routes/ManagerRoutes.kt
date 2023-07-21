package com.example.routes

import com.example.data.DatabaseConnection.dbQuery
import com.example.data.ManagerData
import com.example.domain.model.Manager
import com.example.domain.model.ManagerRequest
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

fun Route.managerRoutes(){

    route("/manager"){
        get("/manager/{id}") {
            val id = call.parameters["id"]?.toInt()?: return@get call.respondText("Invalid id", status = HttpStatusCode.BadRequest)
            val manager = dbQuery{
                ManagerData.select {
                    ManagerData.id eq id
                }.map {
                    val id = it[ManagerData.id]
                    val name= it[ManagerData.name]
                    val experience = it[ManagerData.experience]
                    Manager(id,name,experience)
                }.firstOrNull()
            }
            manager?.let {
                call.respond(it) }
            }
        get {
            val managerList= dbQuery {
                    ManagerData.selectAll()
                        .map {
                            val id = it[ManagerData.id]
                            val name= it[ManagerData.name]
                            val experience = it[ManagerData.experience]
                            Manager(id,name,experience)
                    }
            }
            if (managerList.isNotEmpty()){
                call.respond(managerList)
            }else {
                call.respond("No data is present for managers")
            }
        }

        post {
            val request = call.receive<ManagerRequest>()
            val dbInsert = dbQuery {
                ManagerData.insert {
                    it[name] = request.name
                    it[experience]= request.yearsOfExperience
                }
            }
                call.respond("Manager Data Inserted Successfully")

        }

        post ("/update/{id}"){
            val id = call.parameters["id"]?.toInt()?: return@post call.respondText("Invalid id", status = HttpStatusCode.BadRequest)
            val request = call.receive<ManagerRequest>()
            val dbUpdate = dbQuery {
                ManagerData.update({ManagerData.id eq id}) {
                    it[name] = request.name
                    it[experience] = request.yearsOfExperience
                }
            }
            if (dbUpdate == 1){
                call.respond("Updation successful")
            }else{
                call.respond("Updation Failed")
            }
        }
        delete("/{id}") {
            val id = call.parameters["id"]?.toInt()?: return@delete call.respondText("Invalid id", status = HttpStatusCode.BadRequest)
            val dbDelete = dbQuery {
                ManagerData.deleteWhere {
                    this.id eq id
                }
            }
            if (dbDelete == 1){
                call.respond("manager with id $id deleted")
            }else{
                call.respond("deletion failed")
            }
        }
    }

}