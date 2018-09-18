package com.example.omistaja.universal_io.fragments;


import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.omistaja.universal_io.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;


public class WifiApFragment extends Fragment {

    private static final String TAG = "WifiApFragment";
    Context _context;
    Button apscanbtn, wifiOnOff, apdisbtn;
    private ListView wifilist;
    private WifiManager mWifiManager;
    ArrayAdapter<ScanResult> arrayAdapter;
    ArrayList<String> arraylist = new ArrayList<>();
    private IntentFilter apIntentFilter;
    TextView status;
    EditText pass;

    ArrayList<ScanResult> mItems = new ArrayList<>();


    public WifiApFragment() {

    }

    public void onAttach(Context context) {
        super.onAttach(context);
        _context = context;
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = item -> {
        final Fragment p2pFrag = new WifiP2pFragment();

        FragmentTransaction ft = Objects.requireNonNull(getFragmentManager()).beginTransaction();

        switch (item.getItemId()) {
            case R.id.nav_ap:
                return true;
            case R.id.nav_p2p:
                ft.replace(R.id.content_frame, p2pFrag).commit();
                return true;
        }


        return true;
    };


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_wifi_ap, container, false);

        BottomNavigationView p2pap = rootView.findViewById(R.id.bottomNavigationView2);
        p2pap.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        p2pap.setSelectedItemId(R.id.nav_ap);

        apscanbtn = rootView.findViewById(R.id.apscanbtn);
        wifiOnOff = rootView.findViewById(R.id.wifiOnOff);
        wifilist = rootView.findViewById(R.id.wifilist);
        apdisbtn = rootView.findViewById(R.id.apdisbtn);
        status = rootView.findViewById(R.id.apstatus);

        initialWork();
        initButtons();

        return rootView;

    }

    @Override
    public void onResume() {
        super.onResume();
        _context.registerReceiver(wifiReceiver, apIntentFilter);
    }

    private void initButtons() {
        wifiOnOff.setOnClickListener(view -> enableWifi());

        apscanbtn.setOnClickListener(view -> scanapwifi());

        apdisbtn.setOnClickListener(view -> {
            int networkId = mWifiManager.getConnectionInfo().getNetworkId();
            String ssid = mWifiManager.getConnectionInfo().getSSID();
            mWifiManager.disconnect();
            mWifiManager.removeNetwork(networkId);
            mWifiManager.disableNetwork(networkId);
            Toast.makeText(_context, "Disconnected from " + ssid, Toast.LENGTH_SHORT).show();
        });

        wifilist.setOnItemClickListener((adapterView, view, i, l) -> {
            String ssid = ((TextView) view).getText().toString();
            connectToWifi(ssid);
            Toast.makeText(_context, "Wifi SSID: " + ssid, Toast.LENGTH_SHORT).show();
        });
    }

    private void enableWifi() {
        if (mWifiManager == null) {
            Log.d(TAG, "No WiFi here");
        }
        if (mWifiManager.isWifiEnabled()) {
            mWifiManager.setWifiEnabled(false);
            wifiOnOff.setText("Enable WiFi");
            Log.d(TAG, "Disabling WiFi");
            arraylist.clear();
            arrayAdapter.notifyDataSetChanged();
        } else {
            mWifiManager.setWifiEnabled(true);
            wifiOnOff.setText("Disable WiFi");
            Log.d(TAG, "Enabling WiFi");
        }
    }

    private void scanapwifi() {
        Log.d(TAG, "Succesfully pressed ScanAP");
        arraylist.clear();
        arrayAdapter.notifyDataSetChanged();
        mWifiManager.startScan();
        Log.d("WifScanner", "scanWifiNetworks");
        Toast.makeText(getContext(), "Scanning....", Toast.LENGTH_SHORT).show();
    }

    private void initialWork() {
        mWifiManager = (WifiManager) _context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        apIntentFilter = new IntentFilter();
        apIntentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);

        if (mWifiManager.isWifiEnabled()) {
            mWifiManager.setWifiEnabled(false);
            wifiOnOff.setText("Enable WiFi");
        } else {
            mWifiManager.setWifiEnabled(true);
            wifiOnOff.setText("Disable WiFi");
        }
        arrayAdapter = new ArrayAdapter<>(_context, android.R.layout.simple_list_item_1, mItems);
        wifilist.setAdapter(arrayAdapter);
    }

        private BroadcastReceiver wifiReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("WifScanner", "onReceive");
            wifilist.setAdapter(arrayAdapter);
            HashMap<String, Integer> signalStrength = new HashMap<>();
            WifiInfo wifiInfo = mWifiManager.getConnectionInfo();
            List<ScanResult> results = mWifiManager.getScanResults();
            _context.unregisterReceiver(this);
            try {
                if (results != null) {
                    int size = results.size();
                    for (int i=0; i<size; i++) {
                        ScanResult scanresult = results.get(i);
                        if (!scanresult.SSID.isEmpty()) {
                            String key = scanresult.SSID;
                            if (!signalStrength.containsKey(key)) {
                                signalStrength.put(key, i);
                                mItems.add(scanresult);
                                arrayAdapter.notifyDataSetChanged();
                            } else {
                                int position = signalStrength.get(key);
                                ScanResult updateItem = mItems.get(position);
                                if (WifiManager.calculateSignalLevel(wifiInfo.getRssi(), updateItem.level) >
                                        WifiManager.calculateSignalLevel(wifiInfo.getRssi(), scanresult.level)) {
                                    mItems.set(position, updateItem);
                                    arrayAdapter.notifyDataSetChanged();
                                }
                            }
                        }
                    }

                        /*
                        String ssid = scanresult.SSID;
                        int rssi = scanresult.level;
                        String rssiString = String.valueOf(rssi);
                        arraylist.add(String.format("%s ----- Signal: %s", ssid, rssiString));
                        arrayAdapter.notifyDataSetChanged();
                        */


                    _context.unregisterReceiver(wifiReceiver); //stops the continuous scan
                    status.setText("Scanning complete!");
                } else {
                    _context.unregisterReceiver(wifiReceiver);
                    status.setText("Nothing is found. Please make sure you are under any wifi coverage");
                }
            }
            catch(Exception e)
                {
                    Log.w("WifScanner", "Exception: " + e);

                }
            }
        };

        private void finallyConnect(String networkPass, String networkSSID) {
            WifiConfiguration wifiConfig = new WifiConfiguration();
            wifiConfig.SSID = String.format("\"%s\"", networkSSID);
            wifiConfig.preSharedKey = String.format("\"%s\"", networkPass);

            // remember id
            int netId = mWifiManager.addNetwork(wifiConfig);
            mWifiManager.disconnect();
            mWifiManager.enableNetwork(netId, true);
            mWifiManager.reconnect();

            WifiConfiguration conf = new WifiConfiguration();
            conf.SSID = "\"\"" + networkSSID + "\"\"";
            conf.preSharedKey = "\"" + networkPass + "\"";
            mWifiManager.addNetwork(conf);
        }

        private void connectToWifi(final String wifiSSID) {
            final Dialog dialog = new Dialog(_context);
            dialog.setContentView(R.layout.connect);
            dialog.setTitle("Connect to Network");
            final TextView textSSID = dialog.findViewById(R.id.textSSID1);

            Button dialogButton = dialog.findViewById(R.id.okButton);
            pass = dialog.findViewById(R.id.textPassword);
            textSSID.setText(wifiSSID);

            // if button is clicked, connect to the network;
            dialogButton.setOnClickListener(v -> {
                String checkPassword = pass.getText().toString();
                finallyConnect(checkPassword, wifiSSID);
                dialog.dismiss();
                Toast.makeText(_context, "Connected to " + wifiSSID, Toast.LENGTH_SHORT).show();
            });
            dialog.show();
        }
    }
