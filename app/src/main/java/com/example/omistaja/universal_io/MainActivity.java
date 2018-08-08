package com.example.omistaja.universal_io;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
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
import android.os.Bundle;

import com.example.omistaja.universal_io.Fragments.AccelerometerFragment;
import com.example.omistaja.universal_io.Fragments.BluetoothFragment;
import com.example.omistaja.universal_io.Fragments.BluetoothSpeakerFragment;
import com.example.omistaja.universal_io.Fragments.GestureFragment;
import com.example.omistaja.universal_io.Fragments.MicrophoneFragment;
import com.example.omistaja.universal_io.Fragments.NfcFragment;
import com.example.omistaja.universal_io.Fragments.PhotoFragment;
import com.example.omistaja.universal_io.Fragments.UsbFragment;
import com.example.omistaja.universal_io.Fragments.WifiP2pFragment;
import com.example.omistaja.universal_io.NFC.NdefMessageParser;
import com.example.omistaja.universal_io.NFC.ParsedNdefRecord;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.ShareActionProvider;
import androidx.core.app.ActivityCompat;
import androidx.core.view.MenuItemCompat;
import androidx.customview.widget.ViewDragHelper;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import com.google.android.material.navigation.NavigationView;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.slidingpanelayout.widget.SlidingPaneLayout;

import android.view.Menu;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.omistaja.universal_io.Fragments.HomeFragment;

import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "MainActivity";
    FloatingActionButton leftDraw, rightDraw;
    private DrawerLayout drawerLayout;
    public static final int RequestPermissionCode = 1;
    NavigationView navigationView;
    private String currentInput;
    final SwipeDetector swipeDetector = new SwipeDetector();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        requestPermission();
        setRightItemsVisible();


        drawerLayout = findViewById(R.id.drawer_layout);
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
/*
        expandableList = findViewById(R.id.navigationmenuRight);
*/
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.setDrawerIndicatorEnabled(false);
        toggle.syncState();



        initAll();

/*
        leftDraw = findViewById(R.id.leftDraw);
        leftDraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setRightItemsVisible();
                if (!drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
            }

        });


        rightDraw = findViewById(R.id.rightDraw);
        rightDraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!drawerLayout.isDrawerOpen(GravityCompat.END)) {
                    drawerLayout.openDrawer(GravityCompat.END);
                }
            }
        });
*/
/*
        prepareListData();
        mMenuAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild, expandableList);
        expandableList.setAdapter(mMenuAdapter);

        expandableList.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {
                //Log.d("DEBUG", "submenu item clicked");
                return false;
            }
        });
        expandableList.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long l) {
                //Log.d("DEBUG", "heading clicked");
                return false;
            }
        });
*/


        NavigationView navigationView = findViewById(R.id.nav_view_left);
        navigationView.setNavigationItemSelectedListener(this);

        displayRight();

        //Pull method from Fragment to Activity
        //FragmentManager fm = getSupportFragmentManager();
        //MyFragment fragment = (MyFragment)fm.findFragmentById(R.id.content_frame);
        //fragment.method();

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();

        ft.replace(R.id.content_frame, new HomeFragment());

        ft.commit();

    }

    /*
        NfcFragment fragment;
        @Override
        public void onNewIntent(Intent intent) {
            super.onNewIntent(intent);

            // Check if the fragment is an instance of the right fragment
            if (fragment instanceof NfcFragment) {
                Log.d(TAG, "Do I get here?!");
                NfcFragment my = fragment;
                // Pass intent or its data to the fragment's method
                setIntent(intent);
                my.resolveIntent(intent);
                Log.d(TAG, "Yes I do, starting NFC intent");
                //my.processNFC(intent.getStringExtra());
            }

        }
    */

    float startX, startY, endX, endY;


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                startX = ev.getX();
                startY = ev.getY();
                break;
            case MotionEvent.ACTION_UP:
                endX = ev.getX();
                endY = ev.getY();

                float sensitivity = 5;
                // From left to right
                if (endX - startX >= sensitivity) {
                    if (drawerLayout.isDrawerOpen(GravityCompat.END)) {
                        drawerLayout.closeDrawer(GravityCompat.END);
                    } else {
                        drawerLayout.openDrawer(GravityCompat.START);
                    }
                }

                // From right to left
                if (startX - endX >= sensitivity) {
                    if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                        drawerLayout.closeDrawer(GravityCompat.START);
                    } else {
                        drawerLayout.openDrawer(GravityCompat.END);
                    }
                }

                break;
        }

        return false;
    }

    public void onResume() {
        super.onResume();
        setRightItemsVisible();
        setleftItemsInvisible();
    }

    public void onStop() {
        super.onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    public void onDestroy() {

        super.onDestroy();
    }

    /*
        private void prepareListData() {
            listDataHeader = new ArrayList<>();
            listDataChild = new HashMap<>();

            ExpandedMenuModel item1 = new ExpandedMenuModel();
            item1.setIconName("heading1");
            item1.setIconImg(android.R.drawable.ic_delete);
            // Adding data header
            listDataHeader.add(item1);

            ExpandedMenuModel item2 = new ExpandedMenuModel();
            item2.setIconName("heading2");
            item2.setIconImg(android.R.drawable.ic_delete);
            listDataHeader.add(item2);

            ExpandedMenuModel item3 = new ExpandedMenuModel();
            item3.setIconName("heading3");
            item3.setIconImg(android.R.drawable.ic_delete);
            listDataHeader.add(item3);

            // Adding child data
            List<String> heading1 = new ArrayList<>();
            heading1.add("Submenu of item 1");

            List<String> heading2 = new ArrayList<>();
            heading2.add("Submenu of item 2");
            heading2.add("Submenu of item 2");
            heading2.add("Submenu of item 2");

            listDataChild.put(listDataHeader.get(0), heading1);// Header, Child data
            listDataChild.put(listDataHeader.get(1), heading2);

        }
    */
    @Override
    public void onBackPressed() {

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (drawer.isDrawerOpen(GravityCompat.END)) {
            drawer.closeDrawer(GravityCompat.END);
        } else {
            super.onBackPressed();
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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
        int id = item.getItemId();
        DrawerLayout drawer = findViewById(R.id.drawer_layout);

        /*
        if (id == R.id.menu_item_share) {
            drawer.openDrawer(GravityCompat.END);
        }
*/
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

    private void requestPermission() {
        ActivityCompat.requestPermissions(MainActivity.this, new
                String[]{WRITE_EXTERNAL_STORAGE, RECORD_AUDIO, CAMERA, READ_EXTERNAL_STORAGE, ACCESS_COARSE_LOCATION}, RequestPermissionCode);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case RequestPermissionCode:
                if (grantResults.length > 0) {
                    boolean StoragePermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean RecordPermission = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean CameraPermission = grantResults[2] == PackageManager.PERMISSION_GRANTED;
                    boolean ReadPermission = grantResults[3] == PackageManager.PERMISSION_GRANTED;
                    boolean LocationPermission = grantResults[4] == PackageManager.PERMISSION_GRANTED;
                    if (StoragePermission && RecordPermission && CameraPermission && ReadPermission && LocationPermission) {
                        // Toast.makeText(MainActivity.this, "Permission Granted", Toast.LENGTH_LONG).show();
                    } else {
                        //Toast.makeText(MainActivity.this,"Permission  Denied",Toast.LENGTH_LONG).show();
                    }
                }
                break;
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


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        setRightItemsVisible();
        navigationView = findViewById(R.id.nav_view_right);
        Menu navright_Menu = navigationView.getMenu();

        Fragment fragment = null;

        int id = item.getItemId();

        switch (id) {
            case R.id.nav_camera:
                currentInput = "camera";
                navright_Menu.findItem(R.id.nav_speaker).setVisible(false);
                navright_Menu.findItem(R.id.nav_nfc).setVisible(false);
                openOutput();
                break;
            case R.id.nav_microphone:
                currentInput = "microphone";
                navright_Menu.findItem(R.id.nav_nfc).setVisible(false);
                openOutput();
                break;
            case R.id.nav_bluetooth:
                currentInput = "bluetooth";
                navright_Menu.findItem(R.id.nav_nfc).setVisible(false);
                navright_Menu.findItem(R.id.nav_wifi2).setVisible(false);
                navright_Menu.findItem(R.id.nav_usb).setVisible(false);
                openOutput();
                break;
            case R.id.nav_wifi:
                currentInput = "wifi";
                navright_Menu.findItem(R.id.nav_nfc).setVisible(false);
                navright_Menu.findItem(R.id.nav_bluetooth2).setVisible(false);
                navright_Menu.findItem(R.id.nav_usb).setVisible(false);
                openOutput();
                break;
            case R.id.nav_sensor:
                currentInput = "sensor";
                navright_Menu.findItem(R.id.nav_speaker).setVisible(false);
                openOutput();
                break;
            case R.id.nav_gesture:
                currentInput = "gesture";
                navright_Menu.findItem(R.id.nav_nfc).setVisible(false);
                navright_Menu.findItem(R.id.nav_speaker).setVisible(false);
                openOutput();
                break;
            case R.id.nav_nfc:
                currentInput = "nfc";
                navright_Menu.findItem(R.id.nav_speaker).setVisible(false);
                navright_Menu.findItem(R.id.nav_wifi2).setVisible(false);
                navright_Menu.findItem(R.id.nav_usb).setVisible(false);
                navright_Menu.findItem(R.id.nav_bluetooth2).setVisible(false);
                openOutput();
                break;
            case R.id.nav_usb:
                currentInput = "usb";
                navright_Menu.findItem(R.id.nav_speaker).setVisible(false);
                navright_Menu.findItem(R.id.nav_wifi2).setVisible(false);
                navright_Menu.findItem(R.id.nav_nfc).setVisible(false);
                navright_Menu.findItem(R.id.nav_bluetooth2).setVisible(false);
                openOutput();
                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void displayRight() {
        NavigationView navigationViewRight = findViewById(R.id.nav_view_right);
        navigationViewRight.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @SuppressWarnings("StatementWithEmptyBody")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                // Handle navigation view item clicks here.


                Fragment fragment = null;
                int id = item.getItemId();

                switch (id) {
                    case R.id.nav_wifi2:
                        if (currentInput.equals("camera")) {
                            Toast.makeText(MainActivity.this, "Not Implemented", Toast.LENGTH_SHORT).show();
                        } else if (currentInput.equals("microphone")) {
                            Toast.makeText(MainActivity.this, "Not Implemented", Toast.LENGTH_SHORT).show();
                        } else if (currentInput.equals("bluetooth")) {
                            Toast.makeText(MainActivity.this, "Not Implemented", Toast.LENGTH_SHORT).show();
                        } else if (currentInput.equals("wifi")) {
                            Toast.makeText(MainActivity.this, "Not Implemented", Toast.LENGTH_SHORT).show();
                        } else if (currentInput.equals("sensors")) {
                            Toast.makeText(MainActivity.this, "Not Implemented", Toast.LENGTH_SHORT).show();
                        } else if (currentInput.equals("gesture")) {
                            Toast.makeText(MainActivity.this, "Not Implemented", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case R.id.nav_bluetooth2:
                        if (currentInput.equals("camera")) {
                            Toast.makeText(MainActivity.this, "Not Implemented", Toast.LENGTH_SHORT).show();
                        } else if (currentInput.equals("microphone")) {
                            Toast.makeText(MainActivity.this, "Not Implemented", Toast.LENGTH_SHORT).show();
                        } else if (currentInput.equals("bluetooth")) {
                            Toast.makeText(MainActivity.this, "Not Implemented", Toast.LENGTH_SHORT).show();
                        } else if (currentInput.equals("sensors")) {
                            Toast.makeText(MainActivity.this, "Not Implemented", Toast.LENGTH_SHORT).show();
                        } else if (currentInput.equals("gesture")) {
                            Toast.makeText(MainActivity.this, "Not Implemented", Toast.LENGTH_SHORT).show();
                        } else if (currentInput.equals("usb")) {
                            Toast.makeText(MainActivity.this, "Not Implemented", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case R.id.nav_speaker:
                        if (currentInput.equals("microphone")) {
                            fragment = new MicrophoneFragment();
                        } else if (currentInput.equals("bluetooth")) {
                            fragment = new BluetoothSpeakerFragment();
                        } else if (currentInput.equals("wifi")) {
                            Toast.makeText(MainActivity.this, "Not Implemented", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case R.id.nav_nfc:
                        if (currentInput.equals("camera")) {
                            Toast.makeText(MainActivity.this, "Not Implemented", Toast.LENGTH_SHORT).show();
                        } else if (currentInput.equals("sensors")) {
                            Toast.makeText(MainActivity.this, "Not Implemented", Toast.LENGTH_SHORT).show();
                        } else if (currentInput.equals("nfc")) {
                            Toast.makeText(MainActivity.this, "Not Implemented", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case R.id.nav_usb:
                        if (currentInput.equals("camera")) {
                            Toast.makeText(MainActivity.this, "Not Implemented", Toast.LENGTH_SHORT).show();
                        } else if (currentInput.equals("microphone")) {
                            Toast.makeText(MainActivity.this, "Not Implemented", Toast.LENGTH_SHORT).show();
                        } else if (currentInput.equals("sensors")) {
                            Toast.makeText(MainActivity.this, "Not Implemented", Toast.LENGTH_SHORT).show();
                        } else if (currentInput.equals("gesture")) {
                            Toast.makeText(MainActivity.this, "Not Implemented", Toast.LENGTH_SHORT).show();
                        } else if (currentInput.equals("usb")) {
                            Toast.makeText(MainActivity.this, "Not Implemented", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case R.id.nav_screen:
                        if (currentInput.equals("camera")) {
                            fragment = new PhotoFragment();
                        } else if (currentInput.equals("microphone")) {
                            Toast.makeText(MainActivity.this, "Not Implemented", Toast.LENGTH_SHORT).show();
                        } else if (currentInput.equals("bluetooth")) {
                            fragment = new BluetoothFragment();
                        } else if (currentInput.equals("wifi")) {
                            fragment = new WifiP2pFragment();
                        } else if (currentInput.equals("sensor")) {
                            fragment = new AccelerometerFragment();
                        } else if (currentInput.equals("gesture")) {
                            fragment = new GestureFragment();
                        } else if (currentInput.equals("nfc")) {
                            Intent intent = new Intent(MainActivity.this, NfcFragment.class);
                            startActivity(intent);
                        } else if (currentInput.equals("usb")) {
                            fragment = new UsbFragment();
                        }
                        break;
                    case R.id.nav_share:
                        if (currentInput.equals("camera")) {
                            Toast.makeText(MainActivity.this, "Not Implemented", Toast.LENGTH_SHORT).show();
                        } else if (currentInput.equals("microphone")) {
                            Toast.makeText(MainActivity.this, "Not Implemented", Toast.LENGTH_SHORT).show();
                        } else if (currentInput.equals("bluetooth")) {
                            Toast.makeText(MainActivity.this, "Not Implemented", Toast.LENGTH_SHORT).show();
                        } else if (currentInput.equals("wifi")) {
                            Toast.makeText(MainActivity.this, "Not Implemented", Toast.LENGTH_SHORT).show();
                        } else if (currentInput.equals("sensor")) {
                            Toast.makeText(MainActivity.this, "Not Implemented", Toast.LENGTH_SHORT).show();
                        } else if (currentInput.equals("gesture")) {
                            Toast.makeText(MainActivity.this, "Not Implemented", Toast.LENGTH_SHORT).show();
                        } else if (currentInput.equals("nfc")) {
                            Toast.makeText(MainActivity.this, "Not Implemented", Toast.LENGTH_SHORT).show();
                        } else if (currentInput.equals("usb")) {
                            Toast.makeText(MainActivity.this, "Not Implemented", Toast.LENGTH_SHORT).show();
                        }
                        break;
                }

                if (fragment != null) {
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction ft = fragmentManager.beginTransaction();
                    ft.replace(R.id.content_frame, fragment);
                    ft.commit();
                }

                drawerLayout.closeDrawer(GravityCompat.END);
                return true;
            }
        });
    }

    private void openOutput() {
        //hiderightItems(permssion);
        if (!drawerLayout.isDrawerOpen(GravityCompat.END)) {
            drawerLayout.openDrawer(GravityCompat.END);
        }
    }

    private boolean hasCamera() {
        return getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY);
    }

    private boolean hasMicrophone() {
        return getPackageManager().hasSystemFeature(PackageManager.FEATURE_MICROPHONE);
    }

    private boolean hasBluetooth() {
        return getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH);
    }

    private boolean hasWifi() {
        return getPackageManager().hasSystemFeature(PackageManager.FEATURE_WIFI);
    }

    private boolean hasNfc() {
        return getPackageManager().hasSystemFeature(PackageManager.FEATURE_NFC);
    }

    private void initAll() {
        setleftItemsInvisible();
    }

    private void setleftItemsInvisible() {
        navigationView = findViewById(R.id.nav_view_left);
        Menu navleft_Menu = navigationView.getMenu();
        if (!hasCamera()) {
            navleft_Menu.findItem(R.id.nav_camera).setVisible(false);
        }
        if (!hasMicrophone()) {
            navleft_Menu.findItem(R.id.nav_microphone).setVisible(false);
        }
        if (!hasBluetooth()) {
            navleft_Menu.findItem(R.id.nav_bluetooth).setVisible(false);
        }
        if (!hasWifi()) {
            navleft_Menu.findItem(R.id.nav_wifi).setVisible(false);
        }
        if (!hasNfc()) {
            navleft_Menu.findItem(R.id.nav_nfc).setVisible(false);
        }
    }

    /*
    private void hiderightItems(int permission) {

        //navigationView = findViewById(R.id.nav_view_right);
        //Menu navright_Menu = navigationView.getMenu();

        switch(permission){
            case 0:

            case 1:

            case 2:

            case 3:

            case 4:

            case 5:

            case 6:

            case 7:

        }

    }
*/
    private void setRightItemsVisible() {
        navigationView = findViewById(R.id.nav_view_right);
        Menu navright_Menu = navigationView.getMenu();
        navright_Menu.findItem(R.id.nav_nfc).setVisible(true);
        navright_Menu.findItem(R.id.nav_wifi2).setVisible(true);
        navright_Menu.findItem(R.id.nav_usb).setVisible(true);
        navright_Menu.findItem(R.id.nav_bluetooth2).setVisible(true);
        navright_Menu.findItem(R.id.nav_screen).setVisible(true);
        navright_Menu.findItem(R.id.nav_speaker).setVisible(true);
    }

/*
    private void showMessage(int title, int message) {
        mDialog.setTitle(title);
        mDialog.setMessage(getText(message));
        mDialog.show();
    }

    private NdefRecord newTextRecord(String text, Locale locale, boolean encodeInUtf8) {
        byte[] langBytes = locale.getLanguage().getBytes(Charset.forName("US-ASCII"));

        Charset utfEncoding = encodeInUtf8 ? Charset.forName("UTF-8") : Charset.forName("UTF-16");
        byte[] textBytes = text.getBytes(utfEncoding);

        int utfBit = encodeInUtf8 ? 0 : (1 << 7);
        char status = (char) (utfBit + langBytes.length);

        byte[] data = new byte[1 + langBytes.length + textBytes.length];
        data[0] = (byte) status;
        System.arraycopy(langBytes, 0, data, 1, langBytes.length);
        System.arraycopy(textBytes, 0, data, 1 + langBytes.length, textBytes.length);

        return new NdefRecord(NdefRecord.TNF_WELL_KNOWN, NdefRecord.RTD_TEXT, new byte[0], data);
    }

    private void showWirelessSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.nfc_disabled);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
                startActivity(intent);
            }
        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        builder.create().show();
        return;
    }

    private void resolveIntent(Intent intent) {
        String action = intent.getAction();
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
            Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            NdefMessage[] msgs;
            if (rawMsgs != null) {
                msgs = new NdefMessage[rawMsgs.length];
                for (int i = 0; i < rawMsgs.length; i++) {
                    msgs[i] = (NdefMessage) rawMsgs[i];
                }
            } else {
                // Unknown tag type
                byte[] empty = new byte[0];
                byte[] id = intent.getByteArrayExtra(NfcAdapter.EXTRA_ID);
                Tag tag = (Tag) intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
                byte[] payload = dumpTagData(tag).getBytes();
                NdefRecord record = new NdefRecord(NdefRecord.TNF_UNKNOWN, empty, id, payload);
                NdefMessage msg = new NdefMessage(new NdefRecord[] { record });
                msgs = new NdefMessage[] { msg };
                mTags.add(tag);
            }
            // Setup the views
            buildTagViews(msgs);
        }
    }

    private String dumpTagData(Tag tag) {
        StringBuilder sb = new StringBuilder();
        byte[] id = tag.getId();
        sb.append("ID (hex): ").append(toHex(id)).append('\n');
        sb.append("ID (reversed hex): ").append(toReversedHex(id)).append('\n');
        sb.append("ID (dec): ").append(toDec(id)).append('\n');
        sb.append("ID (reversed dec): ").append(toReversedDec(id)).append('\n');

        String prefix = "android.nfc.tech.";
        sb.append("Technologies: ");
        for (String tech : tag.getTechList()) {
            sb.append(tech.substring(prefix.length()));
            sb.append(", ");
        }
        sb.delete(sb.length() - 2, sb.length());
        for (String tech : tag.getTechList()) {
            if (tech.equals(MifareClassic.class.getName())) {
                sb.append('\n');
                String type = "Unknown";
                try {
                    MifareClassic mifareTag;
                    try {
                        mifareTag = MifareClassic.get(tag);
                    } catch (Exception e) {
                        // Fix for Sony Xperia Z3/Z5 phones
                        tag = cleanupTag(tag);
                        mifareTag = MifareClassic.get(tag);
                    }
                    switch (mifareTag.getType()) {
                        case MifareClassic.TYPE_CLASSIC:
                            type = "Classic";
                            break;
                        case MifareClassic.TYPE_PLUS:
                            type = "Plus";
                            break;
                        case MifareClassic.TYPE_PRO:
                            type = "Pro";
                            break;
                    }
                    sb.append("Mifare Classic type: ");
                    sb.append(type);
                    sb.append('\n');

                    sb.append("Mifare size: ");
                    sb.append(mifareTag.getSize() + " bytes");
                    sb.append('\n');

                    sb.append("Mifare sectors: ");
                    sb.append(mifareTag.getSectorCount());
                    sb.append('\n');

                    sb.append("Mifare blocks: ");
                    sb.append(mifareTag.getBlockCount());
                } catch (Exception e) {
                    sb.append("Mifare classic error: " + e.getMessage());
                }
            }

            if (tech.equals(MifareUltralight.class.getName())) {
                sb.append('\n');
                MifareUltralight mifareUlTag = MifareUltralight.get(tag);
                String type = "Unknown";
                switch (mifareUlTag.getType()) {
                    case MifareUltralight.TYPE_ULTRALIGHT:
                        type = "Ultralight";
                        break;
                    case MifareUltralight.TYPE_ULTRALIGHT_C:
                        type = "Ultralight C";
                        break;
                }
                sb.append("Mifare Ultralight type: ");
                sb.append(type);
            }
        }

        return sb.toString();
    }

    private Tag cleanupTag(Tag oTag) {
        if (oTag == null)
            return null;

        String[] sTechList = oTag.getTechList();

        Parcel oParcel = Parcel.obtain();
        oTag.writeToParcel(oParcel, 0);
        oParcel.setDataPosition(0);

        int len = oParcel.readInt();
        byte[] id = null;
        if (len >= 0) {
            id = new byte[len];
            oParcel.readByteArray(id);
        }
        int[] oTechList = new int[oParcel.readInt()];
        oParcel.readIntArray(oTechList);
        Bundle[] oTechExtras = oParcel.createTypedArray(Bundle.CREATOR);
        int serviceHandle = oParcel.readInt();
        int isMock = oParcel.readInt();
        IBinder tagService;
        if (isMock == 0) {
            tagService = oParcel.readStrongBinder();
        } else {
            tagService = null;
        }
        oParcel.recycle();

        int nfca_idx = -1;
        int mc_idx = -1;
        short oSak = 0;
        short nSak = 0;

        for (int idx = 0; idx < sTechList.length; idx++) {
            if (sTechList[idx].equals(NfcA.class.getName())) {
                if (nfca_idx == -1) {
                    nfca_idx = idx;
                    if (oTechExtras[idx] != null && oTechExtras[idx].containsKey("sak")) {
                        oSak = oTechExtras[idx].getShort("sak");
                        nSak = oSak;
                    }
                } else {
                    if (oTechExtras[idx] != null && oTechExtras[idx].containsKey("sak")) {
                        nSak = (short) (nSak | oTechExtras[idx].getShort("sak"));
                    }
                }
            } else if (sTechList[idx].equals(MifareClassic.class.getName())) {
                mc_idx = idx;
            }
        }

        boolean modified = false;

        if (oSak != nSak) {
            oTechExtras[nfca_idx].putShort("sak", nSak);
            modified = true;
        }

        if (nfca_idx != -1 && mc_idx != -1 && oTechExtras[mc_idx] == null) {
            oTechExtras[mc_idx] = oTechExtras[nfca_idx];
            modified = true;
        }

        if (!modified) {
            return oTag;
        }

        Parcel nParcel = Parcel.obtain();
        nParcel.writeInt(id.length);
        nParcel.writeByteArray(id);
        nParcel.writeInt(oTechList.length);
        nParcel.writeIntArray(oTechList);
        nParcel.writeTypedArray(oTechExtras, 0);
        nParcel.writeInt(serviceHandle);
        nParcel.writeInt(isMock);
        if (isMock == 0) {
            nParcel.writeStrongBinder(tagService);
        }
        nParcel.setDataPosition(0);

        Tag nTag = Tag.CREATOR.createFromParcel(nParcel);

        nParcel.recycle();

        return nTag;
    }

    private String toHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = bytes.length - 1; i >= 0; --i) {
            int b = bytes[i] & 0xff;
            if (b < 0x10)
                sb.append('0');
            sb.append(Integer.toHexString(b));
            if (i > 0) {
                sb.append(" ");
            }
        }
        return sb.toString();
    }

    private String toReversedHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; ++i) {
            if (i > 0) {
                sb.append(" ");
            }
            int b = bytes[i] & 0xff;
            if (b < 0x10)
                sb.append('0');
            sb.append(Integer.toHexString(b));
        }
        return sb.toString();
    }

    private long toDec(byte[] bytes) {
        long result = 0;
        long factor = 1;
        for (int i = 0; i < bytes.length; ++i) {
            long value = bytes[i] & 0xffl;
            result += value * factor;
            factor *= 256l;
        }
        return result;
    }

    private long toReversedDec(byte[] bytes) {
        long result = 0;
        long factor = 1;
        for (int i = bytes.length - 1; i >= 0; --i) {
            long value = bytes[i] & 0xffl;
            result += value * factor;
            factor *= 256l;
        }
        return result;
    }

    void buildTagViews(NdefMessage[] msgs) {
        if (msgs == null || msgs.length == 0) {
            return;
        }
        LayoutInflater inflater = LayoutInflater.from(this);
        LinearLayout content = mTagContent;

        // Parse the first message in the list
        // Build views for all of the sub records
        Date now = new Date();
        List<ParsedNdefRecord> records = NdefMessageParser.parse(msgs[0]);
        final int size = records.size();
        for (int i = 0; i < size; i++) {
            TextView timeView = new TextView(this);
            timeView.setText(TIME_FORMAT.format(now));
            content.addView(timeView, 0);
            ParsedNdefRecord record = records.get(i);
            content.addView(record.getView(this, inflater, content, i), 1 + i);
            content.addView(inflater.inflate(R.layout.tag_divider, content, false), 2 + i);
        }
    }

    private void clearTags() {
        mTags.clear();
        for (int i = mTagContent.getChildCount() -1; i >= 0 ; i--) {
            View view = mTagContent.getChildAt(i);
            if (view.getId() != R.id.tag_viewer_text) {
                mTagContent.removeViewAt(i);
            }
        }
    }

    private void copyIds(String text) {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText("NFC IDs", text);
        clipboard.setPrimaryClip(clipData);
        Toast.makeText(this, mTags.size() + " IDs copied", Toast.LENGTH_SHORT).show();
    }

    private String getIdsHex() {
        StringBuilder builder = new StringBuilder();
        for (Tag tag : mTags) {
            builder.append(toHex(tag.getId()));
            builder.append('\n');
        }
        builder.setLength(builder.length() - 1); // Remove last new line
        return builder.toString().replace(" ", "");
    }

    private String getIdsReversedHex() {
        StringBuilder builder = new StringBuilder();
        for (Tag tag : mTags) {
            builder.append(toReversedHex(tag.getId()));
            builder.append('\n');
        }
        builder.setLength(builder.length() - 1); // Remove last new line
        return builder.toString().replace(" ", "");
    }

    private String getIdsDec() {
        StringBuilder builder = new StringBuilder();
        for (Tag tag : mTags) {
            builder.append(toDec(tag.getId()));
            builder.append('\n');
        }
        builder.setLength(builder.length() - 1); // Remove last new line
        return builder.toString();
    }

    private String getIdsReversedDec() {
        StringBuilder builder = new StringBuilder();
        for (Tag tag : mTags) {
            builder.append(toReversedDec(tag.getId()));
            builder.append('\n');
        }
        builder.setLength(builder.length() - 1); // Remove last new line
        return builder.toString();
    }

    @Override
    public void onNewIntent(Intent intent) {
        setIntent(intent);
        resolveIntent(intent);
    }
    */

}
