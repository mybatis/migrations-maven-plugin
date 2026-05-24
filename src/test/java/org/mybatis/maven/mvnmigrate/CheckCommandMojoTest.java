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

import org.apache.ibatis.migration.commands.DownCommand;
import org.apache.ibatis.migration.commands.UpCommand;
import org.apache.maven.plugin.MojoFailureException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

@SuppressWarnings("unchecked")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CheckCommandMojoTest extends AbstractMigrateTestCase {

  private static final Path CHECK_TEST_POM = Path.of("src/test/resources/unit/check-test/check-test-plugin-config.xml");

  @Test
  @Order(1)
  void testCheckGoalSkip() throws Exception {
    CheckCommandMojo mojo = (CheckCommandMojo) testCase.lookupMojo("check", CHECK_TEST_POM);
    Assertions.assertNotNull(mojo);
    testCase.setVariableValueToObject(mojo, "skip", true);
    mojo.execute();
  }

  @Test
  @Order(2)
  void testCheckGoalToString() throws Exception {
    CheckCommandMojo mojo = (CheckCommandMojo) testCase.lookupMojo("check", CHECK_TEST_POM);
    Assertions.assertNotNull(mojo);
    Assertions.assertNotNull(mojo.toString());
    Assertions.assertFalse(mojo.toString().isEmpty());
  }

  @Test
  @Order(3)
  void testCheckGoalAfterAllMigrationsApplied() throws Exception {
    AbstractCommandMojo<UpCommand> upMojo = (AbstractCommandMojo<UpCommand>) testCase.lookupMojo("up", CHECK_TEST_POM);
    Assertions.assertNotNull(upMojo);
    testCase.setVariableValueToObject(upMojo, "upSteps", (String) null);
    upMojo.execute();

    CheckCommandMojo checkMojo = (CheckCommandMojo) testCase.lookupMojo("check", CHECK_TEST_POM);
    Assertions.assertNotNull(checkMojo);
    checkMojo.execute();
  }

  @Test
  @Order(4)
  void testCheckGoalWithPendingMigrations() throws Exception {
    AbstractCommandMojo<DownCommand> downMojo = (AbstractCommandMojo<DownCommand>) testCase.lookupMojo("down",
        CHECK_TEST_POM);
    Assertions.assertNotNull(downMojo);
    testCase.setVariableValueToObject(downMojo, "downSteps", "1");
    downMojo.execute();

    CheckCommandMojo checkMojo = (CheckCommandMojo) testCase.lookupMojo("check", CHECK_TEST_POM);
    Assertions.assertNotNull(checkMojo);
    Assertions.assertThrows(MojoFailureException.class, () -> checkMojo.execute());
  }

}
