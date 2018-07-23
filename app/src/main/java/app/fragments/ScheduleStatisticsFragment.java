package app.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import app.activities.MainActivity;
import app.model.UserInfo;
import app.rest.APIClient;
import app.rest.TaskService;
import app.rest.model.Schedule;
import app.rest.model.Task;
import app.scheduler.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ScheduleStatisticsFragment extends Fragment {
    private BarChart barChart;
    private TextView scheduleView;
    private TextView descriptionView;
    private LinearLayout linearLayout;
    UserInfo userSingletonModel = UserInfo.getInstance();
    Schedule schedule;
    List<Task> data;

    private Gson gson;

    public ScheduleStatisticsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_schedule_statistics, container, false);
        barChart =  view.findViewById(R.id.bar_chart);
        scheduleView =  view.findViewById(R.id.title_view);
        descriptionView =  view.findViewById(R.id.description_view);
        linearLayout =  view.findViewById(R.id.display_layout);

        GsonBuilder builder = new GsonBuilder();
        builder.setDateFormat("M/d/yy hh:mm a");
        gson = builder.create();
        updateUI();

        String token = userSingletonModel.getToken();

        TaskService taskService = APIClient.createService(TaskService.class, token);
        Call<List<Task>> call = taskService.getTasks(schedule.getId());

        call.enqueue(new Callback<List<Task>>() {
            @Override
            public void onResponse(Call<List<Task>> call, Response<List<Task>> response) {
                data = (response.body() != null) ? response.body() : new ArrayList<Task>();
                updateBarChart(data);
            }

            @Override
            public void onFailure(Call<List<Task>> call, Throwable t) {
                Toast.makeText(getContext(), "Failed to get the tasks data", Toast.LENGTH_SHORT).show();

            }
        });

        ((MainActivity) getActivity()).setActionBarTitle(getString(R.string.schedule_statistics));
        ((MainActivity) getActivity()).setToolbar();
        setRetainInstance(true);
        return view;
    }

    private void updateUI() {
        if (getArguments() != null) {
            String data = getArguments().getString("schedule");
            if (data != null) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(data);

                    schedule = gson.fromJson(jsonObject.getJSONObject("schedule").toString(),
                            Schedule.class);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                scheduleView.setText(schedule.getName());
                descriptionView.setText(schedule.getDescription());
                //updateBarChart(schedule.getTasks());
            }
        }
    }

    private void updateBarChart(List<Task> tasks) {
        List<BarEntry> entries = new ArrayList<>();
        for (int i = 0; i < tasks.size(); i++) {
            entries.add(new BarEntry((float) (i +1), (float) tasks.get(i).getPercentage(),
                    tasks.get(i).getName()));
        }

        BarDataSet dataSet = new BarDataSet(entries, "Tasks");
        dataSet.setColor(R.color.colorPrimary);

        BarData barData = new BarData(dataSet);
        barChart.setData(barData);
        barChart.fitScreen();
        barChart.invalidate();

    }
}
