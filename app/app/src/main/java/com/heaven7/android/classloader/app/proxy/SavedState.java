package com.heaven7.android.classloader.app.proxy;

import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;

import java.lang.reflect.Constructor;

public class SavedState extends View.BaseSavedState {
    final Parcelable params;

    public SavedState(Parcelable superState, Parcelable params) {
        super(superState);
        this.params = params;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        super.writeToParcel(out, flags);
        if (params == null) {
            out.writeString(null);
        } else {
            out.writeString(params.getClass().getName());
            params.writeToParcel(out, flags);
        }
    }

    protected SavedState(Parcel in) {
        super(in);
        String cn = in.readString();
        if (cn != null) {
            try {
                Constructor<?> cons = Class.forName(cn).getConstructor(Parcel.class);
                cons.setAccessible(true);
                params = (Parcelable) cons.newInstance(in);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            params = null;
        }
    }

    @Override
    public String toString() {
        String str = "ProxyView.SavedState{"
                + Integer.toHexString(System.identityHashCode(this))
                + (params != null ? params.toString() : "");
        return str + "}";
    }

    public static final Creator<SavedState> CREATOR =
            new Creator<SavedState>() {
                public SavedState createFromParcel(Parcel in) {
                    return new SavedState(in);
                }

                public SavedState[] newArray(int size) {
                    return new SavedState[size];
                }
            };
}