package com.virtualdogbert.constraint

import org.springframework.validation.Errors
import org.springframework.validation.MapBindingResult
import spock.lang.Specification

class WhiteListOrConstraintSpec extends Specification {

    WhiteListOrConstraint whiteListOrConstraint

    def setup() {
        whiteListOrConstraint = new WhiteListOrConstraint(
                this.class,
                'test',
                ['(?:auto|initial)', '(?:initial|inherit|transparent)'],
                null
        )
    }

    void 'test white list OR valid'() {
        when:
            Errors errors = new MapBindingResult([:], 'test')
            whiteListOrConstraint.validate('test', 'auto', errors)
        then:
            !errors.errorCount
    }


    void 'test white list OR invalid'() {
        when:
            Errors errors = new MapBindingResult([:], 'test')
            whiteListOrConstraint.validate('test', 'qwerty', errors)
        then:
            errors.errorCount
    }
}
