package com.rideshare.app.routes

import com.rideshare.app.exceptions.NoDriverAvailable
import com.rideshare.app.exceptions.NoRideAvailable
import com.rideshare.app.exceptions.RideNotFoundException
import com.rideshare.app.payload.request.RideRequestPayload
import com.rideshare.app.payload.request.RideStatusPayload

import com.rideshare.app.service.RideService
import com.rideshare.app.validator.validate
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import io.ktor.server.routing.route
import org.slf4j.LoggerFactory

fun Route.rideRoute(rideService: RideService) {
    val logger = LoggerFactory.getLogger("Application")
    route("/rides") {
        post {
            logger.info("Calling create ride function")
            try {
                //val token = call.requireAuthToken() ?: return@post
                // if (!call.requireRole(listOf("Admin", "manager"), token)) return@post
                val payload = call.receive<RideRequestPayload>().apply { validate() }
                logger.info("Calling create ride function payload: $payload")
                val response = rideService.bookRide(payload)
                response?.let {
                    call.respond(HttpStatusCode.Created, response)
                }
            } catch (e: NoDriverAvailable) {
                logger.error("There is no driver available for ride.")
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to e.message))
            }
            logger.info("Calling create ride function success")
        }

        // get all rides
        get {
            logger.info("Find all ride called")
            call.respond(rideService.findAllRides())
            logger.info("Find all ride success")
        }

        put("/{id}") {
            logger.info("Update ride called")
            try {
                val payload = call.receive<RideStatusPayload>()
                logger.info("Updating status of ride:$payload.status")
                val id = call.parameters["id"]!!
                call.respond(HttpStatusCode.OK, rideService.updateStatus(id, payload))
                logger.info("Updating status of ride is success")
            } catch (e: NoRideAvailable) {
                logger.error("Error while update ride")
                    call.respond(HttpStatusCode.BadRequest, mapOf("error" to e.message))
                }
            catch (e: RideNotFoundException) {
                logger.error("Error while update ride updating")
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to e.message))
            }
        }

        /**
         *
         */
        get ("/{id}"){
            try {
                logger.info("Get ride by id called")
                val id = call.parameters["id"]!!
                val response = rideService.findById(id)
                response?.let {
                    call.respond(HttpStatusCode.OK, response)
                }
            }catch (e: RideNotFoundException) {
                logger.error("Ride not found")
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to e.message))
            }
            catch (e: NoRideAvailable) {
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to e.message))
            }
        }


    }
}