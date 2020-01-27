package com.joonasniemi.joonashue;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private String TAG = "myTag";
    private EditText et;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.bGet).setOnClickListener(this);

        et = findViewById(R.id.etText);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.bGet) {
            if (!et.getText().toString().isEmpty()) {
                String etUrl = et.getText().toString();
                if (!et.getText().toString().contains("http://")) {
                    etUrl = "http://" + etUrl;
                }
                try {
                    URL url = new URL(etUrl);
                    new RetriveJson(this, "POST").execute(url);
                } catch (Exception e) {
                    Log.e(TAG, "Error try URL: " + e);
                }
            } else {
                Log.d(TAG, "EditText is empty");
            }
        }
    }
}
