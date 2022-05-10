package com.example.carnation1;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainscreen);
        Button button = (Button) findViewById(R.id.myPage_BackButton);
        Button parking_Button = (Button)findViewById(R.id.parking_button);
        Button mypage_button = (Button) findViewById(R.id.myPage_button);
        button.setOnClickListener(v -> {
            Intent intent = new Intent(MainScreen.this,MainActivity.class);
            startActivity(intent);
        });
        //todo
        parking_Button.setOnClickListener(v -> {
            Intent intent = new Intent(MainScreen.this,ParkingScreen.class);
            startActivity(intent);
        });

        mypage_button.setOnClickListener(v -> {
            Intent intent = new Intent(MainScreen.this, MyPage.class);
            startActivity(intent);
        });


    }

}