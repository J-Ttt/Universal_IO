package com.example.omistaja.universal_io.Fragments;


import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.omistaja.universal_io.R;

public class ConfirmationFragment extends Fragment {

    Context _context;
    Button noconfirm, yesconfirm;
    TextView confirmtext;

    public ConfirmationFragment() {}

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        _context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_confirmation, container, false);

        confirmtext = rootView.findViewById(R.id.confirmation);
        noconfirm = rootView.findViewById(R.id.noconfirm);
        yesconfirm = rootView.findViewById(R.id.yesconfirm);

        yesconfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        noconfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });



        return rootView;
    }

}
