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
package org.quark.backend.teavm.system;

import org.quark.audio.AudioManager;
import org.quark.audio.DefaultAudioManager;
import org.quark.backend.teavm.input.WebInputKeyboard;
import org.quark.backend.teavm.input.WebInputMouse;
import org.quark.backend.teavm.openal.WebOpenALES10;
import org.quark.backend.teavm.opengl.WebOpenGLES32;
import org.quark.backend.teavm.resource.locator.XHRAssetLocator;
import org.quark.backend.teavm.utility.array.WebArrayFactory;
import org.quark.input.DefaultInputManager;
import org.quark.input.InputManager;
import org.quark.render.DefaultRender;
import org.quark.render.Render;
import org.quark.resource.DefaultAssetManager;
import org.quark.resource.loader.AudioWAVAssetLoader;
import org.quark.resource.loader.TextureDDSAssetLoader;
import org.quark.resource.loader.TexturePNGAssetLoader;
import org.quark.system.Display;
import org.quark.system.DisplayLifecycle;
import org.quark.system.utility.array.ArrayFactory;
import org.teavm.jso.JSBody;
import org.teavm.jso.JSFunctor;
import org.teavm.jso.JSMethod;
import org.teavm.jso.JSObject;
import org.teavm.jso.browser.Window;
import org.teavm.jso.dom.html.HTMLCanvasElement;

import java.util.Timer;
import java.util.TimerTask;

import static org.quark.Quark.*;

/**
 * <code>Web</code> represent the entry-point for any web-based application.
 */
public final class Web {
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
    private final Timer mInputThread = new Timer("QK-Input-Thread");
    private final Timer mAudioThread = new Timer("QK-Audio-Thread");

    /**
     * Hold the delta time (in normalised millisecond).
     */
    private long mTime = 0L;

    /**
     * Hold {@link Display} module.
     */
    private final WebDisplay mDisplay = (WebDisplay) (QKDisplay = new WebDisplay());

    /**
     * Hold {@link Render} module.
     */
    private final DefaultRender mRender = (DefaultRender) (QKRender = new DefaultRender());

    /**
     * Hold {@link AudioManager} module.
     */
    private final DefaultAudioManager mAudio = (DefaultAudioManager) (QKAudio = new DefaultAudioManager());

    /**
     * Hold {@link InputManager} module.
     */
    private final DefaultInputManager mInput = (DefaultInputManager) (QKInput = new DefaultInputManager());

    /**
     * Hold {@link InputManager} module.
     */
    private final DefaultAssetManager mResources
            = (DefaultAssetManager) (QKResources = new DefaultAssetManager(new ThreadGroupService()));

    /**
     * <p>Constructor</p>
     */
    private Web(DisplayLifecycle lifecycle) {
        mLifecycle = lifecycle;
    }

    /**
     * <p>Handle when the module create</p>
     */
    private void onModuleCreate(HTMLCanvasElement element, Display.Preference preference) {
        Window.current().getDocument().addEventListener("visibilitychange", evt -> {
            if (isHidden()) {
                mLifecycle.onPause();
            } else {
                mLifecycle.onResume();
            }
        });
        mDisplay.onModuleCreate(element, preference);

        //!
        //! Create audio module.
        //!
        mAudio.onModuleCreate(new WebOpenALES10());
        mAudioThread.schedule(new TimerTask() {
            @Override
            public void run() {
                mAudio.onModuleUpdate();
            }
        }, 0L, THREAD_AUDIO_DELAY);

        //!
        //! Create input module.
        //!
        mInput.onModuleCreate(
                new WebInputKeyboard(Window.current().getDocument().getDocumentElement()),
                new WebInputMouse(element));
        mInputThread.schedule(new TimerTask() {
            @Override
            public void run() {
                mInput.onModuleUpdate();
            }
        }, 0L, THREAD_INPUT_DELAY);

        //!
        //! Create render module.
        //!
        mRender.onModuleCreate(new WebOpenGLES32(element));


        //!
        //! Create resource module.
        //!
        mResources.registerAssetLocator("EXTERNAL", new XHRAssetLocator());

        mResources.registerAssetLoader(new TexturePNGAssetLoader(), "png");
        mResources.registerAssetLoader(new TextureDDSAssetLoader(), "dds", "s3tc");
        mResources.registerAssetLoader(new AudioWAVAssetLoader(), "wav");

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
        mResources.onModuleDestroy();

        mResources.unloadAllAssets();

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
        mRender.onModuleUpdate();
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
        if (mTime == -1L) {
            mTime = System.currentTimeMillis();
        }

        //!
        //! Render until the display is not active.
        //!
        onModuleRender(System.currentTimeMillis());

        //!
        //! Request to render again.
        //!
        onAnimationRequest(this::onModuleUpdate);
    }

    /**
     * <p>Handle when the module render</p>
     */
    private void onModuleRender(long time) {
        //!
        //! Handle the render notification.
        //!
        mLifecycle.onRender((time - mTime) / 1000.0f);

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
     * <p>Initialise <code>TeaVM</code></p>
     */
    public static void create(String element, DisplayLifecycle lifecycle, Display.Preference preference) {
        final HTMLCanvasElement canvas = (HTMLCanvasElement) Window.current().getDocument().getElementById(element);

        //!
        //! Create memory factory.
        //!
        //! NOTE: Most module requires this module.
        //!
        ArrayFactory.instance = new WebArrayFactory();

        //!
        //! Create entry
        //!
        final Web entry = new Web(lifecycle);
        entry.onModuleCreate(canvas, preference);
        entry.onModuleUpdate();
    }

    /**
     * @see <a href="https://developer.mozilla.org/es/docs/Web/API/Window/requestAnimationFrame">Extension</a>
     */
    @JSBody(params = {"method"}, script = "window.requestAnimationFrame = window.requestAnimationFrame " +
            "|| window.mozRequestAnimationFrame || window.webkitRequestAnimationFrame " +
            "|| window.msRequestAnimationFrame; requestAnimationFrame(method);")
    private static native void onAnimationRequest(AnimationHandler method);

    /**
     * @see <a href="https://developer.mozilla.org/en-US/docs/Web/API/Page_Visibility_API">Extension</a>
     */
    @JSBody(params = {}, script = "return document['hidden'];")
    private static native boolean isHidden();

    /**
     * @see <a href="https://developer.mozilla.org/es/docs/Web/API/Window/requestAnimationFrame">Extension</a>
     */
    @JSFunctor
    private interface AnimationHandler extends JSObject {
        @JSMethod
        void onAnimation();
    }

    /**
     * Implementation for {@link DefaultAssetManager.Service}.
     */
    private final static class ThreadGroupService implements DefaultAssetManager.Service {
        /**
         * {@inheritDoc}
         */
        @Override
        public void shutdown() {
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void execute(Runnable command) {
            new Thread(command).start();
        }
    }
}
