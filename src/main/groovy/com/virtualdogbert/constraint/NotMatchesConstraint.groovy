package com.virtualdogbert.constraint

import grails.gorm.validation.ConstrainedProperty
import groovy.transform.CompileStatic
import org.grails.datastore.gorm.validation.constraints.AbstractConstraint
import org.springframework.context.MessageSource
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

    NotMatchesConstraint(Class<?> constraintOwningClass, String constraintPropertyName, Object constraintParameter, MessageSource messageSource) {
        super(constraintOwningClass, constraintPropertyName, constraintParameter, messageSource)

        if (!(constraintParameter instanceof String)) {
            throw new IllegalArgumentException("Parameter for constraint [$NOT_MATCHES_CONSTRAINT] of property [$constraintPropertyName] of class [$constraintOwningClass] must be a java.lang.String")
        }

        this.check = (String) constraintParameter
    }

    @Override
    protected Object validateParameter(Object constraintParameter) {
        return constraintParameter
    }

    boolean supports(Class type) {
        return type != null
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