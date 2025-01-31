package com.rideshare.app.payload.response

import kotlinx.serialization.Serializable

@Serializable
data class RideResponsePayload(
    val id: String,
    val driverName: String,
    val status: String,
    val car: CarResponsePayload

)