package command

import grails.core.GrailsApplication
import org.owasp.html.PolicyFactory

class CommandBootStrap {
    GrailsApplication grailsApplication

    def init    = { servletContext ->
        ((PolicyFactory) grailsApplication.config.html_sanitizer_policy).sanitize('<a></a>')
    }
}
