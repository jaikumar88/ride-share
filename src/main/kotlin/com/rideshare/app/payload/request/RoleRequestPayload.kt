package com.rideshare.app.payload.request

import jakarta.validation.constraints.*

@kotlinx.serialization.Serializable



data class RoleRequestPayload(
    @field:NotBlank(message = "Role name must not be blank")
    @field:Size(max = 50, message = "Role name must not exceed 50 characters")
    val name: String,

    @field:Size(max = 255, message = "Description must not exceed 255 characters")
    val description: String?
)