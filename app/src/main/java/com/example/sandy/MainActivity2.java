package com.example.sandy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import java.util.concurrent.TimeUnit;

public class MainActivity2 extends AppCompatActivity {

    @SuppressLint("MissingInflatedId")
    ImageView img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        PeriodicWorkRequest workRequest = new PeriodicWorkRequest.Builder(Firebaseworker.class, 15, TimeUnit.MINUTES)
                .build();
        WorkManager.getInstance(this).enqueue(workRequest);

        img=(ImageView) findViewById(R.id.imageview);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(MainActivity2.this,MainActivity.class);
                startActivity(i);
            }
        });
    }
}