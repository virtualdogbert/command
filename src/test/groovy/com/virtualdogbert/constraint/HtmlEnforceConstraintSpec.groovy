package com.virtualdogbert.constraint

import com.virtualdogbert.sanitizer.HtmlPolicy
import org.owasp.html.PolicyFactory
import org.springframework.validation.Errors
import org.springframework.validation.MapBindingResult
import spock.lang.Specification

class HtmlEnforceConstraintSpec extends Specification {

    HtmlEnforceConstraint htmlEnforceConstraint

    def setup() {
        htmlEnforceConstraint = new HtmlEnforceConstraint(
                this.class,
                'test',
                true,
                null
        ) {
            @Override
            PolicyFactory getPolicyFactory() {
                return (PolicyFactory) HtmlPolicy.POLICY_DEFINITION
            }
        }
    }

    void 'test white list OR valid'() {
        when:
            Errors errors = new MapBindingResult([:], 'test')
            htmlEnforceConstraint.processValidate('test', '<b> This works!</b>', errors)
        then:
            !errors.errorCount
    }


    void 'test white list OR invalid'() {
        when:
            Errors errors = new MapBindingResult([:], 'test')
            htmlEnforceConstraint.processValidate('test', '<b> This fails!</b><script>alert("you be hacked")</script', errors)
        then:
            errors.errorCount
    }
}
