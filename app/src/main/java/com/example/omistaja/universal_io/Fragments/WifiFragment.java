package com.example.omistaja.universal_io.Fragments;


import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.QuickContactBadge;

import com.example.omistaja.universal_io.R;


public class WifiFragment extends Fragment {

    Button scanbtn, enablebtn;
    ListView wifilist;
    WifiManager mWifiManager;

    public WifiFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_wifi, container, false);

        mWifiManager = (WifiManager) getActivity().getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        scanbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scanwifi();
            }
        });

        return rootView;
    }

    private void scanwifi() {

    }

}
