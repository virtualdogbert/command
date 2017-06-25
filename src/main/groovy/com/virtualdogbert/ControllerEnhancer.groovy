/*
 * Copyright 2017 the original author or authors.
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

import grails.artefact.Enhances
import grails.util.Holders
import org.grails.core.artefact.ControllerArtefactHandler
/**
 * A trait for adding a default errorsHandler for dealing with command errors. Also the AST will  delete to this handler
 * by default.
 */
@Enhances(ControllerArtefactHandler.TYPE)
trait ControllerEnhancer {

    boolean errorsHandler(List commandObjects) {
        List errors = commandObjects.inject([]) { result, commandObject ->

            if (commandObject.hasErrors()) {
                result + (commandObject.errors)
            } else {
                result
            }

        } as List

        if (errors) {
            response.status = Holders.config.getProperty('command.response.code', Integer, 409)
            respond errors, [formats:['json', 'xml']]
            return true
        }

        return false
    }
}
