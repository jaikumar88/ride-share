package com.rideshare.app.security

import com.rideshare.app.utils.RoleResolver
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.response.respond

suspend fun ApplicationCall.requireAuthToken(): String? {
    val token = request.headers["X-Auth-Token"]
    if (token == null || !SecurityUtils.validateToken(token)) {
        respond(HttpStatusCode.Unauthorized, mapOf("error" to "Invalid or missing token"))
        return null
    }
    return token
}

suspend fun ApplicationCall.requireRole(requiredRoles: List<String>, token: String): Boolean {
    val userRoleIds = SecurityUtils.extractRolesFromToken(token)
    println("userRoleIds: $userRoleIds")
    val userRoles = RoleResolver.resolveRoleNames(userRoleIds)

    println("User Roles: $userRoles")
    println("Required Roles: $requiredRoles")

    if (requiredRoles.any { it in userRoles }) {
        return true
    }

    respond(HttpStatusCode.Forbidden, mapOf("error" to "Access denied"))
    return false
}