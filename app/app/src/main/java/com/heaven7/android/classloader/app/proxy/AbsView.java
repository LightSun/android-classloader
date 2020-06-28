package com.heaven7.android.classloader.app.proxy;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.os.Parcelable;
import android.view.MotionEvent;
import android.view.View;

public abstract class AbsView<P extends Parcelable> {

    private final View proxyView;
    private P params;

    public AbsView(View proxyView) {
        if(!(proxyView instanceof ProxyView) && !(proxyView instanceof ProxyViewGroup)){
            throw new IllegalStateException("proxy view must be ProxyView or ProxyViewGroup.");
        }
        this.proxyView = proxyView;
    }

    public View getProxyView() {
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
        params = onCreate(ta);
    }

    public abstract int[] getStyleId();

    protected abstract P onCreate(TypedArray ta);

    public void onDestroy() {

    }
    //--------------------------------------
    public void onDraw(Canvas canvas) {

    }
    public void onPreDispatchDraw(Canvas canvas) {

    }
    public void onPostDispatchDraw(Canvas canvas) {

    }
    public boolean onTouchEvent(MotionEvent event) {
        return false;
    }

    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        ProxyViewDelegate pv = (ProxyViewDelegate) getProxyView();
        int w = View.getDefaultSize(pv.getSuggestedMinimumWidth(), widthMeasureSpec);
        int h = View.getDefaultSize(pv.getSuggestedMinimumHeight(), heightMeasureSpec);
        pv.applyWidthHeight(w, h);
    }
    public void onLayout(boolean changed, int left, int top, int right, int bottom) {

    }
    public void onSizeChanged(int w, int h, int oldw, int oldh) {

    }
    public void onConfigurationChanged(Configuration newConfig) {
    }

}
