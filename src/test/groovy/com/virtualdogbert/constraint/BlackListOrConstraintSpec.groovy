package com.virtualdogbert.constraint

import org.springframework.validation.Errors
import org.springframework.validation.MapBindingResult
import spock.lang.Specification

class BlackListOrConstraintSpec extends Specification {

    BlackListOrConstraint blackListOrConstraint

    def setup() {
        blackListOrConstraint = new BlackListOrConstraint()
        blackListOrConstraint.setOwningClass(this.class)
        blackListOrConstraint.propertyName = 'test'
        blackListOrConstraint.setParameter(['(?:auto|initial)', '(?:initial|inherit|transparent)'])
    }

    void 'test black list OR valid'() {
        when:
            Errors errors = new MapBindingResult([:], 'test')
            blackListOrConstraint.validate('test', 'auto', errors)
        then:
            !errors.errorCount
    }


    void 'test black list OR invalid'() {
        when:
            Errors errors = new MapBindingResult([:], 'test')
            blackListOrConstraint.validate('test', 'initial', errors)
        then:
            errors.errorCount
    }
}
