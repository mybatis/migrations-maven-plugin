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

import org.apache.ibatis.migration.commands.NewCommand;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

/**
 * Goal which executes the ibatis migration new command.
 *
 * @version $Id$
 * @goal new
 */
public final class NewCommandMojo extends AbstractCommandMojo<NewCommand> {

    /**
     * New file description.
     *
     * @parameter expression="${migration.description}"
     * @required
     */
    private String description;
    
    /**
     * New file based on template.
     *
     * @parameter expression="${migration.template}"
     */
    private String template;

    /**
     * {@inheritDoc}
     */
    @Override
    protected NewCommand createCommandClass() {
        return new NewCommand(this.getRepository(), this.getEnvironment(), template, this.isForce());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        if (isSkip()) {
            return;
        }

        init();
        getCommand().execute(description);
    }

}
