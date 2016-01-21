package com.example.gobol.tabcommunication.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.gobol.numberqtydialog.dialogs.DigitAlertDialogCreator;
import com.example.gobol.numberqtydialog.interfaces.DigitDialogResponseHelper;
import com.example.gobol.numberqtydialog.util.DecimalDigitsInputFilter;
import com.example.gobol.tabcommunication.R;

public class TestActivity extends AppCompatActivity implements DigitDialogResponseHelper {

    private static final String TAG = TestActivity.class.getSimpleName();
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        textView = (TextView) findViewById(R.id.textView2);
        textView.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(6,
                2)});
        final DigitAlertDialogCreator digitAlertDialogCreator = new DigitAlertDialogCreator(TestActivity.this, this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                digitAlertDialogCreator.showDigitDialog(DigitAlertDialogCreator.FLAG_DIGIT_DIALOG_QTY, view, String.valueOf(textView.getTag()));

            }
        });
    }


    @Override
    public void onQtySelected(int flag, AlertDialog alertDialog) {
        Log.e(TAG, "qty selected");
        if (alertDialog != null) {
            alertDialog.dismiss();
        }
    }

    @Override
    public void onPriceSelected(int flag, AlertDialog alertDialog) {
        Log.e(TAG, "price selected");
        if (alertDialog != null) {
            alertDialog.dismiss();
        }

    }

    @Override
    public void noCancelClicked(int flag) {

    }

    @Override
    public void onDigitEnter(String s, String tag) {
        Log.d(TAG, "tag of the widget" + s);
        textView.setText(s);
    }

    @Override
    public void onDigitRemove(String s, String tag) {
        Log.d(TAG, "tag of the widget" + s);
        textView.setText(s);
    }


}
