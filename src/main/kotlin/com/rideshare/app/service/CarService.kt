package com.rideshare.app.service

import com.rideshare.app.payload.request.CarRequestPayload
import com.rideshare.app.payload.response.CarResponsePayload

interface CarService {
    fun create(car: CarRequestPayload): CarResponsePayload
    fun findAll(): List<CarResponsePayload>
    fun findById(id: String): CarResponsePayload
    fun update(carId: String, carRequestPayload: CarRequestPayload): CarResponsePayload
    fun delete(carId: String): Boolean

}