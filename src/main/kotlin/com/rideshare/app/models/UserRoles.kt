package com.rideshare.app.models

import org.jetbrains.exposed.sql.Table

object UserRolesTable : Table("user_roles") {
    val user = reference("user_id", UsersTable)
    val role = reference("role_id", RolesTable)
    override val primaryKey = PrimaryKey(user, role)
}