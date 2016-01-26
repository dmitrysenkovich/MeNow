package com.sam_solutions.app.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Actual phone validator.
 */
public class PhoneValidator implements ConstraintValidator<Phone, String> {
    /**
     * For interface implementation only.
     * @param paramA dummy parameter.
     */
    @Override
    public void initialize(Phone paramA) {
    }

    /**
     * Checks if phone in string
     * matcher some pattern.
     * @param phoneNo phone number in string.
     * @param ctx provides contextual data when applying validator.
     * @return check result.
     */
    @Override
    public boolean isValid(String phoneNo, ConstraintValidatorContext ctx) {
        if(phoneNo == null || phoneNo.isEmpty()) {
            return false;
        }

        return phoneNo.matches("^([0-9\\(\\)\\\\+ \\-]*)$");
    }
}