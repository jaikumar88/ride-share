package com.rideshare.app.payload.request

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size
import kotlinx.serialization.Serializable

@Serializable
data class CarRequestPayload(
    @field:NotBlank(message = "Make must not be blank")
    @field:Size(max = 50, message = "Make must not exceed 255 characters")
    val make : String,
    @field:NotBlank(message = "Model must not be blank")
    @field:Size(max = 50, message = "Model must not exceed 255 characters")
    val model : String,
    @field:NotBlank(message = "Color must not be blank")
    @field:Size(max = 50, message = "Color must not exceed 255 characters")
    val color : String,
    @field:NotEmpty(message = "Name must not be blank")
    @field:Pattern(regexp = "ECONOMY|SEDAN|SUV|LUXURY", message = "Invalid status")
    val carType : String,
    @field:NotBlank(message = "Registration no must not be blank")
    val regNo : String,
    @field:NotBlank(message = "Insurance id must not be blank")
    @field:Pattern(regexp = "^[a-zA-Z0-9]+$", message = "Only alphanumeric characters are allowed")
    val insuranceId : String
)