package com.rideshare.app.payload.response

import kotlinx.serialization.Serializable

@Serializable
data class CarResponsePayload(
    val id: String,
    val make : String,
    val model : String,
    val color : String,
    val carType : String,
    val regNo : String,
    val insuranceId : String,
    val createdAt: String,
    val updatedAt: String
)