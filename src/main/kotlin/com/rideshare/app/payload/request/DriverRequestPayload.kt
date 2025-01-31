package com.rideshare.app.payload.request

import com.rideshare.app.config.DriverStatus
import com.rideshare.app.validator.ValidEnum
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size
import kotlinx.serialization.Serializable

@Serializable
data class DriverRequestPayload(
    @field:NotBlank(message = "Name must not be blank")
    @field:Size(max = 255, message = "Name must not exceed 255 characters")
    val name: String,
    @field:NotBlank(message = "Email must not be blank")
    @field:Email(message = "Invalid email format")
    val email: String,
    @field:NotBlank(message = "Login id must not be blank")
    @field:Pattern(regexp = "^[a-zA-Z0-9]+$", message = "Only alphanumeric characters are allowed")
    val loginId: String,
    @field:NotBlank(message = "Password must not be blank")
    @field:Size(min = 8, message = "Password must be at least 8 characters long")
    val password: String,
    @field:ValidEnum(enumClass = DriverStatus::class, message = "Invalid driver status")
    val status: String?, // initial status for driver will be created
    val carId: String?,
    @field:NotEmpty(message = "At least one role ID must be provided")
    val roleIds: List<String>,
)