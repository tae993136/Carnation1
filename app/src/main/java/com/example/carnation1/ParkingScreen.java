package com.example.carnation1;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class ParkingScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage);
        Button button = (Button) findViewById(R.id.button);

        button.setOnClickListener(v -> {
            Intent intent = new Intent(ParkingScreen.this,Mainscreen.class);
            startActivity(intent);
        });
    }
}