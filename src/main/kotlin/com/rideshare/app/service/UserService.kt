package com.rideshare.app.service

import com.rideshare.app.payload.request.UserRequestPayload
import com.rideshare.app.payload.response.UserResponsePayload

interface UserService {

    fun create(userPayload: UserRequestPayload): UserResponsePayload
    fun findAll(): List<UserResponsePayload>
    fun findById(id: String): UserResponsePayload
    fun update(id: String, userPayload: UserRequestPayload): UserResponsePayload
    fun delete(id: String): Boolean
}