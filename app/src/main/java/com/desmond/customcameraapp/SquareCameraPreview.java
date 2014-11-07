package com.desmond.customcameraapp;

import android.content.Context;
import android.util.AttributeSet;
import android.view.SurfaceView;

/**
 *
 */
public class SquareCameraPreview extends SurfaceView {

    public static final String TAG = SquareCameraPreview.class.getSimpleName();

    private static final double ASPECT_RATIO = 3.0 / 4.0;

    public SquareCameraPreview(Context context) {
        super(context);
    }

    public SquareCameraPreview(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SquareCameraPreview(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * Measure the view and its content to determine the measured width and the
     * measured height
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);

        if (width > height * ASPECT_RATIO) {
            width = (int) (height * ASPECT_RATIO + 0.5);
        }
        else {
            height = (int) (width / ASPECT_RATIO + 0.5);
        }

        setMeasuredDimension(width, height);
    }

    public int getViewWidth() {
        return getWidth();
    }

    public int getViewHeight() {
        return getHeight();
    }
}
