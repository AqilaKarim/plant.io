package id.sample.test;

import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import static id.sample.test.WifiActivity.RESPONSE;

public class TempActivity extends AppCompatActivity {
    //define custom font
    Typeface dopestyle;
    Typeface lightfont;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temp);
        //set custom font
        lightfont = Typeface.createFromAsset(getAssets(),"fonts/TheLightFont.ttf");
        dopestyle = Typeface.createFromAsset(getAssets(),"fonts/Dopestyle.ttf");
        TextView conditemp = (TextView) findViewById(R.id.conditemp);
        TextView contemp = (TextView) findViewById(R.id.contemp);
        conditemp.setTypeface(lightfont);
        contemp.setTypeface(dopestyle);
        //get value sent by the wifi server before
        SharedPreferences pref = getSharedPreferences("HTTP_HELPER_PREFS",MODE_PRIVATE);
        if (pref.contains(RESPONSE)) {
            contemp.setText(pref.getString(RESPONSE, ""));
        }
        else {
            contemp.setText("-");
            conditemp.setText("NOT CONNECTED");
        }
    }
}
