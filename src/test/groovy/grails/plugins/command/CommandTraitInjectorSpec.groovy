package grails.plugins.command

import com.virtualdogbert.CommandEnhanced
import grails.artefact.Artefact
import spock.lang.Specification
/**
 * Created by virtualdogbert on 6/3/17.
 */
class CommandTraitInjectorSpec extends Specification {

         void 'test that the job trait is applied'() {
             expect:
                 CommandEnhanced.isAssignableFrom TraitCommand
         }
}

@Artefact('Command')
class TraitCommand {

}

