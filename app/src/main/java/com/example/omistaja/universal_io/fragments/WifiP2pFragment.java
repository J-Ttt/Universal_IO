package com.example.omistaja.universal_io.fragments;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

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
import android.widget.Toast;

import com.example.omistaja.universal_io.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class WifiP2pFragment extends Fragment {

    private Context _context;
    private static final String TAG = "WifiP2pFragment";
    private Button p2pscanbtn, wifiOnOff, sendbtn2;
    private ListView wifilist;
    private EditText writemsg2;
    private TextView msgview2, status2;
    private WifiManager mWifiManager;
    private WifiP2pManager mManager;
    private WifiP2pManager.Channel mChannel;
    private ArrayAdapter<String> arrayAdapter;
    private IntentFilter mIntentFilter;
    private List<WifiP2pDevice> peers = new ArrayList<>();
    String[] deviceNameArray;
    WifiP2pDevice[] deviceArray;

    static final int MESSAGE_READ = 1;

    ServerClass serverClass;
    ClientClass clientClass;
    SendReceive sendReceive;

    public WifiP2pFragment() {
        //Default empty constructor
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        _context = context;
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = item -> {
        final Fragment apFrag = new WifiApFragment();

        FragmentTransaction ft = Objects.requireNonNull(getFragmentManager()).beginTransaction();

        switch (item.getItemId()) {
            case R.id.nav_ap:
                ft.replace(R.id.content_frame, apFrag).commit();
                return true;
            case R.id.nav_p2p:
                return true;
        }

        return true;
    };

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_wifi, container, false);

        BottomNavigationView p2pap = rootView.findViewById(R.id.bottomNavigationView2);
        p2pap.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        p2pap.setSelectedItemId(R.id.nav_p2p);

        p2pscanbtn = rootView.findViewById(R.id.p2pscanbtn);
        wifiOnOff = rootView.findViewById(R.id.wifiOnOff);
        wifilist = rootView.findViewById(R.id.wifilist);
        msgview2 = rootView.findViewById(R.id.msgview2);
        writemsg2 = rootView.findViewById(R.id.writemsg2);
        sendbtn2 = rootView.findViewById(R.id.sendbtn2);
        status2 = rootView.findViewById(R.id.status2);

        initialWork();
        initButtons();


        return rootView;
    }

    private void initButtons() {
        p2pscanbtn.setOnClickListener(view -> {
            scanp2pwifi();
            Log.d(TAG, "Starting to discover networks");
        });


        wifiOnOff.setOnClickListener(view -> WifiP2pFragment.this.enableWifi());

        wifilist.setOnItemClickListener((adapterView, view, i, l) -> {
            {
                final WifiP2pDevice device = deviceArray[i];
                WifiP2pConfig config = new WifiP2pConfig();
                config.deviceAddress = device.deviceAddress;

                mManager.connect(mChannel, config, new WifiP2pManager.ActionListener() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(_context, "Connected to " + device.deviceName, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(int i) {
                        Toast.makeText(_context, "Not connected", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        sendbtn2.setOnClickListener(view -> {
            String msg = writemsg2.getText().toString();
            sendReceive.write(msg.getBytes());
        });
    }

    //Should set other participant Host and other Client //Doesn't work right now

    WifiP2pManager.ConnectionInfoListener connectionInfoListener = new WifiP2pManager.ConnectionInfoListener() {
        @Override
        public void onConnectionInfoAvailable(WifiP2pInfo wifiP2pInfo) {
            final InetAddress groupOwnerAddress = wifiP2pInfo.groupOwnerAddress;

            if (wifiP2pInfo.groupFormed && wifiP2pInfo.isGroupOwner) {
                status2.setText("Host");
                serverClass = new ServerClass();
                serverClass.start();
            } else if (wifiP2pInfo.groupFormed) {
                status2.setText("Client");
                clientClass = new ClientClass(groupOwnerAddress);
                clientClass.start();
            }
        }
    };

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
                status2.setText("Discovery Success");
            }

            @Override
            public void onFailure(int i) {
                status2.setText("Discovery Failed");
            }
        });
    }

    private void initialWork() {

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
        _context.registerReceiver(wifiReceiver, mIntentFilter);
    }

    WifiP2pManager.PeerListListener peerListListener = new WifiP2pManager.PeerListListener() {
        @Override
        public void onPeersAvailable(WifiP2pDeviceList peerList) {

            if (!peerList.getDeviceList().equals(peers)) {
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
                    Toast.makeText(getContext(), "WiFi is enabled", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "WiFi is disabled", Toast.LENGTH_SHORT).show();
                }

            } else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {
                Log.d(TAG, "Do I get peers?");
                if (mManager != null) {
                    mManager.requestPeers(mChannel, peerListListener);
                    Log.d(TAG, "Yes I do");
                } else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)) {

                    if (mManager == null) {
                        return;
                    }

                    NetworkInfo networkInfo = intent.getParcelableExtra(WifiP2pManager.EXTRA_NETWORK_INFO);

                    if (networkInfo.isConnected()) {
                        mManager.requestConnectionInfo(mChannel, connectionInfoListener);
                    } else {
                        status2.setText("Disconnected");
                    }

                } else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)) {

                } else if (WifiP2pManager.WIFI_P2P_DISCOVERY_CHANGED_ACTION.equals(action)) {

                }
            }
        }
    };


    Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_READ:
                    byte[] readBuff = (byte[]) msg.obj;
                    String tempMsg = new String(readBuff, 0, msg.arg1);
                    msgview2.setText(tempMsg);
                    break;
            }

            return true;
        }
    });


    public class ServerClass extends Thread {
        Socket socket;
        ServerSocket serverSocket;

        @Override
        public void run() {
            try {
                serverSocket = new ServerSocket(8888);
                socket = serverSocket.accept();

                sendReceive = new SendReceive(socket);
                sendReceive.start();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private class SendReceive extends Thread {
        private Socket socket;
        private InputStream inputStream;
        private OutputStream outputStream;

        public SendReceive(Socket soc) {
            socket = soc;
            InputStream tempIn = null;
            OutputStream tempOut = null;

            try {
                tempIn = socket.getInputStream();
                tempOut = socket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }

            inputStream = tempIn;
            outputStream = tempOut;
        }

        @Override
        public void run() {
            byte[] buffer = new byte[1024];
            int bytes;

            while (socket != null) {
                try {
                    bytes = inputStream.read(buffer);
                    if (bytes > 0) {
                        mHandler.obtainMessage(MESSAGE_READ, bytes, -1, buffer).sendToTarget();
                    }
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

    public class ClientClass extends Thread {
        Socket socket;
        String hostAdd;

        public ClientClass(InetAddress hostAddress) {
            hostAdd = hostAddress.getHostAddress();
            socket = new Socket();
        }

        @Override
        public void run() {
            try {
                socket.connect(new InetSocketAddress(hostAdd, 8888), 500);
                sendReceive = new SendReceive(socket);
                sendReceive.start();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
