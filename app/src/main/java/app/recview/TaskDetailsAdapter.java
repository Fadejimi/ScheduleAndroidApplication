package app.recview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.List;

import app.scheduler.R;
import androidx.recyclerview.widget.RecyclerView;
import app.rest.model.Task;
import app.rest.model.TasksMilestone;
import app.utils.DateUtil;

public class TaskDetailsAdapter extends RecyclerView.Adapter<TaskDetailsAdapter.ViewHolder> {
    private List<TasksMilestone> data;
    private Task task;
    private Context mCon;

    public TaskDetailsAdapter(Context mCon, List<TasksMilestone> tasks, Task task) {
        this.data = tasks;
        this.mCon = mCon;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView nameTextView;
        //private TextView percentageView;
        private RatingBar ratingBar;
        private ImageView menuView;

        public ViewHolder(View itemView) {
            super(itemView);

            nameTextView = itemView.findViewById(app.scheduler.R.id.title_view);
            //percentageView = (TextView) itemView.findViewById(R.id.percentage_view);
            ratingBar = itemView.findViewById(app.scheduler.R.id.rating_view);
            menuView = itemView.findViewById(app.scheduler.R.id.img_more);

            menuView.setVisibility(View.GONE);
        }
    }

    @Override
    public TaskDetailsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(app.scheduler.R.layout.recyclerview_item,
                parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final TaskDetailsAdapter.ViewHolder holder, final int position) {
        holder.nameTextView.setText(DateUtil.formatter(data.get(position).getCreatedAt()));
        double percentage = data.get(position).getPercentage();
        StringBuilder sb = new StringBuilder(String.valueOf(percentage));
        sb.append(mCon.getString(R.string.percentage));
        //holder.percentageView.setText(sb.toString());
        double rating = percentage/100 * 5;
        holder.ratingBar.setRating((float) rating);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
