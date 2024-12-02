package com.example.sandy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;


import android.os.Bundle;
import android.os.Handler;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;


public class MainActivity extends AppCompatActivity {
    EditText e1,e2;
    ImageView img2;
    DatabaseReference databaseReference;
    TextView t1;
    private static final int REQUEST_SMS_PERMISSION = 1;

    private String generatedOTP;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        e1=findViewById(R.id.user);
       // e2=findViewById(R.id.pass);
        img2=findViewById(R.id.imageview1);
      //  t1=findViewById(R.id.sign);
        databaseReference = FirebaseDatabase.getInstance().getReference("users");
        img2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = e1.getText().toString().trim();


                if (phone.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Check user data in Firebase
                /*validateUser(phone);*/
                if (ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.SEND_SMS)
                        == PackageManager.PERMISSION_GRANTED) {
                    sendOtp();
                } else {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.SEND_SMS}, REQUEST_SMS_PERMISSION);
                }


            }
        });
        /*t1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Signup.class);
                startActivity(intent);
            }
        });*/




    }



    private String generateOTP() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000); // Generates a 6-digit OTP
        return String.valueOf(otp);
    }
    private void sendOtp() {
        String phoneNumber = e1.getText().toString().trim();
        generatedOTP = generateOTP();
        String message = "Your OTP is: " + generatedOTP;

        if (!phoneNumber.isEmpty()) {
            try {
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(phoneNumber, null, message, null, null);
                Toast.makeText(this, "OTP Sent!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, otpgen.class);
                intent.putExtra("phone",phoneNumber);
                intent.putExtra("otp", generatedOTP);
                startActivity(intent);
                finish(); // Close this activity
            } catch (Exception e) {
                Toast.makeText(this, "Failed to send OTP. Please try again.", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Please enter a valid phone number.", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_SMS_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                sendOtp();
            } else {
                Toast.makeText(this, "SMS permission is required to send OTP", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /*private void validateUser(final String phone, final String password) {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean found = false;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String dbPhone = snapshot.child("phone").getValue(String.class);
                    String dbPassword = snapshot.child("password").getValue(String.class);
                    if (dbPhone.equals(phone) && dbPassword.equals(password)) {
                        found = true;
                        String name = snapshot.child("name").getValue(String.class);
                        int points = snapshot.child("points").getValue(Integer.class);

                        // Pass user details to next activity or display them
                        Intent intent = new Intent(MainActivity.this, MainActivity3.class);
                        intent.putExtra("userName", name);
                        intent.putExtra("userPoints", points);
                        startActivity(intent);
                        finish();
                        break;
                    }
                }

                if (!found) {
                    Toast.makeText(MainActivity.this, "Invalid phone number or password", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(MainActivity.this, "Database error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }*/
    private void validateUser(final String phone) {
        // Reference to the specific user based on phone number
        DatabaseReference userRef = databaseReference.child(phone);

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean found = false;
                String name = "";
                Integer shop1Points = null; // Initialize to null for clarity
                Integer shop2Points = null; // Initialize to null for clarity
                Integer shop3Points=null;

                // Check if the user exists
                if (dataSnapshot.exists()) {
                    // Retrieve shop1 details
                    if (dataSnapshot.child("Grocery_Shop").exists()) {
                        name = dataSnapshot.child("Grocery_Shop").child("name").getValue(String.class);
                        shop1Points = dataSnapshot.child("Grocery_Shop").child("points").getValue(Integer.class);
                        found = true; // User found if shop1 exists
                    }

                    // Retrieve shop2 details
                    if (dataSnapshot.child("S_Fashion_Brand").exists()) {
                        // If shop2 exists, we can still set the same name or handle it differently
                        String shop2Name = dataSnapshot.child("S_Fashion_Brand").child("name").getValue(String.class);
                        shop2Points = dataSnapshot.child("S_Fashion_Brand").child("points").getValue(Integer.class);
                        // You can decide if you want to set the name from shop2 if shop1 exists
                        if (shop2Name != null) {
                            name = shop2Name; // Update name to shop2 if you prefer
                        }
                    }
                    if(dataSnapshot.child("electromart").exists())
                    {
                        String shop3name=dataSnapshot.child("electromart").child("name").getValue(String.class);
                        shop3Points=dataSnapshot.child("electromart").child("points").getValue(Integer.class);
                        if(shop3name!=null)
                        {
                            name=shop3name;
                        }
                    }

                    Log.d("UserValidation", "Name: " + name + ", Shop1 Points: " + shop1Points + ", Shop2 Points: " + shop2Points + ", Shop3 Points: ");


                    // Pass user details to the next activity
                    Intent intent = new Intent(MainActivity.this, MainPage.class);
                    intent.putExtra("userName", name);
                    intent.putExtra("shop1Points", shop1Points); // Pass shop1 points
                    intent.putExtra("shop2Points", shop2Points); // Pass shop2 points
                    intent.putExtra("shop3Points",shop3Points);
                    startActivity(intent);
                    finish();
                }

                if (!found) {
                    Toast.makeText(MainActivity.this, "User not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(MainActivity.this, "Database error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }




}