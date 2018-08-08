package com.example.omistaja.universal_io.Fragments;


import android.annotation.SuppressLint;
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
                case R.id.nav_miscsens:
                    fragment = new LHPmeterFragment();
                    break;

            }

            if (fragment != null) {
                FragmentManager fragmentManager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
                FragmentTransaction ft = fragmentManager.beginTransaction();
                ft.replace(R.id.content_frame, fragment);
                ft.commit();
            }

            return true;
        }
    };

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_accelerometer, container, false);

        BottomNavigationView p2pap = rootView.findViewById(R.id.bottomNavigationView3);
        p2pap.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);
        sensorManager = (SensorManager) _context.getApplicationContext().getSystemService(Context.SENSOR_SERVICE);
        assert sensorManager != null;
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

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
