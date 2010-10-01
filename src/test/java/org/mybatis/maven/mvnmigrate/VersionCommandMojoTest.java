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

import org.apache.ibatis.migration.commands.UpCommand;
import org.apache.ibatis.migration.commands.VersionCommand;

/**
 * @version $Id$
 */
@SuppressWarnings("unchecked")
public class VersionCommandMojoTest extends AbstractMigrateTestCase {

    public void testUpGoal() throws Exception {
        AbstractCommandMojo<UpCommand> mojo = (AbstractCommandMojo<UpCommand>) lookupMojo("up", testPom);
        assertNotNull(mojo);
        setVariableValueToObject(mojo, "upSteps", "1");
        mojo.execute();
    }

    public void testVersionGoal() throws Exception {
        AbstractCommandMojo<VersionCommand> mojo = (AbstractCommandMojo<VersionCommand>) lookupMojo("version", testPom);
        assertNotNull(mojo);
        setVariableValueToObject(mojo, "version", "20100400000003");
        mojo.execute();
    }

    public void testVersionDownGoal() throws Exception {
        AbstractCommandMojo<VersionCommand> mojo = (AbstractCommandMojo<VersionCommand>) lookupMojo("version", testPom);
        assertNotNull(mojo);
        setVariableValueToObject(mojo, "version", "20100400000001");
        mojo.execute();
    }
}
