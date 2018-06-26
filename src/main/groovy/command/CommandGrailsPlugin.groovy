package command

import com.virtualdogbert.CommandArtefactHandler
import com.virtualdogbert.GrailsCommandClass
import com.virtualdogbert.GrailsCommandFactory
import com.virtualdogbert.constraint.*
import grails.plugins.Plugin
import org.grails.datastore.gorm.validation.constraints.eval.ConstraintsEvaluator
import org.grails.datastore.gorm.validation.constraints.eval.DefaultConstraintEvaluator
import org.grails.datastore.gorm.validation.constraints.registry.ConstraintRegistry
import org.springframework.beans.factory.config.MethodInvokingFactoryBean
import org.springframework.context.ApplicationContext

class CommandGrailsPlugin extends Plugin {

    // the version or versions of Grails the plugin is designed for
    def grailsVersion = "3.3.0 > *"

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
        registerCustomConstraints(applicationContext)
    }

    private void registerCustomConstraints(ApplicationContext ctx) {
        //This method for registering constraints came from longwa
        List<ConstraintRegistry> registries = []
        DefaultConstraintEvaluator evaluator = ctx.getBean(ConstraintsEvaluator) as DefaultConstraintEvaluator

        // Register with both the default constraint as well as the gorm registry (it's stupid that it needs both)
        // Also the ConstraintsEvaluator evaluator constructs a new internal registry and doesn't seem to expose it
        // so we are forced to invade it's privacy if we want custom constraints for Validateable instances.
        registries << evaluator.constraintRegistry
        registries << ctx.getBean("gormValidatorRegistry", ConstraintRegistry)

        registries.each { ConstraintRegistry registry ->
            registry.addConstraint(CascadeConstraint)
            registry.addConstraint(BlackListAndConstraint)
            registry.addConstraint(BlackListOrConstraint)
            registry.addConstraint(WhiteListAndConstraint)
            registry.addConstraint(WhiteListOrConstraint)
            registry.addConstraint(NotMatchesConstraint)
            registry.addConstraint(HtmlEnforceConstraint)
        }
    }
}
