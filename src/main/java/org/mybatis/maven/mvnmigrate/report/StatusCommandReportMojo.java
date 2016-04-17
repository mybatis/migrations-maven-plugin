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
package org.mybatis.maven.mvnmigrate.report;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.ibatis.migration.Change;
import org.apache.ibatis.migration.commands.StatusCommand;
import org.apache.ibatis.migration.operations.StatusOperation;
import org.apache.ibatis.migration.options.SelectedOptions;
import org.apache.maven.doxia.sink.Sink;
import org.apache.maven.doxia.siterenderer.Renderer;
import org.apache.maven.model.ReportPlugin;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.apache.maven.reporting.AbstractMavenReport;
import org.apache.maven.reporting.MavenReportException;
import org.codehaus.plexus.util.xml.Xpp3Dom;

/**
 * Extends {@link AbstractMavenReport}.
 * <p/>
 * Class to generate a Maven report.
 *
 * @version $Id$
 */
@Mojo(name = "status-report")
public final class StatusCommandReportMojo extends AbstractMavenReport {

  private static final File DEFAULT_REPO = new File(".");

  private static final String DEFAULT_ENVIRONMENT = "development";

  private static final boolean DEFAULT_FORCE = false;

  /**
   * The Maven project to analyze.
   */
  @Parameter(property = "project", required = true, readonly = true)
  private MavenProject project;

  /**
   * Target folder.
   */
  @Parameter(property = "project.build.directory", readonly = true)
  private File outputDirectory;

  /**
   * The projects in the reactor for aggregation report.
   */
  @Parameter(property = "reactorProjects", readonly = true)
  protected List<MavenProject> reactorProjects;

  /**
   * The project site renderer.
   */
  @Component(role = Renderer.class)
  private Renderer siteRenderer;

  /**
   * Location of migrate repository.
   */
  @Parameter(property = "migration.path", defaultValue = ".")
  protected File repository;

  /**
   * Environment to configure. Default environment is 'development'.
   */
  @Parameter(property = "migration.env", defaultValue = "development")
  protected String environment;

  /**
   * Forces script to continue even if SQL errors are encountered.
   */
  @Parameter(property = "migration.force", defaultValue = "false")
  protected boolean force;

  /**
   * Skip migration actions.
   */
  @Parameter(property = "migration.skip", defaultValue = "false")
  protected boolean skip;

  /**
   * Aggregate report results.
   */
  @Parameter(property = "migration.aggregate", defaultValue = "false")
  protected boolean aggregate;

  /** {@inheritDoc} */
  @Override
  protected void executeReport(Locale locale) throws MavenReportException {
    if (aggregate && !project.isExecutionRoot()) {
      return;
    }

    if (skip && !aggregate) {
      if (this.getLog().isInfoEnabled()) {
        this.getLog().info(getBundle(locale).getString("migration.status.report.skipped"));
      }
      return;
    }

    // Step 0: Checking pom availability
    if ("pom".equals(this.project.getPackaging()) && !aggregate) {
      if (this.getLog().isInfoEnabled()) {
        this.getLog().info("migration.status.report.skipped.pom");
      }
      return;
    }

    if (this.outputDirectory == null || !this.outputDirectory.exists()) {
      if (this.getLog().isInfoEnabled()) {
        this.getLog().info(getBundle(locale).getString(getBundle(locale).getString("migration.status.report.skipped.no.target")));
      }
      return;
    }

    Map<MavenProject, List<Change>> aggregateReport = new HashMap<MavenProject, List<Change>>();

    for (MavenProject mavenProject : reactorProjects) {

      Map<String, ReportPlugin> reportPluginMap = mavenProject.getReporting().getReportPluginsAsMap();
      ReportPlugin plug = reportPluginMap.get(getBundle(locale).getString("migration.plugin.key"));

      Xpp3Dom configurationDom = (Xpp3Dom) plug.getConfiguration();

      File reactorRepo = DEFAULT_REPO;
      String reactorEnv = DEFAULT_ENVIRONMENT;
      boolean reactorForce = DEFAULT_FORCE;
      boolean skipStatusCommand = false;

      for (int i = 0; i < configurationDom.getChildCount(); i++) {
        Xpp3Dom child = configurationDom.getChild(i);
        if ("repository".equalsIgnoreCase(child.getName())) {
          reactorRepo = new File(child.getValue());
        } else if ("environment".equalsIgnoreCase(child.getName())) {
          reactorEnv = child.getValue();
        } else if ("force".equalsIgnoreCase(child.getName())) {
          reactorForce = Boolean.valueOf(child.getValue());
        } else if ("skip".equalsIgnoreCase(child.getName())) {
          skipStatusCommand = Boolean.valueOf(child.getValue());
        }
      }

      if (skipStatusCommand) {
        continue;
      }

      final SelectedOptions options = new SelectedOptions();
      options.getPaths().setBasePath(reactorRepo);
      options.setEnvironment(reactorEnv);
      options.setForce(reactorForce);

      StatusCommand analyzer = new StatusCommand(options);
      try {
        analyzer.execute();
        StatusOperation operation = analyzer.getOperation();
        List<Change> analysis = operation.getCurrentStatus();

        aggregateReport.put(mavenProject, analysis);
      } catch (RuntimeException e) {
        throw e;
      } catch (Exception e) {
        throw new MavenReportException(getBundle(locale).getString("migration.status.report.error"), e);
      }
    }

    // Step 2: Create sink and bundle
    Sink sink = getSink();
    ResourceBundle bundle = getBundle(locale);

    // Step 3: Generate the report
    MigrationStatusReportView view = new MigrationStatusReportView();
    view.generateReport(aggregateReport, sink, bundle, aggregate);
  }

  /** {@inheritDoc} */
  @Override
  protected String getOutputDirectory() {
    if (this.getLog().isInfoEnabled()) {
      this.getLog().info(outputDirectory.toString());
    }
    return this.outputDirectory.toString();
  }

  /** {@inheritDoc} */
  @Override
  protected MavenProject getProject() {
    return this.project;
  }

  /** {@inheritDoc} */
  @Override
  protected Renderer getSiteRenderer() {
    return this.siteRenderer;
  }

  /**
   * Return the output name of the report.
   *
   * @return the noutput name.
   */
  @Override
  public String getOutputName() {
    return "migration-status-analysis";
  }

  /**
   * Return the name of the report.
   *
   * @return the name of the report.
   */
  @Override
  public String getName(Locale locale) {
    return getBundle(locale).getString("migration.status.report.name");
  }

  /**
   * Return the description of the report.
   *
   * @return the description of the report.
   */
  @Override
  public String getDescription(Locale locale) {
    return getBundle(locale).getString("migration.status.report.description");
  }

  /**
   * Return the {@link ResourceBundle} given the current locale.
   *
   * @param locale
   *          the current locale.
   */
  protected ResourceBundle getBundle(Locale locale) {
    return ResourceBundle.getBundle("migration-report", locale, this.getClass().getClassLoader());
  }

}
