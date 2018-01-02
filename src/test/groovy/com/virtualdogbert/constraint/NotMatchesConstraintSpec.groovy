package com.virtualdogbert.constraint

import org.springframework.validation.Errors
import org.springframework.validation.MapBindingResult
import spock.lang.Specification

class NotMatchesConstraintSpec extends Specification {

    NotMatchesConstraint notMatchesConstraint

    def setup() {
        notMatchesConstraint = new NotMatchesConstraint()
        notMatchesConstraint.setOwningClass(this.class)
        notMatchesConstraint.propertyName = 'test'
        notMatchesConstraint.setParameter('(?:auto|initial)')
    }

    void 'test notMatches valid'() {
        when:
            Errors errors = new MapBindingResult([:], 'test')
            notMatchesConstraint.validate('test', 'qwerty', errors)
        then:
            !errors.errorCount
    }


    void 'test notMatches invalid'() {
        when:
            Errors errors = new MapBindingResult([:], 'test')
            notMatchesConstraint.validate('test', 'auto', errors)
        then:
            errors.errorCount
    }
}
