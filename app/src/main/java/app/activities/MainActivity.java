package app.activities;

import android.content.Context;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import app.fragments.ScheduleStatisticsFragment;
import app.fragments.TaskFragment;
import app.fragments.TaskStatisticsFragment;
import app.rest.model.Schedule;
import com.scheduler.R;

import app.fragments.ScheduleFragment;
import app.rest.model.Task;


public class MainActivity extends AppCompatActivity {

    public static final String SCHEDULE_FRAGMENT = "schedules";
    public static final String TASK_FRAGMENT = "tasks";
    public static final String SCHEDULE_STATISTICS_FRAGMENT = "schedule_statistics";
    public static final String TASK_STATISTICS_FRAGMENT = "task_statistics";
    static FragmentTransaction transaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        transaction = getSupportFragmentManager().beginTransaction();

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

    public static void showStatistics(Schedule schedule) {
        String jsonString = schedule.toJson();
        Bundle bundle = new Bundle();
        bundle.putString("schedule", jsonString);
        ScheduleStatisticsFragment fragment = new ScheduleStatisticsFragment();
        fragment.setArguments(bundle);
        transaction.replace(R.id.content_view, fragment, SCHEDULE_STATISTICS_FRAGMENT);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public static void showTasks(Schedule schedule) {
        String jsonString = schedule.toJson();
        Bundle bundle = new Bundle();
        bundle.putString("schedule", jsonString);
        TaskFragment fragment = new TaskFragment();
        fragment.setArguments(bundle);
        transaction.replace(R.id.content_view, fragment, TASK_FRAGMENT);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public static void showTaskStatistics(Task task, Schedule schedule) {
        String jsonString = task.toJson();
        String scheduleString = schedule.toJson();
        Bundle bundle = new Bundle();
        bundle.putString("task", jsonString);
        bundle.putString("schedule", scheduleString);
        TaskStatisticsFragment fragment = new TaskStatisticsFragment();
        fragment.setArguments(bundle);
        transaction.replace(R.id.content_view, fragment, TASK_STATISTICS_FRAGMENT);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
