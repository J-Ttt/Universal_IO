package com.example.omistaja.universal_io.Fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.omistaja.universal_io.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SensorsFragment extends Fragment {


    public SensorsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_sensors, container, false);

        return rootView;
    }

}
