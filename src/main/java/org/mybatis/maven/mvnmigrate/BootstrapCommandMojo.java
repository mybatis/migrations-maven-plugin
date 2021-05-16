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

import org.apache.ibatis.migration.commands.BootstrapCommand;
import org.apache.ibatis.migration.options.SelectedOptions;
import org.apache.maven.plugins.annotations.Mojo;

/**
 * Goal which execute the ibatis migration bootstrap command.
 */
@Mojo(name = "bootstrap")
public final class BootstrapCommandMojo extends AbstractCommandMojo<BootstrapCommand> {

  @Override
  protected BootstrapCommand createCommandClass(SelectedOptions options) {
    return new BootstrapCommand(options);
  }

}
