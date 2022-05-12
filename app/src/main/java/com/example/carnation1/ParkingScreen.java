package com.example.carnation1;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.carnation1.Client.ServerConnection;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;

public class ParkingScreen extends AppCompatActivity {

    protected static final int RESULT_CODE_DATEPICKER = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parking_screen);
        findViewById(R.id.myPage_BackButton).setOnClickListener(v -> {
            Intent intent = new Intent(ParkingScreen.this, MainScreen.class);
            startActivity(intent);
        });
        findViewById(R.id.parkingScreen_Refresh).setOnClickListener(v -> loadParkingLotStructure());
        loadParkingLotStructure();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode== RESULT_CODE_DATEPICKER && resultCode == RESULT_OK)
            finish();
    }

    private void loadParkingLotStructure() {
        ListView listViewLeft = findViewById(R.id.parkingLotView_List_Left);
        ListView listViewRight = findViewById(R.id.parkingLotView_List_Right);
        ParkingLotListAdapter adapterLeft = new ParkingLotListAdapter(this, "L");
        ParkingLotListAdapter adapterRight = new ParkingLotListAdapter(this, "R");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("type", "parkingLotStructure");
        JSONObject jsonResult = ServerConnection.send(jsonObject);
        if (jsonResult != null && jsonResult.get("result").toString().equals("OK")) {
            Log.d("#ParkingScreen", jsonResult.toJSONString());
            for (int i = 0; i < ((JSONArray) jsonResult.get("data")).size(); i++) {
                JSONObject item = (JSONObject) ((JSONArray) jsonResult.get("data")).get(i);
                adapterLeft.addItem(Integer.parseInt(item.get("name").toString()), item.get("position").toString());
                adapterRight.addItem(Integer.parseInt(item.get("name").toString()), item.get("position").toString());
            }
            listViewLeft.setAdapter(adapterLeft);
            listViewRight.setAdapter(adapterRight);
            adapterLeft.notifyDataSetChanged();
            adapterRight.notifyDataSetChanged();
        } else {
            Toast.makeText(this, "주차장 시스템과의 연결이 되지 않습니다.", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    class ParkingLotListAdapter extends BaseAdapter {
        private final String TAG = this.getClass().getSimpleName();
        private final Activity activity;
        private final ArrayList<ItemData> arrayList = new ArrayList<>();
        private String direction = null;

        public ParkingLotListAdapter(Activity activity) {
            this.activity = activity;
        }

        public ParkingLotListAdapter(Activity activity, String direction) {
            this(activity);
            this.direction = direction;
        }

        public void clear() {
            arrayList.clear();
            this.notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return arrayList.size();
        }

        @Override
        public Object getItem(int position) {
            return arrayList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater inflater = activity.getLayoutInflater();
                if (direction.equals("L"))
                    convertView = inflater.inflate(R.layout.parkinglot_list_item_left, parent, false);
                else
                    convertView = inflater.inflate(R.layout.parkinglot_list_item_right, parent, false);
            }
            ConstraintLayout constraintLayout = (ConstraintLayout) convertView;
            ItemData itemData = arrayList.get(position);
            ImageButton imageButton = constraintLayout.findViewById(R.id.parkingLot_Item_Image);
            TextView textView = constraintLayout.findViewById(R.id.parkingLot_Item_Text);

            imageButton.setImageResource(itemData.getDirection().equals("L") ? R.drawable.parking_lot_left : R.drawable.parking_lot_right);
            textView.setText(String.valueOf(itemData.getNumber()));
            @SuppressLint("DefaultLocale")
            String cellName = String.format("%s열 %d번", itemData.getDirection(), itemData.getPositionNum());
            imageButton.setOnClickListener(v -> {
                Intent intent = new Intent(ParkingScreen.this, DatePickerActivity.class);
                intent.putExtra("parkingSpot", String.valueOf(itemData.number))
                .putExtra("position", cellName);
                startActivityForResult(intent, RESULT_CODE_DATEPICKER);
            });

            return constraintLayout;
        }

        public void addItem(int number, String position) {
            ItemData item = new ItemData(number, position);
            if (item.getDirection().equals(direction))
                arrayList.add(item);
        }
    }

    static class ItemData {
        private int number = 0;
        private int positionNum = 0;
        private String direction = null;

        public ItemData(int number, String position) {
            this.number = number;
            if (position.charAt(0) == 'L')
                direction = "L";
            else direction = "R";
            positionNum = Integer.parseInt(position.substring(1));
        }

        public int getNumber() {
            return number;
        }

        public int getPositionNum() {
            return positionNum;
        }

        public String getDirection() {
            return direction;
        }
    }
}