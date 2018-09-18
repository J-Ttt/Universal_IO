package com.example.omistaja.universal_io.fragments;


import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.omistaja.universal_io.R;

import java.io.IOException;

/**
 * A simple {@link Fragment} subclass.
 */
public class MicrophoneLiveFragment extends Fragment {

    private MediaRecorder mediaRecorder;
    Button livebutton, stopbutton;
    String outputFile;



    public MicrophoneLiveFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_microphone_live, container, false);

        livebutton = rootView.findViewById(R.id.livebutton);
        stopbutton = rootView.findViewById(R.id.stopbutton);

        outputFile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/recording.3gp";

        MediaRecorderReady();

        livebutton.setEnabled(true);
        stopbutton.setEnabled(false);

        livebutton.setOnClickListener(view -> {
            try {
                mediaRecorder.prepare();
                mediaRecorder.start();

            } catch (IOException e) {
                // Do something
            }
            livebutton.setEnabled(false);
            stopbutton.setEnabled(true);

            MediaPlayer mediaPlayer = new MediaPlayer();

            try {
                mediaPlayer.setDataSource(outputFile);
                mediaPlayer.prepare();
                mediaPlayer.start();

                Toast.makeText(getActivity(), "Playing audio", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                // Do something
            }

            Toast.makeText(getActivity(), "Live started", Toast.LENGTH_SHORT).show();
        });


        stopbutton.setOnClickListener(view -> {
            mediaRecorder.stop();
            mediaRecorder.release();
            mediaRecorder = null;
            livebutton.setEnabled(true);
            stopbutton.setEnabled(false);


            Toast.makeText(getActivity(), "Live ended", Toast.LENGTH_SHORT).show();
        });

        return rootView;
    }

    public void MediaRecorderReady() {
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        mediaRecorder.setOutputFile(outputFile);
    }

}
