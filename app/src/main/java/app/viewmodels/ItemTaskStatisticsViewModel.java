package app.viewmodels;

import android.databinding.BaseObservable;

import app.rest.model.TasksMilestone;
import app.utils.DateUtil;

public class ItemTaskStatisticsViewModel extends BaseObservable {
    private TasksMilestone tasksMilestone;

    public ItemTaskStatisticsViewModel(TasksMilestone tasksMilestone) {
        this.tasksMilestone = tasksMilestone;
    }

    public String getName() {
        return DateUtil.formatter(tasksMilestone.getCreatedAt());
    }

    public float getRating() {
        return (float) tasksMilestone.getPercentage() / 100 * 5;
    }

    public void setTasksMilestone(TasksMilestone tasksMilestone) {
        this.tasksMilestone = tasksMilestone;
    }
}
