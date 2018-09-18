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

public class MiscSensorFragment extends Fragment implements SensorEventListener {

    Context _context;
    Sensor lightSensor, humiSensor, pressuSensor, proxiSensor, tempSensor;
    SensorManager sensorManager;
    TextView lightSens, humiSens, pressuSens, tempSens, proxiSens;

    public MiscSensorFragment() {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        _context = context;
    }

    private BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener = item -> {
        final Fragment acceloFrag = new AccelerometerFragment();
        final Fragment magnoFrag = new MagnometerFragment();
        final Fragment gyroFrag = new GyroscopeFragment();

        FragmentTransaction ft = Objects.requireNonNull(getFragmentManager()).beginTransaction();

        switch (item.getItemId()) {
            case R.id.nav_accelo:
                ft.replace(R.id.content_frame, acceloFrag).commit();
                return true;
            case R.id.nav_gyro:
                ft.replace(R.id.content_frame, gyroFrag).commit();
                return true;
            case R.id.nav_magno:
                ft.replace(R.id.content_frame, magnoFrag).commit();
                return true;
            case R.id.nav_miscsens:
                return true;

        }
        return false;
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_lhpmeter, container, false);

        BottomNavigationView p2pap = rootView.findViewById(R.id.bottomNavigationView3);
        p2pap.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);
        p2pap.setSelectedItemId(R.id.nav_miscsens);
        lightSens = rootView.findViewById(R.id.lightsens);
        humiSens = rootView.findViewById(R.id.humiditysens);
        pressuSens = rootView.findViewById(R.id.pressuresens);
        tempSens = rootView.findViewById(R.id.tempsens);
        proxiSens = rootView.findViewById(R.id.proxisens);



        initialWork();


        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
    }
    

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Sensor sensor = sensorEvent.sensor;
        if (sensor.getType() == Sensor.TYPE_LIGHT) {
            lightSens.setText("Light: " + sensorEvent.values[0]);
        } else if (sensor.getType() == Sensor.TYPE_RELATIVE_HUMIDITY) {
            humiSens.setText("Humidity: " + sensorEvent.values[0]);
        } else if (sensor.getType() == Sensor.TYPE_PRESSURE) {
            pressuSens.setText("Pressure: " + sensorEvent.values[0]);
        } else if (sensor.getType() == Sensor.TYPE_PROXIMITY) {
            proxiSens.setText("Proximity: " + sensorEvent.values[0]);
        } else if (sensor.getType() == Sensor.TYPE_AMBIENT_TEMPERATURE) {
            tempSens.setText("Temperature: " + sensorEvent.values[0]);
        }
    }

    private void initialWork() {
        sensorManager = (SensorManager) _context.getApplicationContext().getSystemService(Context.SENSOR_SERVICE);

        assert sensorManager != null;
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        if (lightSensor != null) {
            sensorManager.registerListener(this, lightSensor, SensorManager.SENSOR_DELAY_NORMAL);
        } else {
            lightSens.setText("Light: Not Supported");
        }

        humiSensor = sensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY);
        if (humiSensor != null) {
            sensorManager.registerListener(this, humiSensor, SensorManager.SENSOR_DELAY_NORMAL);
        } else {
            humiSens.setText("Humidity: Not Supported");
        }

        pressuSensor = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
        if (pressuSensor != null) {
            sensorManager.registerListener(this, pressuSensor, SensorManager.SENSOR_DELAY_NORMAL);
        } else {
            pressuSens.setText("Pressure: Not Supported");
        }

        proxiSensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        if (proxiSensor != null) {
            sensorManager.registerListener(this, proxiSensor, SensorManager.SENSOR_DELAY_NORMAL);
        } else {
            proxiSens.setText("Proximity: Not Supported");
        }

        tempSensor = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
        if (tempSensor != null) {
            sensorManager.registerListener(this, tempSensor, SensorManager.SENSOR_DELAY_NORMAL);
        } else {
            tempSens.setText("Temperature: Not supported");
        }





    }
}
