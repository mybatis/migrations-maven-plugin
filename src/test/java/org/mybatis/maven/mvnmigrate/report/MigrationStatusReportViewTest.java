/*
 *    Copyright 2010-2025 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.mybatis.maven.mvnmigrate.report;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.ibatis.migration.Change;
import org.apache.maven.doxia.sink.impl.SinkAdapter;
import org.apache.maven.project.MavenProject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class MigrationStatusReportViewTest {

  private ResourceBundle getBundle() {
    return ResourceBundle.getBundle("migration-report", Locale.ENGLISH, getClass().getClassLoader());
  }

  @Test
  void testGenerateReportWithEmptyChanges() {
    MigrationStatusReportView view = new MigrationStatusReportView();
    Map<MavenProject, List<Change>> changes = new HashMap<>();
    MavenProject project = new MavenProject();
    project.setName("test-project");
    changes.put(project, new ArrayList<>());

    SinkAdapter sink = new SinkAdapter();
    Assertions.assertDoesNotThrow(() -> view.generateReport(changes, sink, getBundle(), false));
  }

  @Test
  void testGenerateReportWithAppliedChanges() {
    MigrationStatusReportView view = new MigrationStatusReportView();
    Map<MavenProject, List<Change>> changes = new HashMap<>();
    MavenProject project = new MavenProject();
    project.setName("test-project");

    List<Change> changeList = new ArrayList<>();
    Change appliedChange = new Change(new BigDecimal("20100400000001"), "Create Changelog",
        "20100400000001_create_changelog.sql");
    appliedChange.setAppliedTimestamp("2025-01-01 00:00:00");
    changeList.add(appliedChange);
    changes.put(project, changeList);

    SinkAdapter sink = new SinkAdapter();
    Assertions.assertDoesNotThrow(() -> view.generateReport(changes, sink, getBundle(), false));
  }

  @Test
  void testGenerateReportWithPendingChanges() {
    MigrationStatusReportView view = new MigrationStatusReportView();
    Map<MavenProject, List<Change>> changes = new HashMap<>();
    MavenProject project = new MavenProject();
    project.setName("test-project");

    List<Change> changeList = new ArrayList<>();
    Change pendingChange = new Change(new BigDecimal("20100400000001"), "Create Changelog",
        "20100400000001_create_changelog.sql");
    changeList.add(pendingChange);
    changes.put(project, changeList);

    SinkAdapter sink = new SinkAdapter();
    Assertions.assertDoesNotThrow(() -> view.generateReport(changes, sink, getBundle(), false));
  }

  @Test
  void testGenerateReportAggregateMode() {
    MigrationStatusReportView view = new MigrationStatusReportView();
    Map<MavenProject, List<Change>> changes = new HashMap<>();

    MavenProject project1 = new MavenProject();
    project1.setName("module-1");
    List<Change> changeList1 = new ArrayList<>();
    Change change1 = new Change(new BigDecimal("20100400000001"), "Create Changelog",
        "20100400000001_create_changelog.sql");
    change1.setAppliedTimestamp("2025-01-01 00:00:00");
    changeList1.add(change1);
    changes.put(project1, changeList1);

    MavenProject project2 = new MavenProject();
    project2.setName("module-2");
    List<Change> changeList2 = new ArrayList<>();
    Change change2 = new Change(new BigDecimal("20100400000002"), "First Migration",
        "20100400000002_first_migration.sql");
    changeList2.add(change2);
    changes.put(project2, changeList2);

    SinkAdapter sink = new SinkAdapter();
    Assertions.assertDoesNotThrow(() -> view.generateReport(changes, sink, getBundle(), true));
  }

  @Test
  void testGenerateReportWithMixedChanges() {
    MigrationStatusReportView view = new MigrationStatusReportView();
    Map<MavenProject, List<Change>> changes = new HashMap<>();
    MavenProject project = new MavenProject();
    project.setName("test-project");

    List<Change> changeList = new ArrayList<>();
    Change appliedChange = new Change(new BigDecimal("20100400000001"), "Create Changelog",
        "20100400000001_create_changelog.sql");
    appliedChange.setAppliedTimestamp("2025-01-01 00:00:00");
    changeList.add(appliedChange);

    Change pendingChange = new Change(new BigDecimal("20100400000002"), "First Migration",
        "20100400000002_first_migration.sql");
    changeList.add(pendingChange);
    changes.put(project, changeList);

    SinkAdapter sink = new SinkAdapter();
    Assertions.assertDoesNotThrow(() -> view.generateReport(changes, sink, getBundle(), false));
  }

  @Test
  void testGenerateChangesTable() {
    MigrationStatusReportView view = new MigrationStatusReportView();

    List<Change> changeList = new ArrayList<>();
    Change appliedChange = new Change(new BigDecimal("20100400000001"), "Create Changelog",
        "20100400000001_create_changelog.sql");
    appliedChange.setAppliedTimestamp("2025-01-01 00:00:00");
    changeList.add(appliedChange);

    Change pendingChange = new Change(new BigDecimal("20100400000002"), "First Migration",
        "20100400000002_first_migration.sql");
    changeList.add(pendingChange);

    SinkAdapter sink = new SinkAdapter();
    Assertions.assertDoesNotThrow(() -> view.generateChangesTable(sink, changeList));
  }

}
