package com.heaven7.android.classloader.app.proxy;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import com.heaven7.android.classloader.app.R;

public class ProxyView extends View {

    private AbsView mView;

    public ProxyView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }
    public ProxyView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }
    @TargetApi(21)
    public ProxyView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }
    private void init(Context context, AttributeSet attrs){
        if(attrs == null){
            throw new IllegalStateException();
        }
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ProxyView);
        try {
            String cn = a.getString(R.styleable.ProxyView_proxy_delegate);
            mView = (AbsView) Class.forName(cn).getConstructor(ProxyView.class).newInstance(this);
            if(mView.getStyleId() != null){
                final int ap = a.getResourceId(R.styleable.ProxyView_proxy_style, 0);
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
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mView.onDraw(canvas);
    }
    @Override
    protected void onDetachedFromWindow() {
        mView.onDetachedFromWindow();
        super.onDetachedFromWindow();
    }
    public static class SavedState extends BaseSavedState{
        final Parcelable params;
        public SavedState(Parcelable superState, Parcelable params) {
            super(superState);
            this.params = params;
        }
        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            if(params == null){
                out.writeString(null);
            }else {
                out.writeString(params.getClass().getName());
                params.writeToParcel(out, flags);
            }
        }
        protected SavedState(Parcel in) {
            super(in);
            String cn = in.readString();
            if(cn != null){
                try {
                    params = (Parcelable) Class.forName(cn).getConstructor(Parcel.class).newInstance(in);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }else {
                params = null;
            }
        }
        @Override
        public String toString() {
            String str = "ProxyView.SavedState{"
                    + Integer.toHexString(System.identityHashCode(this))
                    + (params!= null ? params.toString() : "");
            return str + "}";
        }

        public static final Parcelable.Creator<SavedState> CREATOR =
                new Parcelable.Creator<SavedState>() {
                    public SavedState createFromParcel(Parcel in) {
                        return new SavedState(in);
                    }
                    public SavedState[] newArray(int size) {
                        return new SavedState[size];
                    }
                };
    }
}
