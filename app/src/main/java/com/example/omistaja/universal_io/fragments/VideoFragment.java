package com.example.omistaja.universal_io.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
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
import android.widget.VideoView;

import com.example.omistaja.universal_io.R;

import java.util.Objects;

import static android.app.Activity.RESULT_OK;


public class VideoFragment extends Fragment {

    private static final int REQUEST_VIDEO_CAPTURE = 1;
    VideoView videoView;
    Button videobutton;
    Context _context;
    BottomNavigationView bottomNavigationView;

    public VideoFragment() {

    }

    public void onAttach(Context context) {
        super.onAttach(context);
        _context = context;
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_camera2, container, false);

        BottomNavigationView bottomNavigationView = rootView.findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(menuItem -> {
            final Fragment photoFrag = new PhotoFragment();

            switch (menuItem.getItemId()) {
                case R.id.nav_photobut:
                    FragmentTransaction ft = Objects.requireNonNull(getFragmentManager()).beginTransaction();
                    ft.replace(R.id.content_frame, photoFrag).commit();
                    return true;
                case R.id.nav_videobut:
                    return true;
            }

            return false;
        });

        bottomNavigationView.setSelectedItemId(R.id.nav_videobut);

        videobutton = rootView.findViewById(R.id.videobutton);

        videoView = rootView.findViewById(R.id.videoview);

        if(!hasCamera()){
            videobutton.setEnabled(false);
        }

        /*
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                mediaPlayer.reset();
                videoView.start();
            }
        });
        */

        videobutton.setOnClickListener(v -> {
            Intent videoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
            startActivityForResult(videoIntent, REQUEST_VIDEO_CAPTURE);
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
        if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) {
            Uri videoUri = data.getData();
            videoView.setVideoURI(videoUri);
            videoView.start();
        }
    }


    private boolean hasCamera(){
        return _context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY);
    }

}
