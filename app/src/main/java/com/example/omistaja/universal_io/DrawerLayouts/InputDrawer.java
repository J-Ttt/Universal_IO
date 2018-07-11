package com.example.omistaja.universal_io.DrawerLayouts;

import android.app.Activity;
import android.graphics.Camera;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.omistaja.universal_io.Fragments.BluetoothFragment;
import com.example.omistaja.universal_io.Fragments.PhotoFragment;
import com.example.omistaja.universal_io.Fragments.MicrophoneFragment;
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

        if (id == R.id.nav_camera) {
            fragment = new PhotoFragment();
        } else if (id == R.id.nav_microphone) {
            fragment = new MicrophoneFragment();
        } else if (id == R.id.nav_bluetooth) {
            fragment = new BluetoothFragment();
        } else if (id == R.id.nav_wifi) {
            Toast.makeText(c, "Not yet implemented", Toast.LENGTH_SHORT).show();
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

