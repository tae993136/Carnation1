package com.example.carnation1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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
    EditText editText;
    Button button;
    Socket socket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView) findViewById(R.id.textView);
        editText = (EditText) findViewById(R.id.editText);
        button = (Button) findViewById(R.id.button);
        socket = new Socket();

        button.setOnClickListener(v -> {
            send(editText.getText().toString());
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
                String message;
                OutputStream os = socket.getOutputStream();
                bytes = data.getBytes(StandardCharsets.UTF_8);
                os.write(bytes);
                os.flush();

                InputStream is = socket.getInputStream();
                bytes = new byte[100];
                int readByteCount = is.read(bytes);
                message = new String(bytes, 0, readByteCount, StandardCharsets.UTF_8);
                String finalMessage = message;


                runOnUiThread(() -> textView.setText(finalMessage));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        thread.start();
    }
}