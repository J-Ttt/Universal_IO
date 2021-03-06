package com.example.omistaja.universal_io.fragments;



import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.omistaja.universal_io.R;
import com.example.omistaja.universal_io.RuntimePermissions;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.IOException;
import java.util.Objects;


public class MicrophoneFragment extends Fragment {

    private Context _context;
    private Button playbutton, stopbutton, recordbutton;
    private MediaRecorder myMediaRecorder;
    private String outputFile;


    public MicrophoneFragment() {

    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = item -> {
        final Fragment micliveFrag = new MicrophoneLiveFragment();

        FragmentTransaction ft = Objects.requireNonNull(getFragmentManager()).beginTransaction();


        switch (item.getItemId()) {
            case R.id.nav_ap:
                return true;
            case R.id.nav_p2p:
                ft.replace(R.id.content_frame, micliveFrag).commit();
                return true;
        }


        return true;
    };

    public void onAttach(Context context) {
        super.onAttach(context);
        _context = context;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_microphone, container, false);

        playbutton = rootView.findViewById(R.id.playbutton);
        stopbutton = rootView.findViewById(R.id.stopbutton);
        recordbutton = rootView.findViewById(R.id.recordbutton);

        playbutton.setEnabled(false);
        stopbutton.setEnabled(false);

        
        outputFile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/recording.3gp";
        myMediaRecorder = new MediaRecorder();
        myMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        myMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        myMediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        myMediaRecorder.setOutputFile(outputFile);

        recordbutton.setOnClickListener(view -> {
            try {
                myMediaRecorder.prepare();
                myMediaRecorder.start();
            } catch (IOException e) {
                // Do something
            }
            recordbutton.setEnabled(false);
            stopbutton.setEnabled(true);

            Toast.makeText(getActivity(), "Recording started", Toast.LENGTH_SHORT).show();
        });


        stopbutton.setOnClickListener(view -> {
            myMediaRecorder.stop();
            myMediaRecorder.release();
            myMediaRecorder = null;
            recordbutton.setEnabled(true);
            stopbutton.setEnabled(false);
            playbutton.setEnabled(true);
            Toast.makeText(getActivity(), "Recording ended", Toast.LENGTH_SHORT).show();
        });

        playbutton.setOnClickListener(view -> {
            MediaPlayer mediaPlayer = new MediaPlayer();

            try {
                mediaPlayer.setDataSource(outputFile);
                mediaPlayer.prepare();
                mediaPlayer.start();

                Toast.makeText(getActivity(), "Playing audio", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                // Do something
            }

        });


        return rootView;
    }

}




