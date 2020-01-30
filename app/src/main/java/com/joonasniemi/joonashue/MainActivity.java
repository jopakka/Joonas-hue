package com.joonasniemi.joonashue;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

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
            JSONParser parser = new JSONParser();
            JSONArray jArray = (JSONArray) parser.parse(line);
            JSONObject jObj = (JSONObject) jArray.get(0);
            if (jObj.containsKey("success")) {
                Log.d(TAG, "Light 1 On");
            } else {
                Log.d(TAG, "Light 1 error");
            }
        } catch (ExecutionException e) {
            Log.e(TAG, "ExecutionException: " + e);
        } catch (InterruptedException e) {
            Log.e(TAG, "InterruptedException: " + e);
        } catch (Exception e) {
            Log.e(TAG, "Random error: " + e);
        }
    }

    private void lightOff(){
        try {
            JSONObject json = new JSONObject();
            json.put("on", false);
            String line = new HandleHttp("PUT", json).execute("http://" + bridgeIp + "/api/" +
                    username + "/lights/1/state").get();
            JSONParser parser = new JSONParser();
            JSONArray jArray = (JSONArray) parser.parse(line);
            JSONObject jObj = (JSONObject) jArray.get(0);
            if (jObj.containsKey("success")) {
                Log.d(TAG, "Light 1 Off");
            } else {
                Log.d(TAG, "Light 1 error");
            }
        } catch (ExecutionException e) {
            Log.e(TAG, "ExecutionException: " + e);
        } catch (InterruptedException e) {
            Log.e(TAG, "InterruptedException: " + e);
        } catch (Exception e) {
            Log.e(TAG, "Random error: " + e);
        }
    }
}
