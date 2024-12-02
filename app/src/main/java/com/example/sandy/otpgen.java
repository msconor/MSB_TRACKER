package com.example.sandy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class otpgen extends AppCompatActivity {
    private EditText[] otpFields = new EditText[6];
    private ImageView verifyOtpButton;
    private String generatedOTP,phone;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otpgen);
        generatedOTP = getIntent().getStringExtra("otp");


        // Initialize OTP fields
        otpFields[0] = findViewById(R.id.num1);
        otpFields[1] = findViewById(R.id.num2);
        otpFields[2] = findViewById(R.id.num3);
        otpFields[3] = findViewById(R.id.num4);
        otpFields[4] = findViewById(R.id.num5);
        otpFields[5] = findViewById(R.id.num6);
        databaseReference = FirebaseDatabase.getInstance().getReference("users");
        verifyOtpButton=findViewById(R.id.img_rectangle);
        verifyOtpButton.setOnClickListener(v -> verifyOtp());
        setupOtpFields();
        phone=getIntent().getStringExtra("phone");


    }


    private void verifyOtp() {
        StringBuilder enteredOtp = new StringBuilder();

        for (EditText otpField : otpFields) {
            enteredOtp.append(otpField.getText().toString().trim());
        }

        if (enteredOtp.toString().equals(generatedOTP)) {
            Toast.makeText(otpgen.this, "OTP Verified Successfully!", Toast.LENGTH_SHORT).show();
            validateuser(phone);
        } else {
            Toast.makeText(otpgen.this, "Incorrect OTP. Please try again.", Toast.LENGTH_SHORT).show();
        }
    }

   d

    private void setupOtpFields() {
        for (int i = 0; i < otpFields.length; i++) {
            final int index = i;
            otpFields[i].addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (s.length() == 1 && index < otpFields.length - 1) {

                        otpFields[index + 1].requestFocus();
                    } else if (s.length() == 0 && index > 0) {

                        otpFields[index - 1].requestFocus();
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                    // No action needed after text change
                }
            });
        }
    }
}