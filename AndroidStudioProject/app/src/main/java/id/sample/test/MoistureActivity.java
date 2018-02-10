package id.sample.test;

import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import static id.sample.test.WifiActivity.RESPONSE;

public class MoistureActivity extends AppCompatActivity {
    //define custom font
    Typeface dopestyle;
    Typeface lightfont;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moisture);
        //set custom font
        lightfont = Typeface.createFromAsset(getAssets(),"fonts/TheLightFont.ttf");
        dopestyle = Typeface.createFromAsset(getAssets(),"fonts/Dopestyle.ttf");
        TextView condimoist = (TextView) findViewById(R.id.condimoist);
        TextView conmoist = (TextView) findViewById(R.id.conmoist);
        condimoist.setTypeface(lightfont);
        conmoist.setTypeface(dopestyle);
        //get value sent by the wifi server before
        SharedPreferences pref = getSharedPreferences("HTTP_HELPER_PREFS",MODE_PRIVATE);
        if (pref.contains(RESPONSE)) {
            conmoist.setText(pref.getString(RESPONSE, ""));
        }
        else {
            conmoist.setText("-");
            condimoist.setText("NOT CONNECTED");
        }
    }
}
