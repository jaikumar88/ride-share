package com.rideshare.app.service

import com.rideshare.app.payload.request.RideRequestPayload
import com.rideshare.app.payload.request.RideStatusPayload
import com.rideshare.app.payload.response.RideResponsePayload
import io.ktor.util.reflect.TypeInfo

interface RideService{
    fun bookRide(request: RideRequestPayload): RideResponsePayload?
    fun getALlActiveRides(): List<RideResponsePayload>
    fun findAllRides(): List<RideResponsePayload>
    fun updateStatus(id: String, payload: RideStatusPayload): Boolean
    fun findById(string: String): RideResponsePayload?
}