package com.example.omistaja.universal_io;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Switch;

import java.util.Objects;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;


public class SettingsActivity extends AppCompatActivity {

    Switch themeSwitch;
    private static final String PREFS_NAME = "prefs";
    private static final String PREF_LIGHT_THEME = "light_theme";
    Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        boolean useLightTheme = preferences.getBoolean(PREF_LIGHT_THEME, false);

        if (useLightTheme) {
            setTheme(R.style.CustomTheme);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_main);
        toolbar = findViewById(R.id.toolbar);
        themeSwitch = findViewById(R.id.themeSwitch1);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        final Drawable leftArrow = getDrawable(R.drawable.ic_arrow_left_black);

        if (useLightTheme) {
            toolbar.setTitleTextColor(Color.BLACK);
            getSupportActionBar().setHomeAsUpIndicator(leftArrow);
        }

        themeSwitch.setChecked(useLightTheme);
        themeSwitch.setOnCheckedChangeListener((compoundButton, b) -> toggleTheme(b));


    }

    private void toggleTheme(boolean lightTheme) {
        SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
        editor.putBoolean(PREF_LIGHT_THEME, lightTheme);
        editor.apply();
        Intent intent = getIntent();
        startActivity(intent);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        NavUtils.navigateUpFromSameTask(this);
        //Intent intent = new Intent(this, MainActivity.class);
        //startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);

    }
/*

    private void saveTheme(String themeApp) {
        SharedPreferences prefs = this.getSharedPreferences("ThemePrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("Theme", themeApp);
        editor.apply();
    }

    static public int storeTheme(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("ThemePrefs", MODE_PRIVATE);

        return prefs.getInt("Theme", 0);
    }
    */
}
