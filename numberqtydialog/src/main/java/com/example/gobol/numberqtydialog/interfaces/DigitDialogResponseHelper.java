package com.example.gobol.numberqtydialog.interfaces;

import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;

/**
 * Created by Ajinkya on 1/21/2016.
 */
public interface DigitDialogResponseHelper {
    void onQtySelected(int flag, AlertDialog alertDialog);

    void onPriceSelected(int flag, @Nullable AlertDialog alertDialog);

    void noCancelClicked(int flag);

    void onDigitEnter(String s, String tag);

    void onDigitRemove(String s, String tag);


}
