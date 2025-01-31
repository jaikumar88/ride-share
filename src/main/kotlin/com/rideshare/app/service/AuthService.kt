package com.rideshare.app.service

import com.rideshare.app.models.User

interface AuthService {
    fun authenticate(email: kotlin.String, password: kotlin.String): User?
}