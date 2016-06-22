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
package org.quark.backend.lwjgl.system;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryUtil;
import org.quark.render.texture.Image;
import org.quark.system.Display;
import org.quark.system.DisplayMode;

import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Implementation for {@link Display}.
 */
public final class DesktopDisplay implements Display {
    /**
     * Hold the underlying handle of the window implementation.
     */
    private long mHandle = MemoryUtil.NULL;

    /**
     * Hold the cursor of the display.
     */
    private long mCursor = MemoryUtil.NULL;

    /**
     * Hold temporally buffer for holding dimension data.
     */
    private final IntBuffer mDimension = BufferUtils.createIntBuffer(0x02);

    /**
     * <p>Handle when the module create</p>
     */
    public void onModuleCreate(Display.Preference preference,
            GLFWFramebufferSizeCallback resize, GLFWWindowIconifyCallback iconify) {
        if (GLFW.glfwInit()) {
            final DisplayMode mode = preference.getMode();

            //!
            //! Fill each window hint from the configuration.
            //!
            GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE,
                    preference.isResizable() ? GLFW.GLFW_TRUE : GLFW.GLFW_FALSE);
            GLFW.glfwWindowHint(GLFW.GLFW_DECORATED,
                    preference.isDecorated() ? GLFW.GLFW_TRUE : GLFW.GLFW_FALSE);
            GLFW.glfwWindowHint(GLFW.GLFW_SRGB_CAPABLE,
                    preference.isSRGB() ? GLFW.GLFW_TRUE : GLFW.GLFW_FALSE);
            GLFW.glfwWindowHint(GLFW.GLFW_SAMPLES, preference.getSamples());

            if (preference.isFullscreen()) {
                GLFW.glfwWindowHint(GLFW.GLFW_REFRESH_RATE, mode.getRate());

                mHandle = GLFW.glfwCreateWindow(mode.getWidth(), mode.getHeight(), preference.getTitle(),
                        GLFW.glfwGetPrimaryMonitor(), mHandle);

                if (mHandle == MemoryUtil.NULL) {
                    throw new RuntimeException("Pixel-format not accelerated");
                }
            } else {
                mHandle = GLFW.glfwCreateWindow(mode.getWidth(), mode.getHeight(), preference.getTitle(),
                        MemoryUtil.NULL, mHandle);

                if (mHandle == MemoryUtil.NULL) {
                    throw new RuntimeException("Pixel-format not accelerated");
                }

                //!
                //! Center window on windowed mode.
                //!
                final GLFWVidMode video = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());
                GLFW.glfwSetWindowPos(mHandle,
                        (video.width() - mode.getWidth()) / 2, (video.height() - mode.getHeight()) / 2);
            }
            setSynchronised(preference.isSynchronised());

            //!
            //! Attach resizable and iconify callback.
            //!
            GLFW.glfwSetFramebufferSizeCallback(mHandle, resize);
            GLFW.glfwSetWindowIconifyCallback(mHandle, iconify);

            //!
            //! Initialise the context for OpenGL.
            //!
            GLFW.glfwMakeContextCurrent(mHandle);
            GL.createCapabilities();
        }
    }

    /**
     * <p>Handle when the module destroy</p>
     */
    public void onModuleDestroy() {
        if (mCursor != MemoryUtil.NULL) {
            GLFW.glfwDestroyCursor(mCursor);
        }
        if (mHandle != MemoryUtil.NULL) {
            GLFW.glfwDestroyWindow(mHandle);
        }
        GLFW.glfwTerminate();
    }

    /**
     * <p>Handle when the module update</p>
     */
    public void onModuleUpdate() {
        GLFW.glfwSwapBuffers(mHandle);
        GLFW.glfwPollEvents();
    }

    /**
     * <p>Get the handle of the window</p>
     *
     * @return the handle of the window
     */
    public long getHandle() {
        return mHandle;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setDimension(int width, int height) {
        GLFW.glfwSetWindowSize(mHandle, width, height);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setTitle(String title) {
        GLFW.glfwSetWindowTitle(mHandle, title);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setSynchronised(boolean activate) {
        GLFW.glfwSwapInterval(activate ? 1 : 0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setCursor(Image image, int xHotspot, int yHotspot) {
        if (mCursor != MemoryUtil.NULL) {
            //!
            //! Ensure the previously cursor is destroyed.
            //!
            GLFW.glfwDestroyCursor(mCursor);
        }

        if (image == null) {
            //!
            //! By default use the arrow cursor.
            //!
            mCursor = GLFW.glfwCreateStandardCursor(GLFW.GLFW_ARROW_CURSOR);
        } else {
            final GLFWImage hwImage = GLFWImage.malloc();
            hwImage.width(image.getWidth());
            hwImage.height(image.getHeight());
            hwImage.pixels(image.getLayer().get(0).data.data());

            //!
            //! Create the new cursor.
            //!
            mCursor = GLFW.glfwCreateCursor(hwImage, xHotspot, yHotspot);
        }

        //!
        //! Set the new cursor.
        //!
        GLFW.glfwSetCursor(mHandle, mCursor);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void switchToWindowed(DisplayMode mode) {
        if (isFullscreen()) {
            //!
            //! Only proceed if the window is in fullscreen mode
            //!
            GLFW.glfwSetWindowMonitor(mHandle, MemoryUtil.NULL, 0, 0,
                    mode.getWidth(),
                    mode.getHeight(),
                    mode.getRate());

            //!
            //! Center the window on the primary device
            //!
            final GLFWVidMode video = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());
            GLFW.glfwSetWindowPos(mHandle,
                    (video.width() - mode.getWidth()) / 2, (video.height() - mode.getHeight()) / 2);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void switchToFullscreen(DisplayMode mode) {
        if (isWindowed()) {
            //!
            //! Only proceed if the window is in windowed mode
            //!
            GLFW.glfwSetWindowMonitor(mHandle, GLFW.glfwGetPrimaryMonitor(), 0, 0,
                    mode.getWidth(),
                    mode.getHeight(),
                    mode.getRate());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void switchToFullscreen() {
        //!
        //! Switch the display to windowed fullscreen mode using the primary device
        //!
        switchToFullscreen(getDisplayMode());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getWidth() {
        GLFW.nglfwGetWindowSize(mHandle, MemoryUtil.memAddressSafe(mDimension, 0x00), MemoryUtil.NULL);
        return mDimension.get(0x00);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getHeight() {
        GLFW.nglfwGetWindowSize(mHandle, MemoryUtil.NULL, MemoryUtil.memAddressSafe(mDimension, 0x01));
        return mDimension.get(0x01);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isResizable() {
        return GLFW.glfwGetWindowAttrib(mHandle, GLFW.GLFW_RESIZABLE) == GLFW.GLFW_TRUE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isDecorated() {
        return GLFW.glfwGetWindowAttrib(mHandle, GLFW.GLFW_DECORATED) == GLFW.GLFW_TRUE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isWindowed() {
        return GLFW.glfwGetWindowMonitor(mHandle) == MemoryUtil.NULL;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isFullscreen() {
        return GLFW.glfwGetWindowMonitor(mHandle) != MemoryUtil.NULL;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isActive() {
        return mHandle != MemoryUtil.NULL && !GLFW.glfwWindowShouldClose(mHandle);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DisplayMode getDisplayMode() {
        final GLFWVidMode mode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());
        return new DisplayMode(mode.width(), mode.height(), mode.refreshRate());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<DisplayMode> getAvailableDisplayModes() {
        final GLFWVidMode.Buffer videoModeList = GLFW.glfwGetVideoModes(GLFW.glfwGetPrimaryMonitor());

        final List<DisplayMode> modes = new ArrayList<>(videoModeList.limit());

        while (videoModeList.hasRemaining()) {
            final GLFWVidMode videoMode = videoModeList.get();

            modes.add(new DisplayMode(videoMode.width(), videoMode.height(), videoMode.refreshRate()));
        }

        return modes;
    }
}
