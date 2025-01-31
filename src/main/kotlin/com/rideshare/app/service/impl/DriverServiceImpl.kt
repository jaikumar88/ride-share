package com.rideshare.app.service.impl

import com.rideshare.app.exceptions.CarNotFoundException
import com.rideshare.app.exceptions.DriverFoundException
import com.rideshare.app.exceptions.DriverNotFoundException
import com.rideshare.app.exceptions.NotFoundException
import com.rideshare.app.exceptions.UserNotFoundException
import com.rideshare.app.models.Driver
import com.rideshare.app.payload.request.DriverRequestPayload
import com.rideshare.app.payload.request.StatusUpdateRequest
import com.rideshare.app.payload.request.UserRequestPayload
import com.rideshare.app.payload.response.CarResponsePayload
import com.rideshare.app.payload.response.DriverResponsePayload
import com.rideshare.app.payload.response.RoleResponsePayload
import com.rideshare.app.repositories.CarRepository
import com.rideshare.app.repositories.DriverRepository
import com.rideshare.app.repositories.UserRepository
import com.rideshare.app.security.SecurityUtils
import com.rideshare.app.service.DriverService
import org.jetbrains.exposed.sql.transactions.transaction
import org.slf4j.LoggerFactory
import sun.util.logging.resources.logging
import java.util.UUID

class DriverServiceImpl(
    val driverRepository: DriverRepository,
    val carRepository: CarRepository,
    val userRepository: UserRepository,
) : DriverService {

    val logger = LoggerFactory.getLogger("DriverService")
    override fun create(driverPayload: DriverRequestPayload): DriverResponsePayload = transaction() {

        val car = carRepository.findById(driverPayload.carId.toString()) ?: throw CarNotFoundException("Car not found")
        var userRequestPayload = UserRequestPayload(
            name = driverPayload.name,
            loginId = driverPayload.loginId,
            password = driverPayload.password,
            roleIds = driverPayload.roleIds,
            email = driverPayload.email
        )

        var driver: Driver? = null

        // Register driver and create user first
        val user = userRepository.create(userRequestPayload)

        // once user register successfully then create Driver record

        driverPayload?.let {
            driver = driverRepository.create(driverPayload, car, user)
        }

        (if (driver != null) payload(driver, true) else null)!!
    }


    override fun findAll(): List<DriverResponsePayload> {
        return transaction {
            driverRepository.findAll().map {
                payload(it, false)
            }
        }
    }

    override fun findById(id: String): DriverResponsePayload {
        val driver = driverRepository.findById(id) ?: throw DriverNotFoundException("User not found with ID $id")
        return transaction {
            payload(driver, false)
        }
    }


    override fun update(
        id: String,
        driverPayload: DriverRequestPayload,
    ): DriverResponsePayload = transaction(){
        val driver = driverRepository.findById(id) ?: throw UserNotFoundException("User not found with ID $id")
        val car = carRepository.findById(driver.car.toString()) ?: null
        val user =
            userRepository.findById(driver.user.toString()) ?: throw UserNotFoundException("User not found with ID $id")
        val updatedDriver = driverRepository.update(id, driverPayload, car, user)
            ?: throw NotFoundException("User not found with ID $id")

         transaction {
            payload(updatedDriver, false)
        }
    }

    /**
     * Delete driver
     */
    override fun delete(id: String): Boolean {
        if (!driverRepository.delete(id)) throw DriverNotFoundException("Driver not found with ID $id")
        return true
    }

    /**
     * Find next available driver in sequence
     */
    override fun findAvailableDriver(): DriverResponsePayload {
        val driver = driverRepository.findNextAvailableDriver() ?: throw DriverNotFoundException("Driver not found")
        if (driver != null) {
            return payload(driver, false)
        }
        throw NotFoundException("Driver not found")
    }

    /**
     * Update driver status
     */
    override fun updateDriverStatus(id: String?, status: StatusUpdateRequest): Boolean {
        var driverId: String? = null
        try {
            driverId = UUID.fromString(id)?.toString()
        } catch (e: Exception) {
            logger.info("Unable to parse the driver id")
        }
        try {
            driverId?.let {
                return driverRepository.updateDriverAvailability(driverId, status.status)
                    ?: throw NotFoundException("Driver not found")
            }

            status.loginId?.let {
                val user = userRepository.findUserByLogin(status.loginId)
                logger.info("Driver id: ${user?.id}")
                user?.let {
                    return driverRepository.updateDriverAvailabilityByLoginId(user?.id.toString(), status.status)
                        ?: throw NotFoundException("Driver not found")
                }
            }
        } catch (e: Exception) {
            logger.error("Driver already exist")
            throw DriverFoundException("Driver already exist")
        }
        return false
    }

    /**
     * Find all available drivers
     */
    override fun findAllAvailableDrivers(): List<DriverResponsePayload> {
        val drivers = driverRepository.findAllAvailableDrivers() ?: throw NotFoundException("Driver not found")
        return drivers.map {
            payload(it, false)
        }
    }

    /**
     * mapping driver response
     */
    private fun payload(driver: Driver, tokenRequired: Boolean): DriverResponsePayload = transaction() {
        DriverResponsePayload(
            id = driver.id.value.toString(),
            name = driver.user.name,
            loginId = driver.user.loginId,
            createdAt = driver.createdAt.toString(),
            updatedAt = driver.updatedAt.toString(),
            roles = driver.user.roles.map {
                RoleResponsePayload(
                    id = it.id.toString(),
                    name = it.name,
                    description = it.description,
                    createdAt = it.createdAt.toString(),
                    updatedAt = it.updatedAt.toString()
                )
            },
            token = if (tokenRequired) SecurityUtils.generateToken(driver.user.loginId,
                driver.user.roles.map { it.name }) else null,
            status = driver.status,
            car = CarResponsePayload(
                id = driver.car.id.toString(),
                make = driver.car.make,
                model = driver.car.model,
                color = driver.car.color,
                carType = driver.car.carType,
                regNo = driver.car.regNo,
                insuranceId = driver.car.insuranceId,
                createdAt = driver.car.createdAt.toString(),
                updatedAt = driver.car.updatedAt.toString()
            )
        )
    }

}