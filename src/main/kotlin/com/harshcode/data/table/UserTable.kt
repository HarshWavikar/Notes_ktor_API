package com.harshcode.data.table

import org.jetbrains.exposed.sql.Table

object UserTable: Table() {
    val email = varchar("email", 512)
    val name = varchar("name", 512)
    val hashPassword = varchar("hashPassword", 512)

    // Now to make email our primary key we have to override the primarKey variable from the Table class
    override val primaryKey: PrimaryKey? = PrimaryKey(email)
}