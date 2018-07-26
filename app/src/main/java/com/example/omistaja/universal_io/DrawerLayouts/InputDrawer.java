package com.example.omistaja.universal_io.DrawerLayouts;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.content.Context;
import androidx.annotation.NonNull;

import com.example.omistaja.universal_io.Fragments.AccelerometerFragment;
import com.example.omistaja.universal_io.Fragments.GestureFragment;
import com.example.omistaja.universal_io.Fragments.UsbFragment;
import com.example.omistaja.universal_io.Fragments.WifiP2pFragment;
import com.google.android.material.navigation.NavigationView;
import androidx.fragment.app.FragmentActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.pm.PackageManager;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.omistaja.universal_io.Fragments.BluetoothFragment;
import com.example.omistaja.universal_io.Fragments.PhotoFragment;
import com.example.omistaja.universal_io.Fragments.MicrophoneFragment;
import com.example.omistaja.universal_io.R;

public class InputDrawer implements NavigationView.OnNavigationItemSelectedListener {

    private final static String TAG = "InputDrawer";
    private Context _context;
    private DrawerLayout ldrawer;
    private NavigationView drawer;

    public InputDrawer(Context context, NavigationView drwer, DrawerLayout ldrwer) {

        this._context = context;
        this.drawer = drwer;
        this.ldrawer = ldrwer;

    }




    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        Fragment fragment = null;

        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            Log.d(TAG, "Starting to check for req");
            if (!hasCamera()){
                Toast.makeText(_context, "You don't have anykind of camera", Toast.LENGTH_SHORT).show();
            } else {
                fragment = new PhotoFragment();
                Log.d(TAG, "You have it");
            }
        } else if (id == R.id.nav_microphone) {
            Log.d(TAG, "Starting to check for req");
            if (!hasMicrophone()) {
                Toast.makeText(_context, "You don't have microphone", Toast.LENGTH_SHORT).show();
            } else {
                fragment = new MicrophoneFragment();
                Log.d(TAG, "You have it");
            }
        } else if (id == R.id.nav_bluetooth) {
            Log.d(TAG, "Starting to check for req");
            if (!hasBluetooth()) {
                Toast.makeText(_context, "You don't have bluetooth", Toast.LENGTH_SHORT).show();
            } else {
                fragment = new BluetoothFragment();
                Log.d(TAG, "You have it");
            }
        } else if (id == R.id.nav_wifi) {
            Log.d(TAG, "Starting to check for req");
            if (!hasWifi()) {
                Toast.makeText(_context, "You don't have WiFi", Toast.LENGTH_SHORT).show();
            } else {
                fragment = new WifiP2pFragment();
                Log.d(TAG, "You have it");
            }
        } else if (id == R.id.nav_sensor) {
            fragment = new AccelerometerFragment();
        } else if (id == R.id.nav_gesture) {
            fragment = new GestureFragment();
        } else if (id == R.id.nav_usb) {
            fragment = new UsbFragment();
        }

        if (fragment != null) {
            FragmentManager fragmentManager = ((FragmentActivity)_context).getSupportFragmentManager();
            FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }

        ldrawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private boolean hasCamera() {
        if(_context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)) {
            return true;
        } else {
            return false;
        }
    }

    private boolean hasMicrophone() {
        if (_context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_MICROPHONE)) {
            return true;
        }
        return false;
    }

    private boolean hasBluetooth() {
        if (_context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH)) {
            return true;
        } else {
            return false;
        }
    }

    private boolean hasWifi() {
        if (_context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_WIFI)) {
            return true;
        } else {
            return false;
        }
    }
}

