package com.rideshare.app.repositories

import com.rideshare.app.models.Driver
import com.rideshare.app.models.Ride
import com.rideshare.app.models.User
import com.rideshare.app.payload.request.RideRequestPayload

interface RideRepository{
    fun createRide(ride: RideRequestPayload, user: User, driver: Driver): Ride
    fun findAll(): List<Ride>
    fun findById(id: String): Ride
    fun update(id: String, ride: Ride): Ride
    fun updateRideStatus(id: String, ride: Ride): Ride
    fun delete(ride: Ride): Boolean
}