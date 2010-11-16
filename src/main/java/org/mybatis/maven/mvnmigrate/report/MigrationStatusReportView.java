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

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ResourceBundle;

import org.apache.ibatis.migration.Change;
import org.apache.maven.doxia.sink.Sink;
import org.apache.maven.project.MavenProject;

/**
 * View of status report.
 *
 * @version $Id$
 */
public final class MigrationStatusReportView {

    /**
     * Generates the report.
     *
     * @param changes list of {@link Change}
     * @param sink the {@link Sink} instance
     * @param bundle the {@link ResourceBundle} instance
     */
    public void generateReport(Map<MavenProject, List<Change>> changes, Sink sink,
            ResourceBundle bundle, boolean isAggregate) {

        sink.head();
        sink.title();
        sink.text( bundle.getString( "migration.status.report.header" ) );
        sink.title_();
        sink.head_();
        sink.body();

        // Generate title
        sink.section1();
        sink.sectionTitle1();
        sink.text( bundle.getString( "migration.status.report.mainTitle" ) );
        sink.sectionTitle1_();
        sink.section1_();
        sink.lineBreak();

        sink.section2();
        sink.sectionTitle2();
        sink.text( bundle.getString( "migration.status.report.secondSectionTitle" ) );
        sink.sectionTitle2_();

        for (Entry<MavenProject, List<Change>> entries : changes.entrySet()) {
            if (isAggregate) {
                sink.section3();
                sink.sectionTitle3();
                sink.text( bundle.getString( "migration.status.report.moduleTitle" ) + entries.getKey().getName() );
                sink.sectionTitle3_();
            }
            generateStatisticsTable(sink, entries.getValue());
        }

        sink.section2_();
        sink.lineBreak();

        sink.section3();
        sink.sectionTitle2();
        sink.text( bundle.getString( "migration.status.report.thirdSectionTitle" ) );
        sink.sectionTitle2_();
        for (Entry<MavenProject, List<Change>> entries : changes.entrySet()) {
            if (isAggregate) {
                sink.section3();
                sink.sectionTitle3();
                sink.text( bundle.getString( "migration.status.report.moduleTitle" ) + entries.getKey().getName() );
                sink.sectionTitle3_();
            }
            // Generate Unused declared dependencies:
            generateChangesTable(sink, entries.getValue());
        }

        sink.section3_();

        // Closing the report
        sink.body_();
        sink.flush();
        sink.close();
    }

    /**
     * Generates statistic table.
     *
     * @param sink
     * @param changes
     */
    private void generateStatisticsTable(Sink sink, List<Change> changes) {
        sink.table();

        sink.tableRow();
        sink.tableCell();
        sink.text( " Number of migration changes: " );
        sink.tableCell_();

        sink.tableCell();
        sink.text( "" + changes.size() );
        sink.tableCell_();
        sink.tableRow_();

        sink.tableRow();
        sink.tableCell();
        sink.text( " Number of pending migrations: " );
        sink.tableCell_();

        int nop = numberOfPending(changes);

        sink.tableCell();
        sink.text( nop + "  (" + calcPerc(changes.size(), nop) + ")  ");
        sink.nonBreakingSpace();
        sink.figure();
        sink.figureGraphics( nop == 0 ? "images/icon_success_sml.gif" : "images/icon_warning_sml.gif" );
        sink.figure_();
        sink.tableCell_();
        sink.tableRow_();

        sink.table_();
    }

    /**
     * Calculates the percentage.
     *
     * @param size
     * @param nop
     * @return
     */
    private String calcPerc(int tot, int nop) {
        return"" + ( (100*nop)/tot ) + "%";
    }

    /**
     * Return the number of pending change found.
     *
     * @param changes list of {@link Change}
     * @return Return the number of pending change found.
     */
    private int numberOfPending(List<Change> changes) {
        int numberOfPending = 0;
        for (Change change : changes) {
            if (change.getAppliedTimestamp() == null) {
                numberOfPending++;
            }
        }
        return numberOfPending;
    }

    /**
     * Generate a table for the given dependencies iterator.
     *
     * @param sink TODO fillme
     * @param iter TODO fillme
     */
    public void generateChangesTable(Sink sink, List<Change> iter) {
        sink.table();

        sink.tableRow();
        sink.tableCell();
        sink.bold();
        sink.text( "ID" );
        sink.bold_();
        sink.tableCell_();

        sink.tableCell();
        sink.bold();
        sink.text( "Applied At" );
        sink.bold_();
        sink.tableCell_();

        sink.tableCell();
        sink.bold();
        sink.text( "Description" );
        sink.bold_();
        sink.tableCell_();

        sink.tableCell();
        sink.bold();
        sink.text( "Filename" );
        sink.bold_();
        sink.tableCell_();

        sink.tableCell();
        sink.bold();
        sink.text( "Status" );
        sink.bold_();
        sink.tableCell_();

        sink.tableRow_();

        for (Change change : iter) {
            sink.tableRow();

            sink.tableCell();
            sink.text( "" + change.getId() );
            sink.tableCell_();

            sink.tableCell();
            sink.text( change.getAppliedTimestamp() == null ? " ... pending ... " : change.getAppliedTimestamp() );
            sink.tableCell_();

            sink.tableCell();
            sink.text( change.getDescription() );
            sink.tableCell_();

            sink.tableCell();
            sink.text( change.getFilename() );
            sink.tableCell_();

            sink.tableCell();
            sink.figure();
            sink.figureGraphics( change.getAppliedTimestamp() != null ? "images/icon_success_sml.gif" : "images/icon_warning_sml.gif" );
            sink.figure_();
            sink.tableCell_();

            sink.tableRow_();
        }

        sink.table_();
        sink.horizontalRule();
    }

}
