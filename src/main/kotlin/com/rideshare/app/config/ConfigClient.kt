import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.client.engine.cio.*
import io.ktor.client.engine.okhttp.*

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json


@Serializable
data class ConfigResponse(
    val propertySources: List<PropertySource>
)

@Serializable
data class PropertySource(
    val source: Map<String, String>
)

object ConfigClient {

    private val client = HttpClient(OkHttp)

    suspend fun fetchConfig(application: String): Map<String, String> {
        val profile = System.getenv("SPRING_PROFILES_ACTIVE") ?: "default"  // Read from env var
        val url = "http://localhost:8888/$application/$profile"

        val response: HttpResponse = client.get(url)
        if (response.status.value == 200) {
            val jsonResponse: String = response.body()
            val config = Json.decodeFromString<ConfigResponse>(jsonResponse)
            return config.propertySources.firstOrNull()?.source ?: emptyMap()
        } else {
            throw Exception("❌ Failed to fetch configuration: ${response.status}")
        }
    }
}
