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

import java.text.MessageFormat;
import java.util.List;

import org.apache.ibatis.migration.Change;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.mybatis.maven.mvnmigrate.command.MigrationStatusCommand;

/**
 * Goal which check the presence of pending migration.
 *
 * @goal check
 * @phase test
 * @version $Id$
 */
public class CheckCommandMojo extends StatusCommandMojo {

    private static final String LINE_SEPARATOR = System.getProperty("line.separator","\n");

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        if (isSkip()) return;

        init();

        if (getCommand() instanceof MigrationStatusCommand) {
            MigrationStatusCommand  mcommand = (MigrationStatusCommand) getCommand();
            List<Change> changes = mcommand.getMergedStatus();
            int pendings = mcommand.getNumberOfPending(changes);
            if ( pendings > 0 ) {
                Integer[] args = { pendings };
                MessageFormat format = new MessageFormat(getBundle(locale).getString("migration.plugin.execution.check.failed"));
                throw new MojoFailureException(this, LINE_SEPARATOR + format.format(args), createLongMessage(changes));
            }
        }
    }

    /**
     * Creates a user information message about all migration pending changes
     *
     * @param changes List of migration changes
     * @return User information message about all migration pending changes.
     */
    private String createLongMessage(List<Change> changes) {
        StringBuilder builder = new StringBuilder();
        builder.append("ID             Applied At          Description");
        builder.append(LINE_SEPARATOR);
        for (Change change : changes) {
            builder.append(change);
            builder.append(LINE_SEPARATOR);
        }
        return builder.toString();
    }

    @Override
    public String toString() {
        return getBundle(locale).getString("migration.plugin.name") + " " + this.getClass().getSimpleName();
    }

}
