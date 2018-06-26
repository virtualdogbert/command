package com.virtualdogbert.constraint

import grails.gorm.validation.ConstrainedProperty
import groovy.transform.CompileStatic
import org.grails.datastore.gorm.validation.constraints.AbstractConstraint
import org.springframework.context.MessageSource
import org.springframework.validation.Errors

import java.util.regex.Pattern

/**
 * This allows you to specify a list of regexes, and the value of the property must not satisfy all of them, because the checks
 * are combined with and AND
 */
@CompileStatic
class BlackListAndConstraint extends AbstractConstraint {
    List<String> checks

    static final String BLACK_LIST_AND_CONSTRAINT               = "blackListAnd"
    static final String DEFAULT_NOT_BLACK_LIST_AND_MESSAGE_CODE = "default.not.blackListAnd.message"

    BlackListAndConstraint(Class<?> constraintOwningClass, String constraintPropertyName, Object constraintParameter, MessageSource messageSource) {
        super(constraintOwningClass, constraintPropertyName, constraintParameter, messageSource)

        if (!(constraintParameter instanceof List)) {
            throw new IllegalArgumentException("Parameter for constraint [$BLACK_LIST_AND_CONSTRAINT] of property [$constraintPropertyName] of class [$constraintOwningClass] must implement the interface [java.util.List]")
        }

        checks = (List<String>) constraintParameter
    }

    @Override
    protected Object validateParameter(Object constraintParameter) {
       return constraintParameter
    }

    boolean supports(Class type) {
        return type != null
    }


    String getName() {
        return BLACK_LIST_AND_CONSTRAINT
    }


    protected void processValidate(Object target, Object propertyValue, Errors errors) {
        // Check that the value matches one of the regexes, in the list If not, add an error.
        boolean validation = checks.inject(true) { boolean result, String check ->
            result && !(propertyValue ==~ Pattern.compile(check))
        }

        if (!validation) {
            Object[] args = [constraintPropertyName, constraintOwningClass, propertyValue, checks].toArray()
            super.rejectValue(target, errors, DEFAULT_NOT_BLACK_LIST_AND_MESSAGE_CODE, ConstrainedProperty.NOT_PREFIX + BLACK_LIST_AND_CONSTRAINT, args)
        }
    }

}