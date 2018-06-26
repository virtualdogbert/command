package com.virtualdogbert.constraint

import com.virtualdogbert.sanitizer.HtmlChangeListener
import grails.util.Holders
import groovy.transform.CompileStatic
import org.grails.datastore.gorm.validation.constraints.AbstractConstraint
import org.owasp.html.PolicyFactory
import org.springframework.context.MessageSource
import org.springframework.validation.Errors
/**
 * This allows you to specify a list of regexes, and the value of the property must not satisfy all of them, because the checks
 * are combined with and AND
 */
@CompileStatic
class HtmlEnforceConstraint extends AbstractConstraint {
    boolean enabled = true

    static final String ENFORCE_HTML_CONSTRAINT   = "htmlEnforce"
    static final String ENFORCE_HTML_MESSAGE_CODE = "default.htmlEnforce.message"

    HtmlEnforceConstraint(Class<?> constraintOwningClass, String constraintPropertyName, Object constraintParameter, MessageSource messageSource) {
        super(constraintOwningClass, constraintPropertyName, constraintParameter, messageSource)

        if (!(constraintParameter instanceof Boolean)) {
            throw new IllegalArgumentException("Parameter for constraint [$ENFORCE_HTML_CONSTRAINT] of property [$constraintPropertyName] of class [$constraintOwningClass] must be a boolean")
        }

        this.enabled = (boolean) constraintParameter
    }

    @Override
    protected Object validateParameter(Object constraintParameter) {
        return constraintParameter
    }

    boolean supports(Class type) {
        return type != null
    }

    String getName() {
        return ENFORCE_HTML_CONSTRAINT
    }

    /**
     * Processes the validation of the propertyValue, against the checks patterns set, and setting and calling rejectValue
     * if the propertyValue matches any of the patterns in the checks list.
     *
     * @param target The target field to verify.
     * @param propertyValue the property value of the field.
     * @param errors Errors to be sent by rejectValues,.
     */
    protected void processValidate(Object target, Object propertyValue, Errors errors) {
        PolicyFactory htmlPolicy = policyFactory
        List<String> results = []
        htmlPolicy.sanitize((String) propertyValue, new HtmlChangeListener(), results)

        if (results && enabled) {
            Object[] args = [results].toArray()
            super.rejectValue(target, errors, ENFORCE_HTML_MESSAGE_CODE, ENFORCE_HTML_CONSTRAINT, args)
        }
    }

    PolicyFactory getPolicyFactory(){
        (PolicyFactory) Holders.grailsApplication.config.html_sanitizer_policy
    }

}