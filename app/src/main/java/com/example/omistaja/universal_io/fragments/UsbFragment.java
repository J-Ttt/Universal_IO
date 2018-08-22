package com.example.omistaja.universal_io.fragments;


import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.omistaja.universal_io.R;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class UsbFragment extends Fragment {

    private static final String TAG = "UsbFragment";
    Context _context;
    PendingIntent mPermissionIntent;
    UsbDevice device;
    TextView usblist;
    Button checkusb;



    public UsbFragment() {}

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        _context = context;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_usb, container, false);

        usblist = rootView.findViewById(R.id.usblist);
        checkusb = rootView.findViewById(R.id.checkusb);

        checkusb.setOnClickListener(view -> {
            usblist.setText("");
            checkInfo();
        });


        return rootView;
    }

    private void checkInfo() {
        UsbManager usbManager = (UsbManager)_context.getSystemService(Context.USB_SERVICE);
        mPermissionIntent = PendingIntent.getBroadcast(_context, 0, new Intent(
                ACTION_USB_PERMISSION), 0);
        IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
        Objects.requireNonNull(getActivity()).registerReceiver(mUsbReceiver, filter);
        assert usbManager != null;
        HashMap<String , UsbDevice> deviceList = usbManager.getDeviceList();
        Iterator<UsbDevice> deviceIterator = deviceList.values().iterator();
        StringBuilder i = new StringBuilder();
        while (deviceIterator.hasNext()) {
            device = deviceIterator.next();
            usbManager.requestPermission(device, mPermissionIntent);
            i.append("\n" + "DeviceID: ").append(device.getDeviceId())
                    .append("\n").append("DeviceName: ")
                    .append(device.getDeviceName())
                    .append("\n").append("DeviceClass: ")
                    .append(device.getDeviceClass())
                    .append(" - ").append("DeviceSubClass: ")
                    .append(device.getDeviceSubclass())
                    .append("\n").append("VendorID: ")
                    .append(device.getVendorId())
                    .append("\n").append("ProductID: ")
                    .append(device.getProductId())
                    .append("\n");
        }



        usblist.setText(i.toString());
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
