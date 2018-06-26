package com.virtualdogbert.sanitizer

import groovy.transform.CompileStatic

@CompileStatic
class HtmlChangeListener implements org.owasp.html.HtmlChangeListener<List<String>> {


    void discardedTag(List<String> context, String elementName) {
         context << elementName
    }

    void discardedAttributes(List<String> context, String tagName, String... attributeNames) {
        context << tagName
    }
}
