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

import org.apache.ibatis.migration.commands.VersionCommand;
import org.apache.ibatis.migration.options.SelectedOptions;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

/**
 * Goal which execute the ibatis migration version command.
 *
 * @version $Id$
 * @goal version
 */
public final class VersionCommandMojo extends AbstractCommandMojo<VersionCommand> {

    /**
     * Version string.
     *
     * @parameter expression="${migration.version}"
     * @required
     */
    private String version;

    /**
     * {@inheritDoc}
     *
     * @param options
     */
    @Override
    protected VersionCommand createCommandClass(SelectedOptions options) {
        return new VersionCommand(options);
    }

    /** {@inheritDoc} */
    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        if (isSkip()) {
            return;
        }

        init();
        getCommand().execute(this.version);
    }

}
