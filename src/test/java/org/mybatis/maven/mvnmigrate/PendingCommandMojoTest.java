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

import org.apache.ibatis.migration.commands.DownCommand;
import org.apache.ibatis.migration.commands.PendingCommand;
import org.apache.ibatis.migration.commands.UpCommand;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@SuppressWarnings("unchecked")
public class PendingCommandMojoTest extends AbstractMigrateTestCase {

  @BeforeEach
  public void init() throws Exception {
    initEnvironment();
  }

  @Test
  public void testUpPendingDownAllDown() throws Exception {
    runUpGoal();
    runPendingGoal();
    runDownGoal();
    runALLDownGoal();
  }

  protected void runUpGoal() throws Exception {
    AbstractCommandMojo<UpCommand> mojo = (AbstractCommandMojo<UpCommand>) testCase.lookupMojo("up", testPom);
    Assertions.assertNotNull(mojo);
    testCase.setVariableValueToObject(mojo, "upSteps", null);
    mojo.execute();
  }

  protected void runPendingGoal() throws Exception {
    AbstractCommandMojo<PendingCommand> mojo = (AbstractCommandMojo<PendingCommand>) testCase.lookupMojo("pending",
        testPom);
    Assertions.assertNotNull(mojo);
    mojo.execute();
  }

  protected void runDownGoal() throws Exception {
    AbstractCommandMojo<DownCommand> mojo = (AbstractCommandMojo<DownCommand>) testCase.lookupMojo("down", testPom);
    Assertions.assertNotNull(mojo);
    testCase.setVariableValueToObject(mojo, "downSteps", "1");
    mojo.execute();
  }

  protected void runALLDownGoal() throws Exception {
    AbstractCommandMojo<DownCommand> mojo = (AbstractCommandMojo<DownCommand>) testCase.lookupMojo("down", testPom);
    Assertions.assertNotNull(mojo);
    testCase.setVariableValueToObject(mojo, "downSteps", "ALL");
    mojo.execute();
  }

}
