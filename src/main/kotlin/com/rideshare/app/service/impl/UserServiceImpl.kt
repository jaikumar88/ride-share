package com.rideshare.app.service.impl


import com.rideshare.app.exceptions.NotFoundException
import com.rideshare.app.exceptions.RoleNotFoundException
import com.rideshare.app.exceptions.UserAlreadyExistException
import com.rideshare.app.exceptions.UserNotFoundException
import com.rideshare.app.extensions.toResponse
import com.rideshare.app.models.User
import com.rideshare.app.payload.request.UserRequestPayload
import com.rideshare.app.payload.response.RoleResponsePayload
import com.rideshare.app.payload.response.UserResponsePayload
import com.rideshare.app.repositories.UserRepository
import com.rideshare.app.security.SecurityUtils

import com.rideshare.app.service.UserService
import org.jetbrains.exposed.sql.transactions.transaction
import org.postgresql.util.PSQLException
import org.slf4j.LoggerFactory


class UserServiceImpl(private val userRepository: UserRepository) : UserService {
    val logger = LoggerFactory.getLogger("UserService")
    override fun create(userPayload: UserRequestPayload): UserResponsePayload {
        return transaction {
            try {
                val user = userRepository.create(userPayload)
                // payload(user, true)
                user.toResponse()
            }catch (e: RoleNotFoundException){
                logger.error("Role not exist"+e.message)
                throw RoleNotFoundException(e.message.toString())
            }
            catch (e: Exception){
                logger.error("SQL exception occured while creating user"+e.message)
                throw UserAlreadyExistException("User already exist with login id: ${userPayload.loginId} Please try other id")
            }
        }
    }


    override fun findAll(): List<UserResponsePayload> {
        return transaction {


            userRepository.findAll().map {
                payload(it, false)
            }
        }
    }

    override fun findById(id: String): UserResponsePayload {
        val user = userRepository.findById(id) ?: throw UserNotFoundException("User not found with ID $id")
        return transaction {
            payload(user, false)
        }
    }


    override fun update(
        id: String,
        userPayload: UserRequestPayload,
    ): UserResponsePayload {

        val user = userRepository.update(id, userPayload) ?: throw UserNotFoundException("User not found with ID $id")
        return transaction {
            payload(user, false)
        }
    }

    override fun delete(id: String): Boolean {
        return transaction {
            if (!userRepository.delete(id)) throw UserNotFoundException("User not found with ID $id")
            true
        }

    }

    private fun payload(user: User, tokenRequired: Boolean): UserResponsePayload = UserResponsePayload(
        id = user.id.value.toString(),
        name = user.name,
        loginId = user.loginId,
        createdAt = user.createdAt.toString(),
        updatedAt = user.updatedAt.toString(),
        token = if (tokenRequired) SecurityUtils.generateToken(user.loginId, user.roles.map { it.name }) else null,
        roles = user.roles.map { role ->
            RoleResponsePayload(
                id = role.id.value.toString(),
                name = role.name,
                description = role.description,
                createdAt = role.createdAt.toString(),
                updatedAt = role.updatedAt.toString()
            )
        }

    )
}