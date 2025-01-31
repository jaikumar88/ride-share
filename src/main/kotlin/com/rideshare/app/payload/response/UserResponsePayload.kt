package com.rideshare.app.payload.response

import kotlinx.serialization.Serializable

@Serializable
data class UserResponsePayload(
    val id: String,
    val name: String,
    val loginId: String,
    val token: String? =null,
    val roles: List<RoleResponsePayload>?,
    val createdAt: String,
    val updatedAt: String
)