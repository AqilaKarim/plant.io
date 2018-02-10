package id.sample.test;
//RPL Project Group 9
//Aqila Fathimah Karim 1506734664
//Whisnu Samudra 15066673706
//Haekal Febriansyah 1506744280
//2017

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;


public class MainActivity extends AppCompatActivity {
    //codes used to pass shared preferences (to save data user inputs eventhough the app is closed)
    //data will only be erased if the user reinstall app or clear app data from settings
    SharedPreferences sharedpreferences;
    public static final String mypreference = "mypref";
    public static final String Name = "nameKey";
    //define custom font styles
    Typeface dopestyle;
    Typeface lightfont;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //set custom font styles
        lightfont = Typeface.createFromAsset(getAssets(),"fonts/TheLightFont.ttf");
        dopestyle = Typeface.createFromAsset(getAssets(),"fonts/Dopestyle.ttf");
        TextView mood = (TextView) findViewById(R.id.Mood);
        mood.setTypeface(lightfont);

        TextView name = (TextView) findViewById(R.id.Plantname);
        //display saved name if available
        sharedpreferences = getSharedPreferences(mypreference,
                Context.MODE_PRIVATE);
        if (sharedpreferences.contains(Name)) {
            name.setText(sharedpreferences.getString(Name, ""));
        }

        //codes used to activate button for navigating between activities on click
        ImageButton moist=(ImageButton)findViewById(R.id.moist);
        moist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent MoistureScreen=new Intent(getApplicationContext(),MoistureActivity.class);
                startActivity(MoistureScreen);
            }
        });


        ImageButton light=(ImageButton)findViewById(R.id.light);
        light.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent LightScreen=new Intent(getApplicationContext(),LightActivity.class);
                startActivity(LightScreen);
            }
        });


        ImageButton temp=(ImageButton)findViewById(R.id.temp);
        temp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent TempScreen=new Intent(getApplicationContext(),TempActivity.class);
                startActivity(TempScreen);
            }
        });

        ImageButton sked=(ImageButton)findViewById(R.id.sched);
        sked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent skedScreen=new Intent(getApplicationContext(),SchedActivity.class);
                startActivity(skedScreen);
            }
        });

        TextView plant = (TextView) findViewById(R.id.Plantname);
        plant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent infoScreen=new Intent(getApplicationContext(),infoActivity.class);
                startActivity(infoScreen);
            }
        });

        ImageButton help=(ImageButton)findViewById(R.id.info);
        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent helpScreen=new Intent(getApplicationContext(),helpActivity.class);
                startActivity(helpScreen);
            }
        });

        ImageView wifi=(ImageView) findViewById(R.id.wifi);
        wifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent wifiscr=new Intent(getApplicationContext(),WifiActivity.class);
                startActivity(wifiscr);
            }
        });

        //codes to read user input for plant name, store the data to shared preferences,
        //get the data and display it on the app on button click
        Button setname = (Button) findViewById(R.id.name);
        final EditText et = (EditText) findViewById(R.id.editText); //EditText is defined as edittext in xml
        setname.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                TextView name = (TextView) findViewById(R.id.Plantname);
                String n = et.getText().toString();
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString(Name, n);
                editor.commit();
                if (sharedpreferences.contains(Name)) {
                    name.setText(sharedpreferences.getString(Name, ""));
                }
            }
        });

    }
}

