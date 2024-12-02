package com.example.sandy;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity3 extends AppCompatActivity {
    TextView t1,t2,t3;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        t1=(TextView)findViewById(R.id.textView1);
        t2=(TextView)findViewById(R.id.textView2);
        t3=(TextView)findViewById(R.id.textView3);

        String name = getIntent().getStringExtra("userName");
        int point1 = getIntent().getIntExtra("shop1Points", 0);
        int point2=getIntent().getIntExtra("shop2Points",0);
        t1.setText("Welcome, " + name);
        t2.setText("Grocery_Shop:"+point1);
        t3.setText("S_Fashion_Brand:"+point2);

    }
}