package com.rideshare.app.service.impl

import com.rideshare.app.exceptions.CarNotFoundException
import com.rideshare.app.exceptions.NotFoundException
import com.rideshare.app.models.Car
import com.rideshare.app.payload.request.CarRequestPayload
import com.rideshare.app.payload.response.CarResponsePayload
import com.rideshare.app.payload.response.RoleResponsePayload
import com.rideshare.app.repositories.CarRepository
import com.rideshare.app.service.CarService
import jdk.jfr.internal.Repository
import org.jetbrains.exposed.exceptions.ExposedSQLException
import org.jetbrains.exposed.sql.transactions.transaction

class CarServiceImpl (val carRepository: CarRepository): CarService{

    override fun create(carPayload: CarRequestPayload): CarResponsePayload {
        try {
            val car = carRepository.create(carPayload)
            return payload(car)
        } catch ( exsql: ExposedSQLException){
            throw exsql
        }
    }

    override fun findAll(): List<CarResponsePayload> {
        return carRepository.findAll().map {
            payload(it)
        }
    }

    override fun findById(id: String): CarResponsePayload {
        val car = carRepository.findById(id) ?: throw CarNotFoundException("Car not found with ID $id")
        return payload(car)
    }

    override fun update(
        carId: String,
        carRequestPayload: CarRequestPayload,
    ): CarResponsePayload {
        val car = carRepository.findById(carId) ?: throw CarNotFoundException("Car not found with ID $carId")
        car.apply {
            carRequestPayload.make?.let {
                make = carRequestPayload.make
            }
            carRequestPayload.carType?.let {
                carType = carRequestPayload.carType
            }
            carRequestPayload.model?.let {
                model = carRequestPayload.model
            }
            carRequestPayload.regNo?.let {
                regNo = carRequestPayload.regNo
            }
            carRequestPayload.insuranceId?.let {
                insuranceId= carRequestPayload.insuranceId
            }
        }
        return payload(car)
    }

    override fun delete(carId: String): Boolean {
        if (!carRepository.delete(carId)) throw CarNotFoundException("Car not found with ID $carId")
        return true
    }

    private fun payload(car: Car) : CarResponsePayload = transaction(){
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