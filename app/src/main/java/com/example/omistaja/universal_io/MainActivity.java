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
import com.example.omistaja.universal_io.Fragments.VideoFragment;
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
import android.view.Gravity;
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
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.ExpandableDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

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
    private String currentInput, currentOutput;
    Fragment fragment = null;
    Drawer result = null;
    Drawer resultAppended = null;
    PrimaryDrawerItem wifiItem, btItem, nfcItem, usbItem, screenItem, shareItem, speakerItem;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        requestPermission();
        setRightItemsVisible();
        drawerBuild();


        drawerLayout = findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);


        toggle.setDrawerIndicatorEnabled(false);
        increaseSwipeEdgeOfDrawer(drawerLayout);
        toggle.syncState();


        initAll();

        /*
        expandableList = findViewById(R.id.navigationmenuRight);
*/

        // To open new test input drawer
        leftDraw = findViewById(R.id.leftDraw);
        leftDraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resultAppended.resetDrawerContent();
                result.openDrawer();
            }

        });

        // To open new test output drawer
        rightDraw = findViewById(R.id.rightDraw);
        rightDraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resultAppended.resetDrawerContent();
                resultAppended.openDrawer();
            }
        });


        NavigationView navigationView = findViewById(R.id.nav_view_left);
        navigationView.setNavigationItemSelectedListener(this);
        displayRight();

        //Pull method from Fragment to Activity
        //FragmentManager fm = getSupportFragmentManager();
        //MyFragment fragment = (MyFragment)fm.findFragmentById(R.id.content_frame);
        //fragment.method();


        //Starts with WelcomeScreen/HomeFragment which is blank atm
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

    //POSSIBLE CHANGE TO THIS DRAWERBUILD BY MIKE PENZ

    public void drawerBuild() {


        AlertDialog.Builder inputOutput = new AlertDialog.Builder(this);



        wifiItem = new PrimaryDrawerItem().withName("WiFi").withIcon(R.drawable.ic_wifi).withIdentifier(1).withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
            @Override
            public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                currentOutput = "WiFi";
                switch (currentInput) {
                    case "Camera":
                        Toast.makeText(MainActivity.this, "Not Implemented", Toast.LENGTH_SHORT).show();
                        break;
                    case "Microphone":
                        Toast.makeText(MainActivity.this, "Not Implemented", Toast.LENGTH_SHORT).show();
                        break;
                    case "Bluetooth":
                        Toast.makeText(MainActivity.this, "Not Implemented", Toast.LENGTH_SHORT).show();
                        break;
                    case "WiFi":
                        Toast.makeText(MainActivity.this, "Not Implemented", Toast.LENGTH_SHORT).show();
                        break;
                    case "Sensors":
                        Toast.makeText(MainActivity.this, "Not Implemented", Toast.LENGTH_SHORT).show();
                        break;
                    case "Gesture":
                        Toast.makeText(MainActivity.this, "Not Implemented", Toast.LENGTH_SHORT).show();
                        break;
                }
                return false;
            }
        });
        btItem = new PrimaryDrawerItem().withName("Bluetooth").withIcon(R.drawable.ic_bluetooth).withIdentifier(2).withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
            @Override
            public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                currentOutput = "Bluetoooth";
                switch (currentInput) {
                    case "Camera":
                        Toast.makeText(MainActivity.this, "Not Implemented", Toast.LENGTH_SHORT).show();
                        break;
                    case "Microphone":
                        Toast.makeText(MainActivity.this, "Not Implemented", Toast.LENGTH_SHORT).show();
                        break;
                    case "Bluetooth":
                        Toast.makeText(MainActivity.this, "Not Implemented", Toast.LENGTH_SHORT).show();
                        break;
                    case "Sensors":
                        Toast.makeText(MainActivity.this, "Not Implemented", Toast.LENGTH_SHORT).show();
                        break;
                    case "Gesture":
                        Toast.makeText(MainActivity.this, "Not Implemented", Toast.LENGTH_SHORT).show();
                        break;
                    case "USB":
                        Toast.makeText(MainActivity.this, "Not Implemented", Toast.LENGTH_SHORT).show();
                        break;
                }
                return false;
            }
        });
        speakerItem = new PrimaryDrawerItem().withName("Speaker").withIcon(R.drawable.ic_speaker).withIdentifier(3).withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
            @Override
            public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                currentOutput = "Speaker";
                switch (currentInput) {
                    case "Microphone": {
                        inputOutput.setMessage("Do you want to use " + currentInput + " to " + currentOutput + "?").setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        fragment = new MicrophoneFragment();
                                        FragmentManager fragmentManager = getSupportFragmentManager();
                                        FragmentTransaction ft = fragmentManager.beginTransaction();
                                        ft.replace(R.id.content_frame, fragment);
                                        ft.commit();
                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.cancel();
                                    }
                                });
                        AlertDialog alertDialog = inputOutput.create();
                        alertDialog.setTitle("Are you sure?");
                        alertDialog.show();
                        break;
                    }
                    case "Bluetooth": {
                        inputOutput.setMessage("Do you want to use " + currentInput + " to " + currentOutput + "?").setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        fragment = new BluetoothSpeakerFragment();
                                        FragmentManager fragmentManager = getSupportFragmentManager();
                                        FragmentTransaction ft = fragmentManager.beginTransaction();
                                        ft.replace(R.id.content_frame, fragment);
                                        ft.commit();
                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.cancel();
                                    }
                                });
                        AlertDialog alertDialog = inputOutput.create();
                        alertDialog.setTitle("Are you sure?");
                        alertDialog.show();
                        break;
                    }
                    case "WiFi":
                        Toast.makeText(MainActivity.this, "Not Implemented", Toast.LENGTH_SHORT).show();
                        break;
                }
                return false;
            }
        });
        nfcItem = new PrimaryDrawerItem().withName("NFC").withIcon(R.drawable.ic_nfc).withIdentifier(4).withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
            @Override
            public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                currentOutput = "NFC";
                switch (currentInput) {
                    case "Camera":
                        Toast.makeText(MainActivity.this, "Not Implemented", Toast.LENGTH_SHORT).show();
                        break;
                    case "Sensors":
                        Toast.makeText(MainActivity.this, "Not Implemented", Toast.LENGTH_SHORT).show();
                        break;
                    case "NFC":
                        Toast.makeText(MainActivity.this, "Not Implemented", Toast.LENGTH_SHORT).show();
                        break;
                }
                return false;
            }
        });
        usbItem = new PrimaryDrawerItem().withName("USB").withIcon(R.drawable.ic_usb).withIdentifier(5).withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
            @Override
            public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                currentOutput = "USB";
                switch (currentInput) {
                    case "Camera":
                        Toast.makeText(MainActivity.this, "Not Implemented", Toast.LENGTH_SHORT).show();
                        break;
                    case "Microphone":
                        Toast.makeText(MainActivity.this, "Not Implemented", Toast.LENGTH_SHORT).show();
                        break;
                    case "Sensors":
                        Toast.makeText(MainActivity.this, "Not Implemented", Toast.LENGTH_SHORT).show();
                        break;
                    case "Gesture":
                        Toast.makeText(MainActivity.this, "Not Implemented", Toast.LENGTH_SHORT).show();
                        break;
                    case "USB":
                        Toast.makeText(MainActivity.this, "Not Implemented", Toast.LENGTH_SHORT).show();
                        break;
                }
                return false;
            }
        });
        screenItem = new PrimaryDrawerItem().withName("Screen").withIcon(R.drawable.ic_smartphone).withIdentifier(6).withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
            @Override
            public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                currentOutput = "Screen";
                switch (currentInput) {
                    case "Photo": {
                        inputOutput.setMessage("Do you want to use " + currentInput + " to " + currentOutput + "?").setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        fragment = new PhotoFragment();
                                        FragmentManager fragmentManager = getSupportFragmentManager();
                                        FragmentTransaction ft = fragmentManager.beginTransaction();
                                        ft.replace(R.id.content_frame, fragment);
                                        ft.commit();
                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.cancel();
                                    }
                                });
                        AlertDialog alertDialog = inputOutput.create();
                        alertDialog.setTitle("Are you sure?");
                        alertDialog.show();
                        break;
                    }
                    case "Video": {
                        inputOutput.setMessage("Do you want to use " + currentInput + " to " + currentOutput + "?").setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        fragment = new VideoFragment();
                                        FragmentManager fragmentManager = getSupportFragmentManager();
                                        FragmentTransaction ft = fragmentManager.beginTransaction();
                                        ft.replace(R.id.content_frame, fragment);
                                        ft.commit();
                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.cancel();
                                    }
                                });
                        AlertDialog alertDialog = inputOutput.create();
                        alertDialog.setTitle("Are you sure?");
                        alertDialog.show();
                        break;
                    }
                    case "Microphone":
                        Toast.makeText(MainActivity.this, "Not Implemented", Toast.LENGTH_SHORT).show();
                        break;
                    case "Bluetooth": {
                        inputOutput.setMessage("Do you want to use " + currentInput + " to " + currentOutput + "?").setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        fragment = new BluetoothFragment();
                                        FragmentManager fragmentManager = getSupportFragmentManager();
                                        FragmentTransaction ft = fragmentManager.beginTransaction();
                                        ft.replace(R.id.content_frame, fragment);
                                        ft.commit();
                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.cancel();
                                    }
                                });
                        AlertDialog alertDialog = inputOutput.create();
                        alertDialog.setTitle("Are you sure?");
                        alertDialog.show();
                        break;
                    }
                    case "WiFi": {
                        inputOutput.setMessage("Do you want to use " + currentInput + " to " + currentOutput + "?").setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        fragment = new WifiP2pFragment();
                                        FragmentManager fragmentManager = getSupportFragmentManager();
                                        FragmentTransaction ft = fragmentManager.beginTransaction();
                                        ft.replace(R.id.content_frame, fragment);
                                        ft.commit();
                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.cancel();
                                    }
                                });
                        AlertDialog alertDialog = inputOutput.create();
                        alertDialog.setTitle("Are you sure?");
                        alertDialog.show();
                        break;
                    }
                    case "Sensors": {
                        inputOutput.setMessage("Do you want to use " + currentInput + " to " + currentOutput + "?").setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        fragment = new AccelerometerFragment();
                                        FragmentManager fragmentManager = getSupportFragmentManager();
                                        FragmentTransaction ft = fragmentManager.beginTransaction();
                                        ft.replace(R.id.content_frame, fragment);
                                        ft.commit();
                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.cancel();
                                    }
                                });
                        AlertDialog alertDialog = inputOutput.create();
                        alertDialog.setTitle("Are you sure?");
                        alertDialog.show();
                        break;
                    }
                    case "Gesture": {
                        inputOutput.setMessage("Do you want to use " + currentInput + " to " + currentOutput + "?").setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        fragment = new GestureFragment();
                                        FragmentManager fragmentManager = getSupportFragmentManager();
                                        FragmentTransaction ft = fragmentManager.beginTransaction();
                                        ft.replace(R.id.content_frame, fragment);
                                        ft.commit();
                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.cancel();
                                    }
                                });
                        AlertDialog alertDialog = inputOutput.create();
                        alertDialog.setTitle("Are you sure?");
                        alertDialog.show();
                        break;
                    }
                    case "NFC":
                        Intent intent = new Intent(MainActivity.this, NfcFragment.class);
                        startActivity(intent);
                        break;
                    case "USB": {
                        inputOutput.setMessage("Do you want to use " + currentInput + " to " + currentOutput + "?").setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        fragment = new UsbFragment();
                                        FragmentManager fragmentManager = getSupportFragmentManager();
                                        FragmentTransaction ft = fragmentManager.beginTransaction();
                                        ft.replace(R.id.content_frame, fragment);
                                        ft.commit();
                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.cancel();
                                    }
                                });
                        AlertDialog alertDialog = inputOutput.create();
                        alertDialog.setTitle("Are you sure?");
                        alertDialog.show();
                        break;
                    }
                }
                return false;
            }
        });
        shareItem = new PrimaryDrawerItem().withName("Share").withIcon(R.drawable.ic_share).withIdentifier(7).withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
            @Override
            public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                currentOutput = "Share";
                switch (currentInput) {
                    case "Camera":
                        Toast.makeText(MainActivity.this, "Not Implemented", Toast.LENGTH_SHORT).show();
                        break;
                    case "Microphone":
                        Toast.makeText(MainActivity.this, "Not Implemented", Toast.LENGTH_SHORT).show();
                        break;
                    case "Bluetooth":
                        Toast.makeText(MainActivity.this, "Not Implemented", Toast.LENGTH_SHORT).show();
                        break;
                    case "WiFi":
                        Toast.makeText(MainActivity.this, "Not Implemented", Toast.LENGTH_SHORT).show();
                        break;
                    case "Sensors":
                        Toast.makeText(MainActivity.this, "Not Implemented", Toast.LENGTH_SHORT).show();
                        break;
                    case "Gesture":
                        Toast.makeText(MainActivity.this, "Not Implemented", Toast.LENGTH_SHORT).show();
                        break;
                    case "NFC":
                        Toast.makeText(MainActivity.this, "Not Implemented", Toast.LENGTH_SHORT).show();
                        break;
                    case "USB":
                        Toast.makeText(MainActivity.this, "Not Implemented", Toast.LENGTH_SHORT).show();
                        break;
                }
                return false;
            }
        });


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        //Input Drawer
        result = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withDelayOnDrawerClose(150000000)
                .withFireOnInitialOnClick(false)
                .withHeader(R.layout.nav_header_main)
                .addDrawerItems(
                        new ExpandableDrawerItem().withName("Camera").withIcon(R.drawable.ic_photo_camera).withSubItems(
                                new SecondaryDrawerItem().withName("Photo").withIcon(R.drawable.ic_photo_camera).withIdentifier(11).withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                                    @Override
                                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                                        currentInput = "Photo";
                                        resultAppended.removeItems(3, 4);
                                        openOutput();
                                        return false;
                                    }
                                }),
                                new SecondaryDrawerItem().withName("Video").withIcon(R.drawable.ic_videocam).withIdentifier(12).withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                                    @Override
                                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                                        currentInput = "Video";
                                        resultAppended.removeItems(3, 4);
                                        openOutput();
                                        return false;
                                    }
                                })
                        ),
                        new PrimaryDrawerItem().withName("Microphone").withIcon(R.drawable.ic_mic).withIdentifier(13).withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                            @Override
                            public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                                currentInput = "Microphone";
                                resultAppended.removeItem(4);
                                openOutput();
                                return false;
                            }
                        }),
                        new PrimaryDrawerItem().withName("Bluetooth").withIcon(R.drawable.ic_bluetooth).withIdentifier(14).withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                            @Override
                            public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                                currentInput = "Bluetooth";
                                resultAppended.removeItems(1, 4, 5);
                                openOutput();
                                return false;
                            }
                        }),
                        new PrimaryDrawerItem().withName("WiFi").withIcon(R.drawable.ic_wifi).withIdentifier(15).withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                            @Override
                            public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                                currentInput = "WiFi";
                                resultAppended.removeItems(2, 4, 5);
                                openOutput();
                                return false;
                            }
                        }),
                        new ExpandableDrawerItem().withName("Sensors").withIcon(R.drawable.ic_sensor).withSubItems(
                                new SecondaryDrawerItem().withName("Accelerometer").withIcon(R.drawable.ic_accelerometer).withIdentifier(16).withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                                    @Override
                                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                                        currentInput = "Accelerometer";
                                        resultAppended.removeItem(3);
                                        openOutput();
                                        return false;
                                    }
                                }),
                                new SecondaryDrawerItem().withName("Gyroscope").withIcon(R.drawable.ic_gyroscope).withIdentifier(17).withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                                    @Override
                                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                                        currentInput = "Gyroscope";
                                        resultAppended.removeItem(3);
                                        openOutput();
                                        return false;
                                    }
                                }),
                                new SecondaryDrawerItem().withName("Magnetometer").withIcon(R.drawable.ic_magneto).withIdentifier(18).withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                                    @Override
                                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                                        currentInput = "Magnetometer";
                                        resultAppended.removeItem(3);
                                        openOutput();
                                        return false;
                                    }
                                }),
                                new SecondaryDrawerItem().withName("Misc Sensors").withIcon(R.drawable.ic_magneto).withIdentifier(19).withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                                    @Override
                                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                                        currentInput = "Misc Sensors";
                                        resultAppended.removeItem(3);
                                        openOutput();
                                        return false;
                                    }
                                })
                        ),
                        new PrimaryDrawerItem().withName("Gesture").withIcon(R.drawable.ic_touch).withIdentifier(20).withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                            @Override
                            public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                                currentInput = "Gesture";
                                resultAppended.removeItems(3, 4);
                                openOutput();
                                return false;
                            }
                        }),
                        new PrimaryDrawerItem().withName("NFC").withIcon(R.drawable.ic_nfc).withIdentifier(21).withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                            @Override
                            public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                                currentInput = "NFC";
                                resultAppended.removeItems(1, 2, 3, 5);
                                openOutput();
                                return false;
                            }
                        }),
                        new PrimaryDrawerItem().withName("USB").withIcon(R.drawable.ic_usb).withIdentifier(22).withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                            @Override
                            public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                                currentInput = "USB";
                                resultAppended.removeItems(1, 2, 3, 4);
                                openOutput();
                                return false;
                            }
                        })
                ).build();

        //Output Drawer
        resultAppended = new DrawerBuilder()
                .withActivity(this)
                .withHeader(R.layout.nav_header_main2)
                .addDrawerItems(wifiItem, btItem, speakerItem, nfcItem, usbItem, screenItem, shareItem)
                .withDrawerGravity(Gravity.END)
                .append(result);

    }



    public static void increaseSwipeEdgeOfDrawer(DrawerLayout drawerLayoutDrawer) {
        try {

            Field mDragger = drawerLayoutDrawer.getClass().getDeclaredField("mLeftDragger");//mRightDragger or mLeftDragger based on Drawer Gravity
            mDragger.setAccessible(true);
            ViewDragHelper draggerObj = (ViewDragHelper) mDragger.get(drawerLayoutDrawer);

            Field mEdgeSize = draggerObj.getClass().getDeclaredField("mEdgeSize");
            mEdgeSize.setAccessible(true);
            int edge = mEdgeSize.getInt(draggerObj);

            mEdgeSize.setInt(draggerObj, 1080);

            Field mDragger2 = drawerLayoutDrawer.getClass().getDeclaredField("mRightDragger");//mRightDragger or mLeftDragger based on Drawer Gravity
            mDragger2.setAccessible(true);
            ViewDragHelper draggerObj2 = (ViewDragHelper) mDragger2.get(drawerLayoutDrawer);

            Field mEdgeSize2 = draggerObj2.getClass().getDeclaredField("mEdgeSize");
            mEdgeSize2.setAccessible(true);
            int edge2 = mEdgeSize2.getInt(draggerObj2);

            mEdgeSize2.setInt(draggerObj2, -500);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onResume() {
        super.onResume();
        setRightItemsVisible();
        setLeftItemsInvisible();
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

        //Hides unnecessary output options with setVisible when choosing input

        switch (id) {
            case R.id.nav_camera:
                currentInput = "Camera";
                navright_Menu.findItem(R.id.nav_speaker).setVisible(false);
                navright_Menu.findItem(R.id.nav_nfc).setVisible(false);
                openOutput();
                break;
            case R.id.nav_microphone:
                currentInput = "Microphone";
                navright_Menu.findItem(R.id.nav_nfc).setVisible(false);
                openOutput();
                break;
            case R.id.nav_bluetooth:
                currentInput = "Bluetooth";
                navright_Menu.findItem(R.id.nav_nfc).setVisible(false);
                navright_Menu.findItem(R.id.nav_wifi2).setVisible(false);
                navright_Menu.findItem(R.id.nav_usb).setVisible(false);
                openOutput();
                break;
            case R.id.nav_wifi:
                currentInput = "WiFi";
                navright_Menu.findItem(R.id.nav_nfc).setVisible(false);
                navright_Menu.findItem(R.id.nav_bluetooth2).setVisible(false);
                navright_Menu.findItem(R.id.nav_usb).setVisible(false);
                openOutput();
                break;
            case R.id.nav_sensor:
                currentInput = "Sensors";
                navright_Menu.findItem(R.id.nav_speaker).setVisible(false);
                openOutput();
                break;
            case R.id.nav_gesture:
                currentInput = "Gesture";
                navright_Menu.findItem(R.id.nav_nfc).setVisible(false);
                navright_Menu.findItem(R.id.nav_speaker).setVisible(false);
                openOutput();
                break;
            case R.id.nav_nfc:
                currentInput = "NFC";
                navright_Menu.findItem(R.id.nav_speaker).setVisible(false);
                navright_Menu.findItem(R.id.nav_wifi2).setVisible(false);
                navright_Menu.findItem(R.id.nav_usb).setVisible(false);
                navright_Menu.findItem(R.id.nav_bluetooth2).setVisible(false);
                openOutput();
                break;
            case R.id.nav_usb:
                currentInput = "USB";
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

                AlertDialog.Builder inputOutput = new AlertDialog.Builder(MainActivity.this);


                int id = item.getItemId();

                if (fragment != null) {
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction ft = fragmentManager.beginTransaction();
                    ft.replace(R.id.content_frame, fragment);
                    ft.commit();
                }

                switch (id) {
                    case R.id.nav_wifi2:
                        currentOutput = "WiFi";
                        switch (currentInput) {
                            case "Camera":
                                Toast.makeText(MainActivity.this, "Not Implemented", Toast.LENGTH_SHORT).show();
                                break;
                            case "Microphone":
                                Toast.makeText(MainActivity.this, "Not Implemented", Toast.LENGTH_SHORT).show();
                                break;
                            case "Bluetooth":
                                Toast.makeText(MainActivity.this, "Not Implemented", Toast.LENGTH_SHORT).show();
                                break;
                            case "WiFi":
                                Toast.makeText(MainActivity.this, "Not Implemented", Toast.LENGTH_SHORT).show();
                                break;
                            case "Sensors":
                                Toast.makeText(MainActivity.this, "Not Implemented", Toast.LENGTH_SHORT).show();
                                break;
                            case "Gesture":
                                Toast.makeText(MainActivity.this, "Not Implemented", Toast.LENGTH_SHORT).show();
                                break;
                        }
                        break;
                    case R.id.nav_bluetooth2:
                        currentOutput = "Bluetooth";
                        switch (currentInput) {
                            case "Camera":
                                Toast.makeText(MainActivity.this, "Not Implemented", Toast.LENGTH_SHORT).show();
                                break;
                            case "Microphone":
                                Toast.makeText(MainActivity.this, "Not Implemented", Toast.LENGTH_SHORT).show();
                                break;
                            case "Bluetooth":
                                Toast.makeText(MainActivity.this, "Not Implemented", Toast.LENGTH_SHORT).show();
                                break;
                            case "Sensors":
                                Toast.makeText(MainActivity.this, "Not Implemented", Toast.LENGTH_SHORT).show();
                                break;
                            case "Gesture":
                                Toast.makeText(MainActivity.this, "Not Implemented", Toast.LENGTH_SHORT).show();
                                break;
                            case "USB":
                                Toast.makeText(MainActivity.this, "Not Implemented", Toast.LENGTH_SHORT).show();
                                break;
                        }
                        break;
                    case R.id.nav_speaker:
                        currentOutput = "Speaker";
                        switch (currentInput) {
                            case "Microphone": {
                                inputOutput.setMessage("Do you want to use " + currentInput + " to " + currentOutput + "?").setCancelable(false)
                                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                fragment = new MicrophoneFragment();
                                                FragmentManager fragmentManager = getSupportFragmentManager();
                                                FragmentTransaction ft = fragmentManager.beginTransaction();
                                                ft.replace(R.id.content_frame, fragment);
                                                ft.commit();
                                            }
                                        })
                                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                dialogInterface.cancel();
                                            }
                                        });
                                AlertDialog alertDialog = inputOutput.create();
                                alertDialog.setTitle("Are you sure?");
                                alertDialog.show();
                                break;
                            }
                            case "Bluetooth": {
                                inputOutput.setMessage("Do you want to use " + currentInput + " to " + currentOutput + "?").setCancelable(false)
                                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                fragment = new BluetoothSpeakerFragment();
                                                FragmentManager fragmentManager = getSupportFragmentManager();
                                                FragmentTransaction ft = fragmentManager.beginTransaction();
                                                ft.replace(R.id.content_frame, fragment);
                                                ft.commit();
                                            }
                                        })
                                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                dialogInterface.cancel();
                                            }
                                        });
                                AlertDialog alertDialog = inputOutput.create();
                                alertDialog.setTitle("Are you sure?");
                                alertDialog.show();
                                break;
                            }
                            case "WiFi":
                                Toast.makeText(MainActivity.this, "Not Implemented", Toast.LENGTH_SHORT).show();
                                break;
                        }
                        break;
                    case R.id.nav_nfc:
                        currentOutput = "NFC";
                        switch (currentInput) {
                            case "Camera":
                                Toast.makeText(MainActivity.this, "Not Implemented", Toast.LENGTH_SHORT).show();
                                break;
                            case "Sensors":
                                Toast.makeText(MainActivity.this, "Not Implemented", Toast.LENGTH_SHORT).show();
                                break;
                            case "NFC":
                                Toast.makeText(MainActivity.this, "Not Implemented", Toast.LENGTH_SHORT).show();
                                break;
                        }
                        break;
                    case R.id.nav_usb:
                        currentOutput = "USB";
                        switch (currentInput) {
                            case "Camera":
                                Toast.makeText(MainActivity.this, "Not Implemented", Toast.LENGTH_SHORT).show();
                                break;
                            case "Microphone":
                                Toast.makeText(MainActivity.this, "Not Implemented", Toast.LENGTH_SHORT).show();
                                break;
                            case "Sensors":
                                Toast.makeText(MainActivity.this, "Not Implemented", Toast.LENGTH_SHORT).show();
                                break;
                            case "Gesture":
                                Toast.makeText(MainActivity.this, "Not Implemented", Toast.LENGTH_SHORT).show();
                                break;
                            case "USB":
                                Toast.makeText(MainActivity.this, "Not Implemented", Toast.LENGTH_SHORT).show();
                                break;
                        }
                        break;
                    case R.id.nav_screen:
                        currentOutput = "Screen";
                        switch (currentInput) {
                            case "Camera": {
                                inputOutput.setMessage("Do you want to use " + currentInput + " to " + currentOutput + "?").setCancelable(false)
                                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                fragment = new PhotoFragment();
                                                FragmentManager fragmentManager = getSupportFragmentManager();
                                                FragmentTransaction ft = fragmentManager.beginTransaction();
                                                ft.replace(R.id.content_frame, fragment);
                                                ft.commit();
                                            }
                                        })
                                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                dialogInterface.cancel();
                                            }
                                        });
                                AlertDialog alertDialog = inputOutput.create();
                                alertDialog.setTitle("Are you sure?");
                                alertDialog.show();
                                break;
                            }
                            case "Microphone":
                                Toast.makeText(MainActivity.this, "Not Implemented", Toast.LENGTH_SHORT).show();
                                break;
                            case "Bluetooth": {
                                inputOutput.setMessage("Do you want to use " + currentInput + " to " + currentOutput + "?").setCancelable(false)
                                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                fragment = new BluetoothFragment();
                                                FragmentManager fragmentManager = getSupportFragmentManager();
                                                FragmentTransaction ft = fragmentManager.beginTransaction();
                                                ft.replace(R.id.content_frame, fragment);
                                                ft.commit();
                                            }
                                        })
                                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                dialogInterface.cancel();
                                            }
                                        });
                                AlertDialog alertDialog = inputOutput.create();
                                alertDialog.setTitle("Are you sure?");
                                alertDialog.show();
                                break;
                            }
                            case "WiFi": {
                                inputOutput.setMessage("Do you want to use " + currentInput + " to " + currentOutput + "?").setCancelable(false)
                                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                fragment = new WifiP2pFragment();
                                                FragmentManager fragmentManager = getSupportFragmentManager();
                                                FragmentTransaction ft = fragmentManager.beginTransaction();
                                                ft.replace(R.id.content_frame, fragment);
                                                ft.commit();
                                            }
                                        })
                                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                dialogInterface.cancel();
                                            }
                                        });
                                AlertDialog alertDialog = inputOutput.create();
                                alertDialog.setTitle("Are you sure?");
                                alertDialog.show();
                                break;
                            }
                            case "Sensors": {
                                inputOutput.setMessage("Do you want to use " + currentInput + " to " + currentOutput + "?").setCancelable(false)
                                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                fragment = new AccelerometerFragment();
                                                FragmentManager fragmentManager = getSupportFragmentManager();
                                                FragmentTransaction ft = fragmentManager.beginTransaction();
                                                ft.replace(R.id.content_frame, fragment);
                                                ft.commit();
                                            }
                                        })
                                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                dialogInterface.cancel();
                                            }
                                        });
                                AlertDialog alertDialog = inputOutput.create();
                                alertDialog.setTitle("Are you sure?");
                                alertDialog.show();
                                break;
                            }
                            case "Gesture": {
                                inputOutput.setMessage("Do you want to use " + currentInput + " to " + currentOutput + "?").setCancelable(false)
                                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                fragment = new GestureFragment();
                                                FragmentManager fragmentManager = getSupportFragmentManager();
                                                FragmentTransaction ft = fragmentManager.beginTransaction();
                                                ft.replace(R.id.content_frame, fragment);
                                                ft.commit();
                                            }
                                        })
                                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                dialogInterface.cancel();
                                            }
                                        });
                                AlertDialog alertDialog = inputOutput.create();
                                alertDialog.setTitle("Are you sure?");
                                alertDialog.show();
                                break;
                            }
                            case "NFC":
                                Intent intent = new Intent(MainActivity.this, NfcFragment.class);
                                startActivity(intent);
                                break;
                            case "USB": {
                                inputOutput.setMessage("Do you want to use " + currentInput + " to " + currentOutput + "?").setCancelable(false)
                                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                fragment = new UsbFragment();
                                                FragmentManager fragmentManager = getSupportFragmentManager();
                                                FragmentTransaction ft = fragmentManager.beginTransaction();
                                                ft.replace(R.id.content_frame, fragment);
                                                ft.commit();
                                            }
                                        })
                                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                dialogInterface.cancel();
                                            }
                                        });
                                AlertDialog alertDialog = inputOutput.create();
                                alertDialog.setTitle("Are you sure?");
                                alertDialog.show();
                                break;
                            }
                        }
                        break;
                    case R.id.nav_share:
                        currentOutput = "Share";
                        switch (currentInput) {
                            case "Camera":
                                Toast.makeText(MainActivity.this, "Not Implemented", Toast.LENGTH_SHORT).show();
                                break;
                            case "Microphone":
                                Toast.makeText(MainActivity.this, "Not Implemented", Toast.LENGTH_SHORT).show();
                                break;
                            case "Bluetooth":
                                Toast.makeText(MainActivity.this, "Not Implemented", Toast.LENGTH_SHORT).show();
                                break;
                            case "WiFi":
                                Toast.makeText(MainActivity.this, "Not Implemented", Toast.LENGTH_SHORT).show();
                                break;
                            case "Sensors":
                                Toast.makeText(MainActivity.this, "Not Implemented", Toast.LENGTH_SHORT).show();
                                break;
                            case "Gesture":
                                Toast.makeText(MainActivity.this, "Not Implemented", Toast.LENGTH_SHORT).show();
                                break;
                            case "NFC":
                                Toast.makeText(MainActivity.this, "Not Implemented", Toast.LENGTH_SHORT).show();
                                break;
                            case "USB":
                                Toast.makeText(MainActivity.this, "Not Implemented", Toast.LENGTH_SHORT).show();
                                break;
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

    //Method that opens output automatically whenever input is chosen
    private void openOutput() {

        if (result.isDrawerOpen()) {
            result.closeDrawer();
        }

        if (!resultAppended.isDrawerOpen()) {
            resultAppended.openDrawer();
        }

        /*
        if (!drawerLayout.isDrawerOpen(GravityCompat.END)) {
            drawerLayout.openDrawer(GravityCompat.END);
        }
        */
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

        setLeftItemsInvisible();
    }


    //Method that checks if user has certain function in the phone and disables availability if not
    private void setLeftItemsInvisible() {
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
            resultAppended.removeItem(15);
        }
        if (!hasNfc()) {
            navleft_Menu.findItem(R.id.nav_nfc).setVisible(false);
            resultAppended.removeItem(21);
        }
    }


    //Method that resets unnecessary hidden outputs for next input
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

}
