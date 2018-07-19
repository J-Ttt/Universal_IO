package com.example.omistaja.universal_io.Fragments;


import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.omistaja.universal_io.R;

import java.util.ArrayList;


public class BluetoothFragment extends Fragment {

    private static final String TAG = "BluetoothFragment";
    private Button scanbtn, btenable;
    private ListView btListView;
    private ArrayList<String> BluetoothArrayList = new ArrayList<>();
    private ArrayAdapter<String> arrayAdapter;
    private BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

    public BluetoothFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_bluetooth, container, false);

        scanbtn = rootView.findViewById(R.id.btscanbtn);
        btenable = rootView.findViewById(R.id.btenable);
        btListView = rootView.findViewById(R.id.bluetoothlist);

        arrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, BluetoothArrayList);
        btListView.setAdapter(arrayAdapter);

        scanbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    scanBT();
                }

        });

        mBluetoothAdapter.enable();

        btenable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG,"Enabling/Disabling BT");
                enabledisableBT();
            }
        });

        return rootView;
    }

    public void scanBT() {
        if (mBluetoothAdapter.isEnabled()) {
            if (mBluetoothAdapter.isDiscovering()) {
                mBluetoothAdapter.cancelDiscovery();
                Log.d(TAG, "Canceling current discovery state");
            }
            if (arrayAdapter != null) {
                arrayAdapter.clear();
                Log.d(TAG, "Clearing ArrayAdapter");
            }
            mBluetoothAdapter.startDiscovery();
            Log.d(TAG, "Started bluetooth discovery");
        } else {
            Log.d(TAG, "You need to enable bluetotoh");
            Toast.makeText(getActivity(), "Enable Bluetooth", Toast.LENGTH_SHORT).show();
        }
    }


    public void enabledisableBT() {
        if (mBluetoothAdapter == null) {
            Log.d(TAG,"Does not have BT");
        }
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBTintent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivity(enableBTintent);

            IntentFilter BTIntent = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
            getActivity().registerReceiver(myReceiver, BTIntent);
        }

        if (mBluetoothAdapter.isEnabled()) {
            mBluetoothAdapter.disable();
            //IntentFilter BTIntent = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
            //getActivity().registerReceiver(myReceiver, BTIntent);
        }
    }

    public void onResume(){
        super.onResume();
        IntentFilter intentFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        getActivity().registerReceiver(myReceiver, intentFilter);
    }


    public void onPause(){
        super.onPause();
        mBluetoothAdapter.cancelDiscovery();
        getActivity().unregisterReceiver(myReceiver);
    }

    private BroadcastReceiver myReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                BluetoothArrayList.add(device.getName());
                arrayAdapter.notifyDataSetChanged();
            }
            if (mBluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, mBluetoothAdapter.ERROR);
                switch (state) {
                    case BluetoothAdapter.STATE_OFF:
                        Log.d(TAG,"Receiver turned off");
                        break;
                    case BluetoothAdapter.STATE_TURNING_OFF:
                        Log.d(TAG,"Receiver turning off");
                        break;
                    case BluetoothAdapter.STATE_ON:
                        Log.d(TAG,"Receiver turned on");
                        break;
                    case BluetoothAdapter.STATE_TURNING_ON:
                        Log.d(TAG,"Receiver turning on");
                        break;

                }
            }
        }
    };

}
