package com.virtualdogbert.constraint

import grails.validation.AbstractConstraint
import grails.validation.ConstrainedProperty
import groovy.transform.CompileStatic
import org.springframework.validation.Errors

import java.util.regex.Pattern

/**
 * This allows you to specify a regex, and if it does match you will get an error, the opposite of the built-in matches constraint.
 */
@CompileStatic
class NotMatchesConstraint extends AbstractConstraint {
    String check

    static final String NOT_MATCHES_CONSTRAINT               = "notMatches"
    static final String DEFAULT_NOT_NOT_MATCHES_MESSAGE_CODE = "default.not.matches.message"

    boolean supports(Class type) {
        return type != null
    }

    /**
     * Sets the check string parameter, which will be compiled into a regex.
     *
     * @param constraintParameter sets the check parameter, which will be used as a regex for validating a constraint property.
     */
    void setParameter(Object constraintParameter) {
        if (!(constraintParameter instanceof String)) {
            throw new IllegalArgumentException("Parameter for constraint [$NOT_MATCHES_CONSTRAINT] of property [$constraintPropertyName] of class [$constraintOwningClass] must be a java.lang.String")
        }

        this.check = (String) constraintParameter
        super.setParameter(constraintParameter)
    }

    String getName() {
        return NOT_MATCHES_CONSTRAINT
    }

    /**
     * Processes the validation of the propertyValue, against the check pattern set, and setting and calling rejectValue
     * if the propertyValue matches the check pattern.
     *
     * @param target The target field to verify.
     * @param propertyValue the property value of the field.
     * @param errors Errors to be sent by rejectValues, if the propertyValue matches, the check pattern.
     */
    protected void processValidate(Object target, Object propertyValue, Errors errors) {
        // Check that the value matches the regex
        boolean validation = propertyValue ==~ Pattern.compile(check)

        //if the value matches the regex add an error
        if (validation) {
            Object[] args = [constraintPropertyName, constraintOwningClass, propertyValue, check].toArray()
            super.rejectValue(target, errors, DEFAULT_NOT_NOT_MATCHES_MESSAGE_CODE, ConstrainedProperty.NOT_PREFIX + NOT_MATCHES_CONSTRAINT, args)
        }
    }

}