package com.rideshare.app.repositories.impl

import com.rideshare.app.exceptions.NotFoundException
import com.rideshare.app.models.Car
import com.rideshare.app.models.Driver
import com.rideshare.app.models.DriverTable
import com.rideshare.app.models.User
import com.rideshare.app.payload.request.DriverRequestPayload
import com.rideshare.app.payload.request.StatusUpdateRequest
import com.rideshare.app.repositories.DriverRepository
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDateTime
import java.util.UUID
import kotlinx.datetime.toKotlinLocalDateTime
import org.jetbrains.exposed.sql.transactions.transaction


class DriverRepositoryImpl : DriverRepository {
    /**
     * Create user
     */
    override fun create(driverRequestPayload: DriverRequestPayload, car: Car?, user: User?): Driver = transaction() {
        val driver = Driver.new {
            this.status = driverRequestPayload.status.toString()
            if (user != null) {
                this.user = user
            }
            if (car != null) {
                this.car = car
            }
            createdAt= java.time.LocalDateTime.now().toKotlinLocalDateTime()
            updatedAt=java.time.LocalDateTime.now().toKotlinLocalDateTime()
        }
       driver
    }

    /**
     * Find all user
     */
    override fun findAll(): List<Driver> = transaction() {
        Driver.all().toList()
    }

    /**
     * Find by id
     */
    override fun findById(id: String): Driver? = transaction() {
        Driver.findById(UUID.fromString(id))
    }

    /**
     * Find next available driver
     */
    override fun findNextAvailableDriver(): Driver? {
        return transaction() {
            Driver.find { DriverTable.status eq "AVAILABLE" }
                .orderBy(DriverTable.lastAssignedTime to SortOrder.ASC) // Ensure sequential order by last assigned time
                .limit(1)                             // Fetch the first available driver
                .firstOrNull()
        }
    }

    override fun findAllAvailableDrivers(): List<Driver> {
        return transaction() {
            Driver.find { DriverTable.status eq "AVAILABLE" }
                .orderBy(DriverTable.lastAssignedTime to SortOrder.ASC).toList() // Ensure sequential order by last assigned time
        }
    }

    override fun updateDriverAvailability(id: String, updatedStatus: String): Boolean = transaction() {
        val driver =
            Driver.findById(UUID.fromString(id)) ?: return@transaction throw NotFoundException("Driver not found")
        driver.apply {
            status = updatedStatus
            lastAssignedTime = LocalDateTime.now().toKotlinLocalDateTime()
            true
        }
        false
    }

    /**
     * Update driver by login id
     */
    override fun updateDriverAvailabilityByLoginId(loginId: String, status: String): Boolean {
        return transaction() {
            val driver = Driver.find { DriverTable.userId eq UUID.fromString(loginId) }
                .orderBy(DriverTable.lastAssignedTime to SortOrder.ASC).toList().firstOrNull()

            (if(driver != null ){
                driver.apply {
                    this.status = status
                }
                true
            }  else {
                false
            })
        }
    }

    /**
     * Update User
     */
    override fun update(id: String, driverRequestPayload: DriverRequestPayload, car: Car?, user: User?): Driver? = transaction() {
        val driver =
            Driver.findById(UUID.fromString(id)) ?: return@transaction throw NotFoundException("Driver not found")
        driver.apply {
            if (car != null) {
                this.car = car
            }
            driverRequestPayload.status.let {
                status = driverRequestPayload.status.toString()
            }
        }
    }

    /**
     * Delete user
     */
    override fun delete(id: String): Boolean = transaction() {
        val driver = Driver.findById(UUID.fromString(id))
        driver?.let {
            driver.delete()
        }
        false
    }

}