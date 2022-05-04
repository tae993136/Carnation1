package com.example.carnation1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
import java.util.GregorianCalendar;


public class DatePickerActivity extends AppCompatActivity {
    public static final String TAG_MSG = "message";
    private TextView textView;
     int mHour = 0, mMin = 0;
     int mYear = 0, mMonth = 0, mDay = 0;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_date_picker);
        Calendar calendar = new GregorianCalendar();
        textView = (TextView)findViewById(R.id.textView);

        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH);
        mDay = calendar.get(Calendar.DAY_OF_MONTH);
        mHour = calendar.get(Calendar.HOUR_OF_DAY);
        mMin = calendar.get(Calendar.MINUTE);
        textView = findViewById(R.id.textView);

        TimePicker timePicker = (TimePicker) findViewById(R.id.TimePicker);


        DatePicker datePicker = findViewById(R.id.vDatePicker);
        datePicker.init(mYear, mMonth, mDay, mOnDateChangedListener);




    }



    public void mOnClick(View v) {
        Intent intent = new Intent();
       //final int REQUEST_CODE = 1;
        String str =Integer.toString(9999);

        intent.putExtra(TAG_MSG, mYear);

        intent.putExtra("mMonth","hello");
        intent.putExtra("mDay", mDay);
        intent.putExtra("mHour", mHour);
        intent.putExtra("mMin", mMin);
       // String msg = intent.getStringExtra("mYear");

        //startActivityForResult(intent, 1);
        setResult(RESULT_OK, intent);
        finish();
    }


    DatePicker.OnDateChangedListener mOnDateChangedListener =
            new DatePicker.OnDateChangedListener() {

        @Override

        public void onDateChanged(DatePicker datePicker, int yy, int mm, int dd) {
            mYear = yy;
            mMonth = mm;
            mDay = dd;
        }

    };
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String msg = data.getStringExtra("mYear");
        textView.setText(msg);
    }

}
