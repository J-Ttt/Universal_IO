package com.example.omistaja.universal_io;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.omistaja.universal_io.DrawerLayouts.InputDrawer;
import com.example.omistaja.universal_io.DrawerLayouts.OutputDrawer;

public class MainActivity extends AppCompatActivity {


    final int CAMERA_PIC_REQUEST = 1;

    private DrawerLayout drawerLayout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();


        FloatingActionButton rightDraw = findViewById(R.id.rightDraw);
        rightDraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!drawerLayout.isDrawerOpen(GravityCompat.END)) {
                    drawerLayout.openDrawer(GravityCompat.END);
                }
            }
        });

        FloatingActionButton leftDraw = findViewById(R.id.leftDraw);
        leftDraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
            }
        });

        NavigationView outputView = findViewById(R.id.nav_view_right);
        OutputDrawer outputDrawer = new OutputDrawer(this, outputView, drawerLayout);
        outputView.setNavigationItemSelectedListener(outputDrawer);

        NavigationView inputView = findViewById(R.id.nav_view_left);
        InputDrawer inputDrawer = new InputDrawer(this, inputView, drawerLayout);
        inputView.setNavigationItemSelectedListener(inputDrawer);
    }

    public void onResume() {
        super.onResume();
    }

    public void onStop() {
        super.onStop();
    }


    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else if (drawerLayout.isDrawerOpen(GravityCompat.END)) {
            drawerLayout.closeDrawer(GravityCompat.END);
        } else {
            super.onBackPressed();
        }
    }

    public void onActivityResult(int requestCode, int requestResult, Intent data) {
        if (requestCode == CAMERA_PIC_REQUEST) {

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
