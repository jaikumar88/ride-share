package com.rideshare.app.payload.request

import com.rideshare.app.config.DriverStatus
import com.rideshare.app.config.RideStatus
import com.rideshare.app.validator.ValidEnum
import kotlinx.serialization.Serializable

@Serializable
data class RideStatusPayload(
    @field:ValidEnum(enumClass = RideStatus::class, message = "Invalid ride status")
    val status : String
)