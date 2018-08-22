package com.example.omistaja.universal_io;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Toast;

import com.example.omistaja.universal_io.fragments.AccelerometerFragment;
import com.example.omistaja.universal_io.fragments.BluetoothFragment;
import com.example.omistaja.universal_io.fragments.BluetoothSpeakerFragment;
import com.example.omistaja.universal_io.fragments.GestureFragment;
import com.example.omistaja.universal_io.fragments.GyroscopeFragment;
import com.example.omistaja.universal_io.fragments.LHPmeterFragment;
import com.example.omistaja.universal_io.fragments.MagnometerFragment;
import com.example.omistaja.universal_io.fragments.MicrophoneFragment;
import com.example.omistaja.universal_io.fragments.NfcFragment;
import com.example.omistaja.universal_io.fragments.PhotoFragment;
import com.example.omistaja.universal_io.fragments.UsbFragment;
import com.example.omistaja.universal_io.fragments.VideoFragment;
import com.example.omistaja.universal_io.fragments.WifiApFragment;
import com.example.omistaja.universal_io.fragments.WifiP2pFragment;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.holder.ImageHolder;
import com.mikepenz.materialdrawer.model.ExpandableDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import java.lang.reflect.Field;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.customview.widget.ViewDragHelper;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class DrawerHelper {

    private Context _context;
    private Fragment fragment = null;
    Drawer inputDrawer = null;
    Drawer outputDrawer = null;
    private PrimaryDrawerItem wifiOutput, btOutput, nfcOutput, usbOutput, screenOutput, shareOutput, speakerOutput, micInput, btInput, touchInput, nfcInput, usbInput;
    private SecondaryDrawerItem photoInput, videoInput, acceleroInput, gyroscopeInput, magnometerInput, miscsensorInput, wifip2pInput, wifiapInput;
    private String currentInput, currentOutput;
    private FragmentActivity fragAct;
    private static final String CAMERA = "Camera";
    private static final String PHOTO = "Photo";
    private static final String VIDEO = "Video";
    private static final String MICROPHONE = "Microphone";
    private static final String BLUETOOTH = "Bluetooth";
    private static final String WIFI = "WiFi";
    private static final String SENSORS = "Sensors";
    private static final String MISCSENSOR = "Misc Sensors";
    private static final String MAGNETO = "Magnetometer";
    private static final String ACCELERO = "Accelerometer";
    private static final String GYROSCOPE = "Gyroscope";
    private static final String WIFIAP = "WiFi AP";
    private static final String WIFIP2P = "WiFi P2P";
    private static final String GESTURE = "Gesture";
    private static final String USB = "USB";
    private static final String NFC = "NFC";
    private static final String SPEAKER = "Speaker";
    private static final String SCREEN = "Screen";

    public DrawerHelper(Context context) {
        this._context = context;
    }

    public void initDrawersDark(Toolbar toolbar, Bundle savedInstanceState) {

        int[] r = {1,2,3,4,5,6,7};

        //Builds confirmation dialog for user on input and output
        AlertDialog.Builder inputOutput = new AlertDialog.Builder(_context);

        fragAct = (AppCompatActivity)this._context;

        //Output drawer contents
        wifiOutput = new PrimaryDrawerItem().withName(WIFI).withIcon(R.drawable.ic_wifi_white).withTextColor(Color.WHITE).withIdentifier(1).withOnDrawerItemClickListener((view, position, drawerItem) -> {
            currentOutput = WIFI;
            switch (currentInput) {
                case CAMERA:
                    Toast.makeText(_context, "Not Implemented", Toast.LENGTH_SHORT).show();
                    break;
                case MICROPHONE:
                    Toast.makeText(_context, "Not Implemented", Toast.LENGTH_SHORT).show();
                    break;
                case BLUETOOTH:
                    Toast.makeText(_context, "Not Implemented", Toast.LENGTH_SHORT).show();
                    break;
                case WIFI:
                    Toast.makeText(_context, "Not Implemented", Toast.LENGTH_SHORT).show();
                    break;
                case SENSORS:
                    Toast.makeText(_context, "Not Implemented", Toast.LENGTH_SHORT).show();
                    break;
                case GESTURE:
                    Toast.makeText(_context, "Not Implemented", Toast.LENGTH_SHORT).show();
                    break;
            }
            return false;
        });
        btOutput = new PrimaryDrawerItem().withName(BLUETOOTH).withIcon(R.drawable.ic_bluetooth_white).withTextColor(Color.WHITE).withIdentifier(2).withOnDrawerItemClickListener((view, position, drawerItem) -> {
            currentOutput = BLUETOOTH;
            switch (currentInput) {
                case CAMERA:
                    Toast.makeText(_context, "Not Implemented", Toast.LENGTH_SHORT).show();
                    break;
                case MICROPHONE:
                    Toast.makeText(_context, "Not Implemented", Toast.LENGTH_SHORT).show();
                    break;
                case BLUETOOTH:
                    Toast.makeText(_context, "Not Implemented", Toast.LENGTH_SHORT).show();
                    break;
                case SENSORS:
                    Toast.makeText(_context, "Not Implemented", Toast.LENGTH_SHORT).show();
                    break;
                case GESTURE:
                    Toast.makeText(_context, "Not Implemented", Toast.LENGTH_SHORT).show();
                    break;
                case USB:
                    Toast.makeText(_context, "Not Implemented", Toast.LENGTH_SHORT).show();
                    break;
            }
            return false;
        });
        speakerOutput = new PrimaryDrawerItem().withName(SPEAKER).withIcon(R.drawable.ic_speaker_white).withTextColor(Color.WHITE).withIdentifier(3).withOnDrawerItemClickListener((view, position, drawerItem) -> {
            currentOutput = SPEAKER;
            switch (currentInput) {
                case MICROPHONE: {
                    inputOutput.setMessage(String.format("Do you want to use %s to %s ?", currentInput, currentOutput)).setCancelable(false)
                            .setPositiveButton("Yes", (dialogInterface, i) -> {
                                fragment = new MicrophoneFragment();
                                FragmentManager fragmentManager = fragAct.getSupportFragmentManager();
                                FragmentTransaction ft = fragmentManager.beginTransaction();
                                ft.replace(R.id.content_frame, fragment);
                                ft.commit();
                            })
                            .setNegativeButton("No", (dialogInterface, i) -> dialogInterface.cancel());
                    AlertDialog alertDialog = inputOutput.create();
                    alertDialog.setTitle("Are you sure?");
                    alertDialog.show();
                    break;
                }
                case BLUETOOTH: {
                    inputOutput.setMessage(String.format("Do you want to use %s to %s ?", currentInput, currentOutput)).setCancelable(false)
                            .setPositiveButton("Yes", (dialogInterface, i) -> {
                                fragment = new BluetoothSpeakerFragment();
                                FragmentManager fragmentManager = fragAct.getSupportFragmentManager();
                                FragmentTransaction ft = fragmentManager.beginTransaction();
                                ft.replace(R.id.content_frame, fragment);
                                ft.commit();
                            })
                            .setNegativeButton("No", (dialogInterface, i) -> dialogInterface.cancel());
                    AlertDialog alertDialog = inputOutput.create();
                    alertDialog.setTitle("Are you sure?");
                    alertDialog.show();
                    break;
                }
                case WIFI:
                    Toast.makeText(_context, "Not Implemented", Toast.LENGTH_SHORT).show();
                    break;
            }
            return false;
        });
        nfcOutput = new PrimaryDrawerItem().withName(NFC).withIcon(R.drawable.ic_nfc_white).withTextColor(Color.WHITE).withIdentifier(4).withOnDrawerItemClickListener((view, position, drawerItem) -> {
            currentOutput = NFC;
            switch (currentInput) {
                case CAMERA:
                    Toast.makeText(_context, "Not Implemented", Toast.LENGTH_SHORT).show();
                    break;
                case SENSORS:
                    Toast.makeText(_context, "Not Implemented", Toast.LENGTH_SHORT).show();
                    break;
                case NFC:
                    Toast.makeText(_context, "Not Implemented", Toast.LENGTH_SHORT).show();
                    break;
            }
            return false;
        });
        usbOutput = new PrimaryDrawerItem().withName(USB).withIcon(R.drawable.ic_usb_white).withTextColor(Color.WHITE).withIdentifier(5).withOnDrawerItemClickListener((view, position, drawerItem) -> {
            currentOutput = USB;
            switch (currentInput) {
                case CAMERA:
                    Toast.makeText(_context, "Not Implemented", Toast.LENGTH_SHORT).show();
                    break;
                case MICROPHONE:
                    Toast.makeText(_context, "Not Implemented", Toast.LENGTH_SHORT).show();
                    break;
                case SENSORS:
                    Toast.makeText(_context, "Not Implemented", Toast.LENGTH_SHORT).show();
                    break;
                case GESTURE:
                    Toast.makeText(_context, "Not Implemented", Toast.LENGTH_SHORT).show();
                    break;
                case USB:
                    Toast.makeText(_context, "Not Implemented", Toast.LENGTH_SHORT).show();
                    break;
            }
            return false;
        });
        screenOutput = new PrimaryDrawerItem().withName(SCREEN).withIcon(R.drawable.ic_smartphone_white).withTextColor(Color.WHITE).withIdentifier(6).withOnDrawerItemClickListener((view, position, drawerItem) -> {
            currentOutput = SCREEN;
            switch (currentInput) {
                case PHOTO: {
                    inputOutput.setMessage(String.format("Do you want to use %s to %s ?", currentInput, currentOutput)).setCancelable(false)
                            .setPositiveButton("Yes", (dialogInterface, i) -> {
                                fragment = new PhotoFragment();
                                FragmentManager fragmentManager = fragAct.getSupportFragmentManager();
                                FragmentTransaction ft = fragmentManager.beginTransaction();
                                ft.replace(R.id.content_frame, fragment);
                                ft.commit();
                            })
                            .setNegativeButton("No", (dialogInterface, i) -> dialogInterface.cancel());
                    AlertDialog alertDialog = inputOutput.create();
                    alertDialog.setTitle("Are you sure?");
                    alertDialog.show();
                    break;
                }
                case VIDEO: {
                    inputOutput.setMessage(String.format("Do you want to use %s to %s ?", currentInput, currentOutput)).setCancelable(false)
                            .setPositiveButton("Yes", (dialogInterface, i) -> {
                                fragment = new VideoFragment();
                                FragmentManager fragmentManager = fragAct.getSupportFragmentManager();
                                FragmentTransaction ft = fragmentManager.beginTransaction();
                                ft.replace(R.id.content_frame, fragment);
                                ft.commit();
                            })
                            .setNegativeButton("No", (dialogInterface, i) -> dialogInterface.cancel());
                    AlertDialog alertDialog = inputOutput.create();
                    alertDialog.setTitle("Are you sure?");
                    alertDialog.show();
                    break;
                }
                case MICROPHONE:
                    Toast.makeText(_context, "Not Implemented", Toast.LENGTH_SHORT).show();
                    break;
                case BLUETOOTH: {
                    inputOutput.setMessage(String.format("Do you want to use %s to %s ?", currentInput, currentOutput)).setCancelable(false)
                            .setPositiveButton("Yes", (dialogInterface, i) -> {
                                fragment = new BluetoothFragment();
                                FragmentManager fragmentManager = fragAct.getSupportFragmentManager();
                                FragmentTransaction ft = fragmentManager.beginTransaction();
                                ft.replace(R.id.content_frame, fragment);
                                ft.commit();
                            })
                            .setNegativeButton("No", (dialogInterface, i) -> dialogInterface.cancel());
                    AlertDialog alertDialog = inputOutput.create();
                    alertDialog.setTitle("Are you sure?");
                    alertDialog.show();
                    break;
                }
                case WIFIP2P: {
                    inputOutput.setMessage(String.format("Do you want to use %s to %s ?", currentInput, currentOutput)).setCancelable(false)
                            .setPositiveButton("Yes", (dialogInterface, i) -> {
                                fragment = new WifiP2pFragment();
                                FragmentManager fragmentManager = fragAct.getSupportFragmentManager();
                                FragmentTransaction ft = fragmentManager.beginTransaction();
                                ft.replace(R.id.content_frame, fragment);
                                ft.commit();
                            })
                            .setNegativeButton("No", (dialogInterface, i) -> dialogInterface.cancel());
                    AlertDialog alertDialog = inputOutput.create();
                    alertDialog.setTitle("Are you sure?");
                    alertDialog.show();
                    break;
                }
                case WIFIAP: {
                    inputOutput.setMessage(String.format("Do you want to use %s to %s ?", currentInput, currentOutput)).setCancelable(false)
                            .setPositiveButton("Yes", (dialogInterface, i) -> {
                                fragment = new WifiApFragment();
                                FragmentManager fragmentManager = fragAct.getSupportFragmentManager();
                                FragmentTransaction ft = fragmentManager.beginTransaction();
                                ft.replace(R.id.content_frame, fragment);
                                ft.commit();
                            })
                            .setNegativeButton("No", (dialogInterface, i) -> dialogInterface.cancel());
                    AlertDialog alertDialog = inputOutput.create();
                    alertDialog.setTitle("Are you sure?");
                    alertDialog.show();
                    break;
                }
                case ACCELERO: {
                    inputOutput.setMessage(String.format("Do you want to use %s to %s ?", currentInput, currentOutput)).setCancelable(false)
                            .setPositiveButton("Yes", (dialogInterface, i) -> {
                                fragment = new AccelerometerFragment();
                                FragmentManager fragmentManager = fragAct.getSupportFragmentManager();
                                FragmentTransaction ft = fragmentManager.beginTransaction();
                                ft.replace(R.id.content_frame, fragment);
                                ft.commit();
                            })
                            .setNegativeButton("No", (dialogInterface, i) -> dialogInterface.cancel());
                    AlertDialog alertDialog = inputOutput.create();
                    alertDialog.setTitle("Are you sure?");
                    alertDialog.show();
                    break;
                }
                case GYROSCOPE: {
                    inputOutput.setMessage(String.format("Do you want to use %s to %s ?", currentInput, currentOutput)).setCancelable(false)
                            .setPositiveButton("Yes", (dialogInterface, i) -> {
                                fragment = new GyroscopeFragment();
                                FragmentManager fragmentManager = fragAct.getSupportFragmentManager();
                                FragmentTransaction ft = fragmentManager.beginTransaction();
                                ft.replace(R.id.content_frame, fragment);
                                ft.commit();
                            })
                            .setNegativeButton("No", (dialogInterface, i) -> dialogInterface.cancel());
                    AlertDialog alertDialog = inputOutput.create();
                    alertDialog.setTitle("Are you sure?");
                    alertDialog.show();
                    break;
                }
                case MAGNETO: {
                    inputOutput.setMessage(String.format("Do you want to use %s to %s ?", currentInput, currentOutput)).setCancelable(false)
                            .setPositiveButton("Yes", (dialogInterface, i) -> {
                                fragment = new MagnometerFragment();
                                FragmentManager fragmentManager = fragAct.getSupportFragmentManager();
                                FragmentTransaction ft = fragmentManager.beginTransaction();
                                ft.replace(R.id.content_frame, fragment);
                                ft.commit();
                            })
                            .setNegativeButton("No", (dialogInterface, i) -> dialogInterface.cancel());
                    AlertDialog alertDialog = inputOutput.create();
                    alertDialog.setTitle("Are you sure?");
                    alertDialog.show();
                    break;
                }

                case GESTURE: {
                    inputOutput.setMessage(String.format("Do you want to use %s to %s ?", currentInput, currentOutput)).setCancelable(false)
                            .setPositiveButton("Yes", (dialogInterface, i) -> {
                                fragment = new GestureFragment();
                                FragmentManager fragmentManager = fragAct.getSupportFragmentManager();
                                FragmentTransaction ft = fragmentManager.beginTransaction();
                                ft.replace(R.id.content_frame, fragment);
                                ft.commit();
                            })
                            .setNegativeButton("No", (dialogInterface, i) -> dialogInterface.cancel());
                    AlertDialog alertDialog = inputOutput.create();
                    alertDialog.setTitle("Are you sure?");
                    alertDialog.show();
                    break;
                }
                case MISCSENSOR: {
                    inputOutput.setMessage(String.format("Do you want to use %s to %s ?", currentInput, currentOutput)).setCancelable(false)
                            .setPositiveButton("Yes", (dialogInterface, i) -> {
                                fragment = new LHPmeterFragment();
                                FragmentManager fragmentManager = fragAct.getSupportFragmentManager();
                                FragmentTransaction ft = fragmentManager.beginTransaction();
                                ft.replace(R.id.content_frame, fragment);
                                ft.commit();
                            })
                            .setNegativeButton("No", (dialogInterface, i) -> dialogInterface.cancel());
                    AlertDialog alertDialog = inputOutput.create();
                    alertDialog.setTitle("Are you sure?");
                    alertDialog.show();
                    break;
                }
                case NFC: {
                    inputOutput.setMessage(String.format("Do you want to use %s to %s ?", currentInput, currentOutput)).setCancelable(false)
                            .setPositiveButton("Yes", (dialogInterface, i) -> {
                                fragment = new NfcFragment();
                                FragmentManager fragmentManager = fragAct.getSupportFragmentManager();
                                FragmentTransaction ft = fragmentManager.beginTransaction();
                                ft.replace(R.id.content_frame, fragment);
                                ft.commit();
                            })
                            .setNegativeButton("No", (dialogInterface, i) -> dialogInterface.cancel());
                    AlertDialog alertDialog = inputOutput.create();
                    alertDialog.setTitle("Are you sure?");
                    alertDialog.show();
                    break;
                }
                case USB: {
                    inputOutput.setMessage(String.format("Do you want to use %s to %s ?", currentInput, currentOutput)).setCancelable(false)
                            .setPositiveButton("Yes", (dialogInterface, i) -> {
                                fragment = new UsbFragment();
                                FragmentManager fragmentManager = fragAct.getSupportFragmentManager();
                                FragmentTransaction ft = fragmentManager.beginTransaction();
                                ft.replace(R.id.content_frame, fragment);
                                ft.commit();
                            })
                            .setNegativeButton("No", (dialogInterface, i) -> dialogInterface.cancel());
                    AlertDialog alertDialog = inputOutput.create();
                    alertDialog.setTitle("Are you sure?");
                    alertDialog.show();
                    break;
                }
            }
            return false;
        });
        shareOutput = new PrimaryDrawerItem().withName("Share").withIcon(R.drawable.ic_share_white).withTextColor(Color.WHITE).withIdentifier(7).withOnDrawerItemClickListener((view, position, drawerItem) -> {
            currentOutput = "Share";
            switch (currentInput) {
                case CAMERA:
                    Toast.makeText(_context, "Not Implemented", Toast.LENGTH_SHORT).show();
                    break;
                case MICROPHONE:
                    Toast.makeText(_context, "Not Implemented", Toast.LENGTH_SHORT).show();
                    break;
                case BLUETOOTH:
                    Toast.makeText(_context, "Not Implemented", Toast.LENGTH_SHORT).show();
                    break;
                case WIFI:
                    Toast.makeText(_context, "Not Implemented", Toast.LENGTH_SHORT).show();
                    break;
                case SENSORS:
                    Toast.makeText(_context, "Not Implemented", Toast.LENGTH_SHORT).show();
                    break;
                case GESTURE:
                    Toast.makeText(_context, "Not Implemented", Toast.LENGTH_SHORT).show();
                    break;
                case NFC:
                    Toast.makeText(_context, "Not Implemented", Toast.LENGTH_SHORT).show();
                    break;
                case USB:
                    Toast.makeText(_context, "Not Implemented", Toast.LENGTH_SHORT).show();
                    break;
            }
            return false;
        });

        //Input drawer contents
        photoInput = newSecondaryItemDrawer(PHOTO, 3, R.drawable.ic_photo_camera_white, Color.WHITE, Color.GRAY, 11, r, wifiOutput, btOutput, usbOutput, screenOutput, nfcOutput);
        videoInput = newSecondaryItemDrawer(VIDEO, 3, R.drawable.ic_videocam_white, Color.WHITE, Color.GRAY, 12, r, wifiOutput, btOutput, usbOutput, screenOutput, nfcOutput);
        micInput = newPrimaryItemDrawer(MICROPHONE, R.drawable.ic_mic_white, Color.WHITE, Color.GRAY, 13, r, speakerOutput, wifiOutput, btOutput, usbOutput, screenOutput);
        btInput = newPrimaryItemDrawer(BLUETOOTH, R.drawable.ic_bluetooth_white, Color.WHITE, Color.GRAY, 14, r, btOutput, screenOutput, speakerOutput);
        wifip2pInput = newSecondaryItemDrawer(WIFIP2P, 3, R.drawable.ic_p2p_white, Color.WHITE, Color.GRAY, 15, r, wifiOutput, screenOutput, speakerOutput);
        wifiapInput = newSecondaryItemDrawer(WIFIAP, 3, R.drawable.ic_ap_white, Color.WHITE, Color.GRAY, 16, r, wifiOutput, screenOutput, speakerOutput);
        acceleroInput = newSecondaryItemDrawer(ACCELERO, 3,  R.drawable.ic_accelerometer_white, Color.WHITE, Color.GRAY, 17, r, wifiOutput, btOutput, usbOutput, screenOutput, nfcOutput);
        gyroscopeInput = newSecondaryItemDrawer(GYROSCOPE, 3, R.drawable.ic_gyroscope_white, Color.WHITE, Color.GRAY, 18, r, wifiOutput, btOutput, usbOutput, screenOutput, nfcOutput);
        magnometerInput = newSecondaryItemDrawer(MAGNETO, 3, R.drawable.ic_magneto_white, Color.WHITE, Color.GRAY, 19, r, wifiOutput, btOutput, usbOutput, screenOutput, nfcOutput);
        miscsensorInput = newSecondaryItemDrawer(MISCSENSOR, 3, R.drawable.ic_magneto_white, Color.WHITE, Color.GRAY, 20, r, wifiOutput, btOutput, usbOutput, screenOutput, nfcOutput);
        touchInput = newPrimaryItemDrawer(GESTURE, R.drawable.ic_touch_white, Color.WHITE, Color.GRAY, 21, r, wifiOutput, btOutput, usbOutput, screenOutput);
        nfcInput = newPrimaryItemDrawer(NFC, R.drawable.ic_nfc_white, Color.WHITE, Color.GRAY, 22, r, nfcOutput, screenOutput);
        usbInput = newPrimaryItemDrawer(USB, R.drawable.ic_usb_white, Color.WHITE, Color.GRAY, 23, r, usbOutput, screenOutput);

        //Input Drawer
        inputDrawer = new DrawerBuilder()
                .withActivity((Activity)_context)
                .withSelectedItem(-1)
                .withToolbar(toolbar)
                .withHeader(R.layout.nav_header_main)
                .withHasStableIds(true)
                .addDrawerItems(
                        new ExpandableDrawerItem().withName(CAMERA).withIcon(R.drawable.ic_photo_camera_white).withTextColor(Color.WHITE).withSelectedTextColor(Color.WHITE).withIconColor(Color.WHITE).withSubItems(photoInput, videoInput),
                        micInput,
                        btInput,
                        new ExpandableDrawerItem().withName(WIFI).withIcon(R.drawable.ic_wifi_white).withTextColor(Color.WHITE).withIconColor(Color.WHITE).withSelectedTextColor(Color.WHITE).withSubItems(wifiapInput, wifip2pInput),
                        new ExpandableDrawerItem().withName(SENSORS).withIcon(R.drawable.ic_sensor_white).withTextColor(Color.WHITE).withIconColor(Color.WHITE).withSelectedTextColor(Color.WHITE).withSubItems(acceleroInput, gyroscopeInput, magnometerInput, miscsensorInput),
                        touchInput,
                        nfcInput,
                        usbInput
                ).withSavedInstance(savedInstanceState)
                .build();

/*
        ImageHolder img = new ImageHolder(R.drawable.ic_bluetooth_white);
        inputDrawer.updateIcon(1, img);
*/
        //Output Drawer
        outputDrawer = new DrawerBuilder()
                .withActivity((Activity)_context)
                .withSelectedItem(-1)
                .withHeader(R.layout.nav_header_main2)
                .withHasStableIds(true)
                .withDrawerGravity(Gravity.END)
                .withSavedInstance(savedInstanceState)
                .build();
    }

    public void initDrawersLight(Toolbar toolbar, Bundle savedInstanceState) {
        int[] r = {1,2,3,4,5,6,7};

        //Builds confirmation dialog for user on input and output
        AlertDialog.Builder inputOutput = new AlertDialog.Builder(_context);

        fragAct = (AppCompatActivity)this._context;

        //Output drawer contents
        wifiOutput = new PrimaryDrawerItem().withSelectable(false).withName(WIFI).withIcon(R.drawable.ic_wifi_black).withTextColor(Color.BLACK).withIdentifier(1).withOnDrawerItemClickListener((view, position, drawerItem) -> {
            currentOutput = WIFI;
            switch (currentInput) {
                case CAMERA:
                    Toast.makeText(_context, "Not Implemented", Toast.LENGTH_SHORT).show();
                    break;
                case MICROPHONE:
                    Toast.makeText(_context, "Not Implemented", Toast.LENGTH_SHORT).show();
                    break;
                case BLUETOOTH:
                    Toast.makeText(_context, "Not Implemented", Toast.LENGTH_SHORT).show();
                    break;
                case WIFI:
                    Toast.makeText(_context, "Not Implemented", Toast.LENGTH_SHORT).show();
                    break;
                case SENSORS:
                    Toast.makeText(_context, "Not Implemented", Toast.LENGTH_SHORT).show();
                    break;
                case GESTURE:
                    Toast.makeText(_context, "Not Implemented", Toast.LENGTH_SHORT).show();
                    break;
            }
            return false;
        });
        btOutput = new PrimaryDrawerItem().withSelectable(false).withName(BLUETOOTH).withIcon(R.drawable.ic_bluetooth_black).withTextColor(Color.BLACK).withIdentifier(2).withOnDrawerItemClickListener((view, position, drawerItem) -> {
            currentOutput = BLUETOOTH;
            switch (currentInput) {
                case CAMERA:
                    Toast.makeText(_context, "Not Implemented", Toast.LENGTH_SHORT).show();
                    break;
                case MICROPHONE:
                    Toast.makeText(_context, "Not Implemented", Toast.LENGTH_SHORT).show();
                    break;
                case BLUETOOTH:
                    Toast.makeText(_context, "Not Implemented", Toast.LENGTH_SHORT).show();
                    break;
                case SENSORS:
                    Toast.makeText(_context, "Not Implemented", Toast.LENGTH_SHORT).show();
                    break;
                case GESTURE:
                    Toast.makeText(_context, "Not Implemented", Toast.LENGTH_SHORT).show();
                    break;
                case USB:
                    Toast.makeText(_context, "Not Implemented", Toast.LENGTH_SHORT).show();
                    break;
            }
            return false;
        });
        speakerOutput = new PrimaryDrawerItem().withName(SPEAKER).withIcon(R.drawable.ic_speaker_black).withTextColor(Color.BLACK).withIdentifier(3).withOnDrawerItemClickListener((view, position, drawerItem) -> {
            currentOutput = SPEAKER;
            switch (currentInput) {
                case MICROPHONE: {
                    inputOutput.setMessage(String.format("Do you want to use %s to %s ?", currentInput, currentOutput)).setCancelable(false)
                            .setPositiveButton("Yes", (dialogInterface, i) -> {
                                fragment = new MicrophoneFragment();
                                FragmentManager fragmentManager = fragAct.getSupportFragmentManager();
                                FragmentTransaction ft = fragmentManager.beginTransaction();
                                ft.replace(R.id.content_frame, fragment);
                                ft.commit();
                            })
                            .setNegativeButton("No", (dialogInterface, i) -> dialogInterface.cancel());
                    AlertDialog alertDialog = inputOutput.create();
                    alertDialog.setTitle("Are you sure?");
                    alertDialog.show();
                    break;
                }
                case BLUETOOTH: {
                    inputOutput.setMessage(String.format("Do you want to use %s to %s ?", currentInput, currentOutput)).setCancelable(false)
                            .setPositiveButton("Yes", (dialogInterface, i) -> {
                                fragment = new BluetoothSpeakerFragment();
                                FragmentManager fragmentManager = fragAct.getSupportFragmentManager();
                                FragmentTransaction ft = fragmentManager.beginTransaction();
                                ft.replace(R.id.content_frame, fragment);
                                ft.commit();
                            })
                            .setNegativeButton("No", (dialogInterface, i) -> dialogInterface.cancel());
                    AlertDialog alertDialog = inputOutput.create();
                    alertDialog.setTitle("Are you sure?");
                    alertDialog.show();
                    break;
                }
                case WIFI:
                    Toast.makeText(_context, "Not Implemented", Toast.LENGTH_SHORT).show();
                    break;
            }
            return false;
        });
        nfcOutput = new PrimaryDrawerItem().withSelectable(false).withName(NFC).withIcon(R.drawable.ic_nfc_black).withTextColor(Color.BLACK).withIdentifier(4).withOnDrawerItemClickListener((view, position, drawerItem) -> {
            currentOutput = NFC;
            switch (currentInput) {
                case CAMERA:
                    Toast.makeText(_context, "Not Implemented", Toast.LENGTH_SHORT).show();
                    break;
                case SENSORS:
                    Toast.makeText(_context, "Not Implemented", Toast.LENGTH_SHORT).show();
                    break;
                case NFC:
                    Toast.makeText(_context, "Not Implemented", Toast.LENGTH_SHORT).show();
                    break;
            }
            return false;
        });
        usbOutput = new PrimaryDrawerItem().withSelectable(false).withName(USB).withIcon(R.drawable.ic_usb_black).withTextColor(Color.BLACK).withIdentifier(5).withOnDrawerItemClickListener((view, position, drawerItem) -> {
            currentOutput = USB;
            switch (currentInput) {
                case CAMERA:
                    Toast.makeText(_context, "Not Implemented", Toast.LENGTH_SHORT).show();
                    break;
                case MICROPHONE:
                    Toast.makeText(_context, "Not Implemented", Toast.LENGTH_SHORT).show();
                    break;
                case SENSORS:
                    Toast.makeText(_context, "Not Implemented", Toast.LENGTH_SHORT).show();
                    break;
                case GESTURE:
                    Toast.makeText(_context, "Not Implemented", Toast.LENGTH_SHORT).show();
                    break;
                case USB:
                    Toast.makeText(_context, "Not Implemented", Toast.LENGTH_SHORT).show();
                    break;
            }
            return false;
        });
        screenOutput = new PrimaryDrawerItem().withName(SCREEN).withIcon(R.drawable.ic_smartphone_black).withTextColor(Color.BLACK).withIdentifier(6).withOnDrawerItemClickListener((view, position, drawerItem) -> {
            currentOutput = SCREEN;
            switch (currentInput) {
                case PHOTO: {
                    inputOutput.setMessage(String.format("Do you want to use %s to %s ?", currentInput, currentOutput)).setCancelable(false)
                            .setPositiveButton("Yes", (dialogInterface, i) -> {
                                fragment = new PhotoFragment();
                                FragmentManager fragmentManager = fragAct.getSupportFragmentManager();
                                FragmentTransaction ft = fragmentManager.beginTransaction();
                                ft.replace(R.id.content_frame, fragment);
                                ft.commit();
                            })
                            .setNegativeButton("No", (dialogInterface, i) -> dialogInterface.cancel());
                    AlertDialog alertDialog = inputOutput.create();
                    alertDialog.setTitle("Are you sure?");
                    alertDialog.show();
                    break;
                }
                case VIDEO: {
                    inputOutput.setMessage(String.format("Do you want to use %s to %s ?", currentInput, currentOutput)).setCancelable(false)
                            .setPositiveButton("Yes", (dialogInterface, i) -> {
                                fragment = new VideoFragment();
                                FragmentManager fragmentManager = fragAct.getSupportFragmentManager();
                                FragmentTransaction ft = fragmentManager.beginTransaction();
                                ft.replace(R.id.content_frame, fragment);
                                ft.commit();
                            })
                            .setNegativeButton("No", (dialogInterface, i) -> dialogInterface.cancel());
                    AlertDialog alertDialog = inputOutput.create();
                    alertDialog.setTitle("Are you sure?");
                    alertDialog.show();
                    break;
                }
                case MICROPHONE:
                    Toast.makeText(_context, "Not Implemented", Toast.LENGTH_SHORT).show();
                    break;
                case BLUETOOTH: {
                    inputOutput.setMessage(String.format("Do you want to use %s to %s ?", currentInput, currentOutput)).setCancelable(false)
                            .setPositiveButton("Yes", (dialogInterface, i) -> {
                                fragment = new BluetoothFragment();
                                FragmentManager fragmentManager = fragAct.getSupportFragmentManager();
                                FragmentTransaction ft = fragmentManager.beginTransaction();
                                ft.replace(R.id.content_frame, fragment);
                                ft.commit();
                            })
                            .setNegativeButton("No", (dialogInterface, i) -> dialogInterface.cancel());
                    AlertDialog alertDialog = inputOutput.create();
                    alertDialog.setTitle("Are you sure?");
                    alertDialog.show();
                    break;
                }
                case WIFIP2P: {
                    inputOutput.setMessage(String.format("Do you want to use %s to %s ?", currentInput, currentOutput)).setCancelable(false)
                            .setPositiveButton("Yes", (dialogInterface, i) -> {
                                fragment = new WifiP2pFragment();
                                FragmentManager fragmentManager = fragAct.getSupportFragmentManager();
                                FragmentTransaction ft = fragmentManager.beginTransaction();
                                ft.replace(R.id.content_frame, fragment);
                                ft.commit();
                            })
                            .setNegativeButton("No", (dialogInterface, i) -> dialogInterface.cancel());
                    AlertDialog alertDialog = inputOutput.create();
                    alertDialog.setTitle("Are you sure?");
                    alertDialog.show();
                    break;
                }
                case WIFIAP: {
                    inputOutput.setMessage(String.format("Do you want to use %s to %s ?", currentInput, currentOutput)).setCancelable(false)
                            .setPositiveButton("Yes", (dialogInterface, i) -> {
                                fragment = new WifiApFragment();
                                FragmentManager fragmentManager = fragAct.getSupportFragmentManager();
                                FragmentTransaction ft = fragmentManager.beginTransaction();
                                ft.replace(R.id.content_frame, fragment);
                                ft.commit();
                            })
                            .setNegativeButton("No", (dialogInterface, i) -> dialogInterface.cancel());
                    AlertDialog alertDialog = inputOutput.create();
                    alertDialog.setTitle("Are you sure?");
                    alertDialog.show();
                    break;
                }
                case ACCELERO: {
                    inputOutput.setMessage(String.format("Do you want to use %s to %s ?", currentInput, currentOutput)).setCancelable(false)
                            .setPositiveButton("Yes", (dialogInterface, i) -> {
                                fragment = new AccelerometerFragment();
                                FragmentManager fragmentManager = fragAct.getSupportFragmentManager();
                                FragmentTransaction ft = fragmentManager.beginTransaction();
                                ft.replace(R.id.content_frame, fragment);
                                ft.commit();
                            })
                            .setNegativeButton("No", (dialogInterface, i) -> dialogInterface.cancel());
                    AlertDialog alertDialog = inputOutput.create();
                    alertDialog.setTitle("Are you sure?");
                    alertDialog.show();
                    break;
                }
                case GYROSCOPE: {
                    inputOutput.setMessage(String.format("Do you want to use %s to %s ?", currentInput, currentOutput)).setCancelable(false)
                            .setPositiveButton("Yes", (dialogInterface, i) -> {
                                fragment = new GyroscopeFragment();
                                FragmentManager fragmentManager = fragAct.getSupportFragmentManager();
                                FragmentTransaction ft = fragmentManager.beginTransaction();
                                ft.replace(R.id.content_frame, fragment);
                                ft.commit();
                            })
                            .setNegativeButton("No", (dialogInterface, i) -> dialogInterface.cancel());
                    AlertDialog alertDialog = inputOutput.create();
                    alertDialog.setTitle("Are you sure?");
                    alertDialog.show();
                    break;
                }
                case MAGNETO: {
                    inputOutput.setMessage(String.format("Do you want to use %s to %s ?", currentInput, currentOutput)).setCancelable(false)
                            .setPositiveButton("Yes", (dialogInterface, i) -> {
                                fragment = new MagnometerFragment();
                                FragmentManager fragmentManager = fragAct.getSupportFragmentManager();
                                FragmentTransaction ft = fragmentManager.beginTransaction();
                                ft.replace(R.id.content_frame, fragment);
                                ft.commit();
                            })
                            .setNegativeButton("No", (dialogInterface, i) -> dialogInterface.cancel());
                    AlertDialog alertDialog = inputOutput.create();
                    alertDialog.setTitle("Are you sure?");
                    alertDialog.show();
                    break;
                }

                case GESTURE: {
                    inputOutput.setMessage(String.format("Do you want to use %s to %s ?", currentInput, currentOutput)).setCancelable(false)
                            .setPositiveButton("Yes", (dialogInterface, i) -> {
                                fragment = new GestureFragment();
                                FragmentManager fragmentManager = fragAct.getSupportFragmentManager();
                                FragmentTransaction ft = fragmentManager.beginTransaction();
                                ft.replace(R.id.content_frame, fragment);
                                ft.commit();
                            })
                            .setNegativeButton("No", (dialogInterface, i) -> dialogInterface.cancel());
                    AlertDialog alertDialog = inputOutput.create();
                    alertDialog.setTitle("Are you sure?");
                    alertDialog.show();
                    break;
                }
                case MISCSENSOR: {
                    inputOutput.setMessage(String.format("Do you want to use %s to %s ?", currentInput, currentOutput)).setCancelable(false)
                            .setPositiveButton("Yes", (dialogInterface, i) -> {
                                fragment = new LHPmeterFragment();
                                FragmentManager fragmentManager = fragAct.getSupportFragmentManager();
                                FragmentTransaction ft = fragmentManager.beginTransaction();
                                ft.replace(R.id.content_frame, fragment);
                                ft.commit();
                            })
                            .setNegativeButton("No", (dialogInterface, i) -> dialogInterface.cancel());
                    AlertDialog alertDialog = inputOutput.create();
                    alertDialog.setTitle("Are you sure?");
                    alertDialog.show();
                    break;
                }
                case NFC: {
                    inputOutput.setMessage(String.format("Do you want to use %s to %s ?", currentInput, currentOutput)).setCancelable(false)
                            .setPositiveButton("Yes", (dialogInterface, i) -> {
                                fragment = new NfcFragment();
                                FragmentManager fragmentManager = fragAct.getSupportFragmentManager();
                                FragmentTransaction ft = fragmentManager.beginTransaction();
                                ft.replace(R.id.content_frame, fragment);
                                ft.commit();
                            })
                            .setNegativeButton("No", (dialogInterface, i) -> dialogInterface.cancel());
                    AlertDialog alertDialog = inputOutput.create();
                    alertDialog.setTitle("Are you sure?");
                    alertDialog.show();
                    break;
                }
                case USB: {
                    inputOutput.setMessage(String.format("Do you want to use %s to %s ?", currentInput, currentOutput)).setCancelable(false)
                            .setPositiveButton("Yes", (dialogInterface, i) -> {
                                fragment = new UsbFragment();
                                FragmentManager fragmentManager = fragAct.getSupportFragmentManager();
                                FragmentTransaction ft = fragmentManager.beginTransaction();
                                ft.replace(R.id.content_frame, fragment);
                                ft.commit();
                            })
                            .setNegativeButton("No", (dialogInterface, i) -> dialogInterface.cancel());
                    AlertDialog alertDialog = inputOutput.create();
                    alertDialog.setTitle("Are you sure?");
                    alertDialog.show();
                    break;
                }
            }
            return false;
        });
        shareOutput = new PrimaryDrawerItem().withSelectable(false).withName("Share").withIcon(R.drawable.ic_share_black).withTextColor(Color.BLACK).withIdentifier(7).withOnDrawerItemClickListener((view, position, drawerItem) -> {
            currentOutput = "Share";
            switch (currentInput) {
                case CAMERA:
                    Toast.makeText(_context, "Not Implemented", Toast.LENGTH_SHORT).show();
                    break;
                case MICROPHONE:
                    Toast.makeText(_context, "Not Implemented", Toast.LENGTH_SHORT).show();
                    break;
                case BLUETOOTH:
                    Toast.makeText(_context, "Not Implemented", Toast.LENGTH_SHORT).show();
                    break;
                case WIFI:
                    Toast.makeText(_context, "Not Implemented", Toast.LENGTH_SHORT).show();
                    break;
                case SENSORS:
                    Toast.makeText(_context, "Not Implemented", Toast.LENGTH_SHORT).show();
                    break;
                case GESTURE:
                    Toast.makeText(_context, "Not Implemented", Toast.LENGTH_SHORT).show();
                    break;
                case NFC:
                    Toast.makeText(_context, "Not Implemented", Toast.LENGTH_SHORT).show();
                    break;
                case USB:
                    Toast.makeText(_context, "Not Implemented", Toast.LENGTH_SHORT).show();
                    break;
            }
            return false;
        });

        //Input drawer contents
        photoInput = newSecondaryItemDrawer(PHOTO, 3, R.drawable.ic_photo_camera_black, Color.BLACK, Color.WHITE, 11, r, wifiOutput, btOutput, usbOutput, screenOutput, nfcOutput);
        videoInput = newSecondaryItemDrawer(VIDEO, 3, R.drawable.ic_videocam_black, Color.BLACK, Color.WHITE, 12, r, wifiOutput, btOutput, usbOutput, screenOutput, nfcOutput);
        micInput = newPrimaryItemDrawer(MICROPHONE, R.drawable.ic_mic_black, Color.BLACK, Color.WHITE, 13, r, speakerOutput, wifiOutput, btOutput, usbOutput, screenOutput);
        btInput = newPrimaryItemDrawer(BLUETOOTH, R.drawable.ic_bluetooth_black, Color.BLACK, Color.WHITE, 14, r, btOutput, screenOutput, speakerOutput);
        wifip2pInput = newSecondaryItemDrawer(WIFIP2P, 3, R.drawable.ic_p2p_black, Color.BLACK, Color.WHITE, 15, r, wifiOutput, screenOutput, speakerOutput);
        wifiapInput = newSecondaryItemDrawer(WIFIAP, 3, R.drawable.ic_ap_black, Color.BLACK, Color.WHITE, 16, r, wifiOutput, screenOutput, speakerOutput);
        acceleroInput = newSecondaryItemDrawer(ACCELERO, 3,  R.drawable.ic_accelerometer_black, Color.BLACK, Color.WHITE, 17, r, wifiOutput, btOutput, usbOutput, screenOutput, nfcOutput);
        gyroscopeInput = newSecondaryItemDrawer(GYROSCOPE, 3, R.drawable.ic_gyroscope_black, Color.BLACK, Color.WHITE, 18, r, wifiOutput, btOutput, usbOutput, screenOutput, nfcOutput);
        magnometerInput = newSecondaryItemDrawer(MAGNETO, 3, R.drawable.ic_magneto_black, Color.BLACK, Color.WHITE, 19, r, wifiOutput, btOutput, usbOutput, screenOutput, nfcOutput);
        miscsensorInput = newSecondaryItemDrawer(MISCSENSOR, 3, R.drawable.ic_magneto_black, Color.BLACK, Color.WHITE, 20, r, wifiOutput, btOutput, usbOutput, screenOutput, nfcOutput);
        touchInput = newPrimaryItemDrawer(GESTURE, R.drawable.ic_touch_black, Color.BLACK, Color.WHITE, 21, r, wifiOutput, btOutput, usbOutput, screenOutput);
        nfcInput = newPrimaryItemDrawer(NFC, R.drawable.ic_nfc_black, Color.BLACK, Color.WHITE, 22, r, nfcOutput, screenOutput);
        usbInput = newPrimaryItemDrawer(USB, R.drawable.ic_usb_black, Color.BLACK, Color.WHITE, 23, r, usbOutput, screenOutput);

        //Input Drawer
        inputDrawer = new DrawerBuilder()
                .withActivity((Activity)_context)
                .withSelectedItem(-1)
                .withToolbar(toolbar)
                .withHeader(R.layout.nav_header_main)
                .withHasStableIds(true)
                .addDrawerItems(
                        new ExpandableDrawerItem().withName(CAMERA).withIcon(R.drawable.ic_photo_camera_black).withTextColor(Color.BLACK).withSelectedTextColor(Color.WHITE).withSubItems(photoInput, videoInput),
                        micInput,
                        btInput,
                        new ExpandableDrawerItem().withName(WIFI).withIcon(R.drawable.ic_wifi_black).withTextColor(Color.BLACK).withSelectedTextColor(Color.WHITE).withSubItems(wifiapInput, wifip2pInput),
                        new ExpandableDrawerItem().withName(SENSORS).withIcon(R.drawable.ic_sensor_black).withTextColor(Color.BLACK).withSelectedTextColor(Color.WHITE).withSubItems(acceleroInput, gyroscopeInput, magnometerInput, miscsensorInput),
                        touchInput,
                        nfcInput,
                        usbInput
                ).withSavedInstance(savedInstanceState)
                .build();

/*
        ImageHolder img = new ImageHolder(R.drawable.ic_bluetooth_white);
        inputDrawer.updateIcon(1, img);
*/
        //Output Drawer
        outputDrawer = new DrawerBuilder()
                .withActivity((Activity)_context)
                .withSelectedItem(-1)
                .withHeader(R.layout.nav_header_main2)
                .withHasStableIds(true)
                .withDrawerGravity(Gravity.END)
                .withSavedInstance(savedInstanceState)
                .build();
    }


    private PrimaryDrawerItem newPrimaryItemDrawer(String name, int imgres, int color, int selectedColor, int identifier, int[] identifiersToBeRemoved, IDrawerItem... drawerItems) {
        ImageHolder img = new ImageHolder(imgres);
        return new PrimaryDrawerItem().withName(name).withIcon(img).withTextColor(color).withSelectedColor(selectedColor).withIdentifier(identifier).withOnDrawerItemClickListener((view, position, drawerItem) -> {
            currentInput = name;
            for (int i : identifiersToBeRemoved) {
                outputDrawer.removeItems(i);
            }
            outputDrawer.addItems(drawerItems);
            openOutput();
            return false;
        });

    }

    private SecondaryDrawerItem newSecondaryItemDrawer(String name, int l, int imgres, int color, int selectedColor, int identifier, int[] identifiersToBeRemoved, IDrawerItem... drawerItems) {

        ImageHolder img = new ImageHolder(imgres);
        return new SecondaryDrawerItem().withName(name).withLevel(l).withIcon(img).withTextColor(color).withSelectedColor(selectedColor).withIdentifier(identifier).withOnDrawerItemClickListener((view, position, drawerItem) -> {
            currentInput = name;
            for (int i : identifiersToBeRemoved) {
                outputDrawer.removeItem(i);
            }
            outputDrawer.addItems(drawerItems);
            openOutput();
            return false;
        });
    }

    static void increaseSwipeEdgeOfDrawer(Drawer inputDrawer) {
        try {

            Field mDragger = inputDrawer.getDrawerLayout().getClass().getDeclaredField("mLeftDragger");//mRightDragger or mLeftDragger based on Drawer Gravity
            mDragger.setAccessible(true);
            ViewDragHelper draggerObj = (ViewDragHelper) mDragger.get(inputDrawer.getDrawerLayout());

            Field mEdgeSize = draggerObj.getClass().getDeclaredField("mEdgeSize");
            mEdgeSize.setAccessible(true);
            int edge = mEdgeSize.getInt(draggerObj);
            mEdgeSize.setInt(draggerObj, 1080);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void openOutput() {

        if (inputDrawer.isDrawerOpen()) {
            inputDrawer.closeDrawer();
        }

        if (!outputDrawer.isDrawerOpen()) {
            outputDrawer.openDrawer();
        }
    }

    //Method that checks if user has certain function in the phone and disables availability if not
    void setInputItemsInvisible() {
        if (!hasCamera()) {
            inputDrawer.removeItems(11, 12);
        }
        if (!hasMicrophone()) {
            inputDrawer.removeItem(13);
        }
        if (!hasBluetooth()) {
            inputDrawer.removeItem(14);
        }
        if (!hasWifi()) {
            inputDrawer.removeItem(15);
        }
        if (!hasNfc()) {
            inputDrawer.removeItem(21);
        }
        if (!hasAccelero()) {
            inputDrawer.removeItem(16);
        }
        if (!hasGyro()) {
            inputDrawer.removeItem(17);
        }
        if (!hasMagno()) {
            inputDrawer.removeItem(18);
        }
    }

    private boolean hasCamera() {
        return _context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY);
    }

    private boolean hasMicrophone() {
        return _context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_MICROPHONE);
    }

    private boolean hasBluetooth() {
        return _context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH);
    }

    private boolean hasWifi() {
        return _context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_WIFI);
    }

    private boolean hasNfc() {
        return _context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_NFC);
    }

    private boolean hasAccelero() {
        return _context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_SENSOR_ACCELEROMETER);
    }

    private boolean hasGyro() {
        return _context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_SENSOR_GYROSCOPE);
    }

    private boolean hasMagno() {
        return _context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_SENSOR_COMPASS);
    }
}
