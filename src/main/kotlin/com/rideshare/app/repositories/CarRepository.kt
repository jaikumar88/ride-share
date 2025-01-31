package com.rideshare.app.repositories

import com.rideshare.app.models.Car
import com.rideshare.app.payload.request.CarRequestPayload

interface CarRepository {
    fun create(car: CarRequestPayload): Car
    fun update(id: String, car: CarRequestPayload): Car?
    fun findAll(): List<Car>
    fun findById(id: String): Car
    fun delete(id: String): Boolean
}