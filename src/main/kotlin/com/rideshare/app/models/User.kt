package com.rideshare.app.models

import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.kotlin.datetime.CurrentDateTime
import org.jetbrains.exposed.sql.kotlin.datetime.datetime
import java.util.UUID



object UsersTable : UUIDTable("users") {
    val loginId = varchar("login_id", 100).uniqueIndex()
    val password = varchar("password", 255)
    val name = varchar("name", 255)
    val createdAt = datetime("created_at").defaultExpression(CurrentDateTime)
    // default value
    val updatedAt = datetime("updated_at").defaultExpression(CurrentDateTime)
}


class User(id: EntityID<UUID> ) : UUIDEntity(id) {
    companion object : UUIDEntityClass<User>(UsersTable)
    var name by UsersTable.name
    var loginId by UsersTable.loginId
    var password by UsersTable.password
    var roles by Role via UserRolesTable
    var createdAt by UsersTable.createdAt
    var updatedAt by UsersTable.updatedAt

}