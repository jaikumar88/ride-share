package com.rideshare.app.repositories

import com.rideshare.app.models.User
import com.rideshare.app.payload.request.UserRequestPayload

interface UserRepository {

    fun create(userRequest: UserRequestPayload): User
    fun findAll(): List<User>
    fun findById(id: String): User?
    fun update(id: String, userRequest: UserRequestPayload): User?
    fun delete(id: String): Boolean
    fun findByLoginId(loginId: String): User?
    fun findUserByLogin(string: String): User?
}