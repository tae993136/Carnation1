package com.example.carnation1;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Mypage extends AppCompatActivity {
    private TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage);
        Intent intent = getIntent();
        String msg = intent.getStringExtra("mMonth");
        textView = (TextView)findViewById(R.id.textView);
        textView.setText(msg);
        Button button = (Button) findViewById(R.id.button);

        button.setOnClickListener(v -> {
            Intent intent1 = new Intent(Mypage.this,Mainscreen.class);
            startActivity(intent1);
        });
    }
}