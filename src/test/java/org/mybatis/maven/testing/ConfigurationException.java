/*
 *    Copyright 2010-2024 the original author or authors.
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
/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.mybatis.maven.testing;

/**
 * Mybatis: Borrowed from 'https://github.com/apache/maven-plugin-testing/blob/master/maven-plugin-testing-harness/src/main/java/org/apache/maven/plugin/testing/ConfiguationException.java'
 * Reason: Removed from release 4.0.0-beta-1 and we need to have junit 5 support.  Maven dropped maven 3 support here!
 * Git: From release 4.0.0-alpha-2
 */

/**
 * ConfigurationException
 *
 * @author jesse
 */
public class ConfigurationException extends Exception {
  /** serialVersionUID */
  static final long serialVersionUID = -6180939638742159065L;

  /**
   * @param message
   *          The detailed message.
   */
  public ConfigurationException(String message) {
    super(message);
  }

  /**
   * @param cause
   *          The detailed cause.
   */
  public ConfigurationException(Throwable cause) {
    super(cause);
  }

  /**
   * @param message
   *          The detailed message.
   * @param cause
   *          The detailed cause.
   */
  public ConfigurationException(String message, Throwable cause) {
    super(message, cause);
  }
}