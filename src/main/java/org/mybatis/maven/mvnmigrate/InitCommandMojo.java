/**
 *    Copyright 2010-2017 the original author or authors.
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
import org.apache.ibatis.migration.options.SelectedOptions;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

/**
 * Goal which executes the MyBatis migration init command.
 *
 * <p>Init command creates a new migrate repository into 'repository' location.
 */
@Mojo(name = "init")
public final class InitCommandMojo extends AbstractCommandMojo<InitializeCommand> {

  /**
   * Sequential number for script prefix. (type 000 to generate 001_create_changelog)
   */
  @Parameter(property = "migration.idpattern")
  private String idPattern;

  @Override
  protected InitializeCommand createCommandClass(SelectedOptions options) {
    if (idPattern != null) {
      options.setIdPattern(idPattern);
    }
    return new InitializeCommand(options);
  }

}
