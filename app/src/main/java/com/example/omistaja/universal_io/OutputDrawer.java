package com.example.omistaja.universal_io;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;

public class OutputDrawer implements NavigationView.OnNavigationItemSelectedListener {

    private Context c;
    private DrawerLayout ldrawer;
    private NavigationView drawer;

    public OutputDrawer(Context cont, NavigationView drwer, DrawerLayout ldrwer) {

        this.c = cont;
        this.drawer = drwer;
        this.ldrawer = ldrwer;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch(id) {
            case R.id.nav_camera:

                break;
        }

        ldrawer.closeDrawer(GravityCompat.START);
        return true;
    }



}
