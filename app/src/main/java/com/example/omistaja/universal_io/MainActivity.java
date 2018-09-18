package com.example.omistaja.universal_io;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.omistaja.universal_io.fragments.MicrophoneLiveFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class MainActivity extends AppCompatActivity {

    public static final int REQUEST_PERMISSION_CODE = 1;
    private static final String TAG = "MainActivity";
    DrawerHelper drawerHelper = new DrawerHelper(this);
    FloatingActionButton settingsButton;
    private static final String PREFS_NAME = "prefs";
    private static final String PREF_LIGHT_THEME = "light_theme";
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        boolean useLightTheme = preferences.getBoolean(PREF_LIGHT_THEME, false);

        if (useLightTheme) {
            setTheme(R.style.CustomTheme);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        settingsButton = findViewById(R.id.settingsButton);
        //requestPermission();

        if (useLightTheme) {
            toolbar.setTitleTextColor(Color.BLACK);
            drawerHelper.initDrawersLight(toolbar, savedInstanceState);
        } else {
            drawerHelper.initDrawersDark(toolbar, savedInstanceState);
        }

        DrawerHelper.increaseSwipeEdgeOfDrawer(drawerHelper.inputDrawer);
        drawerHelper.outputDrawer.getDrawerLayout().setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        drawerHelper.setInputItemsInvisible();
        drawerHelper.inputDrawer.getActionBarDrawerToggle().setDrawerIndicatorEnabled(false);

        settingsButton.setOnClickListener(view -> {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        });

        //Pull method from Fragment to Activity
        //FragmentManager fm = getSupportFragmentManager();
        //MyFragment fragment = (MyFragment)fm.findFragmentById(R.id.content_frame);
        //fragment.method();

/*
        //Starts with WelcomeScreen/HomeFragment which is blank atm
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.content_frame, new MicrophoneLiveFragment());
        ft.commit();
*/

    }
/*
        Fragment fragment;
        @Override
        public void onNewIntent(Intent intent) {
            super.onNewIntent(intent);
            // Check if the fragment is an instance of the right fragment
            if (fragment instanceof NfcActivity) {
                Log.d(TAG, "Do I get here?!");
                NfcActivity my = (NfcActivity) fragment;
                // Pass intent or its data to the fragment's method
                setIntent(intent);
                my.resolveIntent(intent);
                Log.d(TAG, "Yes I do, starting NFC intent");
                //my.processNFC(intent.getStringExtra());
            }
        }
*/



    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (drawerHelper.inputDrawer.isDrawerOpen()) {
            drawerHelper.inputDrawer.closeDrawer();
        } else if (drawerHelper.outputDrawer.isDrawerOpen()) {
            drawerHelper.outputDrawer.closeDrawer();
        } else {
            super.onBackPressed();
        }

        getSupportFragmentManager().popBackStack();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.main, menu);
        //MenuItem shareItem = menu.findItem(R.id.menu_item_share);
        //ShareActionProvider myShareActionProvider =  (ShareActionProvider) MenuItemCompat.getActionProvider(shareItem);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
/*
    private void requestPermission() {
        ActivityCompat.requestPermissions(MainActivity.this, new
                String[]{WRITE_EXTERNAL_STORAGE, RECORD_AUDIO, CAMERA, READ_EXTERNAL_STORAGE, ACCESS_COARSE_LOCATION, ACCESS_FINE_LOCATION}, REQUEST_PERMISSION_CODE);
    }


/*
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        if (requestCode == REQUEST_PERMISSION_CODE && grantResults.length > 0) {
            boolean storagePermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;
            boolean recordPermission = grantResults[1] == PackageManager.PERMISSION_GRANTED;
            boolean cameraPermission = grantResults[2] == PackageManager.PERMISSION_GRANTED;
            boolean readPermission = grantResults[3] == PackageManager.PERMISSION_GRANTED;
            boolean coarsePermission = grantResults[4] == PackageManager.PERMISSION_GRANTED;
            boolean finePermission = grantResults[5] == PackageManager.PERMISSION_GRANTED;
            if (storagePermission && recordPermission && cameraPermission && readPermission && coarsePermission && finePermission) {
                // Toast.makeText(MainActivity.this, "Permission Granted", Toast.LENGTH_LONG).show();
                Log.d(TAG, "Permissions granted");
            } else {
                //Toast.makeText(MainActivity.this,"Permission  Denied",Toast.LENGTH_LONG).show();
                Log.d(TAG, "Permissions denied");
            }
        }
    }

    /*
    public boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), RECORD_AUDIO);
        int result2 = ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA);
        int result3 = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED
                && result2 == PackageManager.PERMISSION_GRANTED && result3 == PackageManager.PERMISSION_GRANTED;
    }
    */
}
