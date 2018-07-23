package app.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import app.activities.MainActivity;
import app.model.UserInfo;
import app.recview.TaskAdapter;
import app.rest.APIClient;
import app.rest.TaskService;
import app.rest.model.Schedule;
import app.rest.model.Task;
import app.scheduler.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class TaskFragment extends Fragment {
    private TextView emptyTextView;
    private ImageView emptyImageView;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private TaskAdapter listAdapter;
    private FloatingActionButton addTaskButton;

    private List<Task> data;
    private Schedule schedule;
    private UserInfo userSingletonModel;

    private String name, description, percentageText, token;

    private Gson gson;
    public TaskFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_task, container, false);

        emptyTextView =  view.findViewById(R.id.empty_textview);
        emptyImageView =  view.findViewById(R.id.empty_image);
        progressBar =  view.findViewById(R.id.progress_bar);
        recyclerView =  view.findViewById(R.id.task_view);
        addTaskButton =  view.findViewById(R.id.add_task);

        schedule = new Schedule();
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        token = userSingletonModel.getToken();

        data = new ArrayList<>();

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("M/dd/yy hh:mm a");
        gson = gsonBuilder.create();

        setData();

        progressBar.setVisibility(View.VISIBLE);

        TaskService taskService = APIClient.createService(TaskService.class, token);
        Call<List<Task>> call = taskService.getTasks(schedule.getId());

        call.enqueue(new Callback<List<Task>>() {
            @Override
            public void onResponse(Call<List<Task>> call, Response<List<Task>> response) {
                progressBar.setVisibility(View.GONE);
                data = (response.body() != null) ? new ArrayList<Task>() : response.body();
            }

            @Override
            public void onFailure(Call<List<Task>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getActivity(), "Failure to find data", Toast.LENGTH_SHORT).show();
            }
        });


        listAdapter = new TaskAdapter(this.getContext(), data);
        recyclerView.setAdapter(listAdapter);
        updateUI();

        ((MainActivity) getActivity()).setActionBarTitle(getString(R.string.task));
        //((MainActivity) getActivity()).setToolbar();
        addTaskButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                showAddTask();
            }
        });

        return view;
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

    public void showAddTask() {

    }


}
