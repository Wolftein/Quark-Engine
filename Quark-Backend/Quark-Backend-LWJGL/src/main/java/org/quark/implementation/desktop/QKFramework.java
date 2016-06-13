/*
 * This file is part of Quark Engine, licensed under the APACHE License.
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
package org.quark.implementation.desktop;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWFramebufferSizeCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryUtil;
import org.quark.system.Framework;
import org.quark.system.FrameworkSetting;

import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * <a href="http://lwjgl.org">LWJGL 3</a> implementation for {@link Framework}.
 *
 * @author Agustin L. Alvarez (wolftein1@gmail.com)
 */
public final class QKFramework implements Framework {
    /**
     * Hold the underlying handle of the window implementation.
     */
    private long mHandle = MemoryUtil.NULL;

    /**
     * Hold temporally buffer for holding dimension data.
     */
    private final IntBuffer mDimension = BufferUtils.createIntBuffer(0x02);

    /**
     * <p>Called to initialise the module</p>
     * <p>
     * (INTERNAL)
     */
    public void create(FrameworkSetting setting, GLFWFramebufferSizeCallback callback) {
        if (GLFW.glfwInit()) {
            final DisplayMode displayMode = setting.getMode();

            //!
            //! Fill each window hint from the configuration.
            //!
            GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE,
                    setting.isResizable() ? GLFW.GLFW_TRUE : GLFW.GLFW_FALSE);
            GLFW.glfwWindowHint(GLFW.GLFW_DECORATED,
                    setting.isDecorated() ? GLFW.GLFW_TRUE : GLFW.GLFW_FALSE);
            GLFW.glfwWindowHint(GLFW.GLFW_SRGB_CAPABLE,
                    setting.isSRGB() ? GLFW.GLFW_TRUE : GLFW.GLFW_FALSE);
            GLFW.glfwWindowHint(GLFW.GLFW_SAMPLES, setting.getSamples());

            if (setting.isFullscreen()) {
                GLFW.glfwWindowHint(GLFW.GLFW_REFRESH_RATE, displayMode.getRate());

                mHandle = GLFW.glfwCreateWindow(displayMode.getWidth(), displayMode.getHeight(), setting.getTitle(),
                        GLFW.glfwGetPrimaryMonitor(), mHandle);

                if (mHandle == MemoryUtil.NULL) {
                    throw new RuntimeException("OpenGL 3.0+ is not supported by the gpu");
                }
            } else {
                mHandle = GLFW.glfwCreateWindow(displayMode.getWidth(), displayMode.getHeight(), setting.getTitle(),
                        MemoryUtil.NULL, mHandle);

                if (mHandle == MemoryUtil.NULL) {
                    throw new RuntimeException("OpenGL 3.0+ is not supported by the gpu");
                }

                //!
                //! Center window on windowed mode.
                //!
                final GLFWVidMode video = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());
                GLFW.glfwSetWindowPos(mHandle,
                        (video.width() - displayMode.getWidth()) / 2, (video.height() - displayMode.getHeight()) / 2);
            }
            setSynchronised(setting.isSynchronised());

            //!
            //! Attach resizable callback.
            //!
            GLFW.glfwSetFramebufferSizeCallback(mHandle, callback);

            //!
            //! Initialise the context for OpenGL.
            //!
            GLFW.glfwMakeContextCurrent(mHandle);
            GL.createCapabilities();
        }
    }

    /**
     * <p>Called to update the module</p>
     * <p>
     * (INTERNAL)
     */
    public void update() {
        GLFW.glfwSwapBuffers(mHandle);
        GLFW.glfwPollEvents();
    }

    /**
     * <p>Called to dispose the module</p>
     * <p>
     * (INTERNAL)
     */
    public void destroy() {
        GLFW.glfwDestroyWindow(mHandle);
        GLFW.glfwTerminate();
    }

    /**
     * <p>Get the handle of the framework</p>
     *
     * @return the handle of the framework
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

            modes.add(new DisplayMode(
                    videoMode.width(),
                    videoMode.height(),
                    videoMode.refreshRate()));
        }

        return modes;
    }
}
