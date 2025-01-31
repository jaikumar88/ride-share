package com.rideshare.app.service.impl

import com.rideshare.app.exceptions.NotFoundException
import com.rideshare.app.exceptions.RoleConflictException
import com.rideshare.app.exceptions.RoleNotFoundException
import com.rideshare.app.models.Role
import com.rideshare.app.payload.request.RoleRequestPayload
import com.rideshare.app.payload.response.RoleResponsePayload
import com.rideshare.app.repositories.RoleRepository
import com.rideshare.app.service.RoleService
import org.jetbrains.exposed.exceptions.ExposedSQLException
import org.jetbrains.exposed.sql.transactions.transaction

class RoleServiceImpl(private val roleRepository: RoleRepository) : RoleService {

    override fun create(rolePayload: RoleRequestPayload): RoleResponsePayload {
        try {
            val role = roleRepository.create(rolePayload)
            return payload(role)
        } catch ( e: ExposedSQLException){

            throw RoleConflictException("Role already exist as: ${rolePayload.name}")
        }
    }

    override fun findAll(): List<RoleResponsePayload> {
        return roleRepository.findAll().map {
            payload(it)
        }
    }

    override fun findById(id: String): RoleResponsePayload {
        val role = roleRepository.findById(id) ?: throw RoleNotFoundException("Role not found with ID $id")
        return payload(role)
    }

    override fun delete(id: String): Boolean {
        if (!roleRepository.delete(id)) throw RoleNotFoundException("Role not found with ID $id")
        return true
    }

    private fun payload(role: Role): RoleResponsePayload = transaction(){
         RoleResponsePayload(
            id = role.id.value.toString(),
            name = role.name,
            description = role.description,
            createdAt = role.createdAt.toString(),
            updatedAt = role.updatedAt.toString()
        )
    }
}