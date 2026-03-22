/*
 *    Copyright 2010-2026 the original author or authors.
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

import java.nio.file.Path;

import org.apache.ibatis.migration.commands.InitializeCommand;
import org.apache.ibatis.migration.commands.NewCommand;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@SuppressWarnings("unchecked")
class InitCommandMojoTest extends AbstractMigrateTestCase {

  @BeforeEach
  void init() throws Exception {
    initEnvironment();
  }

  @Test
  void testNewGoal() throws Exception {
    AbstractCommandMojo<NewCommand> mojo = (AbstractCommandMojo<NewCommand>) testCase.lookupMojo("new", testPom);
    Assertions.assertNotNull(mojo);
    testCase.setVariableValueToObject(mojo, "repository", Path.of("target/init").toFile());
    testCase.setVariableValueToObject(mojo, "description", "test_script");
    mojo.execute();

    final Path newRep = Path.of("target/init/scripts");
    Assertions.assertEquals(4, newRep.toFile().listFiles().length);
  }

  @Test
  void testnewGoalRequiredValue() throws Exception {
    try {
      AbstractCommandMojo<NewCommand> mojo = (AbstractCommandMojo<NewCommand>) testCase.lookupMojo("new", testPom);
      Assertions.assertNotNull(mojo);
      testCase.setVariableValueToObject(mojo, "repository", Path.of("target/init").toFile());
      testCase.setVariableValueToObject(mojo, "description", null);
      mojo.execute();
      Assertions.fail();
    } catch (Exception e) {
    }
  }

  @Test
  void testnewGoalSetEmptyValue() throws Exception {
    try {
      AbstractCommandMojo<NewCommand> mojo = (AbstractCommandMojo<NewCommand>) testCase.lookupMojo("new", testPom);
      Assertions.assertNotNull(mojo);
      testCase.setVariableValueToObject(mojo, "repository", Path.of("target/init").toFile());
      testCase.setVariableValueToObject(mojo, "description", "");
      mojo.execute();
    } catch (Exception e) {
    }
  }

  @Test
  void testNewGoalWithIdPattern() throws Exception {
    AbstractCommandMojo<NewCommand> mojo = (AbstractCommandMojo<NewCommand>) testCase.lookupMojo("new", testPom);
    Assertions.assertNotNull(mojo);
    testCase.setVariableValueToObject(mojo, "repository", Path.of("target/init").toFile());
    testCase.setVariableValueToObject(mojo, "description", "test_script_with_id_pattern");
    testCase.setVariableValueToObject(mojo, "idPattern", "000");
    mojo.execute();
  }

  @Test
  void testNewGoalWithTemplate() throws Exception {
    AbstractCommandMojo<NewCommand> mojo = (AbstractCommandMojo<NewCommand>) testCase.lookupMojo("new", testPom);
    Assertions.assertNotNull(mojo);
    testCase.setVariableValueToObject(mojo, "repository", Path.of("target/init").toFile());
    testCase.setVariableValueToObject(mojo, "description", "test_script_with_template");
    testCase.setVariableValueToObject(mojo, "template",
        "src/test/resources/unit/basic-test/migrate-repository/scripts/20100400000002_first_migration.sql");
    mojo.execute();
  }

  @Test
  void testNewGoalSkip() throws Exception {
    AbstractCommandMojo<NewCommand> mojo = (AbstractCommandMojo<NewCommand>) testCase.lookupMojo("new", testPom);
    Assertions.assertNotNull(mojo);
    testCase.setVariableValueToObject(mojo, "skip", true);
    mojo.execute();
  }

  @Test
  void testInitGoalWithIdPattern() throws Exception {
    Path newRep = Path.of("target/init-idpattern");
    if (newRep.toFile().exists()) {
      MojoExtension.deleteDir(newRep);
    }
    AbstractCommandMojo<InitializeCommand> mojo = (AbstractCommandMojo<InitializeCommand>) testCase.lookupMojo("init",
        testPom);
    Assertions.assertNotNull(mojo);
    testCase.setVariableValueToObject(mojo, "repository", newRep.toFile());
    testCase.setVariableValueToObject(mojo, "idPattern", "000");
    mojo.execute();
    Assertions.assertTrue(newRep.toFile().exists());
  }

  @Test
  void testnewGoalInitRepAlreadyExist() throws Exception {
    try {
      AbstractCommandMojo<InitializeCommand> mojo = (AbstractCommandMojo<InitializeCommand>) testCase.lookupMojo("init",
          testPom);
      Assertions.assertNotNull(mojo);
      testCase.setVariableValueToObject(mojo, "repository", Path.of("target/init").toFile());
      mojo.execute();
    } catch (Exception e) {
    }
  }

}
