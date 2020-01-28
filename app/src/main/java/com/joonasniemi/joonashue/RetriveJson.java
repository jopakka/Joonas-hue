package com.joonasniemi.joonashue;

import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.JsonReader;
import android.util.Log;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class RetriveJson extends AsyncTask<URL, Integer, String> {
    ProgressDialog pd;
    Context context;
    String method;

    private String TAG = "myTag";

    public RetriveJson(Context context, String method) {
        this.context = context;
        this.method = method;
    }

    protected void onPreExecute() {
        super.onPreExecute();

        pd = new ProgressDialog(context);
        pd.setMessage("Please wait");
        pd.setCancelable(false);
        pd.show();
    }

    protected String doInBackground(URL... urls) {
        Log.d(TAG, "doInBackground: " + urls[0]);
        HttpURLConnection urlConnection = null;

        try {
            urlConnection = (HttpURLConnection) urls[0].openConnection();
            urlConnection.setDoOutput(true);
            urlConnection.setRequestMethod(method);

            Log.d(TAG, "Method " + method);
            //if (method.equals("GET")) {
                BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                String line;
                StringBuffer buffer = new StringBuffer();

                while((line = in.readLine()) != null){
                    buffer.append(line);
                    Log.d(TAG, "Buffer line: " + line);
                    try {
                        JsonObject jsonObject = new JsonParser().parse(line).getAsJsonObject();
                        Log.d(TAG, "JsonObject: " + jsonObject);
                    } catch (Exception e){
                        Log.e(TAG, "Try jArray error");
                        int maxLogSize = 1000;
                        String error = e.toString();
                        for(int i = 0; i <= error.length() / maxLogSize; i++) {
                            int start = i * maxLogSize;
                            int end = (i+1) * maxLogSize;
                            end = end > error.length() ? error.length() : end;
                            Log.v(TAG, error.substring(start, end));
                        }
                    }
                }
                in.close();
            //}

        } catch (Exception e) {
            Log.e(TAG, "Try doInBackground error " + e);
        } finally {
            if(urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return null;
    }

    protected void onProgressUpdate(Integer... progress) {
        Log.d(TAG, "onProgressUpdate: " + progress[0]);
    }

    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        Log.d(TAG, "onPostExecute: " + result);
        if (pd.isShowing()) {
            pd.dismiss();
        }
    }
}
