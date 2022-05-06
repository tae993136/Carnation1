package com.example.carnation1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import org.json.simple.JSONObject;

import java.util.Vector;

public class ParkingScreen extends AppCompatActivity {
    View.OnClickListener parkingSpot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parking_screen);
        Button button = (Button) findViewById(R.id.parkingScreen_BackButton);
        Vector<ImageButton> parkingButtons = new Vector<>(8);
        parkingButtons.add(findViewById(R.id.parking_button1));
        parkingButtons.add(findViewById(R.id.parking_button2));
        parkingButtons.add(findViewById(R.id.parking_button3));
        parkingButtons.add(findViewById(R.id.parking_button4));
        parkingButtons.add(findViewById(R.id.parking_button5));
        parkingButtons.add(findViewById(R.id.parking_button6));
        parkingButtons.add(findViewById(R.id.parking_button7));
        parkingButtons.add(findViewById(R.id.parking_button8));

        button.setOnClickListener(v -> {
            Intent intent = new Intent(ParkingScreen.this, MainScreen.class);
            startActivity(intent);
        });
        parkingSpot = view -> {
            JSONObject jsonObject = new JSONObject();
            AlertDialog.Builder builder = new AlertDialog.Builder(ParkingScreen.this);
            builder.setTitle("    ");
            builder.setMessage("이 자리에 예약하시겠습니까?");
            builder.setPositiveButton("아니오", null);
            builder.setNegativeButton("예", (dialogInterface, i) -> {
                Intent intent = new Intent(ParkingScreen.this, DatePickerActivity.class);
                intent.putExtra("parkingSpot", view.getTag().toString());
                startActivity(intent);
            });
            builder.create().show();
        };
        for (ImageButton parkingButton : parkingButtons) {
            parkingButton.setOnClickListener(parkingSpot);
        }
    }
}