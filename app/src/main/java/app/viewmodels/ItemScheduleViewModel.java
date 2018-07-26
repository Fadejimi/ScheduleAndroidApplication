package app.viewmodels;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.databinding.BaseObservable;

import android.view.View;
import android.widget.Toast;

import com.scheduler.R;
import app.activities.MainActivity;

import app.model.UserInfo;
import app.rest.APIClient;
import app.rest.ScheduleService;
import app.rest.model.Schedule;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ItemScheduleViewModel extends BaseObservable {

    private Schedule schedule;
    private Context context;
    private String token = UserInfo.getInstance().getToken();

    public ItemScheduleViewModel(Schedule schedule, Context context) {
        this.schedule = schedule;
        this.context = context;
    }

    public String getName() {
        return schedule.getName();
    }

    public float getRating() {
        return (float) schedule.getRating();
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
        notifyChange();
    }

    public void onClickView(View v) {
        MainActivity.showTasks(schedule);
    }

    public void onClickDelete(View v) {
        showDelete(v);
    }

    public void onClickStatistics(View v) {
        MainActivity.showStatistics(schedule);
    }

    private void showDelete(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
        builder.setMessage("Are you sure you want to delete the schedule")
                .setPositiveButton(R.string.btn_submit, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        /** TODO - ADD THE PROGRESS BAR BUTTON **/
                        ScheduleService service = APIClient.createService(ScheduleService.class,
                                token);
                        Call<Schedule> call = service.deleteSchedule(schedule.getId());

                        call.enqueue(new Callback<Schedule>() {
                            @Override
                            public void onResponse(Call<Schedule> call, Response<Schedule> response) {
                                Toast.makeText(context, "Successfully deleted", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailure(Call<Schedule> call, Throwable t) {
                                Toast.makeText(context, "Failed to delete", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                })
                .setNegativeButton(R.string.btn_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                })
                .create()
                .show();


    }
}
