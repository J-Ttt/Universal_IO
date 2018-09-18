package com.example.omistaja.universal_io.fragments;


import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.omistaja.universal_io.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Objects;

public class GyroscopeFragment extends Fragment implements SensorEventListener {

    Context _context;
    TextView gyroX, gyroY, gyroZ, tvGyro;
    SensorManager sensorManager;
    Sensor gyroScope;

    public GyroscopeFragment() {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        _context = context;
    }

    private BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener = item -> {
        final Fragment acceloFrag = new AccelerometerFragment();
        final Fragment magnoFrag = new MagnometerFragment();
        final Fragment miscFrag = new MiscSensorFragment();

        FragmentTransaction ft = Objects.requireNonNull(getFragmentManager()).beginTransaction();

        switch (item.getItemId()) {
            case R.id.nav_accelo:
                ft.replace(R.id.content_frame, acceloFrag).commit();
                return true;
            case R.id.nav_gyro:
                return true;
            case R.id.nav_magno:
                ft.replace(R.id.content_frame, magnoFrag).commit();
                return true;
            case R.id.nav_miscsens:
                ft.replace(R.id.content_frame, miscFrag).commit();
                return true;

        }
        return false;
    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_gyroscope, container, false);

        BottomNavigationView p2pap = rootView.findViewById(R.id.bottomNavigationView3);
        p2pap.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);
        sensorManager = (SensorManager) _context.getApplicationContext().getSystemService(Context.SENSOR_SERVICE);
        gyroScope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        p2pap.setSelectedItemId(R.id.nav_gyro);
        gyroX = rootView.findViewById(R.id.gyroX);
        gyroY = rootView.findViewById(R.id.gyroY);
        gyroZ = rootView.findViewById(R.id.gyroZ);
        tvGyro = rootView.findViewById(R.id.tvGyro);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        sensorManager.registerListener(this, gyroScope, SensorManager.SENSOR_DELAY_UI, SensorManager.SENSOR_STATUS_ACCURACY_HIGH);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        gyroX.setText("X Value: " + sensorEvent.values[0]);
        gyroY.setText("Y Value: " + sensorEvent.values[1]);
        gyroZ.setText("Z Value: " + sensorEvent.values[2]);
    }
}
