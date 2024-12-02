package com.example.sandy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainPage extends AppCompatActivity {
    TextView t1,t2,t3,t4,r1,r2,r3;
    ViewPager2 viewPager2;
    private Handler slideHandler=new Handler();
    private Handler handler;
    private Runnable refreshRunnable;
    private String phonenumber;
    private DatabaseReference databaseReference;
    ImageView b1,menu;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences sharedPreferences = getSharedPreferences("MODE", Context.MODE_PRIVATE);
        boolean isDarkModeOn = sharedPreferences.getBoolean("night", false);

        // Apply the saved theme
        if (isDarkModeOn) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        viewPager2=findViewById(R.id.viewPager);
        b1=findViewById(R.id.img1);
        menu=findViewById(R.id.menu);
        List<SlideItem> slideItem=new ArrayList<>();
        slideItem.add(new SlideItem(R.drawable.offer_1));
        slideItem.add(new SlideItem(R.drawable.offer_2));
        slideItem.add(new SlideItem(R.drawable.offer_3));
        slideItem.add(new SlideItem(R.drawable.offer_4));
        viewPager2.setAdapter(new SliderAdapter(slideItem,viewPager2));
        viewPager2.setClipToPadding(false);
        viewPager2.setClipChildren(false);
        viewPager2.setOffscreenPageLimit(5);
        viewPager2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

        CompositePageTransformer compositePageTransformer=new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(30));
        compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
               float r=1-Math.abs(position);
               page.setScaleY(1.0f+r*0.15f);

            }
        });
        viewPager2.setPageTransformer(compositePageTransformer);
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                slideHandler.removeCallbacks(slideRunnable);
                slideHandler.postDelayed(slideRunnable,2000);

            }
        });
        t1=(TextView)findViewById(R.id.name1);
        t2=(TextView)findViewById(R.id.points1);
        t3=(TextView)findViewById(R.id.points2);
        t4=(TextView)findViewById(R.id.points3);
        r1=(TextView)findViewById(R.id.r1);
        r2=(TextView)findViewById(R.id.r2);
        r3=(TextView)findViewById(R.id.redeem3);
        r1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://msconor.github.io/clothing/clothing/"));
                startActivity(browserIntent);
            }
        });
        r2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://msconor.github.io/grocery/prj1/"));
                startActivity(browserIntent);
            }
        });
        r3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://msconor.github.io/electro/electrmart/"));
                startActivity(browserIntent);
            }
        });

        String name = getIntent().getStringExtra("userName");
        phonenumber=getIntent().getStringExtra("phonenumber");
        databaseReference = FirebaseDatabase.getInstance().getReference("users");

       b1.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               fetchPoints();
           }
       });

        int point1 = getIntent().getIntExtra("shop1Points", 0);
        int point2=getIntent().getIntExtra("shop2Points",0);
        int point3=getIntent().getIntExtra("shop3Points",0);
        t1.setText("Welcome, " + name);
        /*t2.setText(String.valueOf(point1));
        t3.setText(String.valueOf(point2));*/
        t3.setText("+ "+point1+" pts");
        t2.setText("+ "+point2+" pts");
        t4.setText("+ "+point3+" pts");
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu=new PopupMenu(MainPage.this,menu);
                popupMenu.getMenuInflater().inflate(R.menu.menu,popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        int itemId = menuItem.getItemId();
                        if (itemId == R.id.option1) {
                            Intent i=new Intent(MainPage.this,MainActivity2.class);
                            startActivity(i);
                            return true;
                        }
                        return false;
                    }
                });
                popupMenu.show();

            }
        });


    }




    private void fetchPoints() {
        databaseReference.child(phonenumber).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    int point1 = snapshot.child("electromart").child("points").getValue(Integer.class);
                    int point2 = snapshot.child("S_Fashion_Brand").child("points").getValue(Integer.class);


                    t3.setText("+ "+point1+" pts");
                    t2.setText("+ "+point2+" pts");


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }


    private Runnable slideRunnable=new Runnable() {
        @Override
        public void run() {
            viewPager2.setCurrentItem(viewPager2.getCurrentItem()+1);
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        slideHandler.removeCallbacks(slideRunnable);
    }

    @Override
    protected void onResume() {
        super.onResume();
        slideHandler.postDelayed(slideRunnable,3000);
    }


}