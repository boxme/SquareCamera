package com.desmond.squarecamera;


import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


/**
 *
 */
public class EditSavePhotoFragment extends Fragment {

    public static final String TAG = EditSavePhotoFragment.class.getSimpleName();
    public static final String BITMAP_KEY = "bitmap_byte_array";
    public static final String ROTATION_KEY = "rotation";
    public static final String COVER_HEIGHT_KEY = "cover_height";
    public static final String IMAGE_HEIGHT_KEY = "image_height";

    public static Fragment newInstance(byte[] bitmapByteArray, int rotation,
                                       int coverHeight, int imageViewHeight) {
        Fragment fragment = new EditSavePhotoFragment();

        Bundle args = new Bundle();
        args.putByteArray(BITMAP_KEY, bitmapByteArray);
        args.putInt(ROTATION_KEY, rotation);
        args.putInt(COVER_HEIGHT_KEY, coverHeight);
        args.putInt(IMAGE_HEIGHT_KEY, imageViewHeight);

        fragment.setArguments(args);
        return fragment;
    }

    public EditSavePhotoFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_edit_save_photo, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        int rotation = getArguments().getInt(ROTATION_KEY);
        int coverHeight = getArguments().getInt(COVER_HEIGHT_KEY);
        int imageViewHeight = getArguments().getInt(IMAGE_HEIGHT_KEY);
        byte[] data = getArguments().getByteArray(BITMAP_KEY);

        final View topCoverView = view.findViewById(R.id.cover_top_view);
        final View btnCoverView = view.findViewById(R.id.cover_bottom_view);
        final ImageView photoImageView = (ImageView) view.findViewById(R.id.photo);

        photoImageView.getLayoutParams().height = imageViewHeight;
        topCoverView.getLayoutParams().height = coverHeight;
        btnCoverView.getLayoutParams().height = coverHeight;

        rotatePicture(rotation, data, photoImageView);

        view.findViewById(R.id.save_photo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savePicture();
            }
        });
    }

    private void rotatePicture(int rotation, byte[] data, ImageView photoImageView) {
        Bitmap bitmap = ImageUtility.decodeSampledBitmapFromByte(getActivity(), data);
//        Log.d(TAG, "original bitmap width " + bitmap.getWidth() + " height " + bitmap.getHeight());
        if (rotation != 0) {
            Bitmap oldBitmap = bitmap;

            Matrix matrix = new Matrix();
            matrix.postRotate(rotation);

            bitmap = Bitmap.createBitmap(
                    oldBitmap, 0, 0, oldBitmap.getWidth(), oldBitmap.getHeight(), matrix, false
            );

            oldBitmap.recycle();
        }

        photoImageView.setImageBitmap(bitmap);
    }

    private void savePicture() {
        ImageView photoImageView = (ImageView) getView().findViewById(R.id.photo);

        Bitmap bitmap = ((BitmapDrawable) photoImageView.getDrawable()).getBitmap();
        Uri photoUri = ImageUtility.savePicture(getActivity(), bitmap);

        ((CameraActivity) getActivity()).returnPhotoUri(photoUri);
    }
}
