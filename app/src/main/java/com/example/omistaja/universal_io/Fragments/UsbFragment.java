package com.example.omistaja.universal_io.Fragments;


import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.omistaja.universal_io.R;

import java.lang.reflect.Array;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class UsbFragment extends Fragment {

    private static final String TAG = "UsbFragment";
    Context _context;
    PendingIntent mPermissionIntent;
    ListView usblist;
    ArrayAdapter<String> arrayAdapter;


    public UsbFragment() {}

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        _context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_usb, container, false);

        rootView.findViewById(R.id.usblist);

        UsbManager usbManager = (UsbManager)_context.getSystemService(Context.USB_SERVICE);
        HashMap<String, UsbDevice> deviceList = usbManager.getDeviceList();
        UsbDevice device = deviceList.get("deviceName");
        mPermissionIntent = PendingIntent.getBroadcast(_context, 0, new Intent(ACTION_USB_PERMISSION), 0);
        IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
        _context.registerReceiver(mUsbReceiver, filter);
        usbManager.requestPermission(device, mPermissionIntent);

        return rootView;
    }

    private static final String ACTION_USB_PERMISSION = "com.example.omistaja.universal_io.Fragments";
    private BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (ACTION_USB_PERMISSION.equals(action)) {
                synchronized (this) {
                    UsbDevice device = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);

                    if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                        if (device != null) {

                        }
                    } else {
                        Log.d(TAG, "Permission denied");
                    }
                }
            }
        }
    };

}
