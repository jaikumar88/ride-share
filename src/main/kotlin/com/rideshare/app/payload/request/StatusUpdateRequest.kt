package com.rideshare.app.payload.request

import com.rideshare.app.config.DriverStatus
import com.rideshare.app.validator.ValidEnum
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size
import kotlinx.serialization.Serializable

@Serializable
data class StatusUpdateRequest(
    @field:NotBlank(message = "Login id must not be blank")
    @field:Pattern(regexp = "^[a-zA-Z0-9]+$", message = "Only alphanumeric characters are allowed")
    val loginId: String,
    @field:NotBlank(message = "Status can not be blank")
    @field:ValidEnum(enumClass = DriverStatus::class, message = "Invalid driver status")
    val status: String
)