package com.rideshare.app.routes

import com.rideshare.app.exceptions.DriverFoundException
import com.rideshare.app.exceptions.DriverNotFoundException
import com.rideshare.app.exceptions.RoleNotFoundException
import com.rideshare.app.exceptions.UserAlreadyExistException
import com.rideshare.app.payload.request.DriverRequestPayload
import com.rideshare.app.payload.request.StatusUpdateRequest
import com.rideshare.app.security.requireAuthToken
import com.rideshare.app.security.requireRole
import com.rideshare.app.service.DriverService
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

fun Route.driverRoute(driverService: DriverService) {
    val logger = LoggerFactory.getLogger("Application")
    route("/drivers") {

        post {
            try {
                logger.info("Driver create function called")
                val payload = call.receive<DriverRequestPayload>().apply { validate() }
                call.respond(HttpStatusCode.Created, driverService.create(payload))
                logger.info("Driver created successfully")
            } catch (e: UserAlreadyExistException) {
                logger.error("Error occurred while creating driver UserAlreadyExistException"+e.message)
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to "Unable to create driver already exist"))
            } catch (e: DriverFoundException) {
                logger.error("Error occurred while creating driver. Driver already exist"+e.message)
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to "Unable to create driver already exist "))
            } catch (e: RoleNotFoundException) {
                logger.error("Error occurred while creating driver. Role not exist"+e.message)
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to "Unable to create driver already exist "))
            }
            catch (e: Exception) {
                logger.error("Error occurred while creating driver  "+e.message)
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to "Unable to create driver already exist "))
            }
        }

        get {
            val token = call.requireAuthToken() ?: return@get
            if (!call.requireRole(listOf("Admin", "Manager","Driver"), token)) return@get
            call.respond(driverService.findAll())
        }

        get("/{id}") {
            val token = call.requireAuthToken() ?: return@get
            if (!call.requireRole(listOf("Admin", "Manager","Driver"), token)) return@get

            val id = call.parameters["id"]!!
            call.respond(driverService.findById(id))
        }
        //find available driver
        get("/available") {
            try {
                val driver = driverService.findAvailableDriver()

                driver?.let {
                    call.respond(driver)
                }
                call.respond(HttpStatusCode.NotFound, "No driver available")
                return@get
            }catch (e: DriverNotFoundException){
                logger.error("Error occurred while getting driver")
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to e.message))
            }
        }

        //find available driver
        get("/available/all") {
            try{
            val drivers = driverService.findAllAvailableDrivers()
            drivers?.let {
                call.respond(drivers)
            }
            call.respond(HttpStatusCode.NotFound, "No driver available")
            return@get
            }catch (e: DriverNotFoundException){
                logger.error("Error occurred while getting drivers ")
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to e.message))
            }
        }

        put("/{id}") {
            try{
            logger.info("Update driver called")
            val token = call.requireAuthToken() ?: return@put
            if (!call.requireRole(listOf("Admin", "Manager","Driver"), token)) return@put

            val id = call.parameters["id"]!!
            logger.info("Driver update: $id")
            val payload = call.receive<DriverRequestPayload>()
            call.respond(driverService.update(id, payload))
            logger.info("Driver updated successfully")
            }catch (e: DriverNotFoundException){
                logger.error("Error occurred while getting driver ")
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to e.message))
            }
        }

        put("status/{id}") {
            logger.info("Update driver status called")
            try {
                val token = call.requireAuthToken() ?: return@put
                if (!call.requireRole(listOf("Admin", "Manager","Driver"), token)) return@put

                val id = call.pathParameters["id"]!!
                logger.info("Driver update with: $id")
                val status = call.receive<StatusUpdateRequest>()
                logger.info("New status need to update: $status")
                call.respond(driverService.updateDriverStatus(id, status))
                logger.info("Driver status updated successfully")
            }catch (e: DriverNotFoundException){
        logger.error("Error occurred while getting driver  ")
        call.respond(HttpStatusCode.BadRequest, mapOf("error" to e.message))
    }
        }

        delete("/{id}") {
            try {
                val token = call.requireAuthToken() ?: return@delete
                if (!call.requireRole(listOf("Admin", "Manager","Driver"), token)) return@delete

                val id = call.parameters["id"]!!
                driverService.delete(id)
                call.respond(HttpStatusCode.NoContent)
            }catch (e: DriverNotFoundException){
                logger.error("Error occurred while getting  driver")
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to e.message))
            }
        }
    }
}