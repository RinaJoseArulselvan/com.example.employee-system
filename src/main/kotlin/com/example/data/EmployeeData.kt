package com.example.data

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.ForeignKeyConstraint
import org.jetbrains.exposed.sql.Table

object EmployeeData :Table(name = "employeenew"){
    val employee_Id : Column<Int> = integer("id").autoIncrement()
    val name : Column<String> = varchar("name" , 45)
    val age : Column<Int> = integer("age")
    val managerId : Column<Int> = integer("mid").references(ManagerData.id)
    override val primaryKey = PrimaryKey(employee_Id, name = "PK_eId")

}