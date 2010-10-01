/*
 *    Copyright 2010 The myBatis Team
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

import java.io.File;

import org.apache.ibatis.migration.commands.InitializeCommand;
import org.apache.ibatis.migration.commands.NewCommand;

/**
 * @version $Id$
 */
@SuppressWarnings("unchecked")
public class InitCommandMojoTest extends AbstractMigrateTestCase {

    /**
     * {@inheritDoc}
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        initEnvironment();
    }

    public void testNewGoal() throws Exception {
        AbstractCommandMojo<NewCommand> mojo = (AbstractCommandMojo<NewCommand>) lookupMojo("new", testPom);
        assertNotNull(mojo);
        setVariableValueToObject(mojo, "repository", new File("target/init"));
        setVariableValueToObject(mojo, "description", "test_script");
        mojo.execute();

        final File newRep = new File("target/init/scripts");
        assertEquals(4, newRep.listFiles().length);
    }

    public void testnewGoalRequiredValue() throws Exception {
        try {
            AbstractCommandMojo<NewCommand> mojo = (AbstractCommandMojo<NewCommand>) lookupMojo("new", testPom);
            assertNotNull(mojo);
            setVariableValueToObject(mojo, "repository", new File("target/init"));
            setVariableValueToObject(mojo, "description", null);
            mojo.execute();
            fail();
        } catch (Exception e) {}
    }

    public void testnewGoalSetEmptyValue() throws Exception {
        try{
            AbstractCommandMojo<NewCommand> mojo = (AbstractCommandMojo<NewCommand>) lookupMojo("new", testPom);
            assertNotNull(mojo);
            setVariableValueToObject(mojo, "repository", new File("target/init"));
            setVariableValueToObject(mojo, "description", "");
            mojo.execute();
        } catch (Exception e) {}
    }

    public void testnewGoalInitRepAlreadyExist() throws Exception {
        try{
            AbstractCommandMojo<InitializeCommand> mojo = (AbstractCommandMojo<InitializeCommand>) lookupMojo("init", testPom);
            assertNotNull(mojo);
            setVariableValueToObject(mojo, "repository", new File("target/init"));
            mojo.execute();
        } catch (Exception e) {}
    }

}
