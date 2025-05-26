/*
 *    Copyright 2010-2025 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.mybatis.maven.mvnmigrate;

import java.nio.file.Files;
import java.nio.file.Path;

import org.apache.ibatis.migration.commands.InitializeCommand;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.mybatis.maven.testing.AbstractMojoTestCase;

@ExtendWith(MojoExtension.class)
public abstract class AbstractMigrateTestCase {

  @RegisterExtension
  private static MojoExtension extension = new MojoExtension();

  protected AbstractMojoTestCase testCase;

  protected Path testPom = Path.of("src/test/resources/unit/basic-test/basic-test-plugin-config.xml");

  public AbstractMigrateTestCase() {
    testCase = extension.testCase;
  }

  @SuppressWarnings("unchecked")
  protected void initEnvironment() throws Exception {
    AbstractCommandMojo<InitializeCommand> mojo = (AbstractCommandMojo<InitializeCommand>) testCase.lookupMojo("init",
        testPom);
    Assertions.assertNotNull(mojo);

    final Path newRep = Path.of("target/init");
    testCase.setVariableValueToObject(mojo, "repository", newRep.toFile());
    mojo.execute();
    Assertions.assertTrue(Files.exists(newRep));
  }

}
