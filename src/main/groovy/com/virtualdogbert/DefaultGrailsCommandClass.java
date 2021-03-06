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

package com.virtualdogbert;

import org.grails.core.AbstractGrailsClass;


/**
 * Grails artifact class which represents a Quartz command.
 */
public class DefaultGrailsCommandClass extends AbstractGrailsClass implements GrailsCommandClass {

    public static final String COMMAND = "Command";

    public DefaultGrailsCommandClass(Class clazz) {
        super(clazz, COMMAND);
    }
}
