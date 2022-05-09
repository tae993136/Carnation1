package com.example.carnation1.Client;

import android.os.AsyncTask;
import android.util.Log;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class ServerConnection {
    private static final ServerConnection instance = new ServerConnection();
    private static final String IP = "34.68.12.173";
    private static final int PORT = 7030;
    private static final String TYPE = "type";
    private static final String SESSION_NUMBER = "sessionNumber";
    private static final String USER_NUMBER = "userNumber";
    private static Socket socket;
    private static InputStream is;
    private static OutputStream os;
    public static long sessionNumber;
    public static String userNumber;

    public static Socket getSocket() {
        return socket;
    }

    private ServerConnection() {
    }

    public static ServerConnection getInstance() {
        return instance;
    }

    private static Boolean connect(String ip) {
        if (socket == null) socket = new Socket();
        if (socket.isConnected() && !socket.isClosed())
            return true;
        AsyncTask<Void, Void, Boolean> connector = new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(Void... voids) {
                try {
                    socket.connect(new InetSocketAddress(ip, PORT));
                    os = socket.getOutputStream();
                    is = socket.getInputStream();
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                }
                return true;
            }
        };

        boolean result = false;
        Log.d("#ServerConnection", "Connecting...");
        connector.execute();
        try {
            result = connector.get(5000, TimeUnit.MILLISECONDS);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            return false;
        }
        return result;
    }

    public static boolean connectToLocalhost() {
        return connect("127.0.0.1");
    }

    public static Boolean connect() {
        return connect(IP);
    }

    public static void disconnect() {
        if (!socket.isClosed()) {
            try {
                socket.close();
            } catch (IOException ignored) {
            }
        }
    }

    public static JSONObject send(JSONObject data) {
        AsyncTask<JSONObject, Void, JSONObject> sender = new AsyncTask<JSONObject, Void, JSONObject>() {
            @Override
            protected JSONObject doInBackground(JSONObject... data) {
                if (socket == null || socket.isClosed())
                    return null;
                Log.d("#ServerConnection", data[0].toJSONString());
                try {
                    os.write(data[0].toJSONString().getBytes(StandardCharsets.UTF_8));
                    os.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
                try {
                    byte[] buffer = new byte[1000];
                    String message;
                    int len = is.read(buffer);
                    message = new String(buffer, 0, len, StandardCharsets.UTF_8);
                    Log.d("#ServerConnection", "Recieved " + message);
                    return (JSONObject) new JSONParser().parse(message);
                } catch (IOException | ParseException e) {
                    return null;
                }
            }
        };

        if (!data.containsKey(SESSION_NUMBER))
            data.put(SESSION_NUMBER, sessionNumber);
        if (!data.containsKey(USER_NUMBER))
            data.put(USER_NUMBER, userNumber);

        sender.execute(data);
        JSONObject result = null;
        try {
            result = sender.get(5000, TimeUnit.MILLISECONDS);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            return null;
        }
        return result;
    }
}
