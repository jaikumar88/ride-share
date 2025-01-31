package com.rideshare.app.service.impl

import com.rideshare.app.exceptions.NoDriverAvailable
import com.rideshare.app.exceptions.NotFoundException
import com.rideshare.app.exceptions.RideNotFoundException
import com.rideshare.app.exceptions.UserNotFoundException
import com.rideshare.app.models.Car
import com.rideshare.app.models.Driver
import com.rideshare.app.models.Ride
import com.rideshare.app.models.User
import com.rideshare.app.payload.request.RideRequestPayload
import com.rideshare.app.payload.request.RideStatusPayload
import com.rideshare.app.payload.response.CarResponsePayload
import com.rideshare.app.payload.response.RideResponsePayload
import com.rideshare.app.repositories.DriverRepository
import com.rideshare.app.repositories.RideRepository
import com.rideshare.app.repositories.UserRepository
import com.rideshare.app.service.RideService
import org.jetbrains.exposed.sql.transactions.transaction
import org.slf4j.LoggerFactory

class RideServiceImpl(
    val rideRepository: RideRepository,
    val userRepository: UserRepository,
    val driverRepository: DriverRepository,
) : RideService {

    val logger = LoggerFactory.getLogger("RideService")
    override fun bookRide(request: RideRequestPayload): RideResponsePayload? {
        try {
            val user = userRepository.findById(request.userId) ?: throw UserNotFoundException("User not found")
            val driver = driverRepository.findNextAvailableDriver() ?: throw NoDriverAvailable("Currently there is no driver available please try again after sometime")

            val ride = rideRepository.createRide(request, user as User, driver as Driver)

            driverRepository.updateDriverAvailability(driver.id.toString(), "ASSIGNED")
            return transaction() {
                RideResponsePayload(
                    id = ride.id.toString(),
                    driverName = ride.driver.user.name,
                    status = ride.status,
                    car = payload(ride.driver.car)
                )
            } as RideResponsePayload
        }catch (e: NotFoundException){
            logger.info(e.message)
            throw e
        }
        catch (e: Exception){
            logger.info("Unable to create ride")
            throw e
        }
        return null
    }

    override fun getALlActiveRides(): List<RideResponsePayload> {
        return rideRepository.findAll().map {
           prepareResponse(it)
        }
    }

    override fun findAllRides(): List<RideResponsePayload> =transaction(){
         rideRepository.findAll().map {
            prepareResponse(it)
        }
    }

    override fun updateStatus(id: String, payload: RideStatusPayload): Boolean = transaction(){
        val ride = rideRepository.findById(id)?: throw RideNotFoundException("Ride not found")
        ride.apply {
            status = payload.status
        }
        //rideRepository.updateRideStatus(id, ride)

        if(payload.status == "COMPLETE" || payload.status == "CANCEL") {
            driverRepository.updateDriverAvailability(ride.driver.id.toString(), "AVAILABLE")
        }
        ride != null
    }

    override fun findById(id: String): RideResponsePayload? {
        val ride =  rideRepository.findById(id)?: throw RideNotFoundException("There is no ride found for id: $id")
          return  ride?.let {
            prepareResponse(it)
        }
    }

    private fun prepareResponse(ride: Ride): RideResponsePayload = transaction(){
        RideResponsePayload(
            id = ride.id.toString(),
            driverName = ride.driver.user.name,
            status = ride.status,
            car = payload(ride.driver.car)
        )
    }

    private fun payload(car: Car): CarResponsePayload = transaction() {
        CarResponsePayload(
            id = car.id.value.toString(),
            make = car.make,
            model = car.model,
            color = car.color,
            carType = car.carType,
            regNo = car.regNo,
            insuranceId = car.insuranceId,
            createdAt = car.createdAt.toString(),
            updatedAt = car.updatedAt.toString()
        )
    }
}