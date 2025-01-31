package com.rideshare.app.models


import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.kotlin.datetime.datetime
import org.jetbrains.exposed.sql.kotlin.datetime.CurrentDateTime
import java.util.UUID

object CarsTable : UUIDTable("cars") {
    val driverId = reference("driver_id", DriverTable).nullable()
    val make = varchar("make", 50)
    val model = varchar("model", 50)
    val color = varchar("color", 50)
    val carType = varchar("type", 50)
    val regNo = varchar("reg_no", 50).uniqueIndex()
    val insuranceId = varchar("insurance_id", 100)
    val insuranceExpiry = datetime("insurance_exp").defaultExpression(CurrentDateTime)
    val createdAt = datetime("created_at").defaultExpression(CurrentDateTime)

    // default value
    val updatedAt = datetime("updated_at").defaultExpression(CurrentDateTime)
}

class Car(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<Car>(CarsTable)

    var make by CarsTable.make
    var model by CarsTable.model
    var color by CarsTable.color
    var carType by CarsTable.carType
    var regNo by CarsTable.regNo
    var insuranceId by CarsTable.insuranceId
    var insuranceExpiry by CarsTable.insuranceExpiry
    var createdAt by CarsTable.createdAt
    var updatedAt by CarsTable.updatedAt
}