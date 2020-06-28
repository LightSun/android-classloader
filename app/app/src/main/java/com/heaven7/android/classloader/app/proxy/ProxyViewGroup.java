package com.heaven7.android.classloader.app.proxy;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.heaven7.android.classloader.app.R;

public class ProxyViewGroup extends ViewGroup implements ProxyViewDelegate {

    private AbsViewGroup mView;

    public ProxyViewGroup(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    public ProxyViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }
    @TargetApi(21)
    public ProxyViewGroup(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }
    protected void init(Context context, AttributeSet attrs){
        if(attrs == null){
            throw new IllegalStateException();
        }
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ProxyView);
        try {
            String cn = a.getString(R.styleable.ProxyView_lib_proxy_view_delegate);
            mView = (AbsViewGroup) Class.forName(cn).getConstructor(View.class).newInstance(this);
            if(mView.getStyleId() != null){
                final int ap = a.getResourceId(R.styleable.ProxyView_lib_proxy_view_style, 0);
                if (ap != 0) {
                    TypedArray a2 = context.obtainStyledAttributes(ap, mView.getStyleId());
                    try {
                        mView.initialize(a2);
                    }finally {
                        a2.recycle();
                    }
                }
            }else {
                mView.initialize(null);
            }
        }catch (Exception e){
            throw new RuntimeException(e);
        } finally {
            a.recycle();
        }
    }
    public <P extends Parcelable> AbsView<P> getRealView(){
        return mView;
    }

    public void applyWidthHeight(int measureWidth, int measureHeight){
        super.setMeasuredDimension(measureWidth, measureHeight);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mView.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        mView.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mView.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onConfigurationChanged(Configuration newConfig) {
        mView.onConfigurationChanged(newConfig);
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        return new SavedState(superState, mView.getParams());
    }
    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (!(state instanceof SavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }
        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());
        mView.setParams(ss.params);
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mView.onTouchEvent(event);
    }
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return mView.onInterceptTouchEvent(ev);
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mView.onDraw(canvas);
    }
    @Override
    protected void dispatchDraw(Canvas canvas) {
        mView.onPreDispatchDraw(canvas);
        super.dispatchDraw(canvas);
        mView.onPostDispatchDraw(canvas);
    }

    @Override
    protected void onDetachedFromWindow() {
        mView.onDestroy();
        super.onDetachedFromWindow();
    }
    @Override
    public int getSuggestedMinimumWidth() {
        return super.getSuggestedMinimumWidth();
    }
    @Override
    public int getSuggestedMinimumHeight() {
        return super.getSuggestedMinimumHeight();
    }
}
