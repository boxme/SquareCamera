package com.desmond.customcameraapp;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

    public static Fragment newInstance(byte[] bitmapByteArray) {
        Fragment fragment = new EditSavePhotoFragment();
        Bundle args = new Bundle();
        args.putByteArray(BITMAP_KEY, bitmapByteArray);
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

        ImageView photoImageView = (ImageView) view.findViewById(R.id.photo);
        byte[] data = getArguments().getByteArray(BITMAP_KEY);

        BitmapFactory.Options options = new BitmapFactory.Options();
        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length, options);
        photoImageView.setImageBitmap(bitmap);
    }
}
