package com.example.gobol.tabcommunication.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.gobol.tabcommunication.R;
import com.example.gobol.tabcommunication.adapter.SectionsPagerAdapter;
import com.example.gobol.tabcommunication.fragment.PlaceholderFragment1;
import com.example.gobol.tabcommunication.fragment.PlaceholderFragment2;
import com.example.gobol.tabcommunication.interfaces.IFragmentToActivity;

public class MainActivity extends AppCompatActivity implements IFragmentToActivity {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.action_refresh) {
            int position = tabLayout.getSelectedTabPosition();
            Fragment fragment = mSectionsPagerAdapter.getFragment(tabLayout
                    .getSelectedTabPosition());
            if (fragment != null) {
                switch (position) {
                    case 0:
                        ((PlaceholderFragment1) fragment).onRefresh();
                        break;
                    case 1:
                        ((PlaceholderFragment2) fragment).onRefresh();
                        break;
                }
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void communicateToFragment2() {
        PlaceholderFragment2 fragment = (PlaceholderFragment2) mSectionsPagerAdapter.getFragment(1);
        if (fragment != null) {
            fragment.fragmentCommunication();
        } else {
            Log.i(LOG_TAG, "Fragment 2 is not initialized");
        }
    }
}
