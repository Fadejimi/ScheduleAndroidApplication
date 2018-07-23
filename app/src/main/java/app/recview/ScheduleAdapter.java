package app.recview;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import app.activities.MainActivity;
import app.fragments.ScheduleFragment;
import app.fragments.ScheduleStatisticsFragment;
import app.fragments.TaskFragment;
import app.scheduler.R;

import java.util.List;

import app.rest.model.Schedule;

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ViewHolder> {
    private List<Schedule> schedules;
    private Context mCon;

    public ScheduleAdapter(Context mCon, List<Schedule> schedules) {
        this.mCon = mCon;
        this.schedules = schedules;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView nameTextView;
        private RatingBar ratingBar;
        private ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);

            this.nameTextView = itemView.findViewById(R.id.title_view);
            this.ratingBar = itemView.findViewById(R.id.rating_view);
            this.imageView = itemView.findViewById(R.id.img_more);

            ratingBar.setFocusable(false);
            ratingBar.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return true;
                }
            });
        }
    }

    @Override
    public ScheduleAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item,
                parent, false);

        ScheduleAdapter.ViewHolder viewHolder = new ScheduleAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ScheduleAdapter.ViewHolder holder, final int position) {
        holder.nameTextView.setText(schedules.get(position).getName());

        StringBuilder sb = new StringBuilder(String.valueOf(schedules.get(position).getScheduleMilestone()));
        sb.append(mCon.getString(R.string.percentage));

        //holder.percentageView.setText(sb.toString());
        holder.ratingBar.setRating((float) schedules.get(position).getRating());

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mCon, "Item : " + position + " has been clicked",
                        Toast.LENGTH_SHORT).show();

                showPopUpMenu(holder.imageView, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return schedules.size();
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
            schedule = schedules.get(position);
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            Fragment fragment = null;
            Bundle bundle = new Bundle();
            String tag = null;
            switch(item.getItemId()) {
                case R.id.view_statistics:
                    Toast.makeText(mCon, "Name: " + schedule.getName() + ", statistics",
                            Toast.LENGTH_SHORT).show();

                    fragment = new ScheduleStatisticsFragment();
                    tag = MainActivity.SCHEDULE_STATISTICS_FRAGMENT;

                    break;
                case R.id.view_tasks:
                    Toast.makeText(mCon, "Name: " + schedule.getName() + ", tasks",
                            Toast.LENGTH_SHORT).show();

                    fragment = new TaskFragment();
                    tag = MainActivity.TASK_FRAGMENT;
                    break;
            }

            if (fragment != null && tag != null) {
                bundle.putString("schedule", schedule.toJson());
                fragment.setArguments(bundle);
                FragmentTransaction transaction = ((MainActivity)mCon).getSupportFragmentManager().beginTransaction();
                transaction
                        .replace(R.id.content_view, fragment, tag)
                        .addToBackStack(null)
                        .commit();
            }
            return false;
        }
    }
}
