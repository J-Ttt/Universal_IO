package com.example.omistaja.universal_io.fragments;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import androidx.annotation.NonNull;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.example.omistaja.universal_io.R;
import java.io.ByteArrayOutputStream;
import java.util.Objects;


public class PhotoFragment extends Fragment {

    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1888;
    ImageView imageView;
    Context c;
    Button imagebutton;
    private Context _context;
    BottomNavigationView bottomNavigationView;


    public PhotoFragment() {

    }

    public void onAttach(Context context) {
        super.onAttach(context);
        _context = context;
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_camera, container, false);

        BottomNavigationView bottomNavigationView = rootView.findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(menuItem -> {
            final Fragment videoFrag = new VideoFragment();

            switch (menuItem.getItemId()) {
                case R.id.nav_photobut:
                    return true;
                case R.id.nav_videobut:
                    FragmentTransaction ft = Objects.requireNonNull(getFragmentManager()).beginTransaction();
                    ft.replace(R.id.content_frame, videoFrag).commit();
                    return true;
            }
            return false;
        });

        bottomNavigationView.setSelectedItemId(R.id.nav_photobut);

        imagebutton = rootView.findViewById(R.id.photobutton);

        imageView = rootView.findViewById(R.id.imageview);

        //Click listener and starts image capture activity for result (the image)
        imagebutton.setOnClickListener(v -> {
            Intent photoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            startActivityForResult(photoIntent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);

        });

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {

                Bitmap bmp = (Bitmap) Objects.requireNonNull(data.getExtras()).get("data");
                ByteArrayOutputStream stream = new ByteArrayOutputStream();

                assert bmp != null;
                bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();

                Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);

                //Sets picture taken in imageview
                imageView.setImageBitmap(bitmap);

            }
        }
    }
}




/* Image save method?
class SavePhotoTask extends AsyncTask<byte[], String, String> {
    @Override
    protected String doInBackground(byte[]... jpeg) {
        File photo = new File(Environment.getExternalStorageDirectory(), "photo.jpg");
        if (photo.exists()) {
            photo.delete();
        }

        try {
            FileOutputStream fos = new FileOutputStream(photo.getPath());
            fos.write(jpeg[0]);
            fos.close();
        }
        catch (java.io.IOException e) {
            Log.e("PictureDemo", "Exception in photoCallback", e);
        }
        return null;
    }
}
*/