package com.joonasniemi.joonashue;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.json.JSONException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;
import java.util.HashMap;

public class MyAdapter extends ArrayAdapter<Object> implements CompoundButton.OnCheckedChangeListener,
        AdapterView.OnItemClickListener {
    private static final String TAG = "myTag";
    private String type;
    private String bridgeIp;
    private String username;

    public MyAdapter(@NonNull Context context, ArrayList<Object> list, String type) {
        super(context, 0, list);
        this.type = type;
        bridgeIp = Json.getInstance().getBridgeIp();
        username = Json.getInstance().getUsername();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Object name = getItem(position);
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_light, parent, false);
        }
        TextView tvName = (TextView) convertView.findViewById(R.id.tvListName);
        tvName.setText((String) name);

        Switch lvSwitch = (Switch) convertView.findViewById(R.id.lvSwitch);
        lvSwitch.setTag(position);
        try {
            String line = Json.getInstance().getJsonString();
            JSONParser parser = new JSONParser();
            JSONObject jObj = (JSONObject) parser.parse(line);
            JSONObject jObj2 = (JSONObject) jObj.get("groups");
            if((boolean)((JSONObject)((JSONObject)jObj2.get(Integer.toString(position + 1))).get("state")).get("any_on")) {
                lvSwitch.setChecked(true);
            }
        } catch (Exception e){
            Log.e(TAG, "Exception switch: " + e);
        }
        lvSwitch.setOnCheckedChangeListener(this);

        return convertView;
    }

    @Override
    public int getViewTypeCount() {
        return getCount();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(type.equals("rooms")) {
            Log.d(TAG, "Rooms switch: " + buttonView.getTag());
            JSONObject json = new JSONObject();
            if(isChecked){
                json.put("on", true);
            } else {
                json.put("on", false);
            }
            new HandleHttp("PUT", json).execute("http://" + bridgeIp + "/api/" + username +
                    "/groups/" + (Integer.parseInt(buttonView.getTag().toString()) + 1) + "/action");
        } else if(type.equals("lights")) {
            Log.d(TAG, "Lights switch: " + buttonView.getTag());
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
