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
package org.mybatis.maven.migrations_maven_plugin;

import java.nio.file.Path;

import org.apache.maven.plugin.Mojo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mybatis.maven.mvnmigrate.AbstractMigrateTestCase;

class HelpMojoTest extends AbstractMigrateTestCase {

  private static final Path HELP_TEST_POM = Path.of("src/test/resources/unit/help-test/help-test-plugin-config.xml");

  @Test
  void testHelpGoal() throws Exception {
    Mojo mojo = testCase.lookupMojo("help", HELP_TEST_POM);
    Assertions.assertNotNull(mojo);
    mojo.execute();
  }

  @Test
  void testHelpGoalWithDetail() throws Exception {
    Mojo mojo = testCase.lookupMojo("help", HELP_TEST_POM);
    Assertions.assertNotNull(mojo);
    testCase.setVariableValueToObject(mojo, "detail", true);
    mojo.execute();
  }

  @Test
  void testHelpGoalForSpecificGoal() throws Exception {
    Mojo mojo = testCase.lookupMojo("help", HELP_TEST_POM);
    Assertions.assertNotNull(mojo);
    testCase.setVariableValueToObject(mojo, "goal", "status");
    mojo.execute();
  }

  @Test
  void testHelpGoalWithCustomLineLength() throws Exception {
    Mojo mojo = testCase.lookupMojo("help", HELP_TEST_POM);
    Assertions.assertNotNull(mojo);
    testCase.setVariableValueToObject(mojo, "lineLength", 120);
    mojo.execute();
  }

  @Test
  void testHelpGoalWithInvalidLineLength() throws Exception {
    Mojo mojo = testCase.lookupMojo("help", HELP_TEST_POM);
    Assertions.assertNotNull(mojo);
    testCase.setVariableValueToObject(mojo, "lineLength", 0);
    mojo.execute();
  }

  @Test
  void testHelpGoalWithCustomIndentSize() throws Exception {
    Mojo mojo = testCase.lookupMojo("help", HELP_TEST_POM);
    Assertions.assertNotNull(mojo);
    testCase.setVariableValueToObject(mojo, "indentSize", 4);
    mojo.execute();
  }

  @Test
  void testHelpGoalWithDetailAndGoal() throws Exception {
    Mojo mojo = testCase.lookupMojo("help", HELP_TEST_POM);
    Assertions.assertNotNull(mojo);
    testCase.setVariableValueToObject(mojo, "detail", true);
    testCase.setVariableValueToObject(mojo, "goal", "up");
    mojo.execute();
  }

  @Test
  void testHelpGoalWithUnknownGoal() throws Exception {
    Mojo mojo = testCase.lookupMojo("help", HELP_TEST_POM);
    Assertions.assertNotNull(mojo);
    testCase.setVariableValueToObject(mojo, "goal", "unknown-goal");
    mojo.execute();
  }

  @Test
  void testHelpGoalWithDetailForCheckGoal() throws Exception {
    Mojo mojo = testCase.lookupMojo("help", HELP_TEST_POM);
    Assertions.assertNotNull(mojo);
    testCase.setVariableValueToObject(mojo, "detail", true);
    testCase.setVariableValueToObject(mojo, "goal", "check");
    mojo.execute();
  }

  @Test
  void testHelpGoalWithZeroIndentSize() throws Exception {
    Mojo mojo = testCase.lookupMojo("help", HELP_TEST_POM);
    Assertions.assertNotNull(mojo);
    testCase.setVariableValueToObject(mojo, "indentSize", 0);
    mojo.execute();
  }

}
