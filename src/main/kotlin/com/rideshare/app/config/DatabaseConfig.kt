package com.rideshare.app.config

import kotlinx.serialization.Serializable

@Serializable
data class DatabaseConfig(
    val driver: String,
    val url: String,
    val user: String,
    val password: String
)