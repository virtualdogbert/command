package command

import com.virtualdogbert.CommandArtefactHandler
import com.virtualdogbert.GrailsCommandClass
import com.virtualdogbert.GrailsCommandFactory
import com.virtualdogbert.constraint.BlackListAndConstraint
import com.virtualdogbert.constraint.BlackListOrConstraint
import com.virtualdogbert.constraint.NotMatchesConstraint
import com.virtualdogbert.constraint.WhiteListAndConstraint
import com.virtualdogbert.constraint.WhiteListOrConstraint
import grails.plugins.Plugin
import grails.validation.ConstrainedProperty
import org.springframework.beans.factory.config.MethodInvokingFactoryBean

class CommandGrailsPlugin extends Plugin {

    // the version or versions of Grails the plugin is designed for
    def grailsVersion = "3.0.11 > *"

    def watchedResources = "file:./grails-app/command/**/*Command.groovy"

    def title           = "Command"
    // Headline display name of the plugin
    def author          = "Tucker J. Pelletier AKA virtualdogbert"
    def description     = '''\
This plugins give command objects a convention, and adds some error handling annotations.
'''
    def profiles        = ['web']
    def documentation   = "https://virtualdogbert.github.io/command"
    def license         = "APACHE"
    def issueManagement = [system: "GITHUB", url: "https://github.com/virtualdogbert/command/issues"]
    def scm             = [url: "https://github.com/virtualdogbert/command"]

    Closure doWithSpring() {
        { ->
            // Configure command beans
            grailsApplication.commandClasses.each { GrailsCommandClass commandClass ->
                configureCommandBeans.delegate = delegate
                configureCommandBeans(commandClass)
            }

            commandFactory(GrailsCommandFactory)
        }
    }

    /**
     * Configure command beans.
     */
    def configureCommandBeans = { GrailsCommandClass commandClass ->
        def fullName = commandClass.fullName

        try {
            "${fullName}Class"(MethodInvokingFactoryBean) {
                targetObject = ref("grailsApplication", false)
                targetMethod = "getArtefact"
                arguments = [CommandArtefactHandler.TYPE, commandClass.fullName]
            }

            "${fullName}"(ref("${fullName}Class")) { bean ->
                bean.factoryMethod = "newInstance"
                bean.autowire = "byName"
                bean.scope = "prototype"
            }
        } catch (Exception e) {
            log.error("Error declaring ${fullName}Detail bean in context", e)
        }
    }

    void onStartup(Map<String, Object> event) {
        refreshCommands()
    }

    void onChange(Map<String, Object> event) {
        if (event.source) {
            def commandClass = grailsApplication.addArtefact(CommandArtefactHandler.TYPE, event.source)
            def commandName = "${commandClass.propertyName}"

            beans {
                configureCommandBeans.delegate = delegate
                configureCommandBeans(commandClass)
            }

            refreshCommands()
        }
    }

    void refreshCommands() {
        grailsApplication.commandClasses.each { GrailsCommandClass commandClass ->
            def clz = commandClass.clazz
            clz.grailsCommandClass = commandClass

        }
    }

    void doWithApplicationContext() {
        ConstrainedProperty.registerNewConstraint(BlackListAndConstraint.BLACK_LIST_AND_CONSTRAINT, BlackListAndConstraint)
        ConstrainedProperty.registerNewConstraint(BlackListOrConstraint.BLACK_LIST_OR_CONSTRAINT, BlackListOrConstraint)
        ConstrainedProperty.registerNewConstraint(WhiteListAndConstraint.WHITE_LIST_AND_CONSTRAINT, WhiteListAndConstraint)
        ConstrainedProperty.registerNewConstraint(WhiteListOrConstraint.WHITE_LIST_OR_CONSTRAINT, WhiteListOrConstraint)
        ConstrainedProperty.registerNewConstraint(NotMatchesConstraint.NOT_MATCHES_CONSTRAINT, NotMatchesConstraint)
    }
}
