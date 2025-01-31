package com.rideshare.app.extensions


import com.rideshare.app.models.Driver
import com.rideshare.app.models.Role
import com.rideshare.app.models.User
import com.rideshare.app.payload.response.DriverResponsePayload
import com.rideshare.app.payload.response.RoleResponsePayload
import com.rideshare.app.payload.response.UserResponsePayload
import kotlinx.datetime.toJavaLocalDateTime
import java.time.format.DateTimeFormatter

val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

/**
 * Format response for client
 */
fun User.toResponse(): UserResponsePayload {
    return UserResponsePayload(
        id = id.toString(),
        name = name,
        loginId = loginId,
        createdAt = createdAt.toJavaLocalDateTime().format(formatter),
        updatedAt = updatedAt.toJavaLocalDateTime().format(formatter),
        token = null,
        roles = roles.map {  it.toResponse()}
    )
}

/**
 * Prepare role response
 */
fun Role.toResponse(): RoleResponsePayload {

    return RoleResponsePayload(
        id = id.toString(),
        name = name,
        description = description,
        createdAt = createdAt.toJavaLocalDateTime().format(formatter),
        updatedAt = updatedAt.toJavaLocalDateTime().format(formatter)
    )
}

/**
 * Prepare driver response
 */
fun Driver.toResponse(): DriverResponsePayload{
    return DriverResponsePayload(
        id = id.toString(),
        name = user.name,
        loginId = user.loginId,

        createdAt = createdAt.toJavaLocalDateTime().format(formatter),
        updatedAt = updatedAt.toJavaLocalDateTime().format(formatter),
        status = status,
        roles = user.roles.map { it.toResponse() },
        token = null,
        car = null,
    )
}

