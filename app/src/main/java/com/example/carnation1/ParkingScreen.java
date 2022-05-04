package com.example.carnation1;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class ParkingScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parking_screen);
        Button button = (Button) findViewById(R.id.button);
        ImageButton parking_Button1 = (ImageButton) findViewById(R.id.parking_button1);

        button.setOnClickListener(v -> {
            Intent intent = new Intent(ParkingScreen.this,Mainscreen.class);
            startActivity(intent);
        });

        parking_Button1.setOnClickListener(v->{
            AlertDialog.Builder builder = new AlertDialog.Builder(ParkingScreen.this);
            builder.setTitle("    ");
            builder.setMessage("이 자리에 예약하시겠습니까?");
            builder.setPositiveButton("아니오",null);
            builder.setNegativeButton("예",new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(ParkingScreen.this,DatePickerActivity.class);
                startActivity(intent);
            }
                });

            builder.create().show();

        });
    }
}