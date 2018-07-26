package app.recview;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.scheduler.R;
import com.scheduler.databinding.ScheduleItemBinding;

import java.util.ArrayList;
import java.util.List;

import app.rest.model.Schedule;
import app.viewmodels.ItemScheduleViewModel;

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ScheduleViewHolder> {
    private List<Schedule> schedules;

    public ScheduleAdapter() {
        this.schedules = new ArrayList<>();
    }

    public static class ScheduleViewHolder extends RecyclerView.ViewHolder {
        ScheduleItemBinding itemBinding;

        public ScheduleViewHolder(ScheduleItemBinding itemBinding) {
            super(itemBinding.getRoot());
            this.itemBinding = itemBinding;
        }

        void bindSchedule(Schedule schedule) {
            if (itemBinding.getScheduleViewModel() == null) {
                itemBinding.setScheduleViewModel(new ItemScheduleViewModel(schedule,
                        itemView.getContext()));
            }
            else {
                itemBinding.getScheduleViewModel().setSchedule(schedule);
            }
        }
    }

    @Override
    public ScheduleAdapter.ScheduleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ScheduleItemBinding itemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.schedule_item, parent, false);

        return new ScheduleViewHolder(itemBinding);
    }

    @Override
    public void onBindViewHolder(ScheduleViewHolder holder, final int position) {
        holder.bindSchedule(schedules.get(position));
    }

    @Override
    public int getItemCount() {
        return schedules.size();
    }

    public void setScheduleList(List<Schedule> schedules) {
        this.schedules = schedules;
    }
}
