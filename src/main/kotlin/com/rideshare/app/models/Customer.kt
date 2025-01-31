package com.rideshare.app.models

import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import java.util.UUID

object CustomerTable:  UUIDTable("customer") {
    val userId = reference("user_id", UsersTable)
}


class Customer(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<Customer>(CustomerTable)
    var userId by User referencedOn CustomerTable.userId
}