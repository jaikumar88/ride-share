package com.rideshare.app.repositories.impl

import com.rideshare.app.exceptions.NotFoundException
import com.rideshare.app.exceptions.RideNotFoundException
import com.rideshare.app.models.Driver
import com.rideshare.app.models.Ride
import com.rideshare.app.models.User
import com.rideshare.app.payload.request.RideRequestPayload
import com.rideshare.app.repositories.RideRepository
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID

class RideRepositoryImpl: RideRepository {
    override fun createRide(ride: RideRequestPayload, rideUser: User, rideDriver: Driver): Ride = transaction(){
        Ride.new {
            driver = rideDriver
            user= rideUser
            status = "ACTIVE"
        }
    }

    override fun findAll(): List<Ride> = transaction(){
        Ride.all().toList()
    }

    override fun findById(id: String): Ride = transaction(){
        Ride.findById(UUID.fromString(id))?: return@transaction throw RideNotFoundException(" Ride Not found")
    }

    override fun update(id: String, updatedRide: Ride): Ride = transaction() {
        val ride = Ride.findById(UUID.fromString(id)) ?: return@transaction throw RideNotFoundException("Ride not found")
        ride.apply {

            updatedRide.status?.let {
                status=updatedRide.status
            }
            updatedRide.user?.let {
                user = updatedRide.user
            }
            updatedRide.driver?.let {
                driver = ride.driver
            }
        }
    }

    override fun updateRideStatus(id: String, ride: Ride): Ride = transaction() {

        ride.apply {

            ride.status?.let {
                status=ride.status
            }

        }
    }

    override fun delete(ride: Ride): Boolean = transaction(){
        val ride = Ride.findById(UUID.fromString(id)) ?: return@transaction throw NotFoundException("Ride not found")
        ride?.let {
            ride.delete()
        }
        false
    }

}