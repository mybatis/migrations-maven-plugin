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
package org.mybatis.maven.mvnmigrate.command;

import org.apache.ibatis.migration.Change;
import org.apache.ibatis.migration.MigrationException;
import org.apache.ibatis.migration.commands.DownCommand;
import org.apache.ibatis.migration.options.SelectedOptions;

import java.util.List;

/**
 * Extended {@link DownCommand} class.
 * <p/>
 * Used for retrieve information about migration status.
 *
 * @version $Id$
 */
public final class MigrationDownCommand extends DownCommand {

    /**
     * Extended Apache MyBatis {@link DownCommand}.
     */
    public MigrationDownCommand(SelectedOptions aOptions) {
        super(aOptions);
    }

    /**
     * Return number of changes applied to the changelog table.
     *
     * @return The number of changes.
     */
    public int getNumberOfChanges() {
        int numberOfChanges = 0;
        try {
            List<Change> changes = getChangelog();
            numberOfChanges = changes.size();
        } catch (MigrationException e) {
            numberOfChanges = 0;
        }
        return numberOfChanges;
    }

    /**
     * Return the number of steps parsed from parameters.
     *
     * @param defaultSteps the default number of steps
     * @param params the argument list.
     * @return the parsed number.
     */
    public int parseParameter(int defaultSteps, String... params) {
        return super.getStepCountParameter(defaultSteps, params);
    }

}
