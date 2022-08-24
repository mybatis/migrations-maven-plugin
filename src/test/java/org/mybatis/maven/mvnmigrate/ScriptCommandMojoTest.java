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

import org.apache.ibatis.migration.commands.ScriptCommand;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

@SuppressWarnings("unchecked")
public class ScriptCommandMojoTest extends AbstractMigrateTestCase {

  @Test
  public void testScriptGoal() throws Exception {
    AbstractCommandMojo<ScriptCommand> mojo = (AbstractCommandMojo<ScriptCommand>) rule.lookupMojo("script", testPom);
    Assertions.assertNotNull(mojo);
    rule.setVariableValueToObject(mojo, "v1", "20100400000001");
    rule.setVariableValueToObject(mojo, "v2", "20100400000003");
    mojo.execute();
  }

  @Test
  public void testScriptToFileGoal() throws Exception {
    AbstractCommandMojo<ScriptCommand> mojo = (AbstractCommandMojo<ScriptCommand>) rule.lookupMojo("script", testPom);
    Assertions.assertNotNull(mojo);
    rule.setVariableValueToObject(mojo, "v1", "20100400000001");
    rule.setVariableValueToObject(mojo, "v2", "20100400000003");
    rule.setVariableValueToObject(mojo, "output", new File("target/script_20100400000001-20100400000003.sql"));
    mojo.execute();
    Assertions.assertTrue(new File("target/script_20100400000001-20100400000003.sql").exists());
  }

  @Test
  public void testScriptPending() throws Exception {
    AbstractCommandMojo<ScriptCommand> mojo = (AbstractCommandMojo<ScriptCommand>) rule.lookupMojo("script", testPom);
    Assertions.assertNotNull(mojo);
    rule.setVariableValueToObject(mojo, "v1", "pending");
    rule.setVariableValueToObject(mojo, "output", new File("target/script_pending.sql"));
    mojo.execute();
    Assertions.assertTrue(new File("target/script_pending.sql").exists());
  }

  @Test
  public void testScriptPendingUndo() throws Exception {
    AbstractCommandMojo<ScriptCommand> mojo = (AbstractCommandMojo<ScriptCommand>) rule.lookupMojo("script", testPom);
    Assertions.assertNotNull(mojo);
    rule.setVariableValueToObject(mojo, "v1", "pending_undo");
    rule.setVariableValueToObject(mojo, "output", new File("target/script_pending_undo.sql"));
    mojo.execute();
    Assertions.assertTrue(new File("target/script_pending_undo.sql").exists());
  }

}
