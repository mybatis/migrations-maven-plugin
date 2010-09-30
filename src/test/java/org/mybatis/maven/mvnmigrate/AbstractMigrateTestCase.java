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

import org.apache.maven.plugin.testing.AbstractMojoTestCase;

/**
 * @version $Id$
 */
abstract public class AbstractMigrateTestCase extends AbstractMojoTestCase {

    protected File testPom = new File(getBasedir(), "src/test/resources/unit/basic-test/basic-test-plugin-config.xml");



    /* (non-Javadoc)
     * @see org.apache.maven.plugin.testing.AbstractMojoTestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        cleanup();
    }

    /* (non-Javadoc)
     * @see org.codehaus.plexus.PlexusTestCase#tearDown()
     */
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        cleanup();
    }

    protected void cleanup() throws Exception {
        File derbyDb = new File("target/database.dat");
        if (derbyDb.exists()) {
            deleteDir(derbyDb);
        }
        derbyDb = new File("target/init");
        if (derbyDb.exists()) {
            deleteDir(derbyDb);
        }
    }

    protected static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i=0; i<children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }

        // The directory is now empty so delete it
        return dir.delete();
    }


    protected void initEnvironment() throws Exception {
        AbstractCommandMojo mojo = (AbstractCommandMojo) lookupMojo("init", testPom);
        assertNotNull(mojo);

        final File newRep = new File("target/init");
        setVariableValueToObject(mojo, "repository", newRep);
        mojo.execute();
        assertTrue(newRep.exists());
    }


}
