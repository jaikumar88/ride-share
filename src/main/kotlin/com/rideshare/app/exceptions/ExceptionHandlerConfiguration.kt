package com.rideshare.app.exceptions

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.BadRequestException
import io.ktor.server.plugins.NotFoundException
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.response.respond
import jakarta.validation.ConstraintViolationException

fun Application.configureExceptionHandling() {

    install(StatusPages) {
        status(HttpStatusCode.NotFound) { status ->
            call.respond(
                status,
                mapOf(
                    "error" to "Not Found",
                    "message" to "The requested resource could not be found. Please check the URL and try again."
                )
            )
        }
        exception<IllegalArgumentException> { call, cause ->
            call.respond(
                HttpStatusCode.BadRequest,
                mapOf(
                    "error" to "Invalid input",
                    "message" to cause.message
                )
            )
        }
        status(HttpStatusCode.UnsupportedMediaType) { status ->
            call.respond(
                status,
                mapOf(
                    "error" to "Unsupported Media Type",
                    "message" to "The Content-Type header is not supported. Please use application/json."
                )
            )
        }
        // Handle BadRequestException
        exception<BadRequestException> { call, cause ->
            call.respond(
                HttpStatusCode.BadRequest,
                mapOf(
                    "error" to "Invalid request payload",
                    "details" to (cause.message ?: "Bad Request")
                )
            )
        }
        exception<ConstraintViolationException> { call, cause ->
            val errors = cause.constraintViolations.map {
                "${it.propertyPath}: ${it.message}"
            }
            call.respond(HttpStatusCode.BadRequest, mapOf("errors" to errors))
        }
        exception<NotFoundException> { call, cause ->
            call.respond(
                HttpStatusCode.NotFound,
                mapOf("error" to (cause.message ?: "Not found"))
            )
        }
        exception<NotFoundException> { call, cause ->
            call.respond(
                HttpStatusCode.NotFound,
                mapOf("error" to (cause.message ?: "Not found"))
            )
        }
        exception<BadRequestException> { call, cause ->
            call.respond(
                HttpStatusCode.BadRequest,
                mapOf("error" to (cause.message ?: "Bad request"))
            )
        }
        exception<Exception> { call, cause ->
            call.respond(
                HttpStatusCode.InternalServerError,
                mapOf("error" to "An unexpected error occurred")
            )
            cause.printStackTrace()
        }
    }
}