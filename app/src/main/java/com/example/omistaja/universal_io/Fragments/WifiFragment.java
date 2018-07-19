package com.example.omistaja.universal_io.Fragments;


import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.QuickContactBadge;
import android.widget.Toast;

import com.example.omistaja.universal_io.R;

import java.util.ArrayList;

import static androidx.constraintlayout.widget.Constraints.TAG;


public class WifiFragment extends Fragment {

    private static final String TAG = "WifiFragment";
    private Button wifiscanbtn, wifiOnOff;
    private ListView wifilist;
    private WifiManager mWifiManager;
    private WifiP2pManager mManager;
    private WifiP2pManager.Channel mChannel;
    private ArrayList<String> wifiArrayList = new ArrayList<>();
    private ArrayAdapter<String> arrayAdapter;
    private IntentFilter mIntentFilter;

    public WifiFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_wifi, container, false);

        wifiscanbtn = rootView.findViewById(R.id.wifiscanbtn);
        wifiOnOff = rootView.findViewById(R.id.wifiOnOff);
        wifilist = rootView.findViewById(R.id.wifilist);

        initialWork();

        wifiscanbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scanwifi();
            }
        });

        wifiOnOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enableWifi();
                Log.d(TAG,"Enabling/Disabling WiFi");
            }
        });



        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().registerReceiver(wifiReceiver, mIntentFilter);
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(wifiReceiver);
    }

    private void enableWifi() {
        if (mWifiManager == null) {
            Log.d(TAG, "No WiFi here");
        }
        if (mWifiManager.isWifiEnabled()) {
            mWifiManager.setWifiEnabled(false);
            wifiOnOff.setText("Enable WiFi");
            Log.d(TAG, "Enabling WiFi");
        } else {
            mWifiManager.setWifiEnabled(true);
            wifiOnOff.setText("Disable WiFi");
            Log.d(TAG, "Disabling WiFi");
        }
    }

    public void scanwifi() {

    }

    private void initialWork(){
        mWifiManager = (WifiManager) getActivity().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        mManager = (WifiP2pManager) getActivity().getSystemService(Context.WIFI_P2P_SERVICE);
        mChannel = mManager.initialize(getActivity(), Looper.getMainLooper(), null);

        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);

        //getActivity().registerReceiver(wifiReceiver, mIntentFilter);
        if (mWifiManager.isWifiEnabled()) {
            mWifiManager.setWifiEnabled(false);
            wifiOnOff.setText("Enable WiFi");
        } else {
            mWifiManager.setWifiEnabled(true);
            wifiOnOff.setText("Disable WiFi");
        }

        arrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, wifiArrayList);
        wifilist.setAdapter(arrayAdapter);
    }

    private BroadcastReceiver wifiReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {
                final int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1);
                if (state == WifiP2pManager.WIFI_P2P_STATE_ENABLED) {
                    Toast.makeText(getActivity(), "WiFi is enabled", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "WiFi is disabled", Toast.LENGTH_SHORT).show();
                }
            } else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {

            } else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)) {

            } else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)) {

            }
        }
    };

}
