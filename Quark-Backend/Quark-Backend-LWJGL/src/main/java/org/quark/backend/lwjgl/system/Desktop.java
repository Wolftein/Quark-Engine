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

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWFramebufferSizeCallback;
import org.lwjgl.glfw.GLFWWindowIconifyCallback;
import org.quark.audio.AudioManager;
import org.quark.audio.SimpleAudioManager;
import org.quark.backend.lwjgl.input.DesktopInputKeyboard;
import org.quark.backend.lwjgl.input.DesktopInputMouse;
import org.quark.backend.lwjgl.openal.DesktopALES10;
import org.quark.backend.lwjgl.opengl.DesktopGLES32;
import org.quark.backend.lwjgl.utility.array.DesktopArrayFactory;
import org.quark.input.InputManager;
import org.quark.input.SimpleInputManager;
import org.quark.render.Render;
import org.quark.render.SimpleRender;
import org.quark.resource.SimpleAssetManager;
import org.quark.resource.loader.*;
import org.quark.resource.locator.ClassAssetLocator;
import org.quark.resource.locator.FilesAssetLocator;
import org.quark.system.Display;
import org.quark.system.DisplayLifecycle;
import org.quark.system.utility.array.ArrayFactory;

import java.util.Timer;
import java.util.TimerTask;

import static org.quark.Quark.*;

/**
 * <a href="http://lwjgl.org">LWJGL 3</a> entry.
 */
public final class Desktop {
    /**
     * Encapsulate the delay per update of the input thread.
     * <p>
     * NOTE: Update per 25 millisecond.
     */
    private final static long THREAD_INPUT_DELAY = 25L;

    /**
     * Encapsulate the delay per update of the audio thread.
     * <p>
     * NOTE: Update per 50 millisecond.
     */
    private final static long THREAD_AUDIO_DELAY = 50L;

    /**
     * Hold the lifecycle.
     */
    private final DisplayLifecycle mLifecycle;

    /**
     * Hold all thread(s).
     */
    private final Timer mInputThread = new Timer("QK-Input-Thread", false);
    private final Timer mAudioThread = new Timer("QK-Audio-Thread", false);

    /**
     * Hold the delta time (in normalised millisecond).
     */
    private double mTime = 0.0;

    /**
     * Hold {@link Display} module.
     */
    private final DesktopDisplay mDisplay = (DesktopDisplay) (QKDisplay = new DesktopDisplay());

    /**
     * Hold {@link Render} module.
     */
    private final SimpleRender mRender = (SimpleRender) (QKRender = new SimpleRender());

    /**
     * Hold {@link AudioManager} module.
     */
    private final SimpleAudioManager mAudio = (SimpleAudioManager) (QKAudio = new SimpleAudioManager());

    /**
     * Hold {@link InputManager} module.
     */
    private final SimpleInputManager mInput = (SimpleInputManager) (QKInput = new SimpleInputManager());

    /**
     * <p>Constructor</p>
     */
    private Desktop(DisplayLifecycle lifecycle) {
        mLifecycle = lifecycle;
    }

    /**
     * <p>Handle when the module create</p>
     */
    private void onModuleCreate(Display.Preference preference) {
        //!
        //! Create display module.
        //!
        final GLFWFramebufferSizeCallback resize = GLFWFramebufferSizeCallback.create((window, width, height) ->
        {
            mLifecycle.onResize(width, height);

            //!
            //! NOTE: This is required due to GLFW3 limitation.
            //!
            onModuleRender(GLFW.glfwGetTime());
        });
        final GLFWWindowIconifyCallback iconify = GLFWWindowIconifyCallback.create((window, iconified) ->
        {
            if (iconified) {
                mLifecycle.onPause();
            } else {
                mLifecycle.onResume();
            }
        });
        mDisplay.onModuleCreate(preference, resize, iconify);

        //!
        //! Create audio module.
        //!
        mAudio.onModuleCreate(new DesktopALES10());
        mAudioThread.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                mAudio.onModuleUpdate();
            }
        }, 0L, THREAD_AUDIO_DELAY);

        //!
        //! Create input module.
        //!
        mInput.onModuleCreate(new DesktopInputKeyboard(mDisplay.getHandle()), new DesktopInputMouse(mDisplay.getHandle()));
        mInputThread.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                mInput.onModuleUpdate();
            }
        }, 0L, THREAD_INPUT_DELAY);

        //!
        //! Create render module.
        //!
        mRender.onModuleCreate(new DesktopGLES32());

        //!
        //! Create resource module.
        //!
        QKResources = new SimpleAssetManager();
        QKResources.registerAssetLocator("INTERNAL", new ClassAssetLocator());
        QKResources.registerAssetLocator("EXTERNAL", new FilesAssetLocator());

        QKResources.registerAssetLoader(new TexturePNGAssetLoader(), "png");
        QKResources.registerAssetLoader(new TextureDDSAssetLoader(), "dds", "s3tc");
        QKResources.registerAssetLoader(new ShaderGLSLAssetLoader(mRender.getCapabilities()), "pipeline");
        QKResources.registerAssetLoader(new AudioWAVAssetLoader(), "wav");
        QKResources.registerAssetLoader(new AudioOGGAssetLoader(), "ogg");

        //!
        //! Handle the create notification.
        //!
        mLifecycle.onCreate();
        mLifecycle.onResize(mDisplay.getWidth(), mDisplay.getHeight());
    }

    /**
     * <p>Handle when the module destroy</p>
     */
    private void onModuleDestroy() {
        //!
        //! Handle the destroy notification.
        //!
        mLifecycle.onPause();
        mLifecycle.onDispose();

        //!
        //! Unload resource module.
        //!
        QKResources.unloadAllAssets();

        //!
        //! Unload input module.
        //!
        mInputThread.cancel();
        mInput.onModuleDestroy();

        //!
        //! Unload audio module.
        //!
        mAudioThread.cancel();
        mAudio.onModuleDestroy();

        //!
        //! Unload render module.
        //!
        //! NOTE: Update the render to destroy all render component(s)
        //!
        mRender.onModuleDestroy();

        //!
        //! Unload display module.
        //!
        mDisplay.onModuleDestroy();
    }

    /**
     * <p>Handle when the module update</p>
     */
    private void onModuleUpdate() {
        //!
        //! Calculate the initial time of the frame.
        //!
        mTime = GLFW.glfwGetTime();

        do {
            //!
            //! Render until the display is not active.
            //!
            onModuleRender(GLFW.glfwGetTime());
        } while (mDisplay.isActive());
    }

    /**
     * <p>Handle when the module render</p>
     */
    private void onModuleRender(double time) {
        //!
        //! Handle the render notification.
        //!
        mLifecycle.onRender((float) (time - mTime));

        //!
        //! Update the new delta time.
        //!
        mTime = time;

        //!
        //! Update the render.
        //!
        //! NOTE: House-keeping of render component(s).
        //!
        mRender.onModuleUpdate();

        //!
        //! Update the display.
        //!
        //! NOTE: Will synchronise with the window.
        //!
        mDisplay.onModuleUpdate();
    }

    /**
     * <p>Initialise <code>Desktop</code></p>
     */
    public static void create(DisplayLifecycle lifecycle, Display.Preference preference) {
        //!
        //! Create memory factory.
        //!
        //! NOTE: Most module requires this module.
        //!
        ArrayFactory.instance = new DesktopArrayFactory();

        //!
        //! Create entry
        //!
        final Desktop entry = new Desktop(lifecycle);
        entry.onModuleCreate(preference);
        entry.onModuleUpdate();
        entry.onModuleDestroy();
    }
}