package com.rideshare.app.service

import com.rideshare.app.payload.request.DriverRequestPayload
import com.rideshare.app.payload.request.StatusUpdateRequest
import com.rideshare.app.payload.response.DriverResponsePayload

interface DriverService {
    fun create(driverPayload: DriverRequestPayload): DriverResponsePayload
    fun findAll(): List<DriverResponsePayload>
    fun findById(id: String): DriverResponsePayload
    fun update(id: String, userPayload: DriverRequestPayload): DriverResponsePayload
    fun delete(id: String): Boolean
    fun findAvailableDriver() : DriverResponsePayload
    fun updateDriverStatus(id: String?, status: StatusUpdateRequest): Boolean
    fun findAllAvailableDrivers(): List<DriverResponsePayload>
}