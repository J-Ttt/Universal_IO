package com.example.omistaja.universal_io.Fragments;



import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.omistaja.universal_io.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;


public class WifiP2pFragment extends Fragment {

    private Context _context;
    private static final String TAG = "WifiP2pFragment";
    private Button p2pscanbtn, wifiOnOff;
    private ListView wifilist;
    private WifiManager mWifiManager;
    private WifiP2pManager mManager;
    private WifiP2pManager.Channel mChannel;
    private ArrayAdapter<String> arrayAdapter;
    private IntentFilter mIntentFilter;
    private List<WifiP2pDevice> peers = new ArrayList<>();
    String[] deviceNameArray;
    WifiP2pDevice[] deviceArray;

    public WifiP2pFragment() {

    }

    public void onAttach(Context context) {
        super.onAttach(context);
        _context = context;
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment = null;

            int id = item.getItemId();

            switch (id) {
                case R.id.nav_p2p:
                    fragment = new WifiP2pFragment();
                    break;
                case R.id.nav_ap:
                    fragment = new WifiApFragment();
                    break;
            }

            if (fragment != null) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = fragmentManager.beginTransaction();
                ft.replace(R.id.content_frame, fragment);
                ft.commit();
            }

            return true;
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_wifi, container, false);

        BottomNavigationView p2pap = rootView.findViewById(R.id.bottomNavigationView2);
        p2pap.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        p2pscanbtn = rootView.findViewById(R.id.p2pscanbtn);
        wifiOnOff = rootView.findViewById(R.id.wifiOnOff);
        wifilist = rootView.findViewById(R.id.wifilist);

        initialWork();
        _context.registerReceiver(wifiReceiver, mIntentFilter);

        p2pscanbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scanp2pwifi();
                Log.d(TAG, "Starting to discover networks");
            }
        });


        wifiOnOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enableWifi();
            }
        });

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        _context.registerReceiver(wifiReceiver, mIntentFilter);
    }

    @Override
    public void onPause() {
        super.onPause();
        _context.unregisterReceiver(wifiReceiver);
    }

    private void enableWifi() {
        if (mWifiManager == null) {
            Log.d(TAG, "No WiFi here");
        }
        if (mWifiManager.isWifiEnabled()) {
            mWifiManager.setWifiEnabled(false);
            wifiOnOff.setText("Enable WiFi");
            Log.d(TAG, "Disabling WiFi");
        } else {
            mWifiManager.setWifiEnabled(true);
            wifiOnOff.setText("Disable WiFi");
            Log.d(TAG, "Enabling WiFi");
        }
    }

    private void scanp2pwifi() {
        peers.clear();
        mManager.discoverPeers(mChannel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {

                Log.d(TAG, "Success discovery");
            }

            @Override
            public void onFailure(int i) {
                Log.d(TAG, "Failed discovery");
            }
        });
    }

    private void initialWork(){

        mWifiManager = (WifiManager) _context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        mManager = (WifiP2pManager) _context.getSystemService(Context.WIFI_P2P_SERVICE);
        mChannel = mManager.initialize(getContext(), Looper.getMainLooper(), null);

        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_DISCOVERY_CHANGED_ACTION);


        if (mWifiManager.isWifiEnabled()) {
            mWifiManager.setWifiEnabled(false);
            wifiOnOff.setText("Enable WiFi");
        } else {
            mWifiManager.setWifiEnabled(true);
            wifiOnOff.setText("Disable WiFi");
        }
    }





        WifiP2pManager.PeerListListener peerListListener = new WifiP2pManager.PeerListListener() {
        @Override
        public void onPeersAvailable(WifiP2pDeviceList peerList) {

            if(!peerList.getDeviceList().equals(peers)){
                Log.d(TAG, Integer.toString(peerList.getDeviceList().toArray().length));
                peers.addAll(peerList.getDeviceList());
                Log.d(TAG, "Adding peers 1...");
                Log.d(TAG, Integer.toString(peerList.getDeviceList().toArray().length));
                deviceNameArray = new String[peerList.getDeviceList().size()];
                deviceArray = new WifiP2pDevice[peerList.getDeviceList().size()];
                int index = 0;

                for (WifiP2pDevice device : peerList.getDeviceList()) {
                    Log.d(TAG, "Adding peers 2...");
                    deviceNameArray[index] = device.deviceName;
                    deviceArray[index] = device;
                    index++;
                }
                Log.d(TAG, "Adding peers 3...");
                arrayAdapter = new ArrayAdapter<>(_context, android.R.layout.simple_list_item_1, deviceNameArray);
                wifilist.setAdapter(arrayAdapter);
                Log.d(TAG, "Succesfully added");
                Log.d(TAG, Integer.toString(peerList.getDeviceList().toArray().length));
            }

            if (peers.size() == 0) {
                Toast.makeText(getContext(), "No Devices", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "No devices available");
            }
        }
        };

    private BroadcastReceiver wifiReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {
                final int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1);
                if (state == WifiP2pManager.WIFI_P2P_STATE_ENABLED) {
                    Toast.makeText(getContext(), "WiFi is disabled", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "WiFi is enabled", Toast.LENGTH_SHORT).show();
                }

            } else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {
                Log.d(TAG,"Do I get peers?");
                if (mManager != null) {
                    mManager.requestPeers(mChannel, peerListListener);
                    Log.d(TAG, "Yes I do");
            } else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)) {

            } else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)) {

            } else if (WifiP2pManager.WIFI_P2P_DISCOVERY_CHANGED_ACTION.equals(action)) {

                }
            }
        }
    };
}
