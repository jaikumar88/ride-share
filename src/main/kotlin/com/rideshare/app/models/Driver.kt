package com.rideshare.app.models

import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.kotlin.datetime.CurrentDateTime
import org.jetbrains.exposed.sql.kotlin.datetime.datetime
import java.util.UUID

/**
 *  code to create database table
 */
object DriverTable : UUIDTable("driver") {

    var userId = reference("user_id", UsersTable)
    val carId = reference("car_id", CarsTable).uniqueIndex()
    val status = varchar("status", 50).default("AVAILABLE") // Driver status is available or assigned
    val lastAssignedTime = datetime("last_assigned_time").defaultExpression(CurrentDateTime)
    val createdAt = datetime("created_at").defaultExpression(CurrentDateTime)
    val updatedAt = datetime("updated_at").defaultExpression(CurrentDateTime)
}

/**
 * Model class present the object which used for request and response
 */
class Driver(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<Driver>(DriverTable)
    var user  by User referencedOn DriverTable.userId
    var car by Car referencedOn DriverTable.carId
    var status by DriverTable.status
    var lastAssignedTime by DriverTable.lastAssignedTime
    var createdAt by DriverTable.createdAt
    var updatedAt by DriverTable.updatedAt
}