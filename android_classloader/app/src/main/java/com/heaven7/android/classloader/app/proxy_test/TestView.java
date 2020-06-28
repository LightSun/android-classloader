package com.heaven7.android.classloader.app.proxy_test;

import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;
import android.view.ViewGroup;

import com.heaven7.android.classloader.app.R;
import com.heaven7.android.classloader.app.proxy.AbsView;
import com.heaven7.android.classloader.app.proxy.ProxyView;

public class TestView extends AbsView<TestView.Params> {

    private final Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Rect mRect = new Rect();

    public TestView(ProxyView proxyView) {
        super(proxyView);
    }
    @Override
    public int[] getStyleId() {
        return R.styleable.TestView;
    }

    @Override
    public void setParams(Params params) {
        super.setParams(params);
        invalidate();
    }

    @Override
    protected Params onCreate(TypedArray ta) {
        Params p = new Params();
        p.color = ta.getColor(R.styleable.TestView_test_view_color, Color.BLACK);
       // mPaint.setStyle(Paint.Style.FILL);
        return p;
    }
    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        View view = getProxyView();
        int w = view.getWidth();
        int h = view.getHeight();
        mRect.set(0, 0, w, h);

        mPaint.setColor(getParams().color);
        canvas.drawRect(mRect, mPaint);
    }
    public void onConfigurationChanged(Configuration newConfig) {
        View proxyView = getProxyView();
        ViewGroup.LayoutParams lp = proxyView.getLayoutParams();
        if(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE){
            lp.width = 1000;
            lp.height = 1000;
        }else {
            lp.width = 500;
            lp.height = 500;
        }
        proxyView.setLayoutParams(lp);
    }
    public static class Params implements Parcelable {
        int color;

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.color);
        }

        public Params() {
        }

        protected Params(Parcel in) {
            this.color = in.readInt();
        }

        public static final Parcelable.Creator<Params> CREATOR = new Parcelable.Creator<Params>() {
            @Override
            public Params createFromParcel(Parcel source) {
                return new Params(source);
            }

            @Override
            public Params[] newArray(int size) {
                return new Params[size];
            }
        };
    }
}
