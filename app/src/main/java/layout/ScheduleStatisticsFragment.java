package layout;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.model.Schedule;
import com.model.Task;
import com.scheduler.R;

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

    private Gson gson;

    public ScheduleStatisticsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_schedule_statistics, container, false);
        barChart = (BarChart) view.findViewById(R.id.bar_chart);
        scheduleView = (TextView) view.findViewById(R.id.title_view);
        descriptionView = (TextView) view.findViewById(R.id.description_view);
        linearLayout = (LinearLayout) view.findViewById(R.id.display_layout);

        GsonBuilder builder = new GsonBuilder();
        builder.setDateFormat("M/d/yy hh:mm a");
        gson = builder.create();
        updateUI();
        return view;
    }

    private void updateUI() {
        String data = getArguments().getString("schedule");
        if (data != null) {
            Schedule schedule = null;
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(data);

                schedule = gson.fromJson(jsonObject.getJSONObject("schedule").toString(),
                        Schedule.class);
            }
            catch(JSONException e) {
                e.printStackTrace();
            }

            scheduleView.setText(schedule.getName());
            descriptionView.setText(schedule.getDescription());
            updateBarChart(schedule.getTasks());
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
        barChart.invalidate();
    }
}
