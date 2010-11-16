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

import java.text.MessageFormat;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.mybatis.maven.mvnmigrate.command.MigrationDownCommand;

/**
 * Goal which execute the ibatis migration status command.
 *
 * @version $Id$
 * @goal down
 */
public class DownCommandMojo extends AbstractCommandMojo<MigrationDownCommand> {

    /**
     * Steps to do. (type ALL to apply all down steps, default: 1 step)
     *
     * @parameter expression="${migration.down.steps}"
     */
    protected String downSteps;

    /**
     * {@inheritDoc}
     */
    @Override
    protected MigrationDownCommand createCommandClass() {
        return new MigrationDownCommand(this.getRepository(), this.getEnvironment(), this.isForce());
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
        int numberOfChanges = getCommand().getNumberOfChanges();

        if (numberOfChanges == 0 && getLog().isInfoEnabled()) {
            String[] args = { getClass().getSimpleName() };
            MessageFormat format = new MessageFormat(getBundle(this.getLocale()).getString("migration.plugin.execution.down.zero.change"));
            getLog().info(format.format(args));
            return;
        }

        if (downSteps != null
                && ("ALL".equalsIgnoreCase(downSteps) || getCommand()
                        .parseParameter(1, downSteps) > numberOfChanges)) {
            downSteps = "" + numberOfChanges;
        }
        getCommand().execute(downSteps);
    }

}
