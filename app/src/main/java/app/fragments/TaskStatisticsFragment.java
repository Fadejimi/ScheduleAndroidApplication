package app.fragments;


import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import app.activities.MainActivity;
import app.dialogs.TaskDetailsDialog;
import app.model.UserInfo;
import app.recview.TaskDetailsAdapter;
import app.rest.APIClient;
import app.rest.TaskMilestoneService;
import app.rest.model.Schedule;
import app.rest.model.Task;
import app.rest.model.TasksMilestone;
import app.scheduler.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class TaskStatisticsFragment extends Fragment {
    private TextView emptyTextView;
    private ImageView emptyImageView;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private TaskDetailsAdapter listAdapter;
    private FloatingActionButton addTaskButton;

    private List<TasksMilestone> data;
    private Schedule schedule;
    private Task task;
    private UserInfo userSingletonModel = UserInfo.getInstance();
    private TaskDetailsDialog taskDetailsDialog;

    private String name, description, percentageText, token;

    private Gson gson;

    public TaskStatisticsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_task_statistics, container, false);

        emptyTextView =  view.findViewById(R.id.empty_textview);
        emptyImageView =  view.findViewById(R.id.empty_image);
        progressBar =  view.findViewById(R.id.progress_bar);
        recyclerView =  view.findViewById(R.id.task_view);
        addTaskButton =  view.findViewById(R.id.add_task);

        data = new ArrayList<>();
        schedule = new Schedule();
        task = new Task();

        token = userSingletonModel.getToken();

        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("M/dd/yy hh:mm a");
        gson = gsonBuilder.create();

        setData();

        progressBar.setVisibility(View.VISIBLE);

        TaskMilestoneService taskMilestoneService = APIClient.createService(TaskMilestoneService.class, token);
        Call<List<TasksMilestone>>call = taskMilestoneService.getTaskMilestones(task.getId());

        call.enqueue(new Callback<List<TasksMilestone>>() {
            @Override
            public void onResponse(Call<List<TasksMilestone>> call, Response<List<TasksMilestone>> response) {
                data = (response.body() != null) ? response.body() : new ArrayList<TasksMilestone>();
            }

            @Override
            public void onFailure(Call<List<TasksMilestone>> call, Throwable t) {
                Toast.makeText(getContext(), "Failed to load data", Toast.LENGTH_SHORT).show();
            }
        });

        updateUI();

        listAdapter = new TaskDetailsAdapter(getContext(), data, task);
        recyclerView.setAdapter(listAdapter);
        ((MainActivity) getActivity()).setActionBarTitle(getString(R.string.task_statistics));
        //((MainActivity) getActivity()).setToolbar();

        taskDetailsDialog = new TaskDetailsDialog(getContext(), task);

        addTaskButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                showAddTask();
            }
        });

        return view;
    }

    private void showAddTask() {
        taskDetailsDialog.showDialog();
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

    private void updateUI() {
        if (data.size() == 0) {
            emptyTextView.setVisibility(View.VISIBLE);
            emptyImageView.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
        else {
            emptyTextView.setVisibility(View.GONE);
            emptyImageView.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);

            listAdapter.notifyDataSetChanged();
        }
    }


}
