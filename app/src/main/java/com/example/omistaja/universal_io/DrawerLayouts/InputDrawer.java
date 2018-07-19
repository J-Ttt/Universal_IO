package com.example.omistaja.universal_io.DrawerLayouts;

import android.app.Activity;
import android.graphics.Camera;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.content.Context;
import androidx.annotation.NonNull;
import com.google.android.material.navigation.NavigationView;
import androidx.fragment.app.FragmentActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.omistaja.universal_io.Fragments.BluetoothFragment;
import com.example.omistaja.universal_io.Fragments.PhotoFragment;
import com.example.omistaja.universal_io.Fragments.MicrophoneFragment;
import com.example.omistaja.universal_io.Fragments.WifiFragment;
import com.example.omistaja.universal_io.MainActivity;
import com.example.omistaja.universal_io.R;

public class InputDrawer implements NavigationView.OnNavigationItemSelectedListener {

    private Context c;
    private DrawerLayout ldrawer;
    private NavigationView drawer;

    public InputDrawer(Context cont, NavigationView drwer, DrawerLayout ldrwer) {

        this.c = cont;
        this.drawer = drwer;
        this.ldrawer = ldrwer;



    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        Fragment fragment = null;

        int id = item.getItemId();

        switch (id) {
            case R.id.nav_camera:
                fragment = new PhotoFragment();
                break;
            case R.id.nav_microphone:
                fragment = new MicrophoneFragment();
                break;
            case R.id.nav_bluetooth:
                fragment = new BluetoothFragment();
                break;
            case R.id.nav_wifi:
                fragment = new WifiFragment();
                break;
            case R.id.nav_sensor:
                Toast.makeText(c, "Not yet implemented", Toast.LENGTH_SHORT).show();
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = ((FragmentActivity)c).getSupportFragmentManager();
            FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }

        ldrawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
