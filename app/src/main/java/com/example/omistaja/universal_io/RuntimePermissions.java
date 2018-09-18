package com.example.omistaja.universal_io;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class RuntimePermissions {

    private static final String TAG = "RuntimePermissionsClass";
    public static final int CAMERA_PERMISSION = 10;
    public static final int MICROPHONE_PERMISSION = 11;
    public static final int LOCATION_PERMISSION = 12;

    Context _context;

    public RuntimePermissions(Context context) {
        _context = context;

    }

    public boolean isCameraPermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(_context, Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(_context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(_context, Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "Permissions granted");
                return true;
            } else {
                ActivityCompat.requestPermissions(((Activity) _context), new String[]{Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE}, CAMERA_PERMISSION);
                return false;
            }
        } else {
            Log.d(TAG, "Permission already granted");
            return true;
        }
    }

    public boolean isMicrophonePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(_context, Manifest.permission.RECORD_AUDIO)
                    == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(_context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(_context, Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "Permissions granted");
                return true;
            } else {
                ActivityCompat.requestPermissions(((Activity) _context), new String[]{Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE}, MICROPHONE_PERMISSION);
                return false;
            }
        } else {
            Log.d(TAG, "Permissions already granted");
            return true;
        }
    }

    public boolean isLocationPermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(_context, Manifest.permission.ACCESS_COARSE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(_context, Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "Permssions Granted");
                return true;
            } else {
                ActivityCompat.requestPermissions(((Activity) _context), new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION);
                return false;
            }
        } else {
            Log.d(TAG, "Permissions already granted");
            return true;
        }
    }
}
