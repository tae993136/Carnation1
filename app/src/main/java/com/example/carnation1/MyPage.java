package com.example.carnation1;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class MyPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage);
        Intent intent = getIntent();

        String msg_Year = intent.getStringExtra("mYear") + "년 ";
        String msg_Month = intent.getStringExtra("mMonth") + "월 ";
        String msg_Day = intent.getStringExtra("mDay") + "일 ";
        String msg_Hour = intent.getStringExtra("mHour") + "시 ";
        String msg_Minute = intent.getStringExtra("mMin") + "분";
        String msg = msg_Year + msg_Month + msg_Day + msg_Hour + msg_Minute;
    }
}