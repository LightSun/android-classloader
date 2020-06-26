/*
 * Copyright (c) 2017 Lukas Morawietz
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.heaven7.android.classloader;

import android.annotation.TargetApi;
import android.os.Build;

import com.android.dex.Dex;

import java.nio.ByteBuffer;

import dalvik.system.InMemoryDexClassLoader;

/**
 * @author copied from net
 */
//@RequiresApi(api = Build.VERSION_CODES.O)
@TargetApi(26)
class InMemoryAndroidClassLoader extends BaseAndroidClassLoader {
    //@Nullable
    private Dex last;

    public InMemoryAndroidClassLoader(ClassLoader parent) {
        super(parent);
    }

    @Override
    protected Class<?> loadClass(Dex dex, String name) throws ClassNotFoundException {
        last = dex;
        return new InMemoryDexClassLoader(ByteBuffer.wrap(dex.getBytes()), getParent()).loadClass(name);
    }

    @Override
    protected Dex getLastDex() {
        return last;
    }

    @Override
    public void reset() {
        last = null;
    }
}
