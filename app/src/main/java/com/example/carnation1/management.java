package com.example.carnation1;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.carnation1.Client.ServerConnection;

import org.json.simple.JSONObject;

public class management extends AppCompatActivity {
    View reservationInfoView;

    @SuppressLint("DefaultLocale")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_management);
        Button cancelButton = findViewById(R.id.management_CancelReservation);

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

                Toast.makeText(getBaseContext(), "취소가 완료되었습니다.", Toast.LENGTH_LONG).show();
                finish();
            });
            builder.setNegativeButton("아니오", null);

            builder.create().show();

        });
        reservationInfoView = findViewById(R.id.management_ReservationInfo);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("type", "mypage");
        JSONObject jsonResult = ServerConnection.send(jsonObject);
        boolean isReservationExists = Long.parseLong(jsonResult.get("reservationCount").toString()) > 0L;
        if (isReservationExists) {
            switch (jsonResult.get("status").toString()) {
                case "USING":
                    ((TextView) reservationInfoView.findViewById(R.id.parkingLotReservationView_Status)).setText("이용 중");
                case "ACTIVE":
                default:
                    ((TextView) reservationInfoView.findViewById(R.id.parkingLotReservationView_Status)).setText("예약 중");
            }

            ((ImageView) reservationInfoView.findViewById(R.id.parkingLotReservationView_thumbnail)).setImageResource(R.drawable.reservation_ok);
            String positionReserved = jsonResult.get("position").toString();
            ((TextView) reservationInfoView.findViewById(R.id.parkingLotReservationView_Position))
                    .setText(String.format("%c열 %s칸", positionReserved.charAt(0), positionReserved.substring(1)));
            ((TextView) reservationInfoView.findViewById(R.id.parkingLotReservationView_Date)).setText(
                    String.format("%d년 %d월 %d일",
                            jsonResult.get("year"),
                            jsonResult.get("month"),
                            jsonResult.get("day")
                    ));
            long hour = (long) jsonResult.get("hour");
            ((TextView) reservationInfoView.findViewById(R.id.parkingLotReservationView_Time)).setText(
                    String.format("%s %d시 ~ %s %d시",
                            hour > 11 ? "오후" : "오전",
                            hour % 12 == 0 ? 12 : hour % 12,
                            hour + 1 > 11 ? "오후" : "오전",
                            (hour + 1) % 12 == 0 ? 12 : (hour + 1) % 12
                    ));
        } else {
            ((TextView) reservationInfoView.findViewById(R.id.parkingLotReservationView_Status)).setText("예약 없음");
            ((ImageView) reservationInfoView.findViewById(R.id.parkingLotReservationView_thumbnail)).setImageResource(R.drawable.reservation_no);
            ((TextView) reservationInfoView.findViewById(R.id.parkingLotReservationView_Position)).setText("");
            ((TextView) reservationInfoView.findViewById(R.id.parkingLotReservationView_Date)).setText("");
            ((TextView) reservationInfoView.findViewById(R.id.parkingLotReservationView_Time)).setText("");
            findViewById(R.id.management_CancelReservation).setVisibility(View.GONE);
        }
    }
}