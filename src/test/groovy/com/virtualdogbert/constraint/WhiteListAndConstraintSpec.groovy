package com.virtualdogbert.constraint

import org.springframework.validation.Errors
import org.springframework.validation.MapBindingResult
import spock.lang.Specification

class WhiteListAndConstraintSpec extends Specification {

    WhiteListAndConstraint whiteListAndConstraint

    def setup() {
        whiteListAndConstraint = new WhiteListAndConstraint()
        whiteListAndConstraint.setOwningClass(this.class)
        whiteListAndConstraint.propertyName = 'test'
        whiteListAndConstraint.setParameter(['(?:auto|initial)', '(?:initial|inherit|transparent)'])
    }

    void 'test white list AND valid'() {
        when:
            Errors errors = new MapBindingResult([:], 'test')
            whiteListAndConstraint.validate('test', 'initial', errors)
        then:
            !errors.errorCount
    }


    void 'test white list AND invalid'() {
        when:
            Errors errors = new MapBindingResult([:], 'test')
            whiteListAndConstraint.validate('test', 'auto', errors)
        then:
            errors.errorCount
    }
}
