package com.example.gobol.tabcommunication.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gobol.tabcommunication.R;
import com.example.gobol.tabcommunication.ShowWebChartActivity;
import com.example.gobol.tabcommunication.dialogs.KeyPadDialog;
import com.example.gobol.tabcommunication.interfaces.IFragmentToActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment1 extends Fragment implements View.OnClickListener {
    private IFragmentToActivity mCallback;
    private Button btnFtoA;
    private Button btnFtoF;
    private Button showChat;
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    public PlaceholderFragment1() {
    }

    public void onRefresh() {
        Toast.makeText(getActivity(), "Fragment 1: Refresh called.",
                Toast.LENGTH_SHORT).show();
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static PlaceholderFragment1 newInstance(int sectionNumber) {
        PlaceholderFragment1 fragment = new PlaceholderFragment1();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_placeholder_fragment1, container, false);
        TextView textView = (TextView) rootView.findViewById(R.id.section_label);
        showChat = (Button) rootView.findViewById(R.id.showchart);
        textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
        btnFtoA = (Button) rootView.findViewById(R.id.button);
        btnFtoF = (Button) rootView.findViewById(R.id.button2);
        btnFtoA.setOnClickListener(this);
        btnFtoF.setOnClickListener(this);
        showChat.setOnClickListener(this);
        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCallback = (IFragmentToActivity) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement IFragmentToActivity");
        }
    }

    @Override
    public void onDetach() {
        mCallback = null;
        super.onDetach();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.button:
                mCallback.showToast("Hello from Fragment 1");
                break;

            case R.id.button2:
                //mCallback.communicateToFragment2();
                showQtyKeyPad();

                break;

            case R.id.showchart:
                Intent intent = new Intent(getActivity(), ShowWebChartActivity.class);
                intent.putExtra("NUM1", 5);
                intent.putExtra("NUM2", 4);
                intent.putExtra("NUM3", 1);
                intent.putExtra("NUM4", 5);
                intent.putExtra("NUM5", 67);

                startActivity(intent);
                break;
        }

    }

    private void showQtyKeyPad() {
        boolean isDialogForEdit = false;
        DialogFragment newFragment = KeyPadDialog.newInstance("Enter Qty", isDialogForEdit);
        newFragment.setCancelable(false);
        newFragment.show(getActivity().getSupportFragmentManager(), "dialog");
    }


//    public void chartButtonClick(View view) {
//
//        Intent intent = new Intent(getActivity(), ShowWebChartActivity.class);
//
//        intent.putExtra("NUM1", 5);
//        intent.putExtra("NUM2", 4);
//        intent.putExtra("NUM3", 1);
//        intent.putExtra("NUM4", 5);
//        intent.putExtra("NUM5", 67);
//
//        startActivity(intent);
//
//    }
}
