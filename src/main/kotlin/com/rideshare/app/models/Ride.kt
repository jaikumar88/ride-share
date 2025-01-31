package com.rideshare.app.models

import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.kotlin.datetime.CurrentDateTime
import org.jetbrains.exposed.sql.kotlin.datetime.datetime
import java.util.UUID

object RideTable : UUIDTable("ride") {
    val userId = reference("user_id", UsersTable)
    val driverId = reference("driver_id", DriverTable)
    val status = varchar("status", 50) // Active, Assigned or Complete
    val createdAt = datetime("created_at").defaultExpression(CurrentDateTime)
    // default value
    val updatedAt = datetime("updated_at").defaultExpression(CurrentDateTime)
}

class Ride( id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<Ride>(RideTable)

    var user by User referencedOn RideTable.userId
    var driver by Driver referencedOn RideTable.driverId
    var status by RideTable.status
    var createdAt by RideTable.createdAt
    var updatedAt by RideTable.updatedAt
}