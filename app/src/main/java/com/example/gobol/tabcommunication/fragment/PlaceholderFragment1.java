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

import com.example.gobol.tabcommunication.PersonListActivity;
import com.example.gobol.tabcommunication.R;
import com.example.gobol.tabcommunication.activity.ScrollingTestActivity;
import com.example.gobol.tabcommunication.activity.ShowWebChartActivity;
import com.example.gobol.tabcommunication.activity.TabTest;
import com.example.gobol.tabcommunication.interfaces.IFragmentToActivity;

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
        Button button12 = (Button) rootView.findViewById(R.id.button12);
        button12.setOnClickListener(this);
        Button nested = (Button) rootView.findViewById(R.id.nested);
        nested.setOnClickListener(this);
        Button tabtest = (Button) rootView.findViewById(R.id.tabtest);
        tabtest.setOnClickListener(this);


        btnFtoA.setOnClickListener(this);
        btnFtoF.setOnClickListener(this);
        showChat.setOnClickListener(this);
        return rootView;
    }

    public void MasterDetail(View view) {

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

            case R.id.button12:
                startActivity(new Intent(getActivity(), PersonListActivity.class));

                break;
            case R.id.nested:
                startActivity(new Intent(getActivity(), ScrollingTestActivity.class));

                break;
            case R.id.tabtest:
                startActivity(new Intent(getActivity(), TabTest.class));

                break;
        }

    }

    private void showQtyKeyPad() {
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
