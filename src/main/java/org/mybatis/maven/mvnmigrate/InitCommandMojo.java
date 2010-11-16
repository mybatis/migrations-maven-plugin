/*
 *    Copyright 2010 The myBatis Team
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.mybatis.maven.mvnmigrate;

import org.apache.ibatis.migration.commands.InitializeCommand;

/**
 * Goal which executes the MyBatis migration init command.
 *
 * Init command creates a new migrate repository into 'repository' location.
 *
 * @version $Id$
 * @goal init
 */
public class InitCommandMojo extends AbstractCommandMojo<InitializeCommand> {

    /**
     * {@inheritDoc}
     */
    @Override
    protected InitializeCommand createCommandClass() {
        return new InitializeCommand(this.getRepository(), this.getEnvironment(), this.isForce());
    }

}
