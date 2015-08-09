package com.desmond.squarecamera;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by desmond on 9/8/15.
 */
public class ImageParameters implements Parcelable {

    public boolean mIsPortrait;

    public int mDisplayOrientation;
    public int mLayoutOrientation;

    public int mCoverHeight, mCoverWidth;
    public int mPreviewHeight, mPreviewWidth;

    public ImageParameters(Parcel in) {
        mIsPortrait = (in.readByte() == 1);

        mDisplayOrientation = in.readInt();
        mLayoutOrientation = in.readInt();

        mCoverHeight = in.readInt();
        mCoverWidth = in.readInt();
        mPreviewHeight = in.readInt();
        mPreviewWidth = in.readInt();
    }

    public ImageParameters() {}

    public int calculateCoverWidthHeight() {
       return Math.abs(mPreviewHeight - mPreviewWidth) / 2;
    }

    public int getAnimationParameter() {
        return mIsPortrait ? mCoverHeight : mCoverWidth;
    }

    public boolean isPortrait() {
        return mIsPortrait;
    }

    public ImageParameters createCopy() {
        ImageParameters imageParameters = new ImageParameters();

        imageParameters.mIsPortrait = mIsPortrait;
        imageParameters.mDisplayOrientation = mDisplayOrientation;
        imageParameters.mLayoutOrientation = mLayoutOrientation;

        imageParameters.mCoverHeight = mCoverHeight;
        imageParameters.mCoverWidth = mCoverWidth;
        imageParameters.mPreviewHeight = mPreviewHeight;
        imageParameters.mPreviewWidth = mPreviewWidth;

        return imageParameters;
    }

    public String getStringValues() {
        return "is Portrait: " + mIsPortrait + "," +
                "\ncover height: " + mCoverHeight + " width: " + mCoverWidth
                + "\npreview height: " + mPreviewHeight + " width: " + mPreviewWidth;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (mIsPortrait ? 1 : 0));

        dest.writeInt(mDisplayOrientation);
        dest.writeInt(mLayoutOrientation);

        dest.writeInt(mCoverHeight);
        dest.writeInt(mCoverWidth);
        dest.writeInt(mPreviewHeight);
        dest.writeInt(mPreviewWidth);
    }

    public static final Creator<ImageParameters> CREATOR = new Parcelable.Creator<ImageParameters>() {
        @Override
        public ImageParameters createFromParcel(Parcel source) {
            return new ImageParameters(source);
        }

        @Override
        public ImageParameters[] newArray(int size) {
            return new ImageParameters[size];
        }
    };
}
