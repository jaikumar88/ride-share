package com.rideshare.app.config

import com.rideshare.app.di.repositoryModule
import com.rideshare.app.di.serviceModule
import org.kodein.di.DI

val appModule = DI.Module("AppModule") {
    import(repositoryModule) // Import repository bindings
    import(serviceModule)    // Import service bindings
}
