package com.example.omistaja.universal_io.DrawerLayouts;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.omistaja.universal_io.Fragments.PhotoFragment;
import com.example.omistaja.universal_io.R;

public class OutputDrawer implements NavigationView.OnNavigationItemSelectedListener {

    private Context c;
    private DrawerLayout rdrawer;
    private NavigationView drawer;

    public OutputDrawer(Context cont, NavigationView drwer, DrawerLayout rdrwer) {

        this.c = cont;
        this.drawer = drwer;
        this.rdrawer = rdrwer;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id){
            case R.id.nav_wifi2:
                Toast.makeText(c, "Not yet implemented", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_bluetooth2:
                Toast.makeText(c, "Not yet implemented", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_speaker:
                Toast.makeText(c, "Not yet implemented", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_nfc:
                Toast.makeText(c, "Not yet implemented", Toast.LENGTH_SHORT).show();
                break;
        }

        rdrawer.closeDrawer(GravityCompat.END);
        return true;
    }



}
