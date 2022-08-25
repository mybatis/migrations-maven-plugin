/*
 *    Copyright 2010-2022 the original author or authors.
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
package org.mybatis.maven.mvnmigrate;

import java.io.File;

import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public class MojoExtension implements AfterEachCallback, BeforeEachCallback {

  // Make AbstractMojoTestCase concrete
  protected AbstractMojoTestCase testCase = new AbstractMojoTestCase() {
  };

  protected void cleanup() throws Exception {
    File initMigrationDbFolder;
    initMigrationDbFolder = new File("target/init");
    if (initMigrationDbFolder.exists()) {
      deleteDir(initMigrationDbFolder);
    }
  }

  protected static boolean deleteDir(File dir) {
    if (dir.isDirectory()) {
      String[] children = dir.list();
      for (int i = 0; i < children.length; i++) {
        boolean success = deleteDir(new File(dir, children[i]));
        if (!success) {
          return false;
        }
      }
    }

    // The directory is now empty so delete it
    return dir.delete();
  }

  @Override
  public void beforeEach(ExtensionContext context) throws Exception {
    // Setup test case
    testCase.setUp();
    cleanup();
  }

  @Override
  public void afterEach(ExtensionContext context) throws Exception {
    cleanup();
  }
}
