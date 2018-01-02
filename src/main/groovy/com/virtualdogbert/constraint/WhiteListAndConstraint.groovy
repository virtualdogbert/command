package com.virtualdogbert.constraint

import grails.validation.AbstractConstraint
import grails.validation.ConstrainedProperty
import groovy.transform.CompileStatic
import org.springframework.validation.Errors

import java.util.regex.Pattern

/**
 * This allows you to specify a list of regexes, and the value of the property must satisfy all of them, because the checks
 * are combined with an AND
 */
@CompileStatic
class WhiteListAndConstraint extends AbstractConstraint {
    List<String> checks

    static final String WHITE_LIST_AND_CONSTRAINT               = "whiteListAnd"
    static final String DEFAULT_NOT_WHITE_LIST_AND_MESSAGE_CODE = "default.not.whiteListAnd.message"

    boolean supports(Class type) {
        return type != null
    }

    /**
     * Sets checks list based of the constraintParameter passed in. The constraintParameter must be a List of Strings.
     *
     * @param constraintParameter A list of Strings used by processValidate, as a white list of regexes.
     */
    void setParameter(Object constraintParameter) {
        if (!(constraintParameter instanceof List)) {
            throw new IllegalArgumentException("Parameter for constraint [$WHITE_LIST_AND_CONSTRAINT] of property [$constraintPropertyName] of class [$constraintOwningClass] must implement the interface [java.util.List]")
        }

        this.checks = (List<String>) constraintParameter
        super.setParameter(constraintParameter)
    }

    String getName() {
        return WHITE_LIST_AND_CONSTRAINT
    }

    /**
     * Processes the validation of the propertyValue, against the checks patterns set, and setting and calling rejectValue
     * if the propertyValue doesn't match all of the patterns in the checks list.
     *
     * @param target The target field to verify.
     * @param propertyValue the property value of the field.
     * @param errors Errors to be sent by rejectValues, if the propertyValue matches, the check pattern.
     */
    protected void processValidate(Object target, Object propertyValue, Errors errors) {
        // Check that the value matches one of the regexes, in the list If not, add an error.
        boolean validation = checks.inject(true){ boolean result, String check ->
            result && propertyValue ==~ Pattern.compile(check)
        }

        if (!validation) {
            Object[] args = [constraintPropertyName, constraintOwningClass, propertyValue, checks].toArray()
            super.rejectValue(target, errors, DEFAULT_NOT_WHITE_LIST_AND_MESSAGE_CODE, ConstrainedProperty.NOT_PREFIX + WHITE_LIST_AND_CONSTRAINT, args)
        }
    }

}