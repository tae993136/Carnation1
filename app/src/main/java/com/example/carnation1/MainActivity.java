package com.example.carnation1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.carnation1.Client.ServerConnection;

import org.json.simple.JSONObject;

public class MainActivity extends AppCompatActivity {
    /* IP: 34.132.25.146
     *  포트 번호는 7030
     */
    EditText idText;
    EditText pwText;
    Button button;
    boolean isBackPressed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        idText = (EditText) findViewById(R.id.idText);
        pwText = (EditText) findViewById(R.id.pwText);
        button = (Button) findViewById(R.id.buttonLogin);
        if (ServerConnection.connect())
            Log.d("#MainActivity", "Connected to server");
        findViewById(R.id.main_loadingScreen).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });
        button.setOnClickListener(v -> {
            setLoadingScreen(true);

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("type", "login");
            jsonObject.put("id", idText.getText().toString());
            jsonObject.put("pw", pwText.getText().toString());
            JSONObject response = ServerConnection.send(jsonObject);
            Log.d("#MainActivity", response != null ? response.toJSONString() : "NULL");
            setLoadingScreen(false);
            if (response == null)
                Toast.makeText(this, "서버 연결 오류", Toast.LENGTH_SHORT).show();
            else if (response.containsKey("result") && response.get("result").equals("OK")) {
                ServerConnection.sessionNumber = (long) response.get("sessionNumber");
                ServerConnection.userNumber = response.get("userNumber").toString();
                startActivity(new Intent(this, Mainscreen.class));
            } else {
                Toast.makeText(this, "로그인 실패", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        ServerConnection.disconnect();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (isBackPressed)
            finishAffinity();
        Toast.makeText(this, "다시 누르시면 앱이 종료됩니다.", Toast.LENGTH_SHORT).show();
        new Thread(() -> {
            isBackPressed = true;
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            isBackPressed = false;
        }).start();
    }

    private void setLoadingScreen(boolean value) {
        findViewById(R.id.main_loadingScreen).setVisibility(value ? View.VISIBLE : View.GONE);
    }
}