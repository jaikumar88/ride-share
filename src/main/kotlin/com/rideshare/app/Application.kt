package com.rideshare.app

import com.rideshare.app.config.appModule
import com.rideshare.app.config.initializeDBConfiguration
import com.rideshare.app.exceptions.configureExceptionHandling
import com.rideshare.app.routes.configureRoute

import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.*

import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.routing.routing
import org.kodein.di.DI

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {




    // Initialize DI if you are using it or pass
    // the service bean at initial if not using DI

    val diContainer = DI {
        import(appModule)
    }

    // init route mapping and pass DI container to inject all beans
        routing {
            configureRoute(diContainer)
        }
    // init database

    initializeDBConfiguration(environment)

    // add content negotiation which handle json formatting

    install(ContentNegotiation) {

        json() // @RequestBody
    }


    // Configure exception handling

    configureExceptionHandling()



}
