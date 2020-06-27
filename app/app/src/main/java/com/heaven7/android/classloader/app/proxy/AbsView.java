package com.heaven7.android.classloader.app.proxy;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.os.Parcelable;
import android.view.MotionEvent;
import android.view.View;

public abstract class AbsView<P extends Parcelable> {

    private final ProxyView proxyView;
    private P params;

    public AbsView(ProxyView proxyView) {
        this.proxyView = proxyView;
    }

    public ProxyView getProxyView() {
        return proxyView;
    }

    public Resources getResources() {
        return proxyView.getResources();
    }

    public Context getContext() {
        return proxyView.getContext();
    }

    public void invalidate() {
        getProxyView().invalidate();
    }

    public void postInvalidate() {
        getProxyView().postInvalidate();
    }

    public void post(Runnable task) {
        getProxyView().post(task);
    }

    public void postDelayed(Runnable action, long delayMillis) {
        getProxyView().postDelayed(action, delayMillis);
    }

    public P getParams() {
        return params;
    }

    //you should refresh ui on here
    public void setParams(P params) {
        this.params = params;
    }

    public void initialize(TypedArray ta) {
        params = initialize0(ta);
    }

    public abstract int[] getStyleId();

    protected abstract P initialize0(TypedArray ta);

    public void onDetachedFromWindow() {

    }

    public void onDraw(Canvas canvas) {

    }

    public boolean onTouchEvent(MotionEvent event) {
        return false;
    }

    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int w = View.getDefaultSize(0, widthMeasureSpec);
        int h = View.getDefaultSize(0, heightMeasureSpec);
        getProxyView().applyWidthHeight(w, h);
    }

    public void onLayout(boolean changed, int left, int top, int right, int bottom) {

    }

    public void onSizeChanged(int w, int h, int oldw, int oldh) {

    }
}
