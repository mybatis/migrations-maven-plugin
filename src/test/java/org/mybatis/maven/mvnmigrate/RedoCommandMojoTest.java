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

import org.apache.ibatis.migration.commands.RedoCommand;
import org.apache.ibatis.migration.commands.UpCommand;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@SuppressWarnings("unchecked")
class RedoCommandMojoTest extends AbstractMigrateTestCase {

  @BeforeEach
  void init() throws Exception {
    initEnvironment();
  }

  @Test
  void testRedoGoalSkip() throws Exception {
    AbstractCommandMojo<RedoCommand> mojo = (AbstractCommandMojo<RedoCommand>) testCase.lookupMojo("redo", testPom);
    Assertions.assertNotNull(mojo);
    testCase.setVariableValueToObject(mojo, "skip", true);
    mojo.execute();
  }

  @Test
  void testRedoGoal() throws Exception {
    runUpGoal();
    runRedoGoal();
  }

  @Test
  void testRedoGoalWithSteps() throws Exception {
    runUpGoal();
    AbstractCommandMojo<RedoCommand> mojo = (AbstractCommandMojo<RedoCommand>) testCase.lookupMojo("redo", testPom);
    Assertions.assertNotNull(mojo);
    testCase.setVariableValueToObject(mojo, "redoSteps", "1");
    mojo.execute();
  }

  protected void runUpGoal() throws Exception {
    AbstractCommandMojo<UpCommand> mojo = (AbstractCommandMojo<UpCommand>) testCase.lookupMojo("up", testPom);
    Assertions.assertNotNull(mojo);
    testCase.setVariableValueToObject(mojo, "upSteps", "1");
    mojo.execute();
  }

  protected void runRedoGoal() throws Exception {
    AbstractCommandMojo<RedoCommand> mojo = (AbstractCommandMojo<RedoCommand>) testCase.lookupMojo("redo", testPom);
    Assertions.assertNotNull(mojo);
    mojo.execute();
  }

}
