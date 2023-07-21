package com.example.routes

import com.example.data.DatabaseConnection.dbQuery
import com.example.data.EmployeeData
import com.example.data.ManagerData
import com.example.domain.model.Employee
import com.example.domain.model.EmployeeRequest
import com.example.domain.model.EmployeeWithManager
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

fun Route.employeeRoutes(){
    route("/employee") {
        get("/employees") {
            val employeeList = dbQuery {
                EmployeeData.selectAll()
                    .map {
                        val id = it[EmployeeData.employee_Id]
                        val name = it[EmployeeData.name]
                        val age = it[EmployeeData.age]
                        val mid = it[EmployeeData.managerId]
                        Employee(id,name,age,mid)
                    }
            }
            if (employeeList.isEmpty()) {
                call.respond("Database is empty ")
            }
            call.respond(employeeList)
        }

        post("/employees") {
            val request = call.receive<EmployeeRequest>()
            val dbInsert = dbQuery {
                EmployeeData.insert {
                    it[name] = request.name
                    it[age] = request.age
                    it[managerId] = request.mid
                }
            }
            call.respondText("Employee data inserted successfully ", status = HttpStatusCode.OK)

        }

        get("/get/{id}") {
            val id = call.parameters["id"]?.toInt() ?: return@get call.respondText(
                "Invalid id ",
                status = HttpStatusCode.BadRequest
            )

            val employee = dbQuery {
                EmployeeData.select {
                    EmployeeData.employee_Id eq id
                }.map {
                    val id = it[EmployeeData.employee_Id]
                    val name = it[EmployeeData.name]
                    val age = it[EmployeeData.age]
                    val mid = it[EmployeeData.managerId]
                    Employee(id, name, age ,mid)
                }.firstOrNull()
            }
            if (employee == null) {
                call.respondText("The employee with id $id does not exist")
                return@get
            } else {
                call.respond(employee)
            }
        }
        post("/update/{id}") {
            val id = call.parameters["id"]?.toInt() ?: return@post call.respondText(
                "Invalid Id ",
                status = HttpStatusCode.BadRequest
            )

            val request = call.receive<EmployeeRequest>()
            val result = dbQuery {
                EmployeeData.update({ EmployeeData.employee_Id eq id }) {
                    it[name] = request.name
                    it[age] = request.age
                    it[managerId] = request.mid
                }
            }
            if (result == 1) {
                call.respondText("updated successfully", status = HttpStatusCode.OK)
            } else {
                call.respondText("updation failed or this id does not exist ")
                return@post
            }
        }
        delete("/delete/{id}") {
            val id = call.parameters["id"]?.toInt() ?: return@delete call.respondText(
                "Invalid Id ",
                status = HttpStatusCode.BadRequest
            )

            val dbDelete = dbQuery {
                EmployeeData.deleteWhere {
                    this.employee_Id eq id
                }
            }
            if (dbDelete == 1) {
                call.respondText("The data deleted successfully")
            } else {
                call.respondText("This id does not exist")
            }
        }
        get("/employee-with-manager/{id}"){
            val id = call.parameters["id"]?.toInt() ?: return@get call.respondText(
                "Invalid Id ",
                status = HttpStatusCode.BadRequest
            )
            val result = dbQuery {
                (EmployeeData innerJoin ManagerData)
                    .select { EmployeeData.managerId eq id }
                    .map {
                        EmployeeWithManager(it[EmployeeData.employee_Id],it[EmployeeData.name],it[EmployeeData.age],it[ManagerData.name],it[ManagerData.experience])
                    }
            }
            if (result.isNotEmpty())
                call.respond(result)
            else
                call.respond("Employee with Manager id $id is not found")
        }
        get ("/employee-with-manager"){
            val result = dbQuery {
                (EmployeeData innerJoin ManagerData)
                    .selectAll()
                    .map {
                        EmployeeWithManager(it[EmployeeData.employee_Id],it[EmployeeData.name],it[EmployeeData.age],it[ManagerData.name],it[ManagerData.experience])
                    }
            }
            if (result.isNotEmpty())
                call.respond(result)
            else
                call.respondText("List is Empty" , status = HttpStatusCode.BadRequest)
        }
    }
}