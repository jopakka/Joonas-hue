package com.joonasniemi.joonashue;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.concurrent.ExecutionException;

public class SetupActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "myTag";
    private static final String upnp = "https://discovery.meethue.com/";
    private String bridgeIp;
    private String username;
    private TextView tvIp;
    private TextView tvUser;
    private SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);
        pref = getSharedPreferences("hueInfo", Context.MODE_PRIVATE);
        bridgeIp = pref.getString("bridgeIp", null);
        username = pref.getString("username", null);

        findViewById(R.id.bBridgeIp).setOnClickListener(this);
        findViewById(R.id.bDev).setOnClickListener(this);
        findViewById(R.id.bLogin).setOnClickListener(this);

        tvIp = findViewById(R.id.tvIp);
        tvUser = findViewById(R.id.tvUser);
        tvIp.setText(bridgeIp);
        tvUser.setText(username);


    }

    @Override
    protected void onPause(){
        super.onPause();
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("username", username);
        editor.putString("bridgeIp", bridgeIp);
        editor.apply();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.bBridgeIp) {
            findBridgeIp();
            tvIp.setText(bridgeIp);
        } else if (v.getId() == R.id.bDev){
            setDevToBridge();
            tvUser.setText(username);
        } else if (v.getId() == R.id.bLogin){
            login();
        }
    }

    private void findBridgeIp() {
        try {
            String line = new HandleHttp().execute(upnp).get();
            Log.d(TAG, "Line: " + line);
            JSONParser parser = new JSONParser();
            JSONArray jArray = (JSONArray) parser.parse(line);
            JSONObject jObj = (JSONObject) jArray.get(0);
            bridgeIp = (String) jObj.get("internalipaddress");
            Log.d(TAG, "IP-Address: " + bridgeIp);
        } catch (ParseException e) {
            Log.e(TAG, "ParseException: " + e);
        } catch (ExecutionException e) {
            Log.e(TAG, "ExecutionException: " + e);
        } catch (InterruptedException e) {
            Log.e(TAG, "InterruptedException: " + e);
        }
    }

    private void setDevToBridge() {
        if(bridgeIp != null) {
            try {
                JSONObject dev = new JSONObject();
                dev.put("devicetype", R.string.app_name + "#Admin");
                String line = new HandleHttp("POST", dev).execute("http://" + bridgeIp + "/api").get();
                Log.d(TAG, "Line: " + line);
                JSONParser parser = new JSONParser();
                JSONArray jArray = (JSONArray) parser.parse(line);
                JSONObject jObj = (JSONObject) jArray.get(0);
                if (jObj.containsKey("success")) {
                    JSONObject success = (JSONObject) jObj.get("success");
                    username = (String) success.get("username");
                    Log.d(TAG, "Dev username: " + username);
                } else {
                    JSONObject error = (JSONObject) jObj.get("error");
                    String desc = (String) error.get("description");
                    Log.d(TAG, "" + desc);
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

    private void login(){
        if(bridgeIp != null && username != null) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }
}
