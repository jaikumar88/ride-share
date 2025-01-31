package com.rideshare.app.validator

import jakarta.validation.Validation
import jakarta.validation.Validator
import jakarta.validation.ValidatorFactory
import org.slf4j.LoggerFactory

object ValidatorProvider {
    private val logger = LoggerFactory.getLogger(ValidatorProvider::class.java)

    private val factory: ValidatorFactory by lazy {
        try {
            logger.info("Initializing ValidatorFactory...")
            Validation.buildDefaultValidatorFactory()
        } catch (e: Exception) {
            logger.error("Failed to initialize ValidatorFactory: ${e.message}")
            throw e
        }
    }

    val validator: Validator by lazy {
        factory.validator
    }
}

fun <T> T.validate(): T {
    val violations = ValidatorProvider.validator.validate(this)
    if (violations.isNotEmpty()) {
        throw jakarta.validation.ConstraintViolationException(violations)
    }
    return this
}