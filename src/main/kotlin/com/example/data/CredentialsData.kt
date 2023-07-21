package com.example.data

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table

object CredentialsData :Table(name = "credentials"){
    val userId : Column<Int> = integer("id").autoIncrement()  // in integer("id").autoincrement() the "id" is the column name in the database
    val username : Column<String> = varchar("username",225)
    val password : Column<String> = varchar("password",225)

    override val primaryKey = PrimaryKey(userId, name = "PK_Id")
}