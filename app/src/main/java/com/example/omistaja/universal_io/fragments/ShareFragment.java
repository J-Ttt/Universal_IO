package com.example.omistaja.universal_io.fragments;


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
public class ShareFragment extends Fragment {

    Context _context;

    public ShareFragment() {}

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        _context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_share, container, false);



        return rootView;
    }

}
