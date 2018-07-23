package app.fragments;


import android.app.ProgressDialog;
import android.content.Context;
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

import app.activities.MainActivity;
import app.dialogs.ScheduleDialog;
import app.model.UserInfo;
import app.recview.ScheduleAdapter;
import app.rest.APIClient;
import app.rest.ScheduleService;
import app.rest.model.Schedule;
import app.scheduler.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class ScheduleFragment extends Fragment {
    private TextView emptyTextView;
    private ImageView emptyImageView;
    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private ScheduleAdapter listAdapter;
    private FloatingActionButton addScheduleBtn;

    private final Context context = this.getActivity();
    private String token;
    private List<Schedule> data;
    private UserInfo userSingletonModel;

    public ScheduleFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_schedule, container, false);

        emptyTextView =  view.findViewById(R.id.empty_textview);
        emptyImageView =  view.findViewById(R.id.empty_image);
        progressBar =  view.findViewById(R.id.progress_bar);
        recyclerView =  view.findViewById(R.id.schedule_view);
        addScheduleBtn =  view.findViewById(R.id.add_schedule);

        data = new ArrayList<>();
        //setData();

        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        token = userSingletonModel.getToken();

        // Set up progress before call
        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMax(100);
        progressDialog.setMessage("Loading....");
        progressDialog.setTitle("");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        // show it
        progressDialog.show();

        ScheduleService scheduleService = APIClient.createService(ScheduleService.class, token);
        Call<List<Schedule>> call = scheduleService.getSchedules();

        call.enqueue(new Callback<List<Schedule>>() {
            @Override
            public void onResponse(Call<List<Schedule>> call, Response<List<Schedule>> response) {
                progressDialog.dismiss();
                data = (response.body() != null) ? new ArrayList<Schedule>() : response.body();
            }

            @Override
            public void onFailure(Call<List<Schedule>> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(context, "Failure to find data", Toast.LENGTH_SHORT).show();
            }
        });

        listAdapter = new ScheduleAdapter(this.getContext(), data);
        recyclerView.setAdapter(listAdapter);
        updateUI();

        ((MainActivity) getActivity()).setActionBarTitle(getString(R.string.schedule));
        //((MainActivity) getActivity()).setToolbar();

        addScheduleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "Add Schedule button clicked", Toast.LENGTH_SHORT).show();
                showAddDialog();
            }
        });
        return view;
    }

    public void setData(List<Schedule> schedules) {
        this.data = schedules;

    }

    private void showAddDialog() {
        ScheduleDialog scheduleDialog = new ScheduleDialog(this.getContext());
        scheduleDialog.showDialog();
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
