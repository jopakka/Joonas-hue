package com.joonasniemi.joonashue;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;

import java.net.URL;

public class HueJson {
    private static String TAG = "myTag";
    private static String upnp = "http://discovery.meethue.com/";
    private static String bridgeIp = null;
    private static String base_url = "/api/newdeveloper/";

    public HueJson (){
        try{
            if(bridgeIp == null){
                URL url = new URL(upnp);
                String line = "";
                //JSONParser parser = new JSONParser();
                //Object obj = parser.parse(line);
                //JSONArray array = (JSONArray) obj;
                //JSONObject obj2 = (JSONObject) array.get(0);

                //bridgeIp = (obj2.get("internalipaddress")).toString();
                Log.d(TAG, "Hue bridge ip: " + bridgeIp);
            }
        } catch (Exception e){
            Log.e(TAG, "Exception: " + e);
        }
    }

}
