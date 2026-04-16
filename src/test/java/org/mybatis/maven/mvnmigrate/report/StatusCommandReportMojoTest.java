/*
 *    Copyright 2010-2026 the original author or authors.
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

import java.io.File;
import java.lang.reflect.Field;
import java.nio.file.Path;
import java.util.Locale;

import org.apache.maven.project.MavenProject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class StatusCommandReportMojoTest {

  @Test
  void testExecuteReportSkipsWhenAggregateAndNotExecutionRoot() throws Exception {
    StatusCommandReportMojo mojo = new StatusCommandReportMojo();
    MavenProject project = new MavenProject();
    project.setExecutionRoot(false);
    setField(mojo, "project", project);
    mojo.aggregate = true;

    Assertions.assertDoesNotThrow(() -> mojo.executeReport(Locale.ENGLISH));
  }

  @Test
  void testExecuteReportSkipsWhenSkipEnabled() throws Exception {
    StatusCommandReportMojo mojo = new StatusCommandReportMojo();
    MavenProject project = new MavenProject();
    project.setExecutionRoot(true);
    setField(mojo, "project", project);
    mojo.skip = true;

    Assertions.assertDoesNotThrow(() -> mojo.executeReport(Locale.ENGLISH));
  }

  @Test
  void testExecuteReportSkipsForPomPackaging() throws Exception {
    StatusCommandReportMojo mojo = new StatusCommandReportMojo();
    MavenProject project = new MavenProject();
    project.setPackaging("pom");
    setField(mojo, "project", project);

    Assertions.assertDoesNotThrow(() -> mojo.executeReport(Locale.ENGLISH));
  }

  @Test
  void testExecuteReportSkipsWhenOutputDirectoryMissing() throws Exception {
    StatusCommandReportMojo mojo = new StatusCommandReportMojo();
    MavenProject project = new MavenProject();
    project.setPackaging("jar");
    setField(mojo, "project", project);
    setField(mojo, "outputDirectory", Path.of("target/does-not-exist").toFile());

    Assertions.assertDoesNotThrow(() -> mojo.executeReport(Locale.ENGLISH));
  }

  @Test
  void testMetadataMethods() throws Exception {
    StatusCommandReportMojo mojo = new StatusCommandReportMojo();
    File outputDir = Path.of("target").toFile();
    setField(mojo, "outputDirectory", outputDir);

    Assertions.assertEquals("migration-status-analysis", mojo.getOutputName());
    Assertions.assertEquals("MyBatis Migration Schema", mojo.getName(Locale.ENGLISH));
    Assertions.assertEquals("MyBatis Migration Schema status of database migration",
        mojo.getDescription(Locale.ENGLISH));
    Assertions.assertEquals(outputDir.toString(), mojo.getOutputDirectory());
  }

  private static void setField(Object target, String fieldName, Object value) throws Exception {
    Field field = target.getClass().getDeclaredField(fieldName);
    field.setAccessible(true);
    field.set(target, value);
  }
}
