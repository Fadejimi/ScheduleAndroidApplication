package layout;


import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.media.Rating;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
//import android.support.v4.app.Fragment;
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
import com.model.Schedule;
import com.scheduler.MainActivity;
import com.scheduler.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ScheduleFragment extends Fragment {
    private TextView emptyTextView;
    private ImageView emptyImageView;
    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private ListAdapter listAdapter;
    private FloatingActionButton addScheduleBtn;

    private List<Schedule> data;

    public ScheduleFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_schedule, container, false);

        emptyTextView = (TextView) view.findViewById(R.id.empty_textview);
        emptyImageView = (ImageView) view.findViewById(R.id.empty_image);
        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
        recyclerView = (RecyclerView) view.findViewById(R.id.schedule_view);
        addScheduleBtn = (FloatingActionButton) view.findViewById(R.id.add_schedule);

        data = new ArrayList<>();
        setData();

        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        listAdapter = new ListAdapter(data);
        recyclerView.setAdapter(listAdapter);
        updateUI();


        addScheduleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "Add Schedule button clicked", Toast.LENGTH_SHORT).show();
                showAddDialog();
            }
        });
        return view;
    }

    public void setData() {
        data.add(new Schedule("New Project", "This is a new project", 10));
        data.add(new Schedule("2nd Project", "This is another project", 50));
    }

    public void setData(List<Schedule> schedules) {
        this.data = schedules;

    }

    private void showAddDialog() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.new_schedule);
        dialog.setTitle(getString(R.string.new_schedule));
        dialog.show();

        final EditText nameText = (EditText) dialog.findViewById(R.id.title_edit_text);
        final EditText descriptionText = (EditText) dialog.findViewById(R.id.description_edit_text);
        final BootstrapButton submitButton = (BootstrapButton) dialog.findViewById(R.id.btn_submit);
        final BootstrapButton cancelButton = (BootstrapButton) dialog.findViewById(R.id.btn_cancel);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameText.getText().toString();
                String description = descriptionText.getText().toString();

                if (name.isEmpty() || name.equals("") ||
                        description.isEmpty() || description.equals("")) {
                    Toast.makeText(getActivity(), "Please fill in all the fields",
                            Toast.LENGTH_SHORT).show();
                }
                else {
                    Schedule schedule = new Schedule(name, description, 0);
                    data.add(schedule);
                    updateUI();
                    //
                    Toast.makeText(getActivity(), "Schedule has been created",
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

    private class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {
        private List<Schedule> schedules;

        public ListAdapter(List<Schedule> data) {
            this.schedules = data;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            private TextView nameView;
            //private TextView percentageView;
            private RatingBar ratingView;
            private ImageView moreView;

            public ViewHolder(View itemView) {
                super(itemView);

                this.nameView = (TextView) itemView.findViewById(R.id.title_view);
                //this.percentageView = (TextView) itemView.findViewById(R.id.percentage_view);
                this.ratingView = (RatingBar) itemView.findViewById(R.id.rating_view);
                this.moreView = (ImageView) itemView.findViewById(R.id.img_more);
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
            holder.nameView.setText(schedules.get(position).getName());

            StringBuilder sb = new StringBuilder(String.valueOf(schedules.get(position).getPercentage()));
            sb.append(getString(R.string.percentage));

            //holder.percentageView.setText(sb.toString());
            holder.ratingView.setRating((float) schedules.get(position).getRating());

            holder.moreView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getActivity(), "Item : " + position + " has been clicked",
                            Toast.LENGTH_SHORT).show();

                    showPopUpMenu(holder.moreView, position);
                }
            });
        }

        @Override
        public int getItemCount() {
            return schedules.size();
        }
    }

    public void showPopUpMenu(View view, int position) {
        PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
        MenuInflater menuInflater = popupMenu.getMenuInflater();
        menuInflater.inflate(R.menu.schedule_popup_menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new ScheduleMenuItemClickListener(position));
        popupMenu.show();
    }

    class ScheduleMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {
        Schedule schedule;
        public ScheduleMenuItemClickListener(int position) {
            schedule = data.get(position);
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            Fragment fragment = null;
            String tag = null;
            switch(item.getItemId()) {
                case R.id.view_statistics:
                    Toast.makeText(getActivity(), "Name: " + schedule.getName() + ", statistics",
                            Toast.LENGTH_SHORT).show();

                    fragment = new ScheduleStatisticsFragment();
                    tag = MainActivity.SCHEDULE_STATISTICS_FRAGMENT;

                    break;
                case R.id.view_tasks:
                    Toast.makeText(getActivity(), "Name: " + schedule.getName() + ", tasks",
                            Toast.LENGTH_SHORT).show();

                    fragment = new TaskFragment();
                    tag = MainActivity.TASK_FRAGMENT;
                    break;
            }

            if (fragment != null && tag != null) {
                FragmentManager manager = getActivity().getFragmentManager();
                manager.beginTransaction()
                        .replace(R.id.content_view, fragment, tag)
                        .addToBackStack(null)
                        .commit();
            }
            return false;
        }
    }
}
