package com.example.gobol.tabcommunication.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;

import com.example.gobol.tabcommunication.R;


/**
 * Created by Ajinkya on 1/12/2016.
 */
public class KeyPadDialog extends DialogFragment {

    public static KeyPadDialog newInstance(String title, boolean isDialogForEdit) {
        KeyPadDialog frag = new KeyPadDialog();
        Bundle args = new Bundle();
        frag.setArguments(args);

        return frag;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Context context = getActivity();
        AlertDialog.Builder builderAddCategory = new AlertDialog.Builder(context);
        // Get the layout inflater
        LayoutInflater mInflaterAddCategory = LayoutInflater.from(context);
        View finalViewAddCategory = mInflaterAddCategory.inflate(R.layout.dialogkeypad, null);

        builderAddCategory.setCancelable(false);
        builderAddCategory.setView(finalViewAddCategory);
        builderAddCategory.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int whichButton) {
                        dialog.dismiss();
                    }
                });


        // TODO: 30-Oct-15 make sure to check that the cateogry name is not twice against the diffrent super category
        final AlertDialog alertDialogCategory = builderAddCategory.create();
        alertDialogCategory.show();


        return alertDialogCategory;
    }
}
