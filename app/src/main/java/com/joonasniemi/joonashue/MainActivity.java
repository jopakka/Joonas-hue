package com.joonasniemi.joonashue;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static String TAG = "myTag";
    private static String upnp = "https://discovery.meethue.com/";
    private String bridgeIp = null;
    private EditText et;
    private TextView tvInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.bGet).setOnClickListener(this);

        et = findViewById(R.id.etText);
        tvInfo = findViewById(R.id.tvInfo);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.bGet) {
            Log.d(TAG, "bGet pressed");
            findBridgeIp();
            tvInfo.setText(bridgeIp);
        }
    }

    private void findBridgeIp(){
        try {
            String line = new ReadUrl().execute(upnp).get();
            JSONParser parser = new JSONParser();
            Log.d(TAG, "Line: " + line);
            JSONArray jArray = (JSONArray) parser.parse(line);
            JSONObject jObj = (JSONObject) jArray.get(0);
            bridgeIp = jObj.get("internalipaddress").toString();
            Log.d(TAG, "IP-Address: " + bridgeIp);
        } catch (ParseException e){
            Log.e(TAG, "ParseException: " + e);
        } catch (ExecutionException e){
            Log.e(TAG, "ExecutionException: " + e);
        } catch (InterruptedException e){
            Log.e(TAG, "InterruptedException: " + e);
        }
    }
}
