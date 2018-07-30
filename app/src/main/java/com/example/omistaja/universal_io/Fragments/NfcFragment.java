package com.example.omistaja.universal_io.Fragments;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.IsoDep;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.MifareUltralight;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.nfc.tech.NfcA;
import android.nfc.tech.NfcB;
import android.nfc.tech.NfcF;
import android.nfc.tech.NfcV;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.omistaja.universal_io.MainActivity;
import com.example.omistaja.universal_io.R;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;


public class NfcFragment extends Fragment {

    public static final String MIME_TEXT_PLAIN = "text/plain";
    public static final String TAG = "NfcDemo";
    Context _context;
    NfcAdapter mAdapter;
    public PendingIntent mPendingIntent;
    public TextView nfcView;
    private IntentFilter[] mFilters;
    private String[][] mTechLists;
    public int mCount = 0;

    public NfcFragment() {}

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        _context = context;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_nfc, container, false);

        nfcView = rootView.findViewById(R.id.nfcView);

        return rootView;
    }




}
