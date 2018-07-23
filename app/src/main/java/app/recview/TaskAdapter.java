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

import java.util.List;

import app.activities.MainActivity;
import app.fragments.TaskStatisticsFragment;
import app.model.UserInfo;
import app.rest.APIClient;
import app.rest.TaskService;
import app.rest.model.Schedule;
import app.rest.model.Task;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {
    private List<Task> data;
    private Context mCon;
    private UserInfo userSingletonModel = UserInfo.getInstance();
    private String token;
    private Schedule schedule;

    public TaskAdapter(Context context, List<Task> tasks) {
        this.mCon = context;
        this.data = tasks;
        this.schedule = schedule;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView nameTextView;
        //private TextView percentageView;
        private RatingBar ratingBar;
        private ImageView menuView;

        public ViewHolder(View itemView) {
            super(itemView);

            nameTextView = (TextView) itemView.findViewById(app.scheduler.R.id.title_view);
            //percentageView = (TextView) itemView.findViewById(R.id.percentage_view);
            ratingBar = (RatingBar) itemView.findViewById(app.scheduler.R.id.rating_view);
            menuView = (ImageView) itemView.findViewById(app.scheduler.R.id.img_more);

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
    public TaskAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(app.scheduler.R.layout.recyclerview_item,
                parent, false);

        TaskAdapter.ViewHolder viewHolder = new TaskAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final TaskAdapter.ViewHolder holder, final int position) {
        holder.nameTextView.setText(data.get(position).getName());
        double percentage = data.get(position).getPercentage();
        StringBuilder sb = new StringBuilder(String.valueOf(data.get(position).getPercentage()));
        sb.append(mCon.getString(app.scheduler.R.string.percentage));
        //holder.percentageView.setText(sb.toString());
        double rating = percentage/100 * 5;
        holder.ratingBar.setRating((float) rating);

        holder.menuView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mCon, "Item : " + position + " has been clicked",
                        Toast.LENGTH_SHORT).show();

                showPopUpMenu(holder.menuView, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void showPopUpMenu(View view, int position) {
        PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
        MenuInflater menuInflater = popupMenu.getMenuInflater();
        menuInflater.inflate(app.scheduler.R.menu.task_popup_menu, popupMenu.getMenu());
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
                case app.scheduler.R.id.delete_task:
                    Toast.makeText(mCon, "Name: " + task.getName() + ", delete",
                            Toast.LENGTH_SHORT).show();
                    //fragment = new TaskStasticsFragment();

                    //showUpdateTask(position);
                    //progressBar.setVisibility(View.VISIBLE);
                    token = userSingletonModel.getToken();
                    TaskService service = APIClient.createService(TaskService.class, token);
                    Call<Task> call = service.deleteTask(schedule.getId(), task.getId());

                    call.enqueue(new Callback<Task>() {
                        @Override
                        public void onResponse(Call<Task> call, Response<Task> response) {
                            //progressBar.setVisibility(View.GONE);
                            Toast.makeText(mCon, "Successfully deleted task",
                                    Toast.LENGTH_SHORT).show();

                            //data.remove(position);
                            //updateUI();
                        }

                        @Override
                        public void onFailure(Call<Task> call, Throwable t) {
                            //progressBar.setVisibility(View.GONE);
                            Toast.makeText(mCon, "Failed to delete task",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                    break;
                case app.scheduler.R.id.view_tasks:
                    Toast.makeText(mCon, "Name: " + task.getName() + ", tasks",
                            Toast.LENGTH_SHORT).show();
                    fragment = new TaskStatisticsFragment();
                    tag = MainActivity.TASK_STATISTICS_FRAGMENT;
                    break;
            }

            Bundle bundle = new Bundle();
            bundle.putString("task", task.toJson());
            bundle.putString("schedule", schedule.toJson());
            fragment.setArguments(bundle);
            FragmentTransaction transaction = ((MainActivity) mCon).getSupportFragmentManager().beginTransaction();
            transaction
                    .replace(app.scheduler.R.id.content_view, fragment, tag)
                    .addToBackStack(null)
                    .commit();
            return false;
        }


    }
}


