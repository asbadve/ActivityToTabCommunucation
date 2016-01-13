package com.example.gobol.tabcommunication.fragment;

import android.support.v4.app.Fragment;

/**
 * provides the corresponding fragment at the given position
 *
 * @author Ajinkya
 */
public class FragmentsProvider {

    private static final String TAG = FragmentsProvider.class.getSimpleName();

    public static Fragment getFragment(int position) {
        Fragment fragment = null;

        switch (position) {
            case 0:
                fragment = PlaceholderFragment1.newInstance(position + 1);
                break;
            case 1:

                fragment = PlaceholderFragment2.newInstance(position + 1);

                break;

            case 2:
                fragment = PlaceholderFragment3.newInstance(position + 1);

                break;


        }

        return fragment;
    }


}
