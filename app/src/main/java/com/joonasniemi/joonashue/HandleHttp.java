package com.joonasniemi.joonashue;

import android.Manifest;
import android.os.AsyncTask;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import com.google.gson.JsonObject;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import javax.net.ssl.HttpsURLConnection;

public class HandleHttp extends AsyncTask<String, Void, String> {
    private static String TAG = "myTag";
    private String method;
    private JSONObject jObj;

    public HandleHttp(){
        this.method = "GET";
    }

    public HandleHttp(String method){
        this.method = method;
    }

    public HandleHttp(String method, JSONObject jObj){
        this.method = method;
        this.jObj = jObj;
    }


    @Override
    protected String doInBackground(String... urls) {
        URL url;
        HttpURLConnection conn = null;
        try {
            url = new URL(urls[0]);
            conn = (HttpURLConnection) url.openConnection();
            if(method.equals("POST")){
                conn.setDoOutput(true);
                OutputStream out = new BufferedOutputStream(conn.getOutputStream());
                out.write(jObj.toJSONString().getBytes());
                out.flush();
            } else if(method.equals("PUT")){
                conn.setDoOutput(true);
                conn.setRequestMethod(method);
                OutputStream out = new BufferedOutputStream(conn.getOutputStream());
                out.write(jObj.toJSONString().getBytes());
                out.close();
            } else {
                conn.setRequestMethod(method);
            }
            BufferedReader buff = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = buff.readLine();
            Log.d(TAG, "InputStream: " + line);
            return line;

        } catch (MalformedURLException e) {
            Log.e(TAG, "Incorrect url: " + e);
        } catch (IOException e) {
            Log.e(TAG, "Failed to connect: " + e);
        } catch (Exception e) {
            Log.e(TAG, "Random error: " + e);
        } finally {
            if(conn != null){
                conn.disconnect();
            }
        }
        return null;
    }
}
