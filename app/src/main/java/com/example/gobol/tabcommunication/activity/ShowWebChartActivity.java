package com.example.gobol.tabcommunication.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

import com.example.gobol.numberqtydialog.dialogs.DigitAlertDialogCreator;
import com.example.gobol.numberqtydialog.dialogs.DigitPopupWindow;
import com.example.gobol.numberqtydialog.interfaces.DigitDialogResponseHelper;
import com.example.gobol.tabcommunication.R;

import java.io.IOException;
import java.io.InputStream;

public class ShowWebChartActivity extends AppCompatActivity implements DigitDialogResponseHelper {
    private static final String TAG = ShowWebChartActivity.class.getSimpleName();
    WebView webView;
    private int num1, num2, num3, num4, num5;
    private String abcd = null;
    private DigitAlertDialogCreator digitAlertDialogCreator;
    private DigitPopupWindow digitPopupWindow;
    TextView textView;
    Point point = null;
    Point pointForbutton = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_web_chart);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        textView = (TextView) findViewById(R.id.textView4);
        Button button = (Button) findViewById(R.id.textView3);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pointForbutton != null) {
                    digitPopupWindow.showDigitDialog(DigitPopupWindow.FLAG_DIGIT_DIALOG_PRICE, v, pointForbutton, String.valueOf(textView.getTag()));
                }

            }
        });
        Intent intent = getIntent();
        num1 = intent.getIntExtra("NUM1", 0);
        num2 = intent.getIntExtra("NUM2", 0);
        num3 = intent.getIntExtra("NUM3", 0);
        num4 = intent.getIntExtra("NUM4", 0);
        num5 = intent.getIntExtra("NUM5", 0);
        webView = (WebView) findViewById(R.id.webview);
        abcd = loadJSONFromAsset(getApplicationContext());
        webView.addJavascriptInterface(new WebAppInterface(), "Android");
        //digitAlertDialogCreator = new DigitAlertDialogCreator(ShowWebChartActivity.this, this);
        digitPopupWindow = new DigitPopupWindow(ShowWebChartActivity.this, this);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("file:///android_asset/chart.html");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                if (point != null) {
                    digitPopupWindow.showDigitDialog(DigitPopupWindow.FLAG_DIGIT_DIALOG_QTY, view, point, String.valueOf(textView.getTag()));
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.web_chart, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_click) {
            startActivity(new Intent(ShowWebChartActivity.this, TestActivity.class));
            return true;
        } else if (id == R.id.action_showDialog) {
            if (digitAlertDialogCreator != null && textView != null) {

                View view = this.getCurrentFocus();
                //digitAlertDialogCreator.showDigitDialog(DigitAlertDialogCreator.FLAG_DIGIT_DIALOG_QTY, view, String.valueOf(textView.getTag()));
            }

            return true;
        }


        return super.onOptionsItemSelected(item);
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

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        int[] location = new int[2];
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);


        // Get the x, y location and store it in the location[] array
        // location[0] = x, location[1] = y.
        fab.getLocationOnScreen(location);

        //Initialize the Point with x, and y positions
        point = new Point();
        point.x = location[0];
        point.y = location[1];

        int[] location2 = new int[2];

        Button button = (Button) findViewById(R.id.textView3);

        // Get the x, y location and store it in the location[] array
        // location[0] = x, location[1] = y.
        button.getLocationOnScreen(location);

        //Initialize the Point with x, and y positions
        pointForbutton = new Point();
        pointForbutton.x = location2[0];
        pointForbutton.y = location2[1];


    }

    @Override
    public void onQtySelected(int flag, @Nullable AlertDialog alertDialog) {


    }

    @Override
    public void onPriceSelected(int flag, @Nullable AlertDialog alertDialog) {

    }

    @Override
    public void noCancelClicked(int flag) {

    }

    @Override
    public void onDigitEnter(String s, String tag) {
        textView.setText(s);
        Log.d(TAG, "" + tag);

    }

    @Override
    public void onDigitRemove(String s, String tag) {

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
