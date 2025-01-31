package com.rideshare.app.security

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import java.util.Date

object SecurityUtils {
    private const val SECRET = "your-secret-key"
    private const val ISSUER = "com.infogain"
    private const val AUDIENCE = "com.rideshare.audience"
    private const val EXPIRATION_TIME = 60 * 60 * 1000 // 1 hour in milliseconds

    private val algorithm = Algorithm.HMAC256(SECRET)

    fun generateToken(loginId: String, roles: List<String>): String {
        val expiration = Date(System.currentTimeMillis() + EXPIRATION_TIME)
        return JWT.create()
            .withIssuer(ISSUER)
            .withAudience(AUDIENCE)
            .withClaim("loginId", loginId)
            .withClaim("roles", roles)
            .withExpiresAt(expiration)
            .sign(algorithm)
    }

    fun validateToken(token: String): Boolean {
        return try {
            val verifier = JWT.require(algorithm)
                .withIssuer(ISSUER)
                .withAudience(AUDIENCE)
                .build()
            val decodedJWT = verifier.verify(token)
            decodedJWT.expiresAt.after(Date())
        } catch (e: Exception) {
            false
        }
    }

    fun extractEmailFromToken(token: String): String? {
        return try {
            val verifier = JWT.require(algorithm)
                .withIssuer(ISSUER)
                .withAudience(AUDIENCE)
                .build()
            val decodedJWT = verifier.verify(token)
            decodedJWT.getClaim("email").asString()
        } catch (e: Exception) {
            null
        }
    }

    fun extractRolesFromToken(token: String): List<String> {
        return try {
            val verifier = JWT.require(algorithm)
                .withIssuer(ISSUER)
                .withAudience(AUDIENCE)
                .build()
            val decodedJWT = verifier.verify(token)
            decodedJWT.getClaim("roles").asList(String::class.java)
        } catch (e: Exception) {
            emptyList()
        }
    }


}