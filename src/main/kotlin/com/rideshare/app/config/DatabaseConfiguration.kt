package com.rideshare.app.config



import com.rideshare.app.exceptions.NotFoundException
import com.rideshare.app.models.CarsTable
import com.rideshare.app.models.DriverTable
import com.rideshare.app.models.RideTable
import com.rideshare.app.models.RolesTable
import com.rideshare.app.models.UserRolesTable
import com.rideshare.app.models.UsersTable
import io.ktor.server.application.ApplicationEnvironment
import kotlinx.serialization.Serializable
import net.mamoe.yamlkt.Yaml

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import java.io.File


data class AppInfo(val name: String, val version: String, val server: ServerInfo)
data class ServerInfo(val host: String, val port: String)

@Serializable
data class AppConfig(
    val database: DatabaseConfig
)

fun initializeDBConfiguration(environment: ApplicationEnvironment) {

    val yamlFile = File("src/main/resources/application.yaml")

    // Read the YAML file content as a string
    val yamlContent = yamlFile.readText()

    // Parse the YAML content into a Map or a data class
    val config = Yaml.decodeFromString(AppConfig.serializer(), yamlContent)

    // Another way to read properties from yaml file
   /* val driver = environment.config.propertyOrNull("database.driver")?.getString() ?: throw NotFoundException("Driver is not configured")
    val url = environment.config.propertyOrNull("database.url")?.getString() ?: throw NotFoundException("URL is not configured")
    val user = environment.config.propertyOrNull("database.user")?.getString() ?: throw NotFoundException("user is not configured")
    val password = environment.config.propertyOrNull("database.password")?.getString() ?: throw NotFoundException("Password is not configured")*/

    Database.connect(
        driver = config.database.driver,
        url =  config.database.url,
        user = config.database.user,
        password = config.database.password
    )

    transaction(){
        SchemaUtils.create(UsersTable)
        SchemaUtils.create(RolesTable)
        SchemaUtils.create(UserRolesTable)
        SchemaUtils.create(CarsTable)
        SchemaUtils.create(DriverTable)
        SchemaUtils.create(RideTable)
    }
}