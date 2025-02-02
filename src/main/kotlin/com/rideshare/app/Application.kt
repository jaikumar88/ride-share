package com.rideshare.app

import com.rideshare.app.config.appModule
import com.rideshare.app.config.initDatabase
import com.rideshare.app.config.initializeDBConfiguration
import com.rideshare.app.exceptions.configureExceptionHandling
import com.rideshare.app.routes.configureRoute
import io.ktor.http.HttpStatusCode

import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.*
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty

import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.response.respondText
import io.ktor.server.routing.routing
import kotlinx.coroutines.runBlocking
import org.kodein.di.DI

fun main(args: Array<String>) {
    embeddedServer(Netty, port = 9099) {


        // Initialize DI if you are using it or pass
        // the service bean at initial if not using DI

        val diContainer = DI {
            import(appModule)
        }

        // init route mapping and pass DI container to inject all beans
        routing {
            configureRoute(diContainer)
        }

        // Install plugins
        install(StatusPages) {
            exception<Throwable> { call, cause ->
                call.respondText(
                    text = "Internal Server Error: ${cause.localizedMessage}",
                    status = HttpStatusCode.InternalServerError
                )
            }
        }

        // init database

        //initializeDBConfiguration(environment)
        runBlocking(){
            initDatabase()
        }

        // add content negotiation which handle json formatting

        install(ContentNegotiation) {

            json() // @RequestBody
        }


        // Configure exception handling

        configureExceptionHandling()

    }.start(wait = true)


}
