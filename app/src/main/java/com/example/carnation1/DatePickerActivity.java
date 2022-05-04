package com.example.carnation1;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.TimePicker;
import java.util.Calendar;
import java.util.GregorianCalendar;


public class DatePickerActivity extends AppCompatActivity {
    private TimePicker timePicker;
    private TextView textView;
    private int mHour = 0, mMin = 0;
    private int mYear = 0, mMonth = 0, mDay = 0;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_date_picker);
        Calendar calendar = new GregorianCalendar();


        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH);
        mDay = calendar.get(Calendar.DAY_OF_MONTH);
        textView = findViewById(R.id.textView);
        timePicker = (TimePicker) findViewById(R.id.TimePicker);

        if(android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            mHour = timePicker.getHour();
            mMin = timePicker.getMinute();
        }else{
            mHour = timePicker.getCurrentHour();
            mMin = timePicker.getCurrentMinute();
        }

        DatePicker datePicker = findViewById(R.id.vDatePicker);
        datePicker.init(mYear, mMonth, mDay, mOnDateChangedListener);




    }



    public void mOnClick(View v) {
        Intent intent = new Intent();
        intent.putExtra("mYear", mYear);
        intent.putExtra("mMonth", mMonth);
        intent.putExtra("mDay", mDay);
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


}
