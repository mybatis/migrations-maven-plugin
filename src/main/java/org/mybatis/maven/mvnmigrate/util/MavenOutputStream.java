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
package org.mybatis.maven.mvnmigrate.util;

import java.io.IOException;
import java.io.OutputStream;

import org.apache.maven.plugin.logging.Log;

/**
 * A custom {@link OutputStream}.
 *
 * Writes all complete line (ended with \n character) to a maven
 * logger.
 *
 * @version $Id$
 */
public class MavenOutputStream extends OutputStream {

    /**
     * The new line '\n' char constant.
     */
    private static final char NEW_LINE = '\n';

    /**
     * The buffer used to maintain the line
     */
    final private StringBuilder buff = new StringBuilder();

    /**
     * The maven {@link Log}
     */
    final private Log log;

    /**
     * Creates a new instance of {@link MavenOutputStream}.
     *
     * @param log the maven logger L
     */
    public MavenOutputStream(final Log log) {
        this.log = log;
    }

    /**
     * {@inheritDoc}
     */
    public void write(byte[] b, int off, int len) throws IOException {
        for (int i = off; i < len; i++) {
            write((int) b[i]);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void write(byte[] b) throws IOException {
        write(b, 0, b.length);
    }

    /**
     * {@inheritDoc}
     */
    public void write(int data) throws IOException {
        if (NEW_LINE == data) {
            if (this.log.isInfoEnabled()) {
                this.log.info(buff.toString());
            }
            flush();
        } else {
            this.buff.append((char) data);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void flush() throws IOException {
        super.flush();
        this.buff.delete(0, this.buff.capacity());
    }

}
