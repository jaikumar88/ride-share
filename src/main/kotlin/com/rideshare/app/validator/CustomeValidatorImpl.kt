package com.rideshare.app.validator

import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext

class EnumValidator : ConstraintValidator<ValidEnum, String> {
    private lateinit var allowedValues: List<String>

    override fun initialize(annotation: ValidEnum) {
        allowedValues = annotation.enumClass.java.enumConstants.map { it.name }
    }

    override fun isValid(value: String?, context: ConstraintValidatorContext): Boolean {
        return value != null && allowedValues.contains(value)
    }
}
