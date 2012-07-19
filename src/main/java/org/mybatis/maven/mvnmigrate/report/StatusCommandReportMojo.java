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
package org.mybatis.maven.mvnmigrate.report;

import org.apache.ibatis.migration.Change;
import org.apache.ibatis.migration.options.SelectedOptions;
import org.apache.maven.doxia.sink.Sink;
import org.apache.maven.doxia.siterenderer.Renderer;
import org.apache.maven.model.ReportPlugin;
import org.apache.maven.project.MavenProject;
import org.apache.maven.reporting.AbstractMavenReport;
import org.apache.maven.reporting.MavenReportException;
import org.codehaus.plexus.util.xml.Xpp3Dom;
import org.mybatis.maven.mvnmigrate.command.MigrationStatusCommand;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Extends {@link AbstractMavenReport}.
 * <p/>
 * Class to generate a Maven report.
 *
 * @version $Id$
 * @goal status-report
 */
public final class StatusCommandReportMojo extends AbstractMavenReport {

    private static final File DEFAULT_REPO = new File(".");

    private static final String DEFAULT_ENVIRONMENT = "development";

    private static final boolean DEFAULT_FORCE = false;

    /**
     * The Maven project to analyze.
     *
     * @parameter expression="${project}"
     * @required
     * @readonly
     */
    private MavenProject project;

    /**
     * Target folder.
     *
     * @parameter expression="${project.build.directory}"
     * @readonly
     */
    private File outputDirectory;

    /**
     * The projects in the reactor for aggregation report.
     *
     * @parameter expression="${reactorProjects}"
     * @readonly
     */
    protected List<MavenProject> reactorProjects;

    /**
     * The project site renderer.
     *
     * @component
     * @required
     * @readonly
     */
    private Renderer siteRenderer;

    /**
     * Location of migrate repository.
     *
     * @parameter expression="${migration.path}" default-value="."
     */
    protected File repository;

    /**
     * Environment to configure. Default environment is 'development'.
     *
     * @parameter expression="${migration.env}" default-value="development"
     */
    protected String environment;

    /**
     * Forces script to continue even if SQL errors are encountered.
     *
     * @parameter expression="${migration.force}" default-value="false"
     */
    protected boolean force;

    /**
     * Skip migration actions.
     *
     * @parameter expression="${migration.skip}" default-value="false"
     */
    protected boolean skip;

    /**
     * Aggregate report results.
     *
     * @parameter expression="${migration.aggregate}" default-value="false"
     */
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
                this.getLog()
                    .info(getBundle(locale).getString(getBundle(locale).getString(
                        "migration.status.report.skipped.no.target")));
            }
            return;
        }

        Map<MavenProject, List<Change>> aggregateReport = new HashMap<MavenProject, List<Change>>();

        for (MavenProject mavenProject : reactorProjects) {

            @SuppressWarnings("unchecked")
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
                } else if ("enviroment".equalsIgnoreCase(child.getName())) {
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

            MigrationStatusCommand analyzer = new MigrationStatusCommand(options);
            try {
                List<Change> analysis = null;
                analysis = analyzer.getMergedStatus();

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
    public String getOutputName() {
        return "migration-status-analysis";
    }

    /**
     * Return the name of the report.
     *
     * @return the name of the report.
     */
    public String getName(Locale locale) {
        return getBundle(locale).getString("migration.status.report.name");
    }

    /**
     * Return the description of the report.
     *
     * @return the description of the report.
     */
    public String getDescription(Locale locale) {
        return getBundle(locale).getString("migration.status.report.description");
    }

    /**
     * Return the {@link ResourceBundle} given the current locale.
     *
     * @param locale the current locale.
     */
    protected ResourceBundle getBundle(Locale locale) {
        return ResourceBundle.getBundle("migration-report", locale, this.getClass().getClassLoader());
    }

}
