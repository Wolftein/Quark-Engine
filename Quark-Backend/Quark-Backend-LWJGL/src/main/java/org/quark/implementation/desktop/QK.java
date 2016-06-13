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

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWFramebufferSizeCallback;
import org.quark.implementation.desktop.audio.QKOpenAL_1_0;
import org.quark.implementation.desktop.input.QKInputKeyboard;
import org.quark.implementation.desktop.input.QKInputMouse;
import org.quark.implementation.desktop.render.QKOpenGL_3_0;
import org.quark.input.DefaultInputManager;
import org.quark.resource.DefaultAssetManager;
import org.quark.resource.loader.*;
import org.quark.resource.locator.ClassAssetLocator;
import org.quark.system.FrameworkLifecycle;
import org.quark.system.FrameworkSetting;

import java.util.Timer;
import java.util.TimerTask;

import static org.quark.Quark.*;

/**
 * <a href="http://lwjgl.org">LWJGL 3</a> entry.
 *
 * @author Agustin L. Alvarez (wolftein1@gmail.com)
 */
public final class QK {
    /**
     * Encapsulate the delay per update of the input thread.
     */
    private final static long THREAD_INPUT_DELAY = 25L;

    /**
     * Encapsulate the delay per update of the audio thread.
     */
    private final static long THREAD_AUDIO_DELAY = 50L;

    /**
     * Hold the lifecycle of the application.
     */
    private final FrameworkLifecycle mLifecycle;

    /**
     * Hold the setting of the application.
     */
    private final FrameworkSetting mSetting;

    /**
     * Hold the framework of the application
     */
    private final QKFramework mFramework = (QKFramework) (Qk = new QKFramework());

    /**
     * Hold the audio of the application
     */
    private final QKOpenAL_1_0 mAudio = (QKOpenAL_1_0) (QkAudioManager = new QKOpenAL_1_0());

    /**
     * Hold the render of the application
     */
    private final QKOpenGL_3_0 mRender = (QKOpenGL_3_0) (QkRender = new QKOpenGL_3_0());

    /**
     * Hold the input of the application
     */
    private DefaultInputManager mInput = null;

    /**
     * Hold the resources of the application
     */
    private final DefaultAssetManager mAssetManager = (DefaultAssetManager) (QkAssetManager = new DefaultAssetManager());

    /**
     * Hold the time of the render.
     */
    private double mTime = -1.0;

    /**
     * Hold all thread(s).
     */
    private Timer mInputThread = new Timer("QK-Input-Thread", true);
    private Timer mAudioThread = new Timer("QK-Audio-Thread", true);

    /**
     * <p>Constructor</p>
     */
    protected QK(FrameworkLifecycle lifecycle, FrameworkSetting setting) {
        mLifecycle = lifecycle;
        mSetting = setting;
    }

    /**
     * <p>Called to initialise the module</p>
     */
    protected void onCreate() {
        Thread.currentThread().setName("QK-Main");

        //!
        //! Initialise (LWJGLFramework)
        //!
        mFramework.create(mSetting, GLFWFramebufferSizeCallback.create((window, width, height) -> {
            mLifecycle.onResize(width, height);

            //!
            //! Refresh the screen.
            //!
            onRender(GLFW.glfwGetTime());
        }));


        //!
        //! Initialise (LWJGLOpenAL)
        //!
        mAudio.create();
        mAudioThread.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                mAudio.update();
            }
        }, 0L, THREAD_AUDIO_DELAY);


        //!
        //! Initialise (LWJGLOpenGL)
        //!
        mRender.create();

        //!
        //! Initialise (DefaultInputManager)
        //!
        mInput = (DefaultInputManager) (QkInputManager = new DefaultInputManager(
                new QKInputKeyboard(mFramework.getHandle()),
                new QKInputMouse(mFramework.getHandle())));
        mInput.create();
        mInputThread.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                mInput.update();
            }
        }, 0L, THREAD_INPUT_DELAY);

        //!
        //! Initialise (DefaultAssetManager)
        //!
        QkAssetManager = new DefaultAssetManager();
        QkAssetManager.registerAssetLocator("INTERNAL", new ClassAssetLocator());
        QkAssetManager.registerAssetLoader(
                new TexturePNGAssetLoader(), "png");
        QkAssetManager.registerAssetLoader(
                new TextureDDSAssetLoader(), "dds", "s3tc");
        QkAssetManager.registerAssetLoader(
                new ShaderGLSLAssetLoader(QkRender.getCapabilities()), "pipeline");
        QkAssetManager.registerAssetLoader(
                new AudioWAVEAssetLoader(), "wav", "wave");
        QkAssetManager.registerAssetLoader(
                new AudioOGGAssetLoader(), "ogg");

        //!
        //! Notify the user.
        //!
        mLifecycle.onCreate();
    }

    /**
     * <p>Called to update the module</p>
     */
    protected void onLoop() {
        //!
        //! Calculate the initial time of the frame.
        //!
        mTime = GLFW.glfwGetTime();

        //!
        //! Run until the framework is closed.
        //!
        while (mFramework.isActive()) {
            onRender(GLFW.glfwGetTime());
        }
    }

    /**
     * <p>Called to render the module</p>
     */
    protected void onRender(double time) {
        //!
        //! Render the frame.
        //!
        mLifecycle.onRender((float) (time - mTime));

        //!
        //! Update the renderer.
        //!
        mRender.update();

        //!
        //! Update the new delta time.
        //!
        mTime = time;

        //!
        //! Update the framework.
        //!
        mFramework.update();
    }

    /**
     * <p>Called to dispose the module</p>
     */
    protected void onDestroy() {
        //!
        //! Notify the user.
        //!
        mLifecycle.onDispose();

        //!
        //! Destroy (DefaultAssetManager)
        //!
        mAssetManager.unloadAllAssets();

        //!
        //! Destroy (DefaultInputManager)
        //!
        mInputThread.cancel();
        mInput.destroy();

        //!
        //! Destroy (LWJGLOpenAL)
        //!
        mAudioThread.cancel();
        mAudio.destroy();

        //!
        //! Destroy (LWJGLOpenGL)
        //!
        mRender.destroy();

        //!
        //! Destroy (LWJGLFramework)
        //!
        mFramework.destroy();
    }

    /**
     * <p>Initialise <code>QuarkApplication</code></p>
     */
    public static void create(FrameworkLifecycle lifecycle, FrameworkSetting setting) {
        final QK entry = new QK(lifecycle, setting);
        entry.onCreate();
        entry.onLoop();
        entry.onDestroy();
    }
}
