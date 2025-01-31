package com.rideshare.app.di

import com.rideshare.app.repositories.CarRepository
import com.rideshare.app.repositories.DriverRepository
import com.rideshare.app.repositories.RideRepository
import com.rideshare.app.repositories.RoleRepository
import com.rideshare.app.repositories.UserRepository

import com.rideshare.app.service.AuthService
import com.rideshare.app.service.CarService
import com.rideshare.app.service.DriverService
import com.rideshare.app.service.RideService
import com.rideshare.app.service.RoleService
import com.rideshare.app.service.UserService
import com.rideshare.app.service.impl.AuthServiceImpl
import com.rideshare.app.service.impl.CarServiceImpl
import com.rideshare.app.service.impl.DriverServiceImpl
import com.rideshare.app.service.impl.RideServiceImpl
import com.rideshare.app.service.impl.RoleServiceImpl
import com.rideshare.app.service.impl.UserServiceImpl

import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton


val serviceModule = DI.Module("serviceModule") {

    /**
     * Create User service bean
     */
    bind<UserService>() with singleton {
        UserServiceImpl(instance<UserRepository>())
    }

    /**
     * Create role service bean
     */

    bind<RoleService>() with singleton{
        RoleServiceImpl(instance<RoleRepository>())
    }
    /**
     * create Auth service bean
     */
    bind<AuthServiceImpl>() with singleton {
        AuthServiceImpl(instance<UserRepository>())
    }

    /**
     * Create Driver service bean
     */
    bind<DriverService>() with singleton {
        DriverServiceImpl(instance<DriverRepository>(), instance<CarRepository>(), instance<UserRepository>())
    }

    bind<CarService>() with singleton {
        CarServiceImpl(instance<CarRepository>())
    }

    bind<RideService>() with singleton {
        RideServiceImpl(instance<RideRepository>(), instance<UserRepository>(), instance<DriverRepository>())
    }



}