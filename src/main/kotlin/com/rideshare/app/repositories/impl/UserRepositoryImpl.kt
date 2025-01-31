package com.rideshare.app.repositories.impl

import com.rideshare.app.exceptions.NotFoundException
import com.rideshare.app.exceptions.RoleNotFoundException
import com.rideshare.app.exceptions.UserNotFoundException
import com.rideshare.app.models.Role
import com.rideshare.app.models.User
import com.rideshare.app.models.UsersTable
import com.rideshare.app.payload.request.UserRequestPayload
import com.rideshare.app.repositories.UserRepository
import kotlinx.datetime.toKotlinLocalDateTime
import org.jetbrains.exposed.sql.SizedCollection
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID

class UserRepositoryImpl : UserRepository {

    /**
     * Create user
     */
    override fun create(request: UserRequestPayload): User = transaction() {
        // Find all roles for the user exist or not
        val roles = request.roleIds.map { roleId ->
            Role.findById(UUID.fromString(roleId)) ?: throw RoleNotFoundException("Role not found with ID $roleId")
        }
            val user = User.new {
                name = request.name
                loginId = request.loginId
                password = request.password
                createdAt = java.time.LocalDateTime.now().toKotlinLocalDateTime()
                updatedAt = java.time.LocalDateTime.now().toKotlinLocalDateTime()
            }
            // after creating user assign the roles
            user.roles = SizedCollection(roles)
            user

    }

    /**
     * Find all user
     */
    override fun findAll(): List<User> = transaction() {
        User.all().toList()
    }

    /**
     * Find by id
     */
    override fun findById(id: String): User? = transaction() {
        User.findById(UUID.fromString(id))
    }

    override fun findUserByLogin(id: String): User? = transaction() {
        User.find() { UsersTable.loginId eq id }
            .toList().firstOrNull()
    }

    /**
     * Update User
     */
    override fun update(id: String, request: UserRequestPayload): User? = transaction() {
        val user = User.findById(UUID.fromString(id)) ?: return@transaction throw UserNotFoundException("User not found")

        val roles = request.roleIds.map { roleId ->
            Role.findById(UUID.fromString(roleId)) ?: throw RoleNotFoundException("Role not found with ID $roleId")
        }
        user.apply {
            request.name?.let {
                name = request.name
            }
            request.loginId?.let {
                loginId = request.loginId
            }
            this.roles = SizedCollection(roles)
        }
        user
    }

    /**
     * Delete user
     */
    override fun delete(id: String): Boolean = transaction() {
        val user = User.findById(UUID.fromString(id))?: throw UserNotFoundException("User not found by id:$id")
        user?.let {
            user.delete()
        }
        false
    }

    override fun findByLoginId(loginId: String): User? {

        return transaction {
            User.find { UsersTable.loginId eq loginId }
                .singleOrNull()
                ?.also { user ->
                    // Force eager loading of roles
                    user.roles.toList()
                    println("Roles loaded: ${user.roles.map { it.name }}")
                }
        }
    }


}