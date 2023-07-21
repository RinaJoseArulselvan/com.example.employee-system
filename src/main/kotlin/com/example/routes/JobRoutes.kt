package com.example.routes

import com.example.data.DatabaseConnection.dbQuery
import com.example.data.EmployeeData
import com.example.data.JobData
import com.example.domain.model.JobRequest
import com.example.domain.model.Jobs
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

fun  Route.jobRoutes(){
    route("/job"){
        get {
            val jobList = dbQuery{
                JobData.selectAll().map {
                    val jobId = it[JobData.jobId]
                    val eid = it[JobData.empId]
                    val name = it[JobData.jobName]
                    Jobs(jobId,eid,name)
                }
            }
            if (jobList.isNotEmpty()){
                call.respond(jobList)
            }else{
                call.respond("There is no data found for Job")
            }
        }
        post {
            val request = call.receive<JobRequest>()
            val checkIdExists = dbQuery {
                EmployeeData.select { EmployeeData.employee_Id eq request.eid }.firstOrNull()
            }
            if (checkIdExists == null){
                call.respond("This employee id does not exist")
                return@post
            }
            val dbInsert = dbQuery {
                JobData.insert {
                        it[empId] = request.eid
                        it[jobName] = request.name
                }
            }
            call.respond("Job data inserted successfully")
        }

        get ("/{id}"){
            val id = call.parameters["id"]?.toInt()?: return@get call.respond("Invalid id")
            val result = dbQuery {
                JobData.select { JobData.empId eq id}.map {
                    val id= it[JobData.jobId]
                    val eid = it[JobData.empId]
                    val name = it[JobData.jobName]
                   Jobs(id,eid, name)
                }.firstOrNull()
            }
            if (result!=null){
                call.respond(result)
            }else{
                call.respond("There is no job assigned for this employee id")
            }
        }

        post("/{id}") {
            val id = call.parameters["id"]?.toInt() ?: return@post call.respond("Invalid Id")
            val request = call.receive<JobRequest>()
            val dbUpdate = dbQuery {
                JobData.update({JobData.empId eq id}) {
                    it[jobName] = request.name
                }
            }
            if (dbUpdate == null){
                call.respond("Updation failed")
            }else
                call.respond("updation successful")
        }

        delete("/{id}"){
            val id = call.parameters["id"]?.toInt() ?: return@delete call.respond("Invalid Id")
            val dbDelete = dbQuery {
                JobData.deleteWhere {
                    empId eq id
                }
            }
            if (dbDelete== null){
                call.respond("Deletion failed")
            }else{
                call.respond("Deletion successful")
            }
        }

    }
}