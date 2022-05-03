package com.example.carnation1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.*;
import org.json.simple.parser.JSONParser;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class MainActivity extends AppCompatActivity {
    /* IP: 104.197.76.225
     *  포트 번호는 7030
     */
    TextView textView;
    EditText idText;
    EditText pwText;
    Button button;
    Socket socket;
    char next_context = 0;
    String userNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView) findViewById(R.id.textView);

        idText = (EditText) findViewById(R.id.idText);
        pwText = (EditText) findViewById(R.id.pwText);
        button = (Button) findViewById(R.id.button);
        socket = new Socket();


        button.setOnClickListener(v -> {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("type", "login");
                jsonObject.put("id", idText.getText().toString());
                jsonObject.put("pw", pwText.getText().toString());
                send(jsonObject.toString());


            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (next_context==0)
            {
                Intent intent = new Intent(MainActivity.this, Mainscreen.class);
                startActivity(intent);
            }

        });
    }

    @Override
    protected void onDestroy() {
        if (!socket.isClosed()) {
            try {
                socket.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        super.onDestroy();
    }

    public void send(String data) {
        Thread thread = new Thread(() -> {
            try {
                if (!socket.isConnected())
                    socket.connect(new InetSocketAddress("104.197.76.225", 7030));

                byte[] bytes;
                String id_message;
                OutputStream os = socket.getOutputStream();
                bytes = data.getBytes(StandardCharsets.UTF_8);
                os.write(bytes);
                os.flush();

                InputStream is = socket.getInputStream();
                bytes = new byte[100];
                int readByteCount = is.read(bytes);
                id_message = new String(bytes, 0, readByteCount, StandardCharsets.UTF_8);

                JSONObject jsonObject = (JSONObject) new JSONParser().parse(id_message);
                if (jsonObject.get("result").equals("OK"))
                {
                    userNumber = jsonObject.get("userNumber").toString();
                }
                String finalMessage = id_message;
                runOnUiThread(() -> textView.setText(finalMessage));

            } catch (Exception e) {
                e.printStackTrace();
            }

        });
        thread.start();
    }
}