package com.rideshare.app.repositories.impl

import com.rideshare.app.models.Role
import com.rideshare.app.payload.request.RoleRequestPayload
import com.rideshare.app.repositories.RoleRepository
import org.jetbrains.exposed.sql.transactions.transaction

import java.util.UUID

class RoleRepositoryImpl: RoleRepository{

    override fun create(rolePayload: RoleRequestPayload): Role = transaction {
        Role.new {
            name = rolePayload.name
            description = rolePayload.description
        }
    }

    override fun findAll(): List<Role> = transaction {
        Role.all().toList()
    }

    override fun findById(id: String): Role? = transaction {
        Role.findById(UUID.fromString(id))
    }

    override fun update(id: String, role: Role): Boolean = transaction(){
        val role = Role.findById(UUID.fromString(id))
        role?.let {
            role.apply {
                name = role.name
                description = role.description
            }
            true
        }
        false
    }

    override fun delete(id: String): Boolean = transaction {
        val role = Role.findById(UUID.fromString(id)) ?: return@transaction false
        role.delete()
        true
    }
}