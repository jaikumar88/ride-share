package com.rideshare.app.repositories

import com.rideshare.app.models.Role
import com.rideshare.app.payload.request.RoleRequestPayload

interface RoleRepository {
    fun create(payload: RoleRequestPayload): Role
    fun findAll() : List<Role>
    fun findById(id: String): Role?
    fun update(id: String, role: Role): Boolean
    fun delete(id: String): Boolean
}