package com.example.omistaja.universal_io;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.p2p.WifiP2pManager;

public class BroadcastReceiverAdapter extends BroadcastReceiver {

    private WifiP2pManager wifiManager;
    private WifiP2pManager.Channel wifiChannel;


    public BroadcastReceiverAdapter(WifiP2pManager wifiManager, WifiP2pManager.Channel wifiChannel){
        this.wifiManager = wifiManager;
        this.wifiChannel = wifiChannel;
    }

    @Override
    public void onReceive(Context context, Intent intent) {

    }
}
