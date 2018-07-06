package com.example.omistaja.universal_io.DrawerLayouts;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;

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


        rdrawer.closeDrawer(GravityCompat.END);
        return true;
    }



}
