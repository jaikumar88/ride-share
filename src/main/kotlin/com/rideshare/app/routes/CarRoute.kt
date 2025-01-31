package com.rideshare.app.routes

import com.rideshare.app.exceptions.CarNotFoundException
import com.rideshare.app.payload.request.CarRequestPayload
import com.rideshare.app.service.CarService
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

fun Route.carRoute(carService: CarService) {
    val logger = LoggerFactory.getLogger("Application")
    route("cars") {
        post {
            try {
                val payload = call.receive<CarRequestPayload>().apply { validate() }
                call.respond(HttpStatusCode.Created, carService.create(payload))
            }catch (e: Exception){
                logger.error("Error occurred while creating car")
                call.respond(HttpStatusCode.BadRequest, "Unable to create car")
            }
        }

        get {
            call.respond(carService.findAll())
        }

        get("/{id}") {
            val id = call.parameters["id"]!!
            try {
                call.respond(carService.findById(id))
            }catch (e: CarNotFoundException){
                logger.error("Car not found ")
                call.respond(HttpStatusCode.NotFound, mapOf("error" to e.message))
            }
        }
        delete("/{id}") {
            val id = call.parameters["id"]!!
            try {
                carService.delete(id)
                call.respond(HttpStatusCode.NoContent)
            }catch (e: CarNotFoundException){
                logger.error("Car not found")
                call.respond(HttpStatusCode.NotFound, mapOf("error" to e.message))
            }
        }
    }
}