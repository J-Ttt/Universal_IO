package com.example.omistaja.universal_io.Fragments;



import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.omistaja.universal_io.R;


public class MicrophoneFragment extends Fragment {


    public MicrophoneFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_microphone, container, false);
    }

}




