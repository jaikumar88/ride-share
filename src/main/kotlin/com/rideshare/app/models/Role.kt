package com.rideshare.app.models


import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.Table

import org.jetbrains.exposed.sql.kotlin.datetime.CurrentDateTime
import org.jetbrains.exposed.sql.kotlin.datetime.datetime
import java.util.UUID

object RolesTable : UUIDTable("roles") {
    val name = varchar("name", 50).uniqueIndex()
    val description = varchar("description", 255).nullable()
    val createdAt = datetime("created_at").defaultExpression(CurrentDateTime)
    val updatedAt = datetime("updated_at").defaultExpression(CurrentDateTime)
}


// implementing JPARepository CRUDRepository

class Role(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<Role>(RolesTable)
    var name by RolesTable.name
    var description by RolesTable.description
    var createdAt by RolesTable.createdAt
    var updatedAt by RolesTable.updatedAt
}
