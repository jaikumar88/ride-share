package com.rideshare.app.exceptions

open class ApplicationException(message: String) : RuntimeException(message)

class NotFoundException(message: String) : ApplicationException(message)

class UserNotFoundException(message: String): Exception(message) {
    override fun toString(): String {
        return super.toString()
    }
}

class BadRequestException(message: String) : RuntimeException(message)

class NoDriverAvailable(message: String): Exception(message)

class NoRideAvailable(message: String): Exception(message)

class RoleNotFoundException(message: String): Exception(message)

class UserAlreadyExistException(message : String): Exception(message)

class DriverFoundException(message: String): Exception(message)

class DriverNotFoundException(message: String): Exception(message)

class CarNotFoundException(message: String): Exception(message)

class RideNotFoundException(message: String): Exception(message)

class UserConflictException(message: String): Exception(message)

class DriverConflictException(message: String): Exception(message)

class RoleConflictException (message: String): Exception(message)
