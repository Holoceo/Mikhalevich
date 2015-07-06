package com.redmadintern.mikhalevich.ui.activity;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.redmadintern.mikhalevich.R;
import com.redmadintern.mikhalevich.ui.fragment.EventReportFragment;


public class MainActivity extends AppCompatActivity {
    public static final long REPORT_ID = 23;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            Fragment reportFragment = EventReportFragment.newInstance(REPORT_ID);
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .add(R.id.container, reportFragment)
                    .commit();
        }
    }
}
