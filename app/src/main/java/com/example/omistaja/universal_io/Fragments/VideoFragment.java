package com.example.omistaja.universal_io.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.omistaja.universal_io.R;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;


public class VideoFragment extends Fragment {

    private static final int REQUEST_VIDEO_CAPTURE = 1;
    VideoView videoView;
    Button videobutton;
    Context c;

    public VideoFragment() {

    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment = null;

            int id = item.getItemId();

            if (id == R.id.nav_photobut) {
                fragment = new PhotoFragment();
            } else if (id == R.id.nav_videobut) {
                fragment = new VideoFragment();
            }

            if (fragment != null) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = fragmentManager.beginTransaction();
                ft.replace(R.id.content_frame, fragment);
                ft.commit();
            }

            return true;
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_camera2, container, false);

        BottomNavigationView photovid = rootView.findViewById(R.id.bottomNavigationView);
        photovid.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


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

        videobutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent videoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                startActivityForResult(videoIntent, REQUEST_VIDEO_CAPTURE);
            }
        });

        return rootView;
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
        if(getActivity().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)) {
            return true;
        } else {
            return false;
        }
    }

}
