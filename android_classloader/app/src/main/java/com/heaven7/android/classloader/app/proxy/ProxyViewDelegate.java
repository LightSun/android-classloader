package com.heaven7.android.classloader.app.proxy;


public interface ProxyViewDelegate {

    int getSuggestedMinimumWidth();
    int getSuggestedMinimumHeight();

    void applyWidthHeight(int w, int h);

}
