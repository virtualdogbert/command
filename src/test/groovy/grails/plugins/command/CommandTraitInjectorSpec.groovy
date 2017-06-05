package grails.plugins.command

import com.virtualdogbert.CommandEnhanced
import grails.artefact.Artefact
import spock.lang.Specification


class CommandTraitInjectorSpec extends Specification {

         void 'test that the job trait is applied'() {
             expect:
                 CommandEnhanced.isAssignableFrom TraitCommand
         }
}

@Artefact('Command')
class TraitCommand {

}

