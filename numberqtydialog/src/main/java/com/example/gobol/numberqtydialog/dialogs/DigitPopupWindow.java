package com.example.gobol.numberqtydialog.dialogs;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gobol.numberqtydialog.R;
import com.example.gobol.numberqtydialog.interfaces.DigitDialogResponseHelper;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Field;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Ajinkya on 1/21/2016.
 */

public class DigitPopupWindow implements View.OnClickListener, TextWatcher, View.OnLongClickListener {
    private static final String TAG = DigitAlertDialogCreator.class.getSimpleName();
    private StringBuilder stringBuilder = null;
    private int flag = 0;
    private TextView numberTextView = null;
    //    private AlertDialog alertDialog = null;
    private String tag = null;
    int popupWidth = 450;
    int popupHeight = 800;
    private PopupWindow popup = null;
    private TextView titleTextView = null;
    private boolean isInitViewDone = false;

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
    public DigitPopupWindow(@NonNull Context context, @NonNull DigitDialogResponseHelper digitDialogResponseHelper, int style) {
        this.context = context;
        this.digitDialogResponseHelper = digitDialogResponseHelper;
        this.style = style;
        stringBuilder = new StringBuilder();
    }


    /***
     * @param context
     * @param digitDialogResponseHelper
     */
    public DigitPopupWindow(@NonNull Context context, @NonNull DigitDialogResponseHelper digitDialogResponseHelper) {
        this.context = context;
        this.digitDialogResponseHelper = digitDialogResponseHelper;
        stringBuilder = new StringBuilder();

    }


    public boolean showDigitDialog(@QtyFlag final int flag, View ancherView, Point p, String tag) {


//        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
//                context, R.style.AlertDialogBlackTheme);


        LayoutInflater mInflaterAddCategory = LayoutInflater.from(context);
        final View view = mInflaterAddCategory.inflate(R.layout.dialogkeypad, null);
        // Creating the PopupWindow
        if (popup == null) {
            popup = new PopupWindow(context);
        }

        popup.setContentView(view);

        popup.setFocusable(false);//this is to ensure that it not closed when click outside
        popup.setOutsideTouchable(false);//to ignore other clickable outside the dialog window

        // Some offset to align the popup a bit to the right, and a bit down, relative to button's position.
        int OFFSET_X = 30;
        int OFFSET_Y = 30;

        // Clear the default translucent background
        popup.setBackgroundDrawable(new BitmapDrawable());

        clearStringBuilderAndTextView();
        if (!isInitViewDone) {
            inItViews(view);
            this.flag = flag;
            this.tag = tag;

            switch (flag) {
                case FLAG_DIGIT_DIALOG_PRICE:
                    if (titleTextView != null)
                        titleTextView.setText(R.string.title_dialog_price);
                    break;
                case FLAG_DIGIT_DIALOG_QTY:
                    if (titleTextView != null)
                        titleTextView.setText(R.string.title_dialog_qty);

                    break;
                default:
                    throw new RuntimeException(context.toString()
                            + " must set appropriate flag");
            }
        }


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
            popup.setWidth(450);
            popup.setHeight(800);
        } else if (screenType == 2) {
            popup.setWidth(450);
            popup.setHeight(800);
        } else if (screenType == 3) {
            popup.setWidth(250);
            popup.setHeight(400);
        } else if (screenType == 4) {
            popup.setWidth(250);
            popup.setHeight(400);
        } else { // undefined
            popup.setWidth(250);
            popup.setHeight(400);
        }

        if (!popup.isShowing()) {
            popup.showAsDropDown(ancherView);
        }

//OR
//        popup.showAtLocation(view, Gravity.NO_GRAVITY, p.x + OFFSET_X, p.y + OFFSET_Y);
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
                                .onPriceSelected(flag, null);
                        break;
                    case FLAG_DIGIT_DIALOG_QTY:
                        digitDialogResponseHelper
                                .onQtySelected(flag, null);
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
            if (popup != null) {
                digitDialogResponseHelper.noCancelClicked(flag);
                closePopup();

            }


        } else {

            if (numberTextView != null && numberTextView != null) {
                String text = setNumberToText(i);
                if (text != null) {
                    String testText = stringBuilder.toString();
                    String abc = testText.concat(text);
                    Pattern mPattern = Pattern.compile("[0-9]{0," + (7 - 1)
                            + "}+((\\.[0-9]{0," + (3 - 1) + "})?)||(\\.)?");

                    Matcher matcher = mPattern.matcher(abc);
                    if (matcher.matches()) {
                        stringBuilder.append(text);
                        String text2 = stringBuilder.toString();
                        numberTextView.setText(text2);
                        digitDialogResponseHelper.onDigitEnter(text2, tag);


                    } else {
                        Toast.makeText(context, "please enter right decimal value", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        }

    }

    private void closePopup() {
        if (popup != null) {
            popup.dismiss();
            isInitViewDone = false;
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
        isInitViewDone = true;//this did becoz of the widget are overdraw every time user click on the button
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

        numberTextView.addTextChangedListener(this);


    }

    public void clearStringBuilderAndTextView() {
        if (stringBuilder != null && numberTextView != null) {
            stringBuilder.delete(0, stringBuilder.length());
            numberTextView.setText("");
        }

    }

}
