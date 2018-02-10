package id.sample.test;

import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import static id.sample.test.WifiActivity.RESPONSE;

public class LightActivity extends AppCompatActivity {
    //define custom font
    Typeface dopestyle;
    Typeface lightfont;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_light);
        //set custom font
        lightfont = Typeface.createFromAsset(getAssets(),"fonts/TheLightFont.ttf");
        dopestyle = Typeface.createFromAsset(getAssets(),"fonts/Dopestyle.ttf");
        TextView condilight = (TextView) findViewById(R.id.condilight);
        TextView conlight = (TextView) findViewById(R.id.conlight);
        condilight.setTypeface(lightfont);
        conlight.setTypeface(dopestyle);
        //get value sent by the wifi server before
        SharedPreferences pref = getSharedPreferences("HTTP_HELPER_PREFS",MODE_PRIVATE);
        if (pref.contains(RESPONSE)) {
            conlight.setText(pref.getString(RESPONSE, ""));
        }
        else {
            conlight.setText("-");
            condilight.setText("NOT CONNECTED");
        }
    }
}
