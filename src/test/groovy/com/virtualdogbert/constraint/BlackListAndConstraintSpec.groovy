package com.virtualdogbert.constraint

import org.springframework.validation.Errors
import org.springframework.validation.MapBindingResult
import spock.lang.Specification

class BlackListAndConstraintSpec extends Specification {

    BlackListAndConstraint blackListAndConstraint

    def setup() {
        blackListAndConstraint = new BlackListAndConstraint()
        blackListAndConstraint.setOwningClass(this.class)
        blackListAndConstraint.propertyName = 'test'
        blackListAndConstraint.setParameter(['(?:auto|initial)', '(?:initial|inherit|transparent)'])
    }

    void 'test black list AND valid'() {
        when:
            Errors errors = new MapBindingResult([:], 'test')
            blackListAndConstraint.validate('test', 'qwerty', errors)
        then:
            !errors.errorCount
    }


    void 'test black list AND invalid'() {
        when:
            Errors errors = new MapBindingResult([:], 'test')
            blackListAndConstraint.validate('test', 'auto', errors)
        then:
            errors.errorCount
    }
}
