package com.redmadintern.mikhalevich.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.Toast;

import com.redmadintern.mikhalevich.R;
import com.redmadintern.mikhalevich.model.local.EventReport;
import com.redmadintern.mikhalevich.model.local.EventStatus;
import com.redmadintern.mikhalevich.ui.view.EventReportView;
import com.redmadintern.mikhalevich.utils.MockUtil;

/**
 * Created by Alexander on 04.07.2015.
 */
public class EventReportFragment extends DialogFragment{
    private static final String KEY_REPORT_ID = "report_id";

    private long reportId;

    private EventReportView reportView;

    public static EventReportFragment newInstance(long reportId) {
        Bundle args = new Bundle();
        args.putLong(KEY_REPORT_ID, reportId);
        EventReportFragment reportFragment = new EventReportFragment();
        reportFragment.setArguments(args);
        return reportFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);
        Bundle args = getArguments();
        reportId = args.getLong(KEY_REPORT_ID);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_event_report, container, false);
        reportView = (EventReportView)root.findViewById(R.id.v_report);
        return root;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        fetchReport();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        EventStatus eventStatus = MockUtil.createStatus();
        reportView.addEventStatus(eventStatus);
        return true;
    }

    private void fetchReport() {
        EventReport eventReport = MockUtil.createEventReportMock(reportId);
        reportView.setEventReport(eventReport);
        reportView.setStatusClickListener(onStatusClickListener);
    }

    private EventReportView.OnStatusClickListener onStatusClickListener = new EventReportView.OnStatusClickListener() {
        @Override
        public void onStatusClick(EventStatus eventStatus) {
            showToast("Status clicked: "+eventStatus.getTitle());
        }

        @Override
        public void onPhoneClick(String phone) {
            showToast("Phone clicked: "+phone);
        }

        @Override
        public void onMapClick(String adress) {
            showToast("Map clicked: "+adress);
        }
    };

    private void showToast(String text) {
        Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
    }
}
