package com.rideshare.app.payload.request

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import kotlinx.serialization.Serializable

@Serializable
data class RideRequestPayload(
    @field:NotBlank(message = "Login id must not be blank")
    val userId: String
)