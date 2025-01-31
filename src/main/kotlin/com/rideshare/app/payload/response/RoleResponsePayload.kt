package com.rideshare.app.payload.response

import com.rideshare.app.models.Role
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.serialization.Serializable
import java.time.format.DateTimeFormatter


@Serializable
data class RoleResponsePayload(
    val id: String,
    val name: String,
    val description: String?,
    val createdAt: String,
    val updatedAt: String
)