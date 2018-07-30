package com.example.omistaja.universal_io;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.nfc.NfcAdapter;
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
import com.example.omistaja.universal_io.Fragments.GestureFragment;
import com.example.omistaja.universal_io.Fragments.GyroscopeFragment;
import com.example.omistaja.universal_io.Fragments.LHPmeterFragment;
import com.example.omistaja.universal_io.Fragments.MicrophoneFragment;
import com.example.omistaja.universal_io.Fragments.NfcFragment;
import com.example.omistaja.universal_io.Fragments.PhotoFragment;
import com.example.omistaja.universal_io.Fragments.UsbFragment;
import com.example.omistaja.universal_io.Fragments.WifiP2pFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.View;

import com.google.android.material.navigation.NavigationView;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.example.omistaja.universal_io.Fragments.HomeFragment;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private final static String TAG = "MainActivity";
    FloatingActionButton leftDraw, rightDraw;
    private DrawerLayout drawerLayout;
    public static final int RequestPermissionCode = 1;
    PendingIntent intent;
    NavigationView navigationView;
    NfcAdapter mAdapter;
    public PendingIntent mPendingIntent;
    private IntentFilter[] mFilters;
    private String[][] mTechLists;
    public int mCount = 0;
    TextView nfcView;
    private String currentInput;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        requestPermission();
        setRightItemsVisible();

        drawerLayout = findViewById(R.id.drawer_layout);
/*
        expandableList = findViewById(R.id.navigationmenuRight);
*/
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();


        initAll();

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

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();

        ft.replace(R.id.content_frame, new HomeFragment());

        ft.commit();

    }

    public void onResume() {
        super.onResume();
        setRightItemsVisible();
        setleftItemsInvisible();
        if (mAdapter != null) mAdapter.enableForegroundDispatch(this, mPendingIntent, mFilters,
                mTechLists);
    }

    public void onStop() {
        super.onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mAdapter != null) mAdapter.disableForegroundDispatch(this);
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

    Fragment fragment;

    @Override
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        // Check if the fragment is an instance of the right fragment
        Log.d(TAG, "Do I get here 1?");
        if (fragment instanceof NfcFragment) {
            NfcFragment my = (NfcFragment) fragment;
            Log.d(TAG, "Do I get here 2?");
            // Pass intent or its data to the fragment's method
            Log.i("Foreground dispatch", "Discovered tag with intent: " + intent);
            my.nfcView.setText("Discovered tag " + ++my.mCount + " with intent: " + intent);
        }
        Log.d(TAG, "Do I get here 3?");
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        DrawerLayout drawer = findViewById(R.id.drawer_layout);

        if (id == R.id.action_settings) {
            drawer.openDrawer(GravityCompat.END);
        }

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
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
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
                            Toast.makeText(MainActivity.this, "Not Implemented", Toast.LENGTH_SHORT).show();
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

    private void NfcInit() {
        mAdapter = NfcAdapter.getDefaultAdapter(this);

        if (mAdapter == null) {
            // Stop here, we definitely need NFC
            Toast.makeText(this, "This device doesn't support NFC.", Toast.LENGTH_LONG).show();
        }

        if (!mAdapter.isEnabled()) {
            nfcView.setText("NFC is disabled.");
        } else {
            nfcView.setText("NFC Enabled, Read NFC");
        }
        mPendingIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        // Setup an intent filter for all MIME based dispatches
        IntentFilter ndef = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
        try {
            ndef.addDataType("*/*");
        } catch (IntentFilter.MalformedMimeTypeException e) {
            throw new RuntimeException("fail", e);
        }
        mFilters = new IntentFilter[]{ndef,};
        // Setup a tech list for all NfcF tags
        mTechLists = new String[][]{new String[]{NfcF.class.getName(), NfcB.class.getName(), NfcA.class.getName(),
                NfcV.class.getName(), IsoDep.class.getName(), Ndef.class.getName(),
                NdefFormatable.class.getName(), MifareClassic.class.getName(),
                MifareUltralight.class.getName()}};
    }
}
