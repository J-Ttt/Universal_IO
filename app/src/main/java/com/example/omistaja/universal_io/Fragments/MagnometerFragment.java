package com.example.omistaja.universal_io.Fragments;


import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.omistaja.universal_io.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class MagnometerFragment extends Fragment implements SensorEventListener {


    Context _context;
    TextView magnetoX, magnetoY, magnetoZ;
    SensorManager sensorManager;
    Sensor magnoMeter;

    public MagnometerFragment() {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        _context = context;
    }

    private BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment = null;

            int id = item.getItemId();

            switch (id) {
                case R.id.nav_accelo:
                    fragment = new AccelerometerFragment();
                    break;
                case R.id.nav_gyro:
                    fragment = new GyroscopeFragment();
                    break;
                case R.id.nav_magno:
                    fragment = new MagnometerFragment();
                    break;

            }

            if (fragment != null) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = fragmentManager.beginTransaction();
                ft.replace(R.id.content_frame, fragment);
                ft.commit();
            }

            return true;
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_magnometer, container, false);

        BottomNavigationView p2pap = rootView.findViewById(R.id.bottomNavigationView3);
        p2pap.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);
        sensorManager = (SensorManager) _context.getApplicationContext().getSystemService(Context.SENSOR_SERVICE);
        magnoMeter = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        magnetoX = rootView.findViewById(R.id.magnetoX);
        magnetoY = rootView.findViewById(R.id.magnetoY);
        magnetoZ = rootView.findViewById(R.id.magnetoZ);

        sensorManager.registerListener(this, magnoMeter, SensorManager.SENSOR_DELAY_UI, SensorManager.SENSOR_STATUS_ACCURACY_HIGH);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        sensorManager.registerListener(this, magnoMeter, SensorManager.SENSOR_DELAY_UI, SensorManager.SENSOR_STATUS_ACCURACY_HIGH);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        magnetoX.setText("X Value: " + sensorEvent.values[0]);
        magnetoY.setText("Y Value: " + sensorEvent.values[1]);
        magnetoZ.setText("Z Value: " + sensorEvent.values[2]);
    }

}
