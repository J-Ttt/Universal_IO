package com.example.omistaja.universal_io.DrawerLayouts;

import android.content.Context;
import androidx.annotation.NonNull;
import com.google.android.material.navigation.NavigationView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.omistaja.universal_io.Fragments.PhotoFragment;
import com.example.omistaja.universal_io.R;

public class OutputDrawer implements NavigationView.OnNavigationItemSelectedListener {

    private Context _context;
    private DrawerLayout rdrawer;
    private NavigationView drawer;

    public OutputDrawer(Context context, NavigationView drwer, DrawerLayout rdrwer) {

        this._context = context;
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
                Toast.makeText(_context, "Not yet implemented", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_bluetooth2:
                Toast.makeText(_context, "Not yet implemented", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_speaker:
                Toast.makeText(_context, "Not yet implemented", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_nfc:
                Toast.makeText(_context, "Not yet implemented", Toast.LENGTH_SHORT).show();
                break;
        }

        rdrawer.closeDrawer(GravityCompat.END);
        return true;
    }



}
