package com.rideshare.app.routes

import com.rideshare.app.service.AuthService
import com.rideshare.app.service.CarService
import com.rideshare.app.service.DriverService
import com.rideshare.app.service.RideService
import com.rideshare.app.service.RoleService
import com.rideshare.app.service.UserService
import com.rideshare.app.service.impl.AuthServiceImpl
import io.ktor.server.routing.Route
import org.kodein.di.DI
import org.kodein.di.direct
import org.kodein.di.instance

fun Route.configureRoute(diContainer: DI) {

    // Route for user authentication
    val authService: AuthServiceImpl = diContainer.direct.instance()
    userAuthRoute(authService)

    // Route configuration for Role
    val roleService: RoleService = diContainer.direct.instance()
    roleRoutes(roleService)

    // Route mapping for user operation
    val userService: UserService = diContainer.direct.instance()
    userRoute(userService)

    // Route mapping for user operation
    val driverService: DriverService = diContainer.direct.instance()
    driverRoute(driverService)

    val carService: CarService = diContainer.direct.instance()
    carRoute(carService)

    val rideService: RideService = diContainer.direct.instance()
    rideRoute(rideService)

}