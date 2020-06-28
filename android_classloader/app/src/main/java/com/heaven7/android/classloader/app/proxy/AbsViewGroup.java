package com.heaven7.android.classloader.app.proxy;

import android.os.Parcelable;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

public abstract class AbsViewGroup<P extends Parcelable> extends AbsView<P> {

    public AbsViewGroup(View proxyView) {
        super(proxyView);
    }

    @Override
    public ViewGroup getProxyView() {
        return (ViewGroup) super.getProxyView();
    }

    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false;
    }
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return false;
    }
}
