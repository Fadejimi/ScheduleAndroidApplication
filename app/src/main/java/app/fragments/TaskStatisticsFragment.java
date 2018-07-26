package app.fragments;


import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import app.activities.MainActivity;
import app.recview.TaskDetailsAdapter;
import app.rest.model.Schedule;
import app.rest.model.Task;
import app.rest.model.TasksMilestone;
import com.scheduler.R;
import com.scheduler.databinding.FragmentTaskStatisticsBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

import app.viewmodels.TaskStatisticsViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class TaskStatisticsFragment extends Fragment implements Observer {
    private List<TasksMilestone> data;
    private Task task;
    private Schedule schedule;
    private final Context context = this.getActivity();
    private FragmentTaskStatisticsBinding binding;
    private TaskStatisticsViewModel taskStatisticsViewModel;

    public TaskStatisticsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding =  DataBindingUtil.inflate(inflater, R.layout.fragment_task_statistics,
                container, false);
        View view = binding.getRoot();

        task = new Task();
        schedule = new Schedule();
        setData();
        initBinding();
        setUpListsofTaskStatisticsView(binding.taskView);
        setUpObserver(taskStatisticsViewModel);

        ((MainActivity) getActivity()).setActionBarTitle(getString(R.string.task_statistics));
        return view;
    }

    private void initBinding() {
        taskStatisticsViewModel = new TaskStatisticsViewModel(context, task);
        binding.setTaskStatisticsViewModel(taskStatisticsViewModel);
    }

    private void setUpListsofTaskStatisticsView(RecyclerView recView) {
        TaskDetailsAdapter adapter = new TaskDetailsAdapter(task);
        recView.setAdapter(adapter);
        recView.setLayoutManager(new LinearLayoutManager(context));
    }

    private void setUpObserver(Observable o) {
        o.addObserver(this);
    }

    @Override
    public void update(Observable o, Object args) {
        if (o instanceof TaskStatisticsViewModel) {
            TaskDetailsAdapter adapter = (TaskDetailsAdapter) binding.taskView.getAdapter();
            TaskStatisticsViewModel viewModel = (TaskStatisticsViewModel) o;
            adapter.setTaskMilestones(viewModel.getTasksMilestones());
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        taskStatisticsViewModel.reset();
    }

    private void setData() {
        if (getArguments() != null) {
            String jsonData = getArguments().getString("schedule");
            if (jsonData != null) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(jsonData);

                    schedule.setId(jsonObject.getInt("id"));
                    schedule.setName(jsonObject.getString("name"));
                    schedule.setDescription(jsonObject.getString("description"));
                    schedule.setScheduleMilestone(jsonObject.getInt("scheduleMilestone"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            String jsonTask = getArguments().getString("task");
            if (jsonTask != null) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(jsonTask);

                    task.setId(jsonObject.getInt("id"));
                    task.setDescription(jsonObject.getString("description"));
                    task.setName(jsonObject.getString("name"));
                }
                catch(JSONException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

}
