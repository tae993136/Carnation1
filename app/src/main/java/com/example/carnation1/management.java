package com.example.carnation1;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.carnation1.Client.ServerConnection;

import org.json.simple.JSONObject;

public class management extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_management);
        Button cancelButton = (Button) findViewById(R.id.management_CancelReservation);

        findViewById(R.id.management_BackButton).setOnClickListener(v -> {
            Intent intent = new Intent(management.this, MyPage.class);
            startActivity(intent);
        });

        cancelButton.setOnClickListener(v -> {

                AlertDialog.Builder builder = new AlertDialog.Builder(management.this);
                builder.setTitle("예약 취소");
                builder.setMessage("예약을 취소하시겠습니까?");

            builder.setPositiveButton("예", (dialog, id) -> {
                //"예" 버튼 클릭시 실행하는 메소드

                JSONObject jsonObject = new JSONObject();
                jsonObject.put("type", "cancel");
                JSONObject jsonResult = ServerConnection.send(jsonObject);

                Toast.makeText(getBaseContext(),"취소가 완료되었습니다.", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(management.this, MyPage.class);
                startActivity(intent);
            });
            builder.setNegativeButton("아니오", null);

                builder.create().show();

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
                    String.format("%s년 %s월 %s일 %s시",
                            jsonResult.get("year").toString(),
                            jsonResult.get("month").toString(),
                            jsonResult.get("day").toString(),
                            jsonResult.get("hour").toString()
                    ));
        } else {
            findViewById(R.id.management_LayoutReservationInfo_Position).setVisibility(View.GONE);
            findViewById(R.id.management_LayoutReservationInfo_DateTime).setVisibility(View.GONE);
        }
    }
}