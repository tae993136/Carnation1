package com.example.carnation1;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MyPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage);
        ((Button) findViewById(R.id.myPage_Reservation_button)).setOnClickListener((view) -> {
            startActivity(new Intent(this, management.class));
        });
    }
}