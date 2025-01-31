package com.rideshare.app.routes

import com.rideshare.app.exceptions.RoleNotFoundException
import com.rideshare.app.exceptions.UserAlreadyExistException
import com.rideshare.app.exceptions.UserNotFoundException
import com.rideshare.app.payload.request.UserRequestPayload
import com.rideshare.app.security.requireAuthToken
import com.rideshare.app.security.requireRole
import com.rideshare.app.service.UserService
import com.rideshare.app.validator.validate
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import io.ktor.server.routing.route
import org.slf4j.LoggerFactory

fun Route.userRoute(userService: UserService) {

    val logger = LoggerFactory.getLogger("Application")

    route("/users") {
        post {
            logger.info("Create user invoked")
            try {
                val payload = call.receive<UserRequestPayload>().apply { validate() }
                call.respond(HttpStatusCode.Created, userService.create(payload))
                logger.info("User created successfully")
            } catch (e: UserAlreadyExistException) {
                logger.info("User already exist")
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to e.message))
            }catch (e: RoleNotFoundException) {
                logger.info("Role not exist in system for user")
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to e.message))
            }


        }

        get {
            logger.info("getAll user invoked")
            val token = call.requireAuthToken() ?: return@get

            if (!call.requireRole(listOf("Admin", "Manager"), token)) {
                logger.info("No permission to access find all user functionality")
                return@get
            }

            call.respond(userService.findAll())
            logger.info("Successfully return response for findALl users")
        }

        get("/{id}") {
            logger.info("get user by id invoked")
            try {
                val token = call.requireAuthToken() ?: return@get
                if (!call.requireRole(listOf("Admin", "Manager"), token)) return@get

                val id = call.parameters["id"]!!
                logger.info("Get user by id $id invoked")
                call.respond(userService.findById(id))
            } catch (e: UserNotFoundException) {
                logger.error("User not found")
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to e.message))
            }
            logger.info("User return successfully")
        }

        put("/{id}") {
            logger.info("Update user invoked")
            try {
                val token = call.requireAuthToken() ?: return@put
                if (!call.requireRole(listOf("Admin", "Manager"), token)) return@put

                val id = call.parameters["id"]!!
                val payload = call.receive<UserRequestPayload>()
                call.respond(userService.update(id, payload))
                logger.info("Update user successfully completed")
            } catch (e: UserNotFoundException) {
                logger.info("Update user failed completed")
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to e.message))
            }

        }

        delete("/{id}") {
            logger.info("Delete user invoked")
            try {
                val token = call.requireAuthToken() ?: return@delete
                if (!call.requireRole(listOf("Admin", "Manager"), token)) return@delete

                val id = call.parameters["id"]!!
                userService.delete(id)
                call.respond(HttpStatusCode.NoContent)
                logger.info("User deleted successfully for: $id")
            }catch (e: UserNotFoundException){
                logger.error("User not found ")
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to e.message))
            }
        }
    }
}