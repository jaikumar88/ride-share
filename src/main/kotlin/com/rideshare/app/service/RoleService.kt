package com.rideshare.app.service

import com.rideshare.app.payload.request.RoleRequestPayload
import com.rideshare.app.payload.response.RoleResponsePayload

interface RoleService {

    fun create(rolePayload: RoleRequestPayload): RoleResponsePayload
    fun findAll(): List<RoleResponsePayload>
    fun findById(id: String): RoleResponsePayload
    fun delete(id: String): Boolean

}