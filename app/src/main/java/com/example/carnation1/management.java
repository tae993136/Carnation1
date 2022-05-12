package com.example.carnation1;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.carnation1.Client.ServerConnection;

import org.json.simple.JSONObject;
import org.w3c.dom.Text;

public class management extends AppCompatActivity {

    private TextView textView_ReservationStatus;
    private TextView textView_ReservationDateTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_management);
        Button cancelButton = (Button) findViewById(R.id.management_CancelReservation);
        findViewById(R.id.management_BackButton).setOnClickListener(v -> {
            Intent intent1 = new Intent(management.this, MyPage.class);
            startActivity(intent1);
        });
        cancelButton.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("인사말").setMessage("반갑습니다");
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        });

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("type", "mypage");
        JSONObject jsonResult = ServerConnection.send(jsonObject);
        boolean isReservationExists = Integer.parseInt(jsonResult.get("reservationCount").toString()) > 0;
        ((TextView) findViewById(R.id.management_TextReservationInfo_Status)).setText(
                isReservationExists ? "예약 중" : "예약 없음");
        if (isReservationExists) {
            String positionReserved = jsonResult.get("position").toString();
            ((TextView) findViewById(R.id.management_TextReservationInfo_Position))
                    .setText(String.format("%c열 %s칸", positionReserved.charAt(0), positionReserved.substring(1)));
            ((TextView) findViewById(R.id.management_TextReservationInfo_DateTime)).setText(
                    String.format("%s년 %s월 %s일 %s %s시",
                            jsonResult.get("year").toString(),
                            jsonResult.get("month").toString(),
                            jsonResult.get("day").toString(),
                            Integer.parseInt(jsonResult.get("hour").toString()) > 11 ? "오후" : "오전",
                            jsonResult.get("hour").toString()
                    ));
        }
        else
        {
            findViewById(R.id.management_LayoutReservationInfo_Position).setVisibility(View.GONE);
            findViewById(R.id.management_LayoutReservationInfo_DateTime).setVisibility(View.GONE);
        }
    }
}