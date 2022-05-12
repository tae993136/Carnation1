package com.example.carnation1;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MyPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage);

        findViewById(R.id.myPage_BackButton).setOnClickListener(v -> {
            Intent intent = new Intent(MyPage.this, MainScreen.class);
            startActivity(intent);
        });
        findViewById(R.id.User_button).setOnClickListener(v -> {
            Intent intent = new Intent(MyPage.this, management.class);
            startActivity(intent);
        });


    }
}