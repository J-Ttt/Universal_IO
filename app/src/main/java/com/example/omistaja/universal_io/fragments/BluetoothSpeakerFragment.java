package com.example.omistaja.universal_io.fragments;


import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.omistaja.universal_io.MainActivity;
import com.example.omistaja.universal_io.R;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Set;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;


public class BluetoothSpeakerFragment extends Fragment {

    private final static String TAG = "BluetoothSpeaker";
    private Context _context;
    Button scanbtn2, btenable2;
    ListView speakerList;
    BluetoothAdapter mBluetoothAdapter;
    ArrayAdapter<String> arrayAdapter;
    ArrayList<String> pairedDevices;
    ArrayList<BluetoothDevice> devices;
    IntentFilter filter;
    Set<BluetoothDevice> devicesArray;


    public BluetoothSpeakerFragment() {}

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        _context = context;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_bluetooth_speaker, container, false);

        scanbtn2 = rootView.findViewById(R.id.btscanbtn2);
        btenable2 = rootView.findViewById(R.id.btenable2);
        speakerList = rootView.findViewById(R.id.speakerlist);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);

        isBTenabled();

        scanbtn2.setOnClickListener(view -> scanBT());

        btenable2.setOnClickListener(view -> enabledisableBT());

        speakerList.setOnItemClickListener((adapterView, view, i, l) -> {
            if (mBluetoothAdapter.isDiscovering()) {
                mBluetoothAdapter.cancelDiscovery();
            }
            if (!Objects.requireNonNull(arrayAdapter.getItem(i)).contains("Paired")) {
                BluetoothDevice selectedDevice = devices.get(i);
                pairDevice(selectedDevice);
                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.putExtra("MAC", selectedDevice.getAddress());
                BluetoothSpeakerFragment.this.startActivity(intent);
            }
            else {
                BluetoothDevice selectedDevice = devices.get(i);
                Intent intent=new Intent(getActivity(), MainActivity.class);
                intent.putExtra("MAC",selectedDevice.getAddress());
                BluetoothSpeakerFragment.this.startActivity(intent);
            }
        });

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        Objects.requireNonNull(getActivity()).registerReceiver(myReceiver, filter);
        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        getActivity().registerReceiver(myReceiver, filter);
        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        getActivity().registerReceiver(myReceiver, filter);
        filter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        getActivity().registerReceiver(myReceiver, filter);
    }

    @Override
    public void onPause() {
        super.onPause();
        Objects.requireNonNull(getActivity()).unregisterReceiver(myReceiver);
    }

    @SuppressLint("SetTextI18n")
    private void isBTenabled() {
        if (mBluetoothAdapter.isEnabled()) {
            btenable2.setText("OFF");
        } else {
            btenable2.setText("ON");
        }
    }

    private void scanBT() {
        mBluetoothAdapter.cancelDiscovery();
        Toast.makeText(_context,"New Scan Start",Toast.LENGTH_SHORT ).show();

        arrayAdapter = new ArrayAdapter<String>(_context, android.R.layout.simple_list_item_1,0);
        speakerList.setAdapter(arrayAdapter);

        devices = new ArrayList<BluetoothDevice>();
        mBluetoothAdapter.startDiscovery();
    }

    private void getPairedDevices() {
        devicesArray = mBluetoothAdapter.getBondedDevices();
        if(devicesArray.size()>0){
            for(BluetoothDevice device:devicesArray){
                pairedDevices.add(device.getName());

            }
        }
    }

    @SuppressLint("SetTextI18n")
    public void enabledisableBT() {
        if (mBluetoothAdapter == null) {
            Log.d(TAG, "Does not have BT");
        }
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBTintent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivity(enableBTintent);

            IntentFilter BTIntent = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
            _context.registerReceiver(myReceiver, BTIntent);
            btenable2.setText("OFF");
        }

        if (mBluetoothAdapter.isEnabled()) {
            mBluetoothAdapter.disable();
            btenable2.setText("ON");
            IntentFilter BTIntent = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
            Objects.requireNonNull(getActivity()).registerReceiver(myReceiver, BTIntent);
        }
    }
    
    

    private BroadcastReceiver myReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();

            Toast.makeText(_context,"new br: "+action,Toast.LENGTH_LONG ).show();

            if(BluetoothDevice.ACTION_FOUND.equals(action)){

                pairedDevices=new ArrayList<>();
                getPairedDevices();
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                Toast.makeText(_context,"Dev: "+device.getName(),Toast.LENGTH_LONG ).show();
                devices.add(device);
                String s = "";
                for(int a = 0; a < pairedDevices.size(); a++){
                    if(device.getName().equals(pairedDevices.get(a))){
                        //append
                        s = "(Paired)";
                        break;
                    }
                }

                arrayAdapter.add(device.getName()+" "+s+" "+"\n"+device.getAddress());

            } else if (BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)) {

            }
        }
    };

    private void pairDevice(BluetoothDevice device) {
        try {
            Method m = device.getClass().getMethod("createBond", (Class[]) null);
            m.invoke(device, (Object[]) null);
        } catch (Exception e) {
            Toast.makeText(_context,"Exception: "+e.getMessage(),Toast.LENGTH_LONG ).show();
        }
    }

}
