package com.example.carnation1;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.carnation1.Client.ServerConnection;

import org.json.simple.JSONObject;

public class ParkingScreen extends AppCompatActivity {
    View.OnClickListener parkingSpot;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parking_screen);
        Button button = (Button) findViewById(R.id.button);

        ImageButton parkingButton1 = findViewById(R.id.parking_button1);
        ImageButton parkingButton2 = findViewById(R.id.parking_button2);
        ImageButton parkingButton3 = findViewById(R.id.parking_button3);
        ImageButton parkingButton4 = findViewById(R.id.parking_button4);
        ImageButton parkingButton5 = findViewById(R.id.parking_button5);
        ImageButton parkingButton6 = findViewById(R.id.parking_button6);
        ImageButton parkingButton7 = findViewById(R.id.parking_button7);
        ImageButton parkingButton8 = findViewById(R.id.parking_button8);


        button.setOnClickListener(v -> {
            Intent intent = new Intent(ParkingScreen.this,Mainscreen.class);
            startActivity(intent);
        });
        parkingSpot = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONObject jsonObject = new JSONObject();
                switch (view.getId()) {
                    case R.id.parking_button1:
                            AlertDialog.Builder builder = new AlertDialog.Builder(ParkingScreen.this);
                            builder.setTitle("    ");
                            builder.setMessage("이 자리에 예약하시겠습니까?");
                            builder.setPositiveButton("아니오",null);
                            builder.setNegativeButton("예",new DialogInterface.OnClickListener(){
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Intent intent = new Intent(ParkingScreen.this,DatePickerActivity.class);
                                    intent.putExtra("parkingSpot","1");
                                    startActivity(intent);
                                }
                            });
                            builder.create().show();
                        break;

                    case R.id.parking_button2:
                        AlertDialog.Builder builder2 = new AlertDialog.Builder(ParkingScreen.this);
                        builder2.setTitle("    ");
                        builder2.setMessage("이 자리에 예약하시겠습니까?");
                        builder2.setPositiveButton("아니오",null);
                        builder2.setNegativeButton("예",new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent = new Intent(ParkingScreen.this,DatePickerActivity.class);
                                intent.putExtra("parkingSpot","2");
                                startActivity(intent);
                            }
                        });
                        builder2.create().show();
                        break;

                    case R.id.parking_button3:
                        Toast.makeText(getApplicationContext(), "Button 3", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.parking_button4:
                        Toast.makeText(getApplicationContext(), "Button 4", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.parking_button5:
                        Toast.makeText(getApplicationContext(), "Button 5", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.parking_button6:
                        Toast.makeText(getApplicationContext(), "Button 6", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.parking_button7:
                        Toast.makeText(getApplicationContext(), "Button 7", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.parking_button8:
                        Toast.makeText(getApplicationContext(), "Button 8", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };
        /*parking_Button1.setOnClickListener(v->{
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

        });*/
    }
}