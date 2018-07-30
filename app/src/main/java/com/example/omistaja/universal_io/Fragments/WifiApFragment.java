package com.example.omistaja.universal_io.Fragments;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

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
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class WifiApFragment extends Fragment {

    private static final String TAG = "WifiApFragment";
    Context _context;
    Button apscanbtn, wifiOnOff;
    private ListView wifilist;
    private WifiManager mWifiManager;
    ArrayAdapter<String> arrayAdapter;
    ArrayList<String> arraylist = new ArrayList<>();
    private IntentFilter apIntentFilter;
    int size = 0;




    public WifiApFragment() {

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
      final View rootView = inflater.inflate(R.layout.fragment_wifi_ap, container, false);

      BottomNavigationView p2pap = rootView.findViewById(R.id.bottomNavigationView2);
      p2pap.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        apscanbtn = rootView.findViewById(R.id.apscanbtn);
        wifiOnOff = rootView.findViewById(R.id.wifiOnOff);
        wifilist = rootView.findViewById(R.id.wifilist);

        initialWork();

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

        arrayAdapter = new ArrayAdapter<>(_context, android.R.layout.simple_list_item_1, arraylist);
        wifilist.setAdapter(arrayAdapter);

      return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        _context.registerReceiver(wifiReceiver, apIntentFilter);
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

    private void scanapwifi() {
        Log.d(TAG, "Succesfully pressed ScanAP");
        arraylist.clear();
        _context.registerReceiver(wifiReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
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
    }

    private BroadcastReceiver wifiReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("WifScanner", "onReceive");
            List<ScanResult> results = mWifiManager.getScanResults();
            wifilist.setAdapter(arrayAdapter);
            size = results.size();
            HashMap<String, Integer> signalStrength = new HashMap<String, Integer>();
            _context.unregisterReceiver(this);
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
        }
    };
}
