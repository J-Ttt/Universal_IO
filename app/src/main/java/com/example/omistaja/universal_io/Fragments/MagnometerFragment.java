package com.example.omistaja.universal_io.Fragments;


import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.omistaja.universal_io.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MagnometerFragment extends Fragment {


    public MagnometerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_magnometer, container, false );

        return rootView;
    }

}
