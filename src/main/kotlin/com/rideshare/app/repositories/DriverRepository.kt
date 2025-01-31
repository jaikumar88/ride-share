package com.rideshare.app.repositories

import com.rideshare.app.models.Car
import com.rideshare.app.models.Driver
import com.rideshare.app.models.User
import com.rideshare.app.payload.request.DriverRequestPayload

interface DriverRepository {
    fun create(driverRequestPayload: DriverRequestPayload, assignedCar: Car?, driverUser: User?): Driver
    fun findAll(): List<Driver>
    fun findById(id: String): Driver?
    fun findNextAvailableDriver(): Driver?
    fun findAllAvailableDrivers(): List<Driver>
    fun updateDriverAvailability(id: String, status: String): Boolean
    fun updateDriverAvailabilityByLoginId(loginId: String, status: String): Boolean
    fun update(id: String, driverRequestPayload: DriverRequestPayload, car: Car?, user: User?): Driver?
    fun delete(id: String): Boolean
}