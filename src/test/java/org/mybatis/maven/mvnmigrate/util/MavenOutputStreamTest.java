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
package org.mybatis.maven.mvnmigrate.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.maven.plugin.logging.Log;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MavenOutputStreamTest {

  private List<String> loggedMessages;
  private MavenOutputStream outputStream;

  @BeforeEach
  void setUp() {
    loggedMessages = new ArrayList<>();
    Log log = new Log() {
      @Override
      public boolean isDebugEnabled() {
        return false;
      }

      @Override
      public void debug(CharSequence content) {
      }

      @Override
      public void debug(CharSequence content, Throwable error) {
      }

      @Override
      public void debug(Throwable error) {
      }

      @Override
      public boolean isInfoEnabled() {
        return true;
      }

      @Override
      public void info(CharSequence content) {
        loggedMessages.add(content.toString());
      }

      @Override
      public void info(CharSequence content, Throwable error) {
      }

      @Override
      public void info(Throwable error) {
      }

      @Override
      public boolean isWarnEnabled() {
        return false;
      }

      @Override
      public void warn(CharSequence content) {
      }

      @Override
      public void warn(CharSequence content, Throwable error) {
      }

      @Override
      public void warn(Throwable error) {
      }

      @Override
      public boolean isErrorEnabled() {
        return false;
      }

      @Override
      public void error(CharSequence content) {
      }

      @Override
      public void error(CharSequence content, Throwable error) {
      }

      @Override
      public void error(Throwable error) {
      }
    };
    outputStream = new MavenOutputStream(log);
  }

  @Test
  void testWriteSingleByteCharacters() throws IOException {
    outputStream.write('H');
    outputStream.write('e');
    outputStream.write('l');
    outputStream.write('l');
    outputStream.write('o');
    Assertions.assertTrue(loggedMessages.isEmpty());
    outputStream.write('\n');
    Assertions.assertEquals(1, loggedMessages.size());
    Assertions.assertEquals("Hello", loggedMessages.get(0));
  }

  @Test
  void testWriteSingleByteNewlineFlushesBuffer() throws IOException {
    outputStream.write('A');
    outputStream.write('B');
    outputStream.write('\n');
    outputStream.write('C');
    outputStream.write('\n');
    Assertions.assertEquals(2, loggedMessages.size());
    Assertions.assertEquals("AB", loggedMessages.get(0));
    Assertions.assertEquals("C", loggedMessages.get(1));
  }

  @Test
  void testWriteByteArray() throws IOException {
    byte[] data = "Hello\n".getBytes();
    outputStream.write(data);
    Assertions.assertEquals(1, loggedMessages.size());
    Assertions.assertEquals("Hello", loggedMessages.get(0));
  }

  @Test
  void testWriteByteArrayWithOffsetAndLength() throws IOException {
    byte[] data = "Hello World\n".getBytes();
    outputStream.write(data, 6, 5);
    outputStream.write('\n');
    Assertions.assertEquals(1, loggedMessages.size());
    Assertions.assertEquals("World", loggedMessages.get(0));
  }

  @Test
  void testWriteByteArrayWithOffsetAndLengthIncludingNewline() throws IOException {
    byte[] data = "Hello\nWorld\n".getBytes();
    outputStream.write(data, 0, data.length);
    Assertions.assertEquals(2, loggedMessages.size());
    Assertions.assertEquals("Hello", loggedMessages.get(0));
    Assertions.assertEquals("World", loggedMessages.get(1));
  }

  @Test
  void testFlushClearsBuffer() throws IOException {
    outputStream.write('A');
    outputStream.write('B');
    outputStream.flush();
    outputStream.write('\n');
    Assertions.assertEquals(1, loggedMessages.size());
    Assertions.assertEquals("", loggedMessages.get(0));
  }

  @Test
  void testWriteWithInfoDisabled() throws IOException {
    List<String> messages = new ArrayList<>();
    Log disabledLog = new Log() {
      @Override
      public boolean isDebugEnabled() {
        return false;
      }

      @Override
      public void debug(CharSequence content) {
      }

      @Override
      public void debug(CharSequence content, Throwable error) {
      }

      @Override
      public void debug(Throwable error) {
      }

      @Override
      public boolean isInfoEnabled() {
        return false;
      }

      @Override
      public void info(CharSequence content) {
        messages.add(content.toString());
      }

      @Override
      public void info(CharSequence content, Throwable error) {
      }

      @Override
      public void info(Throwable error) {
      }

      @Override
      public boolean isWarnEnabled() {
        return false;
      }

      @Override
      public void warn(CharSequence content) {
      }

      @Override
      public void warn(CharSequence content, Throwable error) {
      }

      @Override
      public void warn(Throwable error) {
      }

      @Override
      public boolean isErrorEnabled() {
        return false;
      }

      @Override
      public void error(CharSequence content) {
      }

      @Override
      public void error(CharSequence content, Throwable error) {
      }

      @Override
      public void error(Throwable error) {
      }
    };
    MavenOutputStream disabledOutputStream = new MavenOutputStream(disabledLog);
    disabledOutputStream.write('H');
    disabledOutputStream.write('i');
    disabledOutputStream.write('\n');
    Assertions.assertTrue(messages.isEmpty());
  }

}
