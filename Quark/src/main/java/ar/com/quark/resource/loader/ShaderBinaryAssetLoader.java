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
package ar.com.quark.resource.loader;

import ar.com.quark.render.RenderCapabilities;
import ar.com.quark.render.shader.Shader;
import ar.com.quark.render.shader.ShaderParser;
import ar.com.quark.resource.AssetManager;
import ar.com.quark.resource.AssetKey;
import ar.com.quark.resource.AssetLoader;
import ar.com.quark.system.utility.array.Int8Array;
import ar.com.quark.system.utility.array.ArrayFactory;

import java.io.IOException;
import java.io.InputStream;

/**
 * <code>ShaderAssetLoader</code> encapsulate an {@link AssetLoader} for loading shader(s).
 */
public final class ShaderBinaryAssetLoader implements AssetLoader<Shader, Shader.Descriptor> {
    private final ShaderParser mParser;

    /**
     * <p>Constructor</p>
     */
    public ShaderBinaryAssetLoader(RenderCapabilities capabilities) {
        mParser = new ShaderParser(capabilities);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void load(AssetManager manager, AssetKey<Shader, Shader.Descriptor> key, InputStream input)
            throws IOException {
        //!
        //! Allocate an array and load the entire content in it.
        //!
        final Int8Array content = ArrayFactory.allocateInt8Array(input.available());

        byte[] bytes = new byte[1024];

        while (input.available() > 0) {
            content.write(bytes, 0, input.read(bytes));
        }

        //!
        //! Parse the shader.
        //!
        key.setAsset(mParser.generate(content));

        //!
        //! Dispose the array.
        //!
        ArrayFactory.free(content);
    }
}
