package com.example.carnation1;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.carnation1.Client.ServerConnection;

import org.json.simple.JSONObject;

public class MyPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage);
        findViewById(R.id.myPage_Reservation_button).setOnClickListener((view) -> startActivity(new Intent(this, management.class)));
        findViewById(R.id.myPage_BackButton).setOnClickListener((view) -> startActivity(new Intent(this, MainScreen.class)));
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("type", "validate");
        var result = ServerConnection.send(jsonObject);
        Log.d("Mypage", result.toJSONString());
    }
}