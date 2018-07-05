package com.scheduler;

import android.app.FragmentManager;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.model.Schedule;

import java.util.ArrayList;
import java.util.List;

import layout.ScheduleFragment;
import layout.ScheduleStatisticsFragment;
import layout.TaskFragment;
import layout.TaskStatisticsFragment;

public class MainActivity extends AppCompatActivity {

    public static final String SCHEDULE_FRAGMENT = "schedules";
    public final String TASK_FRAGMENT = "tasks";
    public final String SCHEDULE_STATISTICS_FRAGMENT = "schedule_statistics";
    public final String TASK_STATISTICS_FRAGMENT = "task_statistics";

    private FragmentManager manager;
    List<Schedule> schedules;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        schedules = new ArrayList<>();

        manager = getFragmentManager();
        if (!validFragment()) {
            //setData();
            ScheduleFragment fragment = new ScheduleFragment();
            manager.beginTransaction().add(fragment, SCHEDULE_FRAGMENT).commit();
            fragment.setData(schedules);
        }
    }

    private boolean validFragment() {
        ScheduleFragment scheduleFragment = (ScheduleFragment) manager.findFragmentByTag(SCHEDULE_FRAGMENT);
        TaskFragment taskFragment = (TaskFragment) manager.findFragmentByTag(TASK_FRAGMENT);
        ScheduleStatisticsFragment schStatFragment = (ScheduleStatisticsFragment) manager.
                findFragmentByTag(SCHEDULE_STATISTICS_FRAGMENT);
        TaskStatisticsFragment taskStatFragment = (TaskStatisticsFragment) manager
                .findFragmentByTag(TASK_STATISTICS_FRAGMENT);

        boolean flag = false;
        if (scheduleFragment != null || taskFragment != null ||
                schStatFragment != null || taskStatFragment != null) {
            flag = true;
        }

        if (schStatFragment != null && schStatFragment.isVisible()) {
            int orientation = this.getResources().getConfiguration().orientation;
            if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                schStatFragment.setMode(false);
            }
            else {
                schStatFragment.setMode(true);
            }
        }

        return flag;
    }
}
