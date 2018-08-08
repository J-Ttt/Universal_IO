package com.example.omistaja.universal_io.Fragments;



import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.omistaja.universal_io.R;

import java.io.IOException;


public class MicrophoneFragment extends Fragment {

    private Context _context;
    private Button playbutton, stopbutton, recordbutton;
    private MediaRecorder myMediaRecorder;
    private String outputFile;

    public MicrophoneFragment() {

    }

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

        recordbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    myMediaRecorder.prepare();
                    myMediaRecorder.start();
                } catch (IOException e) {
                    // Do something
                }
                recordbutton.setEnabled(false);
                stopbutton.setEnabled(true);

                Toast.makeText(getActivity(), "Recording started", Toast.LENGTH_SHORT).show();
            }
        });


        stopbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myMediaRecorder.stop();
                myMediaRecorder.release();
                myMediaRecorder = null;
                recordbutton.setEnabled(true);
                stopbutton.setEnabled(false);
                playbutton.setEnabled(true);
                Toast.makeText(getActivity(), "Recording ended", Toast.LENGTH_SHORT).show();
            }
        });

        playbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MediaPlayer mediaPlayer = new MediaPlayer();

                try {
                    mediaPlayer.setDataSource(outputFile);
                    mediaPlayer.prepare();
                    mediaPlayer.start();

                    Toast.makeText(getActivity(), "Playing audio", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    // Do something
                }

            }
        });


        return rootView;
    }

}




