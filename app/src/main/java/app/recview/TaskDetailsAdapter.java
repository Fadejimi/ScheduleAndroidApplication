package app.recview;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.scheduler.R;
import com.scheduler.databinding.TaskMilestoneItemBinding;

import java.util.List;

import app.rest.model.Task;
import app.rest.model.TasksMilestone;
import app.viewmodels.ItemTaskStatisticsViewModel;

public class TaskDetailsAdapter extends RecyclerView.Adapter<TaskDetailsAdapter.TaskDetailsViewHolder> {
    private List<TasksMilestone> data;
    private Task task;
    public TaskDetailsAdapter(Task task) {
        this.task = task;
    }

    public class TaskDetailsViewHolder extends RecyclerView.ViewHolder {
        TaskMilestoneItemBinding binding;

        public TaskDetailsViewHolder(TaskMilestoneItemBinding itemBinding) {
            super(itemBinding.getRoot());
            this.binding = itemBinding;
        }

        void bindTask(TasksMilestone tasksMilestone) {
            if (binding.getTaskViewModel() == null) {
                binding.setTaskViewModel(new ItemTaskStatisticsViewModel(tasksMilestone));
            }
            else {
                binding.getTaskViewModel().setTasksMilestone(tasksMilestone);
            }
        }
    }

    @Override
    public TaskDetailsAdapter.TaskDetailsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        TaskMilestoneItemBinding itemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.task_milestone_item, parent, false);
        TaskDetailsAdapter.TaskDetailsViewHolder viewHolder = new TaskDetailsViewHolder(itemBinding);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TaskDetailsViewHolder taskDetailsViewHolder, int i) {
        taskDetailsViewHolder.bindTask(data.get(i));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setTaskMilestones(List<TasksMilestone> data) {
        this.data = data;
    }
}
