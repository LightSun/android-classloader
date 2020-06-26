package com.heaven7.android.classloader;

import android.os.Build;

import com.heaven7.java.base.util.FileUtils;

import java.io.File;

/**
 * the class loader manager used to create classloader
 * @author heaven7
 */
public class ClassLoaderManager {

    private final File mCacheDirectory;
    private final boolean mMemoryFirst;

    public ClassLoaderManager(File cacheDirectory) {
        this(cacheDirectory, false);
    }
    public ClassLoaderManager(File cacheDirectory, boolean memoryFirst) {
        this.mMemoryFirst = memoryFirst;
        this.mCacheDirectory = cacheDirectory;
    }

    public GeneratedClassLoader createClassLoader(ClassLoader parent){
        if(mMemoryFirst && Build.VERSION.SDK_INT >= 26){
            return new InMemoryAndroidClassLoader(parent);
        }
        return new FileAndroidClassLoader(parent, mCacheDirectory);
    }

    public void clearCache(){
        FileUtils.deleteDir(mCacheDirectory);
    }

}
