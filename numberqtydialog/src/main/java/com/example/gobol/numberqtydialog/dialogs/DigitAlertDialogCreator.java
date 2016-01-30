package com.example.gobol.numberqtydialog.dialogs;

import android.content.Context;
import android.content.res.Configuration;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gobol.numberqtydialog.R;
import com.example.gobol.numberqtydialog.interfaces.DigitDialogResponseHelper;
import com.example.gobol.numberqtydialog.util.DecimalDigitsInputFilter;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Field;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Ajinkya on 1/21/2016.
 */

public class DigitAlertDialogCreator implements View.OnClickListener, TextWatcher, View.OnLongClickListener {
    private static final String TAG = DigitAlertDialogCreator.class.getSimpleName();
    private StringBuilder stringBuilder = null;
    private int flag = 0;
    private TextView numberTextView = null;
    private TextView titleTextView = null;


    private AlertDialog alertDialog = null;
    private String tag = null;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({FLAG_DIGIT_DIALOG_QTY, FLAG_DIGIT_DIALOG_PRICE})
    public @interface QtyFlag {
    }

    public static final int FLAG_DIGIT_DIALOG_QTY = 1;
    public static final int FLAG_DIGIT_DIALOG_PRICE = 2;

    protected Context context;
    private DigitDialogResponseHelper digitDialogResponseHelper;
    private int style;
    private boolean choice;

    /***
     * @param context
     * @param digitDialogResponseHelper
     */
    public DigitAlertDialogCreator(@NonNull Context context, @NonNull DigitDialogResponseHelper digitDialogResponseHelper, int style) {
        this.context = context;
        this.digitDialogResponseHelper = digitDialogResponseHelper;
        this.style = style;
        stringBuilder = new StringBuilder();

    }


    /***
     * @param context
     * @param digitDialogResponseHelper
     */
    public DigitAlertDialogCreator(@NonNull Context context, @NonNull DigitDialogResponseHelper digitDialogResponseHelper) {
        this.context = context;
        this.digitDialogResponseHelper = digitDialogResponseHelper;
        stringBuilder = new StringBuilder();

    }


    public boolean showDigitDialog(@QtyFlag final int flag, View clickView, String tag) {
        this.flag = flag;
        this.tag = tag;
        clearStringBuilderAndTextView();
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context, R.style.AlertDialogBlackTheme);
        LayoutInflater mInflaterAddCategory = LayoutInflater.from(context);
        final View view = mInflaterAddCategory.inflate(R.layout.dialogkeypad, null);
        alertDialogBuilder.setView(view);
        inItViews(view);

        switch (flag) {
            case FLAG_DIGIT_DIALOG_PRICE:
                alertDialogBuilder.setTitle(R.string.title_dialog_price);
                break;
            case FLAG_DIGIT_DIALOG_QTY:
                alertDialogBuilder.setTitle(R.string.title_dialog_qty);
                break;
            default:
                throw new RuntimeException(context.toString()
                        + " must set appropriate flag");
        }

        alertDialogBuilder.setCancelable(true);
        // create alert dialog
        alertDialog = alertDialogBuilder.create();
        alertDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        WindowManager.LayoutParams wmlp = alertDialog.getWindow().getAttributes();

        int[] values = new int[2];
        clickView.getLocationOnScreen(values);
        Log.d("X & Y", values[0] + " " + values[1]);

        int[] location = new int[2];
        clickView.getLocationOnScreen(location);
        wmlp.gravity = Gravity.NO_GRAVITY;
        wmlp.x = values[0];   //x position
        wmlp.y = values[1];   //y position

//        wmlp.gravity = Gravity.NO_GRAVITY;
//        wmlp.x = clickView.getLeft()-(clickView.getWidth()*2);   //x position
//        wmlp.y = clickView.getTop()+(clickView.getHeight()*2);   //y position

        alertDialog.getWindow().setAttributes(wmlp);

        Configuration conf = context.getResources().getConfiguration();
        int screenLayout = 1; // application default behavior
        try {
            Field field = conf.getClass().getDeclaredField("screenLayout");
            screenLayout = field.getInt(conf);
        } catch (Exception e) {
            // NoSuchFieldException or related stuff
        }
// Configuration.SCREENLAYOUT_SIZE_MASK == 15
        int screenType = screenLayout & 15;
// Configuration.SCREENLAYOUT_SIZE_SMALL == 1
// Configuration.SCREENLAYOUT_SIZE_NORMAL == 2
// Configuration.SCREENLAYOUT_SIZE_LARGE == 3
// Configuration.SCREENLAYOUT_SIZE_XLARGE == 4
        if (screenType == 1) {
            wmlp.width = 450;//width
            wmlp.height = 800;//height
        } else if (screenType == 2) {
            wmlp.width = 450;//width
            wmlp.height = 800;//height
        } else if (screenType == 3) {
            wmlp.width = 250;//width
            wmlp.height = 400;//height
        } else if (screenType == 4) {
            wmlp.width = 250;//width
            wmlp.height = 400;//height
        } else { // undefined
            wmlp.width = 250;//width
            wmlp.height = 400;//height
        }


        alertDialog.show();
        return choice;
    }


    @Override
    public void onClick(View v) {

        int i = v.getId();
        if (i == R.id.OkButton) {
            if (digitDialogResponseHelper != null) {
                switch (flag) {
                    case FLAG_DIGIT_DIALOG_PRICE:
                        digitDialogResponseHelper
                                .onPriceSelected(flag, alertDialog);
                        break;
                    case FLAG_DIGIT_DIALOG_QTY:
                        digitDialogResponseHelper
                                .onQtySelected(flag, alertDialog);
                        break;
                    default:
                        throw new RuntimeException(context.toString()
                                + " must set appropriate flag");
                }
            }

        } else if (i == R.id.buttonClear) {
            if (stringBuilder != null) {
                int size = stringBuilder.length();
                if (size > 0) {

                    stringBuilder.deleteCharAt(size - 1);
                    String text = stringBuilder.toString();
                    numberTextView.setText(text);
                    digitDialogResponseHelper.onDigitRemove(text, tag);
                }
            }


        } else if (i == R.id.cancelButton) {
            if (alertDialog != null) {
                digitDialogResponseHelper.noCancelClicked(flag);
                alertDialog.dismiss();
            }


        } else {

            if (numberTextView != null) {
                String text = setNumberToText(i);
                if (text != null) {
                    stringBuilder.append(text);
                }
                if (numberTextView != null) {

                    String text2 = stringBuilder.toString();
                    Pattern mPattern = Pattern.compile("[0-9]{0," + (7 - 1)
                            + "}+((\\.[0-9]{0," + (3 - 1) + "})?)||(\\.)?");

                    Matcher matcher = mPattern.matcher(text2);
                    if (matcher.matches()) {
                        numberTextView.setText(text2);
                        digitDialogResponseHelper.onDigitEnter(text2, tag);
                    } else {
                        Toast.makeText(context, "please enter right decimal value", Toast.LENGTH_SHORT).show();
                    }


                }

            }
        }

    }

    @Override
    public boolean onLongClick(View v) {
        int id = v.getId();
        if (id == R.id.buttonClear) {
            stringBuilder.delete(0, stringBuilder.length());
            numberTextView.setText("");
            digitDialogResponseHelper.onDigitRemove("", tag);
            return true;
        }
        return false;
    }

    private String setNumberToText(int i) {

        if (i == R.id.button0) {

            return "0";
        } else if (i == R.id.button1) {
            return "1";

        } else if (i == R.id.button2) {
            return "2";

        } else if (i == R.id.button3) {
            return "3";

        } else if (i == R.id.button4) {
            return "4";

        } else if (i == R.id.button5) {
            return "5";

        } else if (i == R.id.button6) {
            return "6";

        } else if (i == R.id.button7) {
            return "7";

        } else if (i == R.id.button8) {
            return "8";

        } else if (i == R.id.button9) {
            return "9";

        } else if (i == R.id.buttonDOT) {
            return ".";

        }

        return null;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

        Log.d(TAG, "" + s);
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    private void inItViews(View view) {
        int buttonArray[] = {R.id.button1, R.id.button2, R.id.button3,
                R.id.button4, R.id.button5, R.id.button6, R.id.button7,
                R.id.button8, R.id.button9, R.id.button0, R.id.buttonDOT,
                R.id.buttonClear, R.id.OkButton, R.id.cancelButton};

        for (int i = 0; i < buttonArray.length; i++) {
            Button button = (Button) view.findViewById(buttonArray[i]);
            button.setOnClickListener(this);
        }
        Button buttonClear = (Button) view.findViewById(R.id.buttonClear);
        buttonClear.setOnLongClickListener(this);

        numberTextView = (TextView) view.findViewById(R.id.numberTextView);
        titleTextView = (TextView) view.findViewById(R.id.titleTextView);
        titleTextView.setVisibility(View.GONE);
        numberTextView.addTextChangedListener(this);


    }

    public void clearStringBuilderAndTextView() {
        if (stringBuilder != null && numberTextView != null) {
            stringBuilder.delete(0, stringBuilder.length());
            numberTextView.setText("");
        }

    }

}
