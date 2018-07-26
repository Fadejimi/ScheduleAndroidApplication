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
import app.recview.TaskAdapter;
import app.rest.model.Schedule;
import com.scheduler.R;
import com.scheduler.databinding.FragmentTaskBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Observable;
import java.util.Observer;

import app.viewmodels.TaskViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class TaskFragment extends Fragment implements Observer {
    private Schedule schedule;

    private final Context context = this.getContext();
    private FragmentTaskBinding taskBinding;
    private TaskViewModel taskViewModel;

    public TaskFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        taskBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_task, container,
                false);
        View view = taskBinding.getRoot();

        initBinding();
        setUpListOfTasksView(taskBinding.taskView);
        setUpObserver(taskViewModel);
        ((MainActivity) getActivity()).setActionBarTitle(getString(R.string.task));
        //((MainActivity) getActivity()).setToolbar();
        return view;
    }

    private void initBinding() {
        schedule = new Schedule();
        setData();
        taskViewModel = new TaskViewModel(context, schedule);
        taskBinding.setTaskViewModel(taskViewModel);
    }

    private void setUpListOfTasksView(RecyclerView view) {
        TaskAdapter taskAdapter = new TaskAdapter(schedule);
        view.setAdapter(taskAdapter);
        view.setLayoutManager(new LinearLayoutManager(context));
    }

    private void setUpObserver(Observable o) {
        o.addObserver(this);
    }

    @Override
    public void update(Observable o, Object args) {
        if (o instanceof TaskViewModel) {
            TaskAdapter taskAdapter = (TaskAdapter) taskBinding.taskView.getAdapter();
            TaskViewModel taskViewModel = (TaskViewModel) o;
            taskAdapter.setTasks(taskViewModel.getTaskList());
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        taskViewModel.reset();
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
        }
    }


}
