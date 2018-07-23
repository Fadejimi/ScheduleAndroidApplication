package app.activities;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import app.scheduler.R;

import app.fragments.ScheduleFragment;


public class MainActivity extends AppCompatActivity {

    public static final String SCHEDULE_FRAGMENT = "schedules";
    public static final String TASK_FRAGMENT = "tasks";
    public static final String SCHEDULE_STATISTICS_FRAGMENT = "schedule_statistics";
    public static final String TASK_STATISTICS_FRAGMENT = "task_statistics";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        ScheduleFragment fragment = new ScheduleFragment();
        transaction.replace(R.id.content_view, fragment, SCHEDULE_FRAGMENT);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void setToolbar() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    public void setActionBarTitle(String text) {
        getSupportActionBar().setTitle(text);
    }
}
