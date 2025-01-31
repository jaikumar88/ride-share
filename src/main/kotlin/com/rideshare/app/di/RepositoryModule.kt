package com.rideshare.app.di

import com.rideshare.app.repositories.CarRepository
import com.rideshare.app.repositories.DriverRepository
import com.rideshare.app.repositories.RideRepository
import com.rideshare.app.repositories.RoleRepository
import com.rideshare.app.repositories.UserRepository
import com.rideshare.app.repositories.impl.CarRepositoryImpl
import com.rideshare.app.repositories.impl.DriverRepositoryImpl
import com.rideshare.app.repositories.impl.RideRepositoryImpl
import com.rideshare.app.repositories.impl.RoleRepositoryImpl
import com.rideshare.app.repositories.impl.UserRepositoryImpl
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.singleton

val repositoryModule = DI.Module("repositoryModule") {
    bind<UserRepository>() with singleton { UserRepositoryImpl() }
    bind<RoleRepository>() with singleton { RoleRepositoryImpl() }
    bind<DriverRepository>() with singleton { DriverRepositoryImpl() }
    bind<CarRepository>() with singleton { CarRepositoryImpl() }
    bind<RideRepository>() with singleton { RideRepositoryImpl() }

}