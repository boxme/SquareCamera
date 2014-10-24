package com.desmond.customcameraapp;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 *
 */
public class EditSavePhotoFragment extends Fragment {

    public static final String TAG = EditSavePhotoFragment.class.getSimpleName();
    public static final String BITMAP_KEY = "bitmap_byte_array";
    public static final String ROTATION_KEY = "rotation";
    public static final String COVER_HEIGHT_KEY = "cover_height";
    public static final String IMAGE_HEIGHT_KEY = "image_heigh";

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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_save_photo, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        int rotation = getArguments().getInt(ROTATION_KEY);
        int coverHeight = getArguments().getInt(COVER_HEIGHT_KEY);
        int imageViewHeight = getArguments().getInt(IMAGE_HEIGHT_KEY);
        byte[] data = getArguments().getByteArray(BITMAP_KEY);

        final View topCoverView = getView().findViewById(R.id.cover_top_view);
        final View btnCoverView = getView().findViewById(R.id.cover_bottom_view);
        final ImageView photoImageView = (ImageView) view.findViewById(R.id.photo);

        topCoverView.getLayoutParams().height = coverHeight;
        btnCoverView.getLayoutParams().height = coverHeight;
        photoImageView.getLayoutParams().height = imageViewHeight;

        rotatePicture(rotation, data, photoImageView);

        Button savePictureBtn = (Button) view.findViewById(R.id.save_photo);
        savePictureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savePicture();
            }
        });
    }

    private void rotatePicture(int rotation, byte[] data, ImageView photoImageView) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length, options);

        Log.d(TAG, "original bitmap width " + bitmap.getWidth() + " height " + bitmap.getHeight());

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
        Bitmap oldBitmap = bitmap;

        int startX, cropHeight;
        if (bitmap.getHeight() > bitmap.getWidth()) {
            startX = (bitmap.getHeight() - bitmap.getWidth()) / 2;
            cropHeight = bitmap.getWidth();
        }
        else {
            startX = (bitmap.getWidth() - bitmap.getWidth()) / 2;
            cropHeight = bitmap.getHeight();
        }
        bitmap = Bitmap.createBitmap(
                oldBitmap, 0, startX, oldBitmap.getWidth(), cropHeight
        );

        File mediaStorageDir = new File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                getString(R.string.app_name)
        );

        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile = new File(
                mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg"
        );

        // Saving the bitmap
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);

            FileOutputStream stream = new FileOutputStream(mediaFile);
            stream.write(out.toByteArray());
            stream.close();

            Log.d(TAG, "saving the bitmap");
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        // Mediascanner need to scan for the image saved
        MediaScannerConnection.scanFile(getActivity(), new String[]{mediaFile.toString()}, new String[]{"image/jpeg"}, null);

        // Return the Uri
                ((CameraActivity) getActivity()).returnPhotoUri(Uri.fromFile(mediaFile));
    }
}
