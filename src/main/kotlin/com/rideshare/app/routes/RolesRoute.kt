package com.rideshare.app.routes

import com.rideshare.app.exceptions.NotFoundException
import com.rideshare.app.exceptions.RoleConflictException
import com.rideshare.app.exceptions.RoleNotFoundException
import com.rideshare.app.payload.request.RoleRequestPayload
import com.rideshare.app.security.requireAuthToken
import com.rideshare.app.security.requireRole
import com.rideshare.app.service.RoleService
import com.rideshare.app.validator.validate
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import org.slf4j.LoggerFactory


fun Route.roleRoutes(roleService: RoleService) {
    val logger = LoggerFactory.getLogger("Application")
    route("/roles") {

        post {
            try {
                val payload = call.receive<RoleRequestPayload>().apply { validate() }
                call.respond(HttpStatusCode.Created, roleService.create(payload))
            }catch (e: RoleNotFoundException){
                logger.error("Error found during role creation")
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to e.message))
            } catch (e: RoleConflictException){
                logger.error("Role already exist")
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to e.message))
            }
        }

        get {
            try {
                call.respond(roleService.findAll())
            }catch (e: RoleNotFoundException){
                logger.error("There is no role found")
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to "There is no record found"))
            }
        }
        get("/{id}") {
            val id = call.parameters["id"]!!
            try {
                val token = call.requireAuthToken() ?: return@get
                if (!call.requireRole(listOf("Admin", "Manager"), token)) return@get
                call.respond(roleService.findById(id))
            }catch (e: RoleNotFoundException){
                logger.error("There is no role found for id : $id")
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to "There is no record found for role id: $id"))
            }
        }
        delete("/{id}") {
            val token = call.requireAuthToken() ?: return@delete
            if (!call.requireRole(listOf("Admin", "Manager"), token)) return@delete
            val id = call.parameters["id"]!!
            call.respond(HttpStatusCode.NoContent, roleService.delete(id))
        }
    }
}