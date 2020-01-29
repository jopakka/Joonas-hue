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
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import javax.net.ssl.HttpsURLConnection;

public class ReadUrl extends AsyncTask<String, Void, String> {
    private static String TAG = "myTag";

    @Override
    protected String doInBackground(String... urls) {
        URL url;
        HttpURLConnection conn = null;
        try {
            url = new URL(urls[0]);
            conn = (HttpURLConnection) url.openConnection();
            BufferedReader buff = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = buff.readLine();
            Log.d(TAG, "InputStream: " + line);
            return line;

        } catch (MalformedURLException e){
            Log.e(TAG, "Incorrect url: " + e);
        } catch (IOException e){
            Log.e(TAG, "Failed to connect: " + e);
        } catch (Exception e){
            Log.e(TAG, "Random error: " + e);
        } finally {
            conn.disconnect();
        }
        return null;
    }
}
