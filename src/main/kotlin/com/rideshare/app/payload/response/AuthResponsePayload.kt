package com.rideshare.app.payload.response

import kotlinx.serialization.Serializable

@Serializable
data class AuthResponsePayload(
    val id: String,
    val name: String,

    val email: String,
    val roles: List<String>,
    val token: String
)
