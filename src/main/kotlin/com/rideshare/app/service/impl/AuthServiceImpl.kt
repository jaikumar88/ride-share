package com.rideshare.app.service.impl

import com.rideshare.app.models.Role
import com.rideshare.app.models.User
import com.rideshare.app.payload.response.RoleResponsePayload
import com.rideshare.app.payload.response.UserResponsePayload
import com.rideshare.app.repositories.UserRepository
import org.jetbrains.exposed.sql.SizedIterable
import org.jetbrains.exposed.sql.transactions.transaction
import kotlin.String

class AuthServiceImpl(val userRepository: UserRepository) {
     fun authenticate(loginId: String, password: String): UserResponsePayload? {
        return transaction {
            // Fetch user by email
            val user = userRepository.findByLoginId(loginId)

            user?.roles?.toList()
            println("User found: ${user?.roles}")

            // Log roles to confirm they are fetched
            if (user != null) {
                println("User roles: ${user.roles.map { it.name }}")
            }

            // Validate password (consider hashing in a real application)
            if (user != null && user.password == password) {
                println("Authentication successful for user: ${user.loginId}")
                return@transaction toResponse(user, toRoleRespone(user?.roles))
            } else {
                println("Invalid credentials for email: $loginId")
                null
            }

        }
    }

    private fun toRoleRespone(roles: SizedIterable<Role>?): List<RoleResponsePayload>? {

       val roleList = roles?.map { roleResponse -> RoleResponsePayload(
           id = roleResponse.id.toString(),
           name = roleResponse.name,
           description = roleResponse.description,
           createdAt= roleResponse.createdAt.toString(),
           updatedAt=roleResponse.updatedAt.toString()
       ) }
        return roleList

    }
    private fun toResponse(user: User?, assignedRoles: List<RoleResponsePayload>?): UserResponsePayload {
        return UserResponsePayload(
            id = user?.id.toString(),
            name = user?.name ?: "",
            loginId = user?.loginId ?:"",
            createdAt = user?.createdAt.toString(),
            updatedAt = user?.updatedAt.toString(),
            roles = assignedRoles,
            token = null
        )
    }
    private fun hashPassword(password: String): String {
        // Add password hashing logic here (e.g., BCrypt)
        return password // Placeholder
    }
}