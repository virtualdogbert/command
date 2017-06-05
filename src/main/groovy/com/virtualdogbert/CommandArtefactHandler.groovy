/*
 * Copyright (c) 2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.virtualdogbert

import grails.core.ArtefactHandlerAdapter
import org.codehaus.groovy.ast.ClassNode
import org.grails.compiler.injection.GrailsASTUtils

import java.util.regex.Pattern

import static org.grails.io.support.GrailsResourceUtils.GRAILS_APP_DIR
import static org.grails.io.support.GrailsResourceUtils.REGEX_FILE_SEPARATOR

/**
 * Grails artifact handler for command classes.
 *
 */
class CommandArtefactHandler extends ArtefactHandlerAdapter {

    static final String  TYPE                 = "Command"
    static       Pattern COMMAND_PATH_PATTERN = Pattern.compile(".+" + REGEX_FILE_SEPARATOR + GRAILS_APP_DIR + REGEX_FILE_SEPARATOR + "command" + REGEX_FILE_SEPARATOR + "(.+)\\.(groovy)");

    CommandArtefactHandler() {
        super(TYPE, GrailsCommandClass.class, DefaultGrailsCommandClass.class, TYPE)
    }

    boolean isArtefact(ClassNode classNode) {
        if (classNode == null ||
                !isValidArtefactClassNode(classNode, classNode.getModifiers()) ||
                !classNode.getName().endsWith(DefaultGrailsCommandClass.COMMAND)) {
            return false
        }

        URL url = GrailsASTUtils.getSourceUrl(classNode)

        url && COMMAND_PATH_PATTERN.matcher(url.getFile()).find()
    }

    boolean isArtefactClass(Class clazz) {
        // class shouldn't be null and should ends with Command suffix
        if (clazz == null || !clazz.getName().endsWith(DefaultGrailsCommandClass.COMMAND)) {
            return false
        }

        return true
    }
}
