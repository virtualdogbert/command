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
import grails.converters.JSON
import org.grails.core.artefact.ControllerArtefactHandler
import org.springframework.beans.factory.annotation.Value

/**
 * A trait for adding a default errorsHandler for dealing with command errors. Also the AST will  delete to this handler
 * by default.
 */
@Enhances(ControllerArtefactHandler.TYPE)
trait ControllerEnhancer {
    @Value('${command.response.code:409}')
    int errorHandlerResponseCode

    @Value('${command.response.return:true}')
    boolean returnAsRespond

    boolean errorsHandler(List commandObjects, Map otherParams = [:]) {
        List errors = commandObjects.inject([]) { result, commandObject ->

            otherParams.each { otherParam ->
                if(commandObject.hasProperty(otherParam.key) && commandObject."$otherParam.key" == null){
                    commandObject."$otherParam.key" = otherParam.value
                }

            }
            if ((commandObject.respondsTo('validate') && !commandObject.validate()) ||
                (!commandObject.respondsTo('validate') && commandObject.hasErrors())) {
                result + (commandObject.errors)
            } else {
                result
            }

        } as List

        if (errors) {
            response.status = errorHandlerResponseCode

            if (returnAsRespond) {
                respond errors, [formats: ['json', 'xml']]
            } else {
                render errors as JSON
            }

            return true
        }

        return false
    }
}
