/**
 *    Copyright 2010-2016 the original author or authors.
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
import org.apache.ibatis.migration.commands.StatusCommand;
import org.apache.ibatis.migration.operations.StatusOperation;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;

/**
 * Goal which check the presence of pending migration.
 */
@Mojo(name = "check", defaultPhase = LifecyclePhase.TEST)
public final class CheckCommandMojo extends StatusCommandMojo {

  private static final String LINE_SEPARATOR = System.getProperty("line.separator", "\n");

  /**
   * {@inheritDoc}
   */
  @Override
  public void execute() throws MojoExecutionException, MojoFailureException {
    if (isSkip()) {
      return;
    }

    init();

    if (getCommand() instanceof StatusCommand) {
      StatusCommand command = (StatusCommand) getCommand();
      command.execute();
      StatusOperation operation = command.getOperation();
      List<Change> changes = operation.getCurrentStatus();
      int pendings = operation.getPendingCount();
      if (pendings > 0) {
        Integer[] args = { pendings };
        MessageFormat format = new MessageFormat(getBundle(this.getLocale()).getString("migration.plugin.execution.check.failed"));
        throw new MojoFailureException(this, LINE_SEPARATOR + format.format(args), createLongMessage(changes));
      }
    }
  }

  /**
   * Creates a user information message about all migration pending changes
   *
   * @param changes
   *          List of migration changes
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
    return getBundle(this.getLocale()).getString("migration.plugin.name") + " " + this.getClass().getSimpleName();
  }

}
