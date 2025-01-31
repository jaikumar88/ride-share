package com.rideshare.app.extensions

import com.rideshare.app.models.User
import com.rideshare.app.payload.request.UserRequestPayload

fun User.toUser(userRequestPayload: UserRequestPayload): User? {
    return null
}