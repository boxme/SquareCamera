package com.desmond.squarecamera;

import android.support.annotation.NonNull;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

/**
 * Created by desmond on 4/8/15.
 */
public class ResizeAnimation extends Animation {
//    public static final String TAG = ResizeAnimation.class.getSimpleName();

    final int mStartLength;
    final int mFinalLength;
    final boolean mIsPortrait;
    final View mView;

    public ResizeAnimation(@NonNull View view, final ImageParameters imageParameters) {
        mIsPortrait = imageParameters.isPortrait();
        mView = view;
        mStartLength = mIsPortrait ? mView.getHeight() : mView.getWidth();
        mFinalLength = imageParameters.getAnimationParameter();
//        Log.d(TAG, "Start: " + mStartLength + " final: " + mFinalLength);
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        int newLength = (int) (mStartLength + (mFinalLength - mStartLength) * interpolatedTime);

        if (mIsPortrait) {
            mView.getLayoutParams().height = newLength;
        } else {
            mView.getLayoutParams().width = newLength;
        }

        mView.requestLayout();
    }

    @Override
    public void initialize(int width, int height, int parentWidth, int parentHeight) {
        super.initialize(width, height, parentWidth, parentHeight);
    }

    @Override
    public boolean willChangeBounds() {
        return true;
    }
}
