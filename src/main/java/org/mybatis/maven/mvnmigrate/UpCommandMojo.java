/*
 *    Copyright 2010-2021 the original author or authors.
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

import org.apache.ibatis.migration.commands.UpCommand;
import org.apache.ibatis.migration.options.SelectedOptions;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

/**
 * Goal which execute the ibatis migration status command.
 */
@Mojo(name = "up")
public final class UpCommandMojo extends AbstractCommandMojo<UpCommand> {

  /**
   * Steps to do (default all steps).
   */
  @Parameter(property = "migration.up.steps")
  private String upSteps;

  @Override
  protected UpCommand createCommandClass(SelectedOptions options) {
    return new UpCommand(options);
  }

  @Override
  public void execute() throws MojoExecutionException, MojoFailureException {
    if (isSkip()) {
      return;
    }

    init();
    getCommand().execute(upSteps);
  }

}
