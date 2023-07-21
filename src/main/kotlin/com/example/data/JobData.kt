package com.example.data

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table

object JobData :Table("job"){
    val jobId : Column<Int> = integer("jobId").autoIncrement()
    val empId : Column<Int> = integer("eid").references(EmployeeData.employee_Id).uniqueIndex()
    val jobName : Column<String> = varchar(name = "name", length = 45)

    override val primaryKey = PrimaryKey(jobId)
}