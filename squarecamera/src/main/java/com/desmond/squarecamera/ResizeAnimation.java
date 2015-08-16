package com.desmond.squarecamera;

import android.support.annotation.NonNull;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

/**
 * Created by desmond on 4/8/15.
 */
public class ResizeAnimation extends Animation {
    final int mStartLength;
    final int mFinalLength;
    final boolean mIsPortrait;
    final View mView;

    public ResizeAnimation(@NonNull View view, final ImageParameters imageParameters) {
        mStartLength = view.getContext().getResources().getDimensionPixelSize(R.dimen.squarecamera__cover_start_width);
        mFinalLength = imageParameters.getAnimationParameter();
        mIsPortrait = imageParameters.isPortrait();
        mView = view;
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        int newLength = (int) (mStartLength + (mFinalLength - mStartLength) * interpolatedTime);

        if (mIsPortrait) {
            mView.getLayoutParams().height = newLength;
        } else {
            mView.getLayoutParams().width = newLength;
        }
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
