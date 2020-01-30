package com.joonasniemi.joonashue;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private static final String TAG = "myTag";
    private String bridgeIp;
    private String username;
    private SharedPreferences pref;
    private ListView lvRooms;
    private ArrayList<Object> rooms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rooms = new ArrayList<>();

        pref = getSharedPreferences("hueInfo", Context.MODE_PRIVATE);
        bridgeIp = pref.getString("bridgeIp", null);
        username = pref.getString("username", null);

        lvRooms = findViewById(R.id.lvLights);
        listRooms();
        lvRooms.setOnItemClickListener(this);

        //findViewById(R.id.bOn).setOnClickListener(this);
        //findViewById(R.id.bOff).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        //if(v.getId() == R.id.bOn){
        //    lightOn();
        //} else if(v.getId() == R.id.bOff){
        //    lightOff();
        //}
    }

    private void listRooms() {
        try {
            String line = Json.getInstance().getJsonString();
            JSONParser parser = new JSONParser();
            JSONObject jObj = (JSONObject) parser.parse(line);
            JSONObject jObj2 = (JSONObject) jObj.get("groups");

            for (Object o : jObj2.keySet()) {
                String key = (String) o;
                if (((JSONObject) jObj2.get(key)).get("type").equals("Room")) {
                    rooms.add(((JSONObject) jObj2.get(key)).get("name"));
                    Log.d(TAG, "Key: " + key);
                }
            }

            lvRooms.setAdapter(new MyAdapter(this, rooms, "rooms"));
        } catch (ParseException e) {
            Log.e(TAG, "ParseException: " + e);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.d(TAG, "Item: " + lvRooms.getItemAtPosition(position));
    }
}
