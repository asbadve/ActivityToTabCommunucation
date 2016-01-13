package com.example.gobol.tabcommunication.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gobol.tabcommunication.R;
import com.example.gobol.tabcommunication.interfaces.IFragmentToActivity;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment2 extends Fragment implements View.OnClickListener {
    private IFragmentToActivity mCallback;
    private TextView mTextView1;
    private Button btnFtoA;

    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    public PlaceholderFragment2() {
    }

    public void onRefresh() {
        Toast.makeText(getActivity(), "Fragment 2: Refresh called.",
                Toast.LENGTH_SHORT).show();
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

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static PlaceholderFragment2 newInstance(int sectionNumber) {
        PlaceholderFragment2 fragment = new PlaceholderFragment2();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public void fragmentCommunication() {
        mTextView1.setText("Hello from Tab Fragment 1");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_placeholder_fragment2, container, false);
        TextView textView = (TextView) rootView.findViewById(R.id.section_label);
        textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
        mTextView1 = (TextView) rootView.findViewById(R.id.textView1);
        btnFtoA = (Button) rootView.findViewById(R.id.button);
        btnFtoA.setOnClickListener(this);

        return rootView;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button:
                mCallback.showToast("Hello from Fragment 2");
                break;
        }
    }
}
