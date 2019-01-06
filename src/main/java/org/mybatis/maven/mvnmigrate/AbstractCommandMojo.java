/**
 *    Copyright 2010-2018 the original author or authors.
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
import java.io.PrintStream;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

import org.apache.ibatis.migration.commands.BaseCommand;
import org.apache.ibatis.migration.options.SelectedOptions;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Parameter;
import org.mybatis.maven.mvnmigrate.util.MavenOutputStream;

/**
 * Provides to an abstract class that extends {@link AbstractMojo}.
 */
abstract class AbstractCommandMojo<T extends BaseCommand> extends AbstractMojo {

  private final Locale locale = Locale.ENGLISH;

  /**
   * Location of migrate repository.
   */
  @Parameter(property = "migration.path", defaultValue = ".")
  private File repository;

  /**
   * Environment to configure. Default environment is 'development'.
   */
  @Parameter(property = "migration.env", defaultValue = "development")
  private String environment;

  /**
   * Forces script to continue even if SQL errors are encountered.
   */
  @Parameter(property = "migration.force", defaultValue = "false")
  private boolean force;

  /**
   * Skip migration actions.
   */
  @Parameter(property = "migration.skip", defaultValue = "false")
  private boolean skip;

  /**
   * The command to execute.
   */
  private T command;

  /**
   * execute the command.
   */
  @Override
  public void execute() throws MojoExecutionException, MojoFailureException {
    if (this.isSkip()) {
      return;
    }
    this.init();
    this.command.execute();
  }

  /**
   * Initialize the MyBatis Migration command.
   */
  protected void init() throws MojoFailureException {
    try {
      final SelectedOptions options = new SelectedOptions();
      options.getPaths().setBasePath(this.getRepository());
      options.setEnvironment(this.getEnvironment());
      options.setForce(this.isForce());

      this.command = this.createCommandClass(options);
      final PrintStream out = new PrintStream(new MavenOutputStream(this.getLog()));
      this.command.setPrintStream(out);
      this.command.setDriverClassLoader(this.getClass().getClassLoader());

      if (this.getLog().isInfoEnabled()) {
        final String[] args = { this.command.getClass().getSimpleName(),
            this.getBundle(this.locale).getString("migration.plugin.name") };
        final MessageFormat format = new MessageFormat(
            this.getBundle(this.locale).getString("migration.plugin.execution.command"));
        this.getLog().info(format.format(args));
      }
    } catch (final RuntimeException e) {
      throw e;
    } catch (final Exception e) {
      throw new MojoFailureException(this, e.getMessage(), e.getLocalizedMessage());
    }
  }

  protected Locale getLocale() {
    return this.locale;
  }

  protected File getRepository() {
    return this.repository;
  }

  protected String getEnvironment() {
    return this.environment;
  }

  protected boolean isForce() {
    return this.force;
  }

  /**
   * Return the command.
   *
   * @return {@link BaseCommand} the command created.
   */
  protected T getCommand() {
    return this.command;
  }

  /**
   * Test if the skip flag is setted.
   *
   * @return the skip flag.
   */
  protected boolean isSkip() {
    if (this.skip && this.getLog().isInfoEnabled()) {
      final String[] args = { this.getBundle(this.locale).getString("migration.plugin.name") };
      final MessageFormat format = new MessageFormat(
          this.getBundle(this.locale).getString("migration.plugin.execution.command.skipped"));
      this.getLog().info(format.format(args));
    }
    return this.skip;
  }

  /**
   * The current locale.
   *
   * @param locale
   *          the locale
   * @return the bundle
   */
  protected ResourceBundle getBundle(final Locale locale) {
    return ResourceBundle.getBundle("migration-plugin", locale, this.getClass().getClassLoader());
  }

  /**
   * Creates the specific mojo command.
   *
   * @param options
   *          the options
   * @return The command created.
   */
  protected abstract T createCommandClass(final SelectedOptions options);

}
