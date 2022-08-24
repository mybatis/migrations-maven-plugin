/*
 *    Copyright 2010-2022 the original author or authors.
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

import java.io.File;

import org.apache.ibatis.migration.commands.InitializeCommand;
import org.junit.Rule;
import org.junit.jupiter.api.Assertions;

public abstract class AbstractMigrateTestCase {

  // TODO There is no BaseDir for Junit4 version. Does not seem to be needed and maven site points to other method call
  // that doesn't exist.
  // Leave this for now until we can determine this is needed or not.
  // protected File testPom = new File(getBasedir(), "src/test/resources/unit/basic-test/basic-test-plugin-config.xml");
  protected File testPom = new File("src/test/resources/unit/basic-test/basic-test-plugin-config.xml");

  @Rule
  public MybatisMojoRule rule = new MybatisMojoRule();

  @SuppressWarnings("unchecked")
  protected void initEnvironment() throws Exception {
    AbstractCommandMojo<InitializeCommand> mojo = (AbstractCommandMojo<InitializeCommand>) rule.lookupMojo("init",
        testPom);
    Assertions.assertNotNull(mojo);

    final File newRep = new File("target/init");
    rule.setVariableValueToObject(mojo, "repository", newRep);
    mojo.execute();
    Assertions.assertTrue(newRep.exists());
  }

}
