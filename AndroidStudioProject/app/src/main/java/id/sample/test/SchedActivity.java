package id.sample.test;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import java.util.Calendar;

public class SchedActivity extends AppCompatActivity {
    //variables declaration
    TextView sked;
    Calendar mCurrentDate;
    int day, month, year;
    SharedPreferences prefs;
    public static final String mypreference = "mypref";
    public static final String Date = "dateKey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sched);
        sked = (TextView) findViewById(R.id.textView4);
        //variables to retrieve date from calendar
        mCurrentDate = Calendar.getInstance();
        day = mCurrentDate.get(Calendar.DAY_OF_MONTH);
        month = mCurrentDate.get(Calendar.MONTH);
        year = mCurrentDate.get(Calendar.YEAR);
        month = month +1;
        prefs = getSharedPreferences(mypreference,
                Context.MODE_PRIVATE);
        if (prefs.contains(Date)) {
            sked.setText(prefs.getString(Date, ""));
        }
        else {
            sked.setText(day+"/"+month+"/"+year);
        }
        //code to set text view as date the user picked from calendar on click
        sked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sked = (TextView) findViewById(R.id.textView4);
                DatePickerDialog datePickerDialog = new DatePickerDialog(SchedActivity.this, new DatePickerDialog.OnDateSetListener(){
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfmonth)
                    {
                        monthOfYear = monthOfYear+1;
                        String n= dayOfmonth+"/"+monthOfYear+"/"+year;
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString(Date, n);
                        editor.apply();
                        if (prefs.contains(Date)) {
                            sked.setText(prefs.getString(Date, ""));
                        }
                    }
                }, year,month,day);
                datePickerDialog.show();
            }
        });

    }
}
