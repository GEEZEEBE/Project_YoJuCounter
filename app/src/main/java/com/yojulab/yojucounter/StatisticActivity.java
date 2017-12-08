package com.yojulab.yojucounter;

import android.app.DatePickerDialog;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import com.yojulab.yojucounter.database.DBProvider;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class StatisticActivity extends AppCompatActivity {

    private DBProvider db ;

    TextView pickupDate ;
    ImageView previousDate;
    ImageView nextDate;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic);

        setupLayoutView();
    }

    private void setupLayoutView(){
        db = new DBProvider(this);
        db.open();

        pickupDate = (TextView) findViewById(R.id.pickup_date);
        pickupDate.setText(db.getDateFormat(0));
        pickupDate.setOnClickListener(onClickListener);
        previousDate = (ImageView) findViewById(R.id.previous_date);
        previousDate.setOnClickListener(onClickListener);
        nextDate = (ImageView) findViewById(R.id.next_date);
        nextDate.setOnClickListener(onClickListener);

        textView = (TextView) findViewById(R.id.textView2);

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(getListToString(pickupDate.getText().toString()));
        textView.setText(stringBuilder);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int viewId = view.getId();
            switch (viewId){
                case R.id.pickup_date:
                    // calender class's instance and get current date , month and year from calender
                    final Calendar c = Calendar.getInstance();
                    int mYear = c.get(Calendar.YEAR); // current year
                    int mMonth = c.get(Calendar.MONTH); // current month
                    int mDay = c.get(Calendar.DAY_OF_MONTH); // current day

                    // date picker dialog
                    new DatePickerDialog(view.getContext(),new DatePickerDialog.OnDateSetListener() {

                                @Override
                                public void onDateSet(DatePicker view, int year,
                                                      int monthOfYear, int dayOfMonth) {
                                    // set day of month , month and year value in the edit text
                                    pickupDate.setText(year+(monthOfYear + 1)+dayOfMonth + "");

                                    StringBuilder stringBuilder = new StringBuilder();
                                    stringBuilder.append(getListToString(pickupDate.getText().toString()));

                                    textView.setText(stringBuilder);
                                }
                            }, mYear, mMonth, mDay).show();
                    break;
                case R.id.previous_date:
                    break;
                case R.id.next_date:
                    break;
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }

    protected StringBuilder getListToString(String pickupDate){
        StringBuilder str = new StringBuilder();

        ArrayList<HashMap<String,Object>> arrayList = (ArrayList<HashMap<String, Object>>) db.getDailyStatisticAsDate(pickupDate);

        int cnt = 0;
        for(HashMap<String,Object> hashMap:arrayList){
            str.append(hashMap.get("counter_name").toString()).append(" : ")
                    .append(hashMap.get("count_number").toString())
                    .append("\n");
        }
        return str;
    }
}
