package com.desmond.squarecamera;

import android.support.annotation.NonNull;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

/**
 * Created by desmond on 4/8/15.
 */
public class ResizeHeightAnimation extends Animation {
    final int mStartHeight;
    final int mFinalHeight;
    final View mView;

    public ResizeHeightAnimation(@NonNull View view, final int targetHeight) {
        mStartHeight = view.getContext().getResources().getDimensionPixelSize(R.dimen.cover_start_height);
        mFinalHeight = targetHeight;
        mView = view;
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        int newHeight = (int) (mStartHeight + (mFinalHeight - mStartHeight) * interpolatedTime);
        mView.getLayoutParams().height = newHeight;
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
