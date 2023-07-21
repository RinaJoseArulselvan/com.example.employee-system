package com.example.data

import com.example.domain.model.Manager
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseConnection{

    private fun hikari():HikariDataSource{
        val config = HikariConfig().apply {
            jdbcUrl         = "jdbc:mysql://localhost:3308/employee"
            driverClassName = "com.mysql.cj.jdbc.Driver"
            username        = "root"
            password        = "root"
            maximumPoolSize = 10
        }
        return HikariDataSource(config)
    }


    init {
        val database = Database.connect(hikari())
        transaction(database) {
            SchemaUtils.create(CredentialsData,EmployeeData)
        }
    }
    suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }

}

