package com.rideshare.app.payload.response

import com.rideshare.app.models.Car
import kotlinx.serialization.Serializable

@Serializable
data class DriverResponsePayload(
    val id: String,
    val name: String,
    val loginId: String,
    val createdAt: String,
    val updatedAt: String,
    val roles: List<RoleResponsePayload>?,
    val token: String? =null,
    val status: String,
    val car: CarResponsePayload?
)