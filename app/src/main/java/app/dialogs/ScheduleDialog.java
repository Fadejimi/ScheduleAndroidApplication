package app.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;

import app.activities.MainActivity;
import app.model.UserInfo;
import app.rest.APIClient;
import app.rest.ScheduleService;
import app.rest.model.Schedule;
import app.utils.RestUtil;
import okhttp3.Headers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ScheduleDialog implements DialogInterface {
    private Dialog dialog;
    private Context context;
    private UserInfo userSingletonModel;

    public ScheduleDialog(Context context) {
        this.context = context;
        dialog = new Dialog(context);
        userSingletonModel = UserInfo.getInstance();
    }

    public void showDialog() {
        dialog.setContentView(app.scheduler.R.layout.new_schedule);
        dialog.setTitle(context.getString(app.scheduler.R.string.new_schedule));
        dialog.show();

        final EditText nameText =  dialog.findViewById(app.scheduler.R.id.title_edit_text);
        final EditText descriptionText =  dialog.findViewById(app.scheduler.R.id.description_edit_text);
        final BootstrapButton submitButton = dialog.findViewById(app.scheduler.R.id.btn_submit);
        final BootstrapButton cancelButton = dialog.findViewById(app.scheduler.R.id.btn_cancel);
        final String token = userSingletonModel.getToken();
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name = nameText.getText().toString();
                final String description = descriptionText.getText().toString();


                if (name.isEmpty() || name.equals("") ||
                        description.isEmpty() || description.equals("")) {
                    Toast.makeText(context, "Please fill in all the fields",
                            Toast.LENGTH_SHORT).show();
                } else {
                    final Schedule schedule = new Schedule();
                    schedule.setDescription(description);
                    schedule.setName(name);

                    ScheduleService scheduleService = APIClient.createService(ScheduleService.class,
                            token);

                    //progressBar.setVisibility(View.VISIBLE);
                    Call<Void> call = scheduleService.createSchedule(schedule);
                    call.enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            //progressBar.setVisibility(View.GONE);
                            Headers headers = response.headers();

                            schedule.setId(RestUtil.getId(headers));
                            Log.d("Schedule Id ", RestUtil.getId(headers) + "");
                            //data.add(schedule);
                            //updateUI();
                            Toast.makeText(context, "Schedule has been created",
                                    Toast.LENGTH_SHORT).show();

                            dialog.dismiss();
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            //progressBar.setVisibility(View.GONE);
                            Toast.makeText(context, "Schedule could not be created",
                                    Toast.LENGTH_LONG).show();
                        }
                    });
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
}
