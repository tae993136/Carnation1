package com.example.carnation1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.carnation1.Client.ServerConnection;

import org.json.simple.JSONObject;

public class management extends AppCompatActivity {
    View reservationInfoView;

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
        reservationInfoView = findViewById(R.id.management_ReservationInfo);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("type", "mypage");
        JSONObject jsonResult = ServerConnection.send(jsonObject);
        boolean isReservationExists = Integer.parseInt(jsonResult.get("reservationCount").toString()) > 0;
        ((TextView) reservationInfoView.findViewById(R.id.parkingLotReservationView_Status)).setText(
                isReservationExists ? "예약 중" : "예약 없음");
        if (isReservationExists) {
            ((ImageView) reservationInfoView.findViewById(R.id.parkingLotReservationView_thumbnail)).setImageResource(R.drawable.reservation_ok);
            String positionReserved = jsonResult.get("position").toString();
            ((TextView) reservationInfoView.findViewById(R.id.parkingLotReservationView_Position))
                    .setText(String.format("%c열 %s칸", positionReserved.charAt(0), positionReserved.substring(1)));
            ((TextView) reservationInfoView.findViewById(R.id.parkingLotReservationView_Date)).setText(
                    String.format("%s년 %s월 %s일",
                            jsonResult.get("year").toString(),
                            jsonResult.get("month").toString(),
                            jsonResult.get("day").toString()
                    ));
            int hour = Integer.parseInt(jsonResult.get("hour").toString());
            ((TextView) reservationInfoView.findViewById(R.id.parkingLotReservationView_Time)).setText(
                    String.format("%s %s시 ~ %s %s시",
                            hour > 11 ? "오후" : "오전",
                            hour % 12 == 0 ? 12 : hour % 12,
                            hour + 1 > 11 ? "오후" : "오전",
                            (hour + 1) % 12 == 0 ? 12 : (hour + 1) % 12
                    ));
        } else {
            ((ImageView) reservationInfoView.findViewById(R.id.parkingLotReservationView_thumbnail)).setImageResource(R.drawable.reservation_no);
            ((TextView) reservationInfoView.findViewById(R.id.parkingLotReservationView_Position)).setText("");
            ((TextView) reservationInfoView.findViewById(R.id.parkingLotReservationView_Date)).setText("");
            ((TextView) reservationInfoView.findViewById(R.id.parkingLotReservationView_Time)).setText("");
        }
    }
}