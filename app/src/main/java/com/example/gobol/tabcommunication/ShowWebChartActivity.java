package com.example.gobol.tabcommunication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import java.io.IOException;
import java.io.InputStream;

public class ShowWebChartActivity extends AppCompatActivity {
    WebView webView;
    private int num1, num2, num3, num4, num5;
    private String abcd = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_web_chart);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent intent = getIntent();
        num1 = intent.getIntExtra("NUM1", 0);
        num2 = intent.getIntExtra("NUM2", 0);
        num3 = intent.getIntExtra("NUM3", 0);
        num4 = intent.getIntExtra("NUM4", 0);
        num5 = intent.getIntExtra("NUM5", 0);
        webView = (WebView) findViewById(R.id.webview);
        abcd = loadJSONFromAsset(getApplicationContext());
        webView.addJavascriptInterface(new WebAppInterface(), "Android");

        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("file:///android_asset/chart.html");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    public static String loadJSONFromAsset(Context context) {
        String json = null;
        try {
            InputStream is = context.getAssets().open("test.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }

        return json;
    }


    private class WebAppInterface {
        @JavascriptInterface
        public int[] getNumArray() {
            int a[] = {num1, num2, num3, num4, num5};
            return a;
        }

        @JavascriptInterface
        public String getJson() {
            Log.e("json", abcd);
            return abcd;
        }

        public String[] getItemArray() {
            String a[] = {"NUM1", "NUM2", "NUM3", "NUM4", "NUM5"};
            return a;
        }


        @JavascriptInterface
        public int getNum1() {
            return num1;
        }

        @JavascriptInterface
        public int getNum2() {
            return num2;
        }

        @JavascriptInterface
        public int getNum3() {
            return num3;
        }

        @JavascriptInterface
        public int getNum4() {
            return num4;
        }

        @JavascriptInterface
        public int getNum5() {
            return num5;
        }
    }

}
