/*
 * This file is part of Quark Framework, licensed under the APACHE License.
 *
 * Copyright (c) 2014-2016 Agustin L. Alvarez <wolftein1@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ar.com.quark.audio.factory;

import ar.com.quark.audio.Audio;
import ar.com.quark.audio.AudioFormat;
import ar.com.quark.system.utility.Disposable;
import ar.com.quark.system.utility.Manageable;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

import static ar.com.quark.Quark.QKAudio;

/**
 * Specialised implementation for {@link Audio} that are being stream.
 */
public final class FactoryStreamingAudio extends Audio {
    private InputStream mData;

    /**
     * <p>Constructor</p>
     */
    public FactoryStreamingAudio(InputStream data, AudioFormat format, int duration, int rate) {
        super(format, duration, rate);

        mData = data.markSupported() ? data : new BufferedInputStream(data);
        mData.mark(Integer.MAX_VALUE);
    }

    /**
     * <p>Read audio's data from the {@link InputStream}</p>
     *
     * @param buffer the target buffer
     * @param offset the target offset
     * @param length the target length
     *
     * @return the number of byte(s) read from the stream
     */
    public int read(byte[] buffer, int offset, int length) {
        int bytes = 0;

        try {
            bytes = mData.read(buffer, offset, length);
        } catch (IOException ignored) {
        }
        return bytes;
    }

    /**
     * <p>Reset the stream</p>
     */
    public void reset() {
        try {
            mData.reset();
        } catch (IOException ignored) {
        }
    }

    /**
     * <p>Close the stream</p>
     */
    public void close() {
        try {
            mData.close();
        } catch (IOException ignored) {
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isStreaming() {
        return true;
    }

    /**
     * @see Manageable#delete()
     */
    @Override
    public void delete() {
        super.delete();

        QKAudio.delete(this);
    }

    /**
     * @see Manageable#deleteAllMemory()
     */
    @Override
    public void deleteAllMemory() {
        close();

        mData = null;
    }

    /**
     * @see Disposable#dispose()
     */
    @Override
    public void dispose() {
        QKAudio.delete(this);
    }
}
