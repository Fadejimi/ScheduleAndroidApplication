package app.recview;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;


import com.scheduler.R;
import com.scheduler.databinding.TaskItemBinding;

import java.util.List;


import app.rest.model.Schedule;
import app.rest.model.Task;
import app.viewmodels.ItemTaskViewModel;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {
    private List<Task> data;
    private Schedule schedule;

    public TaskAdapter(Schedule schedule) {
        this.schedule = schedule;
    }

    public class TaskViewHolder extends RecyclerView.ViewHolder {
        TaskItemBinding itemBinding;

        public TaskViewHolder(TaskItemBinding itemBinding) {
            super(itemBinding.getRoot());
            this.itemBinding = itemBinding;
        }

        void bindTask(Task task) {
            if (itemBinding.getTaskViewModel() == null) {
                itemBinding.setTaskViewModel(new ItemTaskViewModel(schedule, task,
                        itemView.getContext()));
            }
            else {
                itemBinding.getTaskViewModel().setTask(task);
                itemBinding.getTaskViewModel().setSchedule(schedule);
            }
        }
    }

    @Override
    public TaskAdapter.TaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        TaskItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.task_item, parent, false);
        TaskAdapter.TaskViewHolder viewHolder = new TaskAdapter.TaskViewHolder(binding);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final TaskAdapter.TaskViewHolder holder, final int position) {
        holder.bindTask(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setTasks(List<Task> tasks) {
        this.data = tasks;
    }
}


