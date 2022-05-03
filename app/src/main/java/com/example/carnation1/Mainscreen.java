package com.example.carnation1;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class Mainscreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainscreen);
        Button button = (Button) findViewById(R.id.button);

        button.setOnClickListener(v -> {
            Intent intent = new Intent(Mainscreen.this,MainActivity.class);
            startActivity(intent);
        });
        Button mypage_button = (Button) findViewById(R.id.mypage_button);
        mypage_button.setOnClickListener(v->{
            Intent intent = new Intent(Mainscreen.this,Mypage.class);
            startActivity(intent);
        });
    }

}