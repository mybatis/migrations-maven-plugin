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
import org.apache.ibatis.migration.commands.StatusCommand;
import org.apache.ibatis.migration.options.SelectedOptions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Extended {@link StatusCommand} class.
 * <p/>
 * Used for retrieve information about migration status.
 *
 * @version $Id$
 */
public final class MigrationStatusCommand extends StatusCommand {

    /** Extended MyBatis {@link StatusCommand}. */
    public MigrationStatusCommand(SelectedOptions options) {
        super(options);
    }

    /**
     * Return true if there is at last one pending script, false otherwise.
     *
     * @return true if there is at last one pending script, false otherwise.
     */
    public boolean checkPending() {
        final List<Change> merged = getMergedStatus();
        for (Change change : merged) {
            if (change.getAppliedTimestamp() == null) {
                return true;
            }
        }
        return false;
    }

    /**
     * Return the number of pending migration contains into the list.
     *
     * @return Return the number of pending migration contains into the list.
     */
    public int getNumberOfPending(List<Change> changes) {
        int numberOfPendings = 0;
        for (Change change : changes) {
            if (change.getAppliedTimestamp() == null) {
                numberOfPendings++;
            }
        }
        return numberOfPendings;
    }

    /**
     * Retrieves the current status of database and return a list of {@link Change}.
     *
     * @return list of database {@link Change}
     */
    public List<Change> getMergedStatus() {
        this.setDriverClassLoader(getClass().getClassLoader());

        List<Change> merged = new ArrayList<Change>();
        List<Change> migrations = getMigrations();
        if (changelogExists()) {
            List<Change> changelog = getChangelog();
            for (Change change : migrations) {
                int index = changelog.indexOf(change);
                if (index > -1) {
                    final Change c = changelog.get(index);
                    c.setFilename(change.getFilename());
                    merged.add(c);
                } else {
                    merged.add(change);
                }
            }
            Collections.sort(merged);
        } else {
            merged.addAll(migrations);
        }

        return merged;
    }

}
