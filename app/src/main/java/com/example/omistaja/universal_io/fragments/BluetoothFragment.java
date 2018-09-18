package com.example.omistaja.universal_io.fragments;


import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.omistaja.universal_io.R;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;


public class BluetoothFragment extends Fragment {

    private Context _context;
    private static final String TAG = "BluetoothFragment";
    private Button scanbtn, btenable, sendmsg, listen;
    private ListView btListView;
    private EditText writemsg;
    private TextView msgview, status;
    //private ArrayList<String> BluetoothArrayList = new ArrayList<>();
    private ArrayAdapter<String> arrayAdapter;
    private BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    BluetoothDevice[] btArray;


    SendReceive sendReceive;

    static final int STATE_LISTENING = 1;
    static final int STATE_CONNECTING = 2;
    static final int STATE_CONNECTED = 3;
    static final int STATE_CONNECTION_FAILED = 4;
    static final int STATE_MESSAGE_RECEIVED = 5;

    private static final String FRAG_NAME = "BTFrag";
    private static final UUID MY_UUID = UUID.fromString("ef0f1856-5e89-4742-8f97-7b0b6cfa9bfd");

    public BluetoothFragment() {

    }

    public void onAttach(Context context) {
        super.onAttach(context);
        _context = context;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_bluetooth, container, false);
        scanbtn = rootView.findViewById(R.id.btscanbtn);
        btenable = rootView.findViewById(R.id.btenable);
        sendmsg = rootView.findViewById(R.id.sendbtn);
        writemsg = rootView.findViewById(R.id.writemsg);
        msgview = rootView.findViewById(R.id.msgview);
        btListView = rootView.findViewById(R.id.bluetoothlist);
        status = rootView.findViewById(R.id.status);
        listen = rootView.findViewById(R.id.listen);

        scanbtn.setOnClickListener(view -> scanBT());


        isBTenabled();

        btenable.setOnClickListener(view -> {
            Log.d(TAG, "Enabling/Disabling BT");
            enabledisableBT();
        });

        listen.setOnClickListener(view -> {
            ServerClass serverClass = new ServerClass();
            serverClass.start();
        });

        btListView.setOnItemClickListener((adapterView, view, i, l) -> {
            ClientClass clientClass = new ClientClass(btArray[i]);
            clientClass.start();

            status.setText("Connecting");
        });

        sendmsg.setOnClickListener(view -> {
            String string = String.valueOf(writemsg.getText());
            sendReceive.write(string.getBytes());
        });

        return rootView;
    }

    public void scanBT() {

        Set<BluetoothDevice> bt = mBluetoothAdapter.getBondedDevices();
        String[] strings = new String[bt.size()];
        btArray = new BluetoothDevice[bt.size()];
        int index = 0;

        if (bt.size() > 0) {
            for (BluetoothDevice device : bt) {
                btArray[index] = device;
                strings[index] = device.getName();
                index++;
            }
            arrayAdapter = new ArrayAdapter<>(_context, android.R.layout.simple_list_item_1, strings);
            btListView.setAdapter(arrayAdapter);
        }

        /*
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

        */
    }


    public void enabledisableBT() {
        if (mBluetoothAdapter == null) {
            Log.d(TAG, "Does not have BT");
        }
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBTintent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivity(enableBTintent);

            IntentFilter BTIntent = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
            _context.registerReceiver(myReceiver, BTIntent);
            btenable.setText("OFF");
        }

        if (mBluetoothAdapter.isEnabled()) {
            mBluetoothAdapter.disable();
            btenable.setText("ON");
            IntentFilter BTIntent = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
            Objects.requireNonNull(getActivity()).registerReceiver(myReceiver, BTIntent);
        }
    }


    public void isBTenabled() {
        if (mBluetoothAdapter.isEnabled()) {
            btenable.setText("OFF");
        } else {
            btenable.setText("ON");
        }
    }

    public void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        _context.registerReceiver(myReceiver, intentFilter);
    }


    public void onPause() {
        super.onPause();
        mBluetoothAdapter.cancelDiscovery();
        _context.unregisterReceiver(myReceiver);
    }

    private BroadcastReceiver myReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            /*
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                BluetoothArrayList.add(device.getName());
                arrayAdapter.notifyDataSetChanged();
            }
            */
            if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);
                switch (state) {
                    case BluetoothAdapter.STATE_OFF:
                        Log.d(TAG, "Receiver turned off");
                        break;
                    case BluetoothAdapter.STATE_TURNING_OFF:
                        Log.d(TAG, "Receiver turning off");
                        break;
                    case BluetoothAdapter.STATE_ON:
                        Log.d(TAG, "Receiver turned on");
                        break;
                    case BluetoothAdapter.STATE_TURNING_ON:
                        Log.d(TAG, "Receiver turning on");
                        break;


                }
            }
        }
    };

    Handler mHandler = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case STATE_LISTENING:
                    status.setText("Listening..");
                    break;
                case STATE_CONNECTING:
                    status.setText("Connecting..");
                    break;
                case STATE_CONNECTED:
                    status.setText("Connected");
                    break;
                case STATE_CONNECTION_FAILED:
                    status.setText("Connection failed");
                    break;
                case STATE_MESSAGE_RECEIVED:
                    byte[] readBuff = (byte[]) msg.obj;
                    String tempMsg = new String(readBuff, 0, msg.arg1);
                    msgview.setText(tempMsg);
                    break;
            }
            return true;
        }
    });


private class ServerClass extends Thread {

    private BluetoothServerSocket serverSocket;




    public ServerClass() {
        try {
            serverSocket = mBluetoothAdapter.listenUsingRfcommWithServiceRecord(FRAG_NAME, MY_UUID);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {

        BluetoothSocket socket = null;

        while (socket == null) {
            try {
                Message message = Message.obtain();
                message.what = STATE_CONNECTING;
                mHandler.sendMessage(message);
                socket = serverSocket.accept();
            } catch (IOException e) {
                e.printStackTrace();
                Message message = Message.obtain();
                message.what = STATE_CONNECTION_FAILED;
                mHandler.sendMessage(message);
            }

            if (socket != null) {
                Message message = Message.obtain();
                message.what = STATE_CONNECTED;
                mHandler.sendMessage(message);

                sendReceive = new SendReceive(socket);
                sendReceive.start();

                break;
            }
        }
    }
}

private class ClientClass extends Thread {

    private BluetoothDevice device;
    private BluetoothSocket socket;


    public ClientClass(BluetoothDevice device1) {

        device = device1;

        try {
            socket = device.createRfcommSocketToServiceRecord(MY_UUID);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        try {
            socket.connect();
            Message message = Message.obtain();
            message.what = STATE_CONNECTED;
            mHandler.sendMessage(message);

            sendReceive = new SendReceive(socket);
            sendReceive.start();

        } catch (IOException e) {
            e.printStackTrace();
            Message message = Message.obtain();
            message.what = STATE_CONNECTION_FAILED;
            mHandler.sendMessage(message);
        }
    }
}

private class SendReceive extends Thread {

    private final BluetoothSocket bluetoothSocket;
    private final InputStream inputStream;
    private final OutputStream outputStream;



    public SendReceive(BluetoothSocket socket) {
        bluetoothSocket = socket;
        InputStream tempIn = null;
        OutputStream tempOut = null;

        try {
            tempIn = bluetoothSocket.getInputStream();
            tempOut = bluetoothSocket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }

        inputStream = tempIn;
        outputStream = tempOut;
    }

    public void run() {
        byte[] buffer = new byte[1024];
        int bytes;

        while (true) {
            try {
                bytes = inputStream.read(buffer);
                mHandler.obtainMessage(STATE_MESSAGE_RECEIVED, bytes, -1, buffer).sendToTarget();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void write(byte[] bytes) {
        try {
            outputStream.write(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
}
