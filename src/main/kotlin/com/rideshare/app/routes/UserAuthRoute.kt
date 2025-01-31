package com.rideshare.app.routes

import com.rideshare.app.payload.request.AuthRequestPayload
import com.rideshare.app.payload.response.AuthResponsePayload
import com.rideshare.app.security.SecurityUtils
import com.rideshare.app.service.AuthService
import com.rideshare.app.service.impl.AuthServiceImpl
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import org.slf4j.LoggerFactory

fun Route.userAuthRoute(authService: AuthServiceImpl){
    val logger = LoggerFactory.getLogger("Application")
    route("/"){

        post("login") {
            try {

                val payload = call.receive<AuthRequestPayload>()

                logger.info("Attempting login with email: ${payload.loginId}")

                // Validate user credentials
                val user = authService.authenticate(payload.loginId, payload.password)
                    ?: return@post call.respond(HttpStatusCode.Unauthorized, mapOf("error" to "Invalid credentials"))

                logger.info("User authenticated: ${user.loginId}")

                if (user != null) {
                    logger.info("User roles: ${user.roles?.map { it.name }}")
                }

                // Process roles and handle null cases explicitly
                val roles = user.roles?.mapNotNull { role ->
                    if (role.name == null) {
                        logger.info("Warning: Role name is null for role ID: ${role.id}")
                        null // Skip null role names
                    } else {
                        logger.info("Role processed: ${role.name}")
                        role.name
                    }
                } ?: emptyList() // Default to an empty list if roles is null

                logger.info("Roles for user ${user.loginId}: $roles")

                // Generate JWT token
                val token = SecurityUtils.generateToken(user.loginId, user.roles?.map { it.id.toString() } ?: emptyList())

                logger.info("Generated token for user ${user.loginId}")

                // Respond with user details and token
                call.respond(
                    AuthResponsePayload(
                        id = user.id.toString(),
                        name = user.name,
                        email = user.loginId,
                        roles = roles, // Finalized roles list
                        token = token
                    )
                )
            } catch (e: Exception) {
                logger.info("Error during /auth/login: ${e.message}")
                call.respond(HttpStatusCode.InternalServerError, mapOf("error" to "An unexpected error occurred"))
            }
        }


        get {


        }
    }
}