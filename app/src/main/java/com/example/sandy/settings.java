package com.example.sandy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Switch;

public class settings extends AppCompatActivity {
    Switch darkModeSwitch;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    boolean nightmode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        darkModeSwitch=findViewById(R.id.darkModeSwitch);
        sharedPreferences=getSharedPreferences("MODE", Context.MODE_PRIVATE);
        nightmode=sharedPreferences.getBoolean("night",false);
        darkModeSwitch.setChecked(nightmode);
        if (nightmode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        darkModeSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = sharedPreferences.edit();

                if(nightmode)
                {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    editor.putBoolean("night",false);
                    nightmode=false;

                }else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    editor.putBoolean("night",true);
                    nightmode=true;

                }
                editor.apply();
            }
        });


    }
}