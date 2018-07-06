package layout;


import android.app.Dialog;
import android.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
//import android.support.v4.app.Fragment;
import android.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import com.scheduler.MainActivity;
import com.scheduler.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class TaskFragment extends Fragment {
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
    public TaskFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_task, container, false);

        emptyTextView = (TextView) view.findViewById(R.id.empty_textview);
        emptyImageView = (ImageView) view.findViewById(R.id.empty_image);
        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
        recyclerView = (RecyclerView) view.findViewById(R.id.task_view);
        addTaskButton = (FloatingActionButton) view.findViewById(R.id.add_task);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        data = new ArrayList<>();

        listAdapter = new ListAdapter(data);
        recyclerView.setAdapter(listAdapter);

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("M/dd/yy hh:mm a");
        gson = gsonBuilder.create();

        setData();
        updateUI();

        ((MainActivity) getActivity()).setActionBarTitle(getString(R.string.task));
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

                    schedule = gson.fromJson(jsonObject.getJSONObject("schedule").toString(),
                            Schedule.class);
                    data = schedule.getTasks();
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
        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.new_task);
        dialog.setTitle("New Task");
        dialog.show();

        final EditText nameEditText = (EditText) dialog.findViewById(R.id.title_edit_text);
        final EditText descriptionEditText = (EditText) dialog.findViewById(R.id.description_edit_text);
        final EditText percentageEditText = (EditText) dialog.findViewById(R.id.percentage_text);
        BootstrapButton submitButton = (BootstrapButton) dialog.findViewById(R.id.btn_submit);
        BootstrapButton cancelButton = (BootstrapButton) dialog.findViewById(R.id.btn_cancel);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = nameEditText.getText().toString();
                description = descriptionEditText.getText().toString();
                percentageText = percentageEditText.getText().toString();

                if (isValid()) {
                    double percentage = Double.parseDouble(percentageText);

                    Task task = new Task(name, description, percentage);
                    data.add(task);
                    updateUI();
                    /** -- ToDo Upload task to server -- **/
                    Toast.makeText(getActivity(), "Task has been created", Toast.LENGTH_SHORT).show();
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
        String errorMessage = "";
        if (name.isEmpty() || name.equals("")) {
            errorMessage += "Task title is empty\n";
        }
        if (description.isEmpty() || description.equals("")) {
            errorMessage += "Description title is empty\n";
        }
        if (percentageText.isEmpty() || percentageText.equals("")) {
            percentageText = "0";
        }

        if (errorMessage.equals("")) {
            return true;
        }
        else {
            Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_SHORT).show();
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

            holder.menuView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getActivity(), "Item : " + position + " has been clicked",
                            Toast.LENGTH_SHORT).show();

                    showPopUpMenu(holder.menuView, position);
                }
            });
        }

        @Override
        public int getItemCount() {
            return data.size();
        }
    }

    public void showPopUpMenu(View view, int position) {
        PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
        MenuInflater menuInflater = popupMenu.getMenuInflater();
        menuInflater.inflate(R.menu.task_popup_menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new TaskMenuItemClickListener(position));
        popupMenu.show();
    }

    class TaskMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {
        Task task;
        int position;
        public TaskMenuItemClickListener(int position) {
            this.position = position;
            task = data.get(position);
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            Fragment fragment = null;
            String tag = null;
            switch(item.getItemId()) {
                case R.id.update_task:
                    Toast.makeText(getActivity(), "Name: " + task.getName() + ", statistics",
                            Toast.LENGTH_SHORT).show();
                    //fragment = new TaskStasticsFragment();

                    showUpdateTask(position);
                    break;
                case R.id.view_tasks:
                    Toast.makeText(getActivity(), "Name: " + task.getName() + ", tasks",
                            Toast.LENGTH_SHORT).show();
                    fragment = new TaskStatisticsFragment();
                    tag = MainActivity.TASK_STATISTICS_FRAGMENT;
                    break;
            }

            FragmentManager fm = getActivity().getFragmentManager();
            fm.beginTransaction()
                    .replace(R.id.content_view, fragment, tag)
                    .addToBackStack(null)
                    .commit();
            return false;
        }

        private void showUpdateTask(int position) {
            Dialog dialog = new Dialog(getActivity());
            dialog.setContentView(R.layout.new_task);
            dialog.setTitle(getString(R.string.update_task));
            dialog.show();

            EditText titleText = (EditText) dialog.findViewById(R.id.title_edit_text);
            final EditText descriptionEditText = (EditText) dialog.findViewById(R.id.description_edit_text);
            final EditText percentageEditText = (EditText) dialog.findViewById(R.id.percentage_text);
            BootstrapButton submitButton = (BootstrapButton) dialog.findViewById(R.id.btn_submit);
            BootstrapButton cancelButton = (BootstrapButton) dialog.findViewById(R.id.btn_cancel);
            final int pos = position;

            titleText.setVisibility(View.GONE);
            descriptionEditText.setVisibility(View.GONE);

            submitButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String percentageText = percentageEditText.getText().toString();

                    if (percentageText != null && isDouble(percentageText)) {
                        double percentage = Double.parseDouble(percentageText);
                        Task task = data.get(pos);
                        task.setPercentage(percentage);

                        listAdapter.notifyItemChanged(pos);
                    }
                }
            });
        }

        private boolean isDouble(String text) {
            try {
                Double.parseDouble(text);
                return true;
            }
            catch (NumberFormatException ex) {
                return false;
            }
        }
    }
}
