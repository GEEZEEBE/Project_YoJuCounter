package com.yojulab.yojucounter;

import android.app.DatePickerDialog;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
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

    protected BarChart mChart;

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

    protected void drawChart(){
        Typeface mTfLight = Typeface.createFromAsset(getAssets(), "OpenSans-Light.ttf");

        mChart = (BarChart) findViewById(R.id.barChart);
//        mChart.setOnChartValueSelectedListener(this);

        mChart.setDrawBarShadow(false);
        mChart.setDrawValueAboveBar(true);

        mChart.getDescription().setEnabled(false);

        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
        mChart.setMaxVisibleValueCount(60);

        // scaling can now only be done on x- and y-axis separately
        mChart.setPinchZoom(false);

        mChart.setDrawGridBackground(false);
        // mChart.setDrawYLabels(false);

//        IAxisValueFormatter xAxisFormatter = new DayAxisValueFormatter(mChart);

        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTypeface(mTfLight);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f); // only intervals of 1 day
        xAxis.setLabelCount(7);
//        xAxis.setValueFormatter(xAxisFormatter);

//        IAxisValueFormatter custom = new MyAxisValueFormatter();

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setTypeface(mTfLight);
        leftAxis.setLabelCount(8, false);
//        leftAxis.setValueFormatter(custom);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setSpaceTop(15f);
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        Legend l = mChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setForm(Legend.LegendForm.SQUARE);
        l.setFormSize(9f);
        l.setTextSize(11f);
        l.setXEntrySpace(4f);

//        MarkerView mv = new CustomMarkerView(this,R.layout.content_marker_view);
//        mv.setChartView(mChart); // For bounds control
//        mChart.setMarker(mv); // Set the marker to the chart
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int viewId = view.getId();
            StringBuilder stringBuilder = null;
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
                                    pickupDate.setText(db.getDateFormat(year,monthOfYear,dayOfMonth));

                                    StringBuilder stringBuilder = new StringBuilder();
                                    stringBuilder.append(getListToString(pickupDate.getText().toString()));

                                    textView.setText(stringBuilder);
                                }
                            }, mYear, mMonth, mDay).show();
                    break;
                case R.id.previous_date:
                    pickupDate.setText(db.getDateFormat(-1, pickupDate.getText().toString()));
                    stringBuilder = new StringBuilder();
                    stringBuilder.append(getListToString(pickupDate.getText().toString()));

                    textView.setText(stringBuilder);
                    break;
                case R.id.next_date:
                    pickupDate.setText(db.getDateFormat(1, pickupDate.getText().toString()));
                    stringBuilder = new StringBuilder();
                    stringBuilder.append(getListToString(pickupDate.getText().toString()));

                    textView.setText(stringBuilder);
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
