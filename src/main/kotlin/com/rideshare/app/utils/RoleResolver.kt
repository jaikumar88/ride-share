package com.rideshare.app.utils




import com.rideshare.app.models.Role
import com.rideshare.app.models.RolesTable

import org.jetbrains.exposed.sql.transactions.transaction
import java.time.format.DateTimeFormatter
import java.util.UUID
import kotlin.collections.map

val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

object RoleResolver {
    fun resolveRoleNames(roleIds: List<String>): List<String> {
        return transaction {
            val uuids = roleIds.map { UUID.fromString(it) }
            Role.find { RolesTable.id inList uuids }.map { it.name }
        }
    }
}

