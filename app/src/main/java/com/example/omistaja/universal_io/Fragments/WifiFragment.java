package com.example.omistaja.universal_io.Fragments;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.omistaja.universal_io.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;


public class WifiFragment extends Fragment {

    private static final String TAG = "WifiFragment";
    private Button p2pscanbtn, wifiOnOff, apscanbtn;
    private ListView wifilist;
    private WifiManager mWifiManager;
    private WifiP2pManager mManager;
    private WifiP2pManager.Channel mChannel;
    private ArrayAdapter<String> arrayAdapter;
    private IntentFilter mIntentFilter, apIntentFilter;
    private List<WifiP2pDevice> peers = new ArrayList<>();
    String[] deviceNameArray;
    WifiP2pDevice[] deviceArray;
    int size = 0;
    List<ScanResult> results;
    String ITEM_KEY = "key";
    ArrayList<String> arraylist = new ArrayList<>();

    public WifiFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_wifi, container, false);

        p2pscanbtn = rootView.findViewById(R.id.p2pscanbtn);
        apscanbtn = rootView.findViewById(R.id.apscanbtn);
        wifiOnOff = rootView.findViewById(R.id.wifiOnOff);
        wifilist = rootView.findViewById(R.id.wifilist);

        initialWork();

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

        apscanbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scanapwifi();
            }
        });

        arrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, arraylist);
        wifilist.setAdapter(arrayAdapter);

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

    @Override
    public void onDestroy(){
        super.onDestroy();
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

    public void scanapwifi() {
        Log.d(TAG, "Succesfully pressed ScanAP");
        peers.clear();
        arraylist.clear();
        getContext().registerReceiver(wifiReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        mWifiManager.startScan();
        Log.d("WifScanner", "scanWifiNetworks");
        Toast.makeText(getContext(), "Scanning....", Toast.LENGTH_SHORT).show();

    }


    public void scanp2pwifi() {
        arraylist.clear();
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

        mWifiManager = (WifiManager) getContext().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        mManager = (WifiP2pManager) getContext().getSystemService(Context.WIFI_P2P_SERVICE);
        mChannel = mManager.initialize(getContext(), Looper.getMainLooper(), null);

        apIntentFilter = new IntentFilter();
        apIntentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);

        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_DISCOVERY_CHANGED_ACTION);

        if (!mWifiManager.isWifiEnabled()) {
            mWifiManager.setWifiEnabled(true);
        } else {
            mWifiManager.setWifiEnabled(false);
        }

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
                arrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, deviceNameArray);
                wifilist.setAdapter(arrayAdapter);
                Log.d(TAG, "Succesfully added");
                Log.d(TAG, Integer.toString(peerList.getDeviceList().toArray().length));
            }

            if (peers.size() == 0) {
                Toast.makeText(getActivity(), "No Devices", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "No devices available");
            }

        }
        };

    private BroadcastReceiver wifiReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            Log.d("WifScanner", "onReceive");
            results = mWifiManager.getScanResults();
            size = results.size();
            getContext().unregisterReceiver(this);
            try
            {
                while (size >= 0)
                {
                    size--;
                    arraylist.add(results.get(size).SSID);
                    arrayAdapter.notifyDataSetChanged();
                }
            }
            catch (Exception e)
            {
                Log.w("WifScanner", "Exception: "+e);

            }

            if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {
                final int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1);
                if (state == WifiP2pManager.WIFI_P2P_STATE_ENABLED) {
                    Toast.makeText(getActivity(), "WiFi is enabled", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "WiFi is disabled", Toast.LENGTH_SHORT).show();
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
