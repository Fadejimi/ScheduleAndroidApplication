package layout;


import android.app.Dialog;
import android.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
//import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.model.Schedule;
import com.model.Task;
import com.scheduler.R;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class TaskStatisticsFragment extends Fragment {
    private TextView emptyTextView;
    private ImageView emptyImageView;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private ListAdapter listAdapter;
    private FloatingActionButton addTaskButton;

    private List<Task> data;
    private Schedule schedule;

    private String name, description, percentageText;

    private Gson gson;

    public TaskStatisticsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_task_statistics, container, false);

        emptyTextView = (TextView) view.findViewById(R.id.empty_textview);
        emptyImageView = (ImageView) view.findViewById(R.id.empty_image);
        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
        recyclerView = (RecyclerView) view.findViewById(R.id.task_view);
        addTaskButton = (FloatingActionButton) view.findViewById(R.id.add_task);

        data = new ArrayList<>();

        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        listAdapter = new ListAdapter(data);
        recyclerView.setAdapter(listAdapter);

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("M/dd/yy hh:mm a");
        gson = gsonBuilder.create();

        setData();
        updateUI();

        addTaskButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                showAddTask();
            }
        });

        return view;
    }

    private void setData() {
        String jsonData = getArguments().getString("tasks");
        if (jsonData != null) {
            JSONArray jsonArray = null;
            try {
                jsonArray = new JSONArray(jsonData);

                data = Arrays.asList(gson.fromJson(jsonArray.toString(),
                        Task[].class));
            }
            catch(JSONException e) {
                e.printStackTrace();
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
        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.new_task);
        dialog.setTitle("New Task");
        dialog.show();

        EditText nameEditText = (EditText) dialog.findViewById(R.id.title_edit_text);
        EditText descriptionEditText = (EditText) dialog.findViewById(R.id.description_edit_text);
        final EditText percentageEditText = (EditText) dialog.findViewById(R.id.percentage_text);
        BootstrapButton submitButton = (BootstrapButton) dialog.findViewById(R.id.btn_submit);
        BootstrapButton cancelButton = (BootstrapButton) dialog.findViewById(R.id.btn_cancel);

        nameEditText.setVisibility(View.GONE);
        descriptionEditText.setVisibility(View.GONE);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                percentageText = percentageEditText.getText().toString();

                if (isValid()) {
                    double percentage = Double.parseDouble(percentageText);

                    Task task = new Task("name", "description", percentage);
                    data.add(task);
                    updateUI();
                    /** -- ToDo Upload task to server -- **/
                    Toast.makeText(getActivity(), "Task has been updated " + percentage + "%",
                            Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }

            }

        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private boolean isValid() {
        String errorString = "";
        if (percentageText.isEmpty() || percentageText.equals("")) {
            errorString += "Please fill in the percentage field\n";
        }
        if (isDouble(percentageText)) {
            errorString += "Invalid percentage value\n";
        }

        if (errorString.equals("")) {
            return true;
        }
        else {
            Toast.makeText(getActivity(), errorString, Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private boolean isDouble(String st) {
        try {
            Double.parseDouble(st);
            return true;
        }
        catch(Exception ex) {
            return false;
        }
    }

    private class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {
        private List<Task> tasks;

        public ListAdapter(List<Task> tasks) {
            this.tasks = tasks;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            private TextView nameTextView;
            //private TextView percentageView;
            private RatingBar ratingBar;
            private ImageView menuView;

            public ViewHolder(View itemView) {
                super(itemView);

                nameTextView = (TextView) itemView.findViewById(R.id.title_view);
                //percentageView = (TextView) itemView.findViewById(R.id.percentage_view);
                ratingBar = (RatingBar) itemView.findViewById(R.id.rating_view);
                menuView = (ImageView) itemView.findViewById(R.id.img_more);

                menuView.setVisibility(View.GONE);
            }
        }

        @Override
        public ListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item,
                    parent, false);

            ViewHolder viewHolder = new ViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(final ListAdapter.ViewHolder holder, final int position) {
            holder.nameTextView.setText(data.get(position).getName());
            StringBuilder sb = new StringBuilder(String.valueOf(data.get(position).getPercentage()));
            sb.append(getString(R.string.percentage));
            //holder.percentageView.setText(sb.toString());
            holder.ratingBar.setRating((float) data.get(position).getRating());


        }

        @Override
        public int getItemCount() {
            return data.size();
        }
    }
}
