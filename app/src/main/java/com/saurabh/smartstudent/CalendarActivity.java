package com.saurabh.smartstudent;

import android.annotation.TargetApi;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.CalendarView;

import com.roomorama.caldroid.CaldroidFragment;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CalendarActivity extends AppCompatActivity {

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        CaldroidFragment caldroidFragment= new CaldroidFragment();
        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Attendance");
        }
        Bundle args = new Bundle();
        Calendar cal = Calendar.getInstance();
        args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
        args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
        args.putInt(CaldroidFragment.START_DAY_OF_WEEK, CaldroidFragment.MONDAY);
        caldroidFragment.setArguments(args);
        getSupportFragmentManager().beginTransaction().replace(R.id.content,caldroidFragment).commit();
        Log.d("abc",cal.getActualMaximum(Calendar.DAY_OF_MONTH)+"   "+cal.getActualMinimum(Calendar.DAY_OF_MONTH));
        for(int i=cal.getActualMinimum(Calendar.DAY_OF_MONTH);i<=cal.getActualMaximum(Calendar.DAY_OF_MONTH);i++){
            cal.set(cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),i);
            Date date= new Date(cal.getTimeInMillis());
            Log.d("abc",date.toString());
            caldroidFragment.setBackgroundResourceForDate(R.drawable.disabled_dates,date);
        }
        HashMap<String,Boolean> map = (HashMap<String, Boolean>) getIntent().getSerializableExtra("data");
        for(Map.Entry<String, Boolean> entry : map.entrySet()) {
            cal.set(cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),Integer.parseInt(entry.getKey()));
            Date date =new Date(cal.getTimeInMillis());
            if(entry.getValue()){
                caldroidFragment.setBackgroundResourceForDate(R.drawable.present_dates,date);
            }
            else{
                caldroidFragment.setBackgroundResourceForDate(R.drawable.absent_dates,date);
            }
        }
        caldroidFragment.refreshView();
    }

}
