package com.example.omistaja.universal_io.fragments;


import android.annotation.SuppressLint;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.omistaja.universal_io.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class AccelerometerFragment extends Fragment implements SensorEventListener {

    private Context _context;
    private TextView accelX, accelY, accelZ;
    private Sensor accelerometer;
    private SensorManager sensorManager;

    public AccelerometerFragment() {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        _context = context;
    }

    private BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener = item -> {
        final Fragment gyroFrag = new GyroscopeFragment();
        final Fragment magnoFrag = new MagnometerFragment();
        final Fragment miscFrag = new LHPmeterFragment();

        FragmentTransaction ft = Objects.requireNonNull(getFragmentManager()).beginTransaction();

        switch (item.getItemId()) {
            case R.id.nav_accelo:
                return true;
            case R.id.nav_gyro:
                ft.replace(R.id.content_frame, gyroFrag).commit();
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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_accelerometer, container, false);

        BottomNavigationView p2pap = rootView.findViewById(R.id.bottomNavigationView3);
        p2pap.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);
        sensorManager = (SensorManager) _context.getApplicationContext().getSystemService(Context.SENSOR_SERVICE);
        assert sensorManager != null;
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        p2pap.setSelectedItemId(R.id.nav_accelo);
        accelX = rootView.findViewById(R.id.accelX);
        accelY = rootView.findViewById(R.id.accelY);
        accelZ = rootView.findViewById(R.id.accelZ);

        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI, SensorManager.SENSOR_STATUS_ACCURACY_HIGH);


        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        sensorManager.registerListener(this, accelerometer,SensorManager.SENSOR_DELAY_UI, SensorManager.SENSOR_STATUS_ACCURACY_HIGH);
    }

    @Override
    public void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this, accelerometer);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        accelX.setText("X Value: " + sensorEvent.values[0]);
        accelY.setText("Y Value: " + sensorEvent.values[1]);
        accelZ.setText("Z Value: " + sensorEvent.values[2]);
    }
}
