package com.rideshare.app.repositories.impl

import com.rideshare.app.exceptions.NotFoundException
import com.rideshare.app.models.Car
import com.rideshare.app.payload.request.CarRequestPayload
import com.rideshare.app.repositories.CarRepository
import org.jetbrains.exposed.sql.transactions.transaction

import java.util.UUID

class CarRepositoryImpl : CarRepository {

    override fun create(car: CarRequestPayload): Car = transaction() {
        Car.new {
            make = car.make
            model = car.model
            color = car.color
            carType = car.carType
            regNo = car.regNo
            insuranceId = car.insuranceId
        }
    }

    override fun update(id: String, updatedCar: CarRequestPayload): Car? = transaction() {
        val car =
            Car.findById(UUID.fromString(id)) ?: return@transaction throw NotFoundException("Car not found exception")
        car.apply {
            updatedCar.make?.let {
                make = updatedCar.make
            }
            updatedCar.model?.let {
                model = updatedCar.model
            }
            updatedCar.color?.let {
                color = updatedCar.color
            }
            updatedCar.carType?.let {
                carType = updatedCar.carType
            }
            updatedCar.regNo?.let {
                regNo = updatedCar.regNo
            }
            updatedCar.insuranceId?.let {
                insuranceId = updatedCar.insuranceId
            }


        }
    }

    override fun findAll(): List<Car> = transaction() {
        Car.all().toList()
    }

    override fun findById(id: String): Car = transaction() {
        Car.findById(UUID.fromString(id)) ?: return@transaction throw NotFoundException("Car not found exception")
    }

    override fun delete(id: String): Boolean = transaction(){
        val car = Car.findById(UUID.fromString(id))
        car?.let {
            car.delete()
            true
        }
        false
    }

}