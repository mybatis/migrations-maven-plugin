/**
 *    Copyright 2010-2015 the original author or authors.
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
import org.apache.ibatis.migration.options.SelectedOptions;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

/**
 * Goal which executes the ibatis migration new command.
 *
 * @version $Id$
 */
@Mojo(name = "new")
public final class NewCommandMojo extends AbstractCommandMojo<NewCommand> {

    /**
     * New file description.
     */
	@Parameter(property="migration.description",required=true)
    private String description;

    /**
     * New file based on template.
     */
	@Parameter(property="migration.template")
    private String template;

    /**
     * Sequential number for script prefix. (type 000 to generate 001_create_changelog)
     */
    @Parameter(property="migration.idpattern")
    private String idPattern;

    /**
     * {@inheritDoc}
     * @param options
     */
    @Override
    protected NewCommand createCommandClass(SelectedOptions options) {
        if (idPattern != null) {
            options.setIdPattern(idPattern);
        }
        return new NewCommand(options);
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
        getCommand().execute(this.description);
    }

}
