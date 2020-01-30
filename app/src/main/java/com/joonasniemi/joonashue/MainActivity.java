package com.joonasniemi.joonashue;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import org.json.simple.JSONObject;

import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "myTag";
    private String bridgeIp;
    private String username;
    private SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pref = getSharedPreferences("hueInfo", Context.MODE_PRIVATE);
        bridgeIp = pref.getString("bridgeIp", null);
        username = pref.getString("username", null);

        findViewById(R.id.bOn).setOnClickListener(this);
        findViewById(R.id.bOff).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.bOn){
            lightOn();
        } else if(v.getId() == R.id.bOff){
            lightOff();
        }
    }

    private void lightOn(){
        try {
            JSONObject json = new JSONObject();
            json.put("on", true);
            String line = new HandleHttp("PUT", json).execute("http://" + bridgeIp + "/api/" +
                    username + "/lights/1/state").get();
        } catch (ExecutionException e) {
            Log.e(TAG, "ExecutionException: " + e);
        } catch (InterruptedException e) {
            Log.e(TAG, "InterruptedException: " + e);
        }
    }

    private void lightOff(){
        try {
            JSONObject json = new JSONObject();
            json.put("on", false);
            String line = new HandleHttp("PUT", json).execute("http://" + bridgeIp + "/api/" +
                    username + "/lights/1/state").get();
        } catch (ExecutionException e) {
            Log.e(TAG, "ExecutionException: " + e);
        } catch (InterruptedException e) {
            Log.e(TAG, "InterruptedException: " + e);
        }
    }
}
