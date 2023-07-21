package com.example.data

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table

object ManagerData :Table(name = "manager"){
    val id:Column<Int> = integer("id").autoIncrement()
    val name:Column<String> = varchar(name = "name", length = 45)
    val experience:Column<Int> = integer("experience")

    override val primaryKey = PrimaryKey(id, name = "pk_mid")
}