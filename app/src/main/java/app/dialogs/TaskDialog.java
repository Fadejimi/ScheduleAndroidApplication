package app.dialogs;


import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;

import app.model.UserInfo;
import app.rest.APIClient;
import app.rest.TaskService;
import app.rest.model.Schedule;
import app.rest.model.Task;
import app.utils.DateUtil;
import app.utils.RestUtil;
import okhttp3.Headers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TaskDialog implements DialogInterface {
    final Dialog dialog;
    String name;
    String description;
    String percentageText;
    UserInfo userSingletonModel = UserInfo.getInstance();
    Schedule schedule;
    Context mCon;

    public TaskDialog(Context context, Schedule schedule) {
        dialog = new Dialog(context);
        this.mCon = context;
        this.schedule = schedule;
    }

    public void showDialog() {

        dialog.setContentView(app.scheduler.R.layout.new_task);
        dialog.setTitle("New Task");
        dialog.show();

        final EditText nameEditText =  dialog.findViewById(app.scheduler.R.id.title_edit_text);
        final EditText descriptionEditText =  dialog.findViewById(app.scheduler.R.id.description_edit_text);
        final EditText percentageEditText =  dialog.findViewById(app.scheduler.R.id.percentage_text);
        BootstrapButton submitButton =  dialog.findViewById(app.scheduler.R.id.btn_submit);
        BootstrapButton cancelButton =  dialog.findViewById(app.scheduler.R.id.btn_cancel);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = nameEditText.getText().toString();
                description = descriptionEditText.getText().toString();
                percentageText = percentageEditText.getText().toString();

                if (isValid()) {
                    double percentage = Double.parseDouble(percentageText);

                    final Task task = new Task();
                    task.setName(name);
                    task.setDescription(description);
                    task.setStartDate(DateUtil.getCurrentDate());
                    task.setEndDate(DateUtil.getCurrentDate());

                    //progressBar.setVisibility(View.VISIBLE);
                    String token = userSingletonModel.getToken();
                    TaskService taskService = APIClient.createService(TaskService.class, token);
                    Call<Void> call = taskService.createTask(schedule.getId(), task);

                    call.enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            //progressBar.setVisibility(View.GONE);
                            Headers headers = response.headers();

                            task.setId(RestUtil.getId(headers));
                            Log.d("Task Id: ", RestUtil.getId(headers) + "");
                            //data.add(task);
                            //updateUI();

                            Toast.makeText(mCon, "Task has been created",
                                    Toast.LENGTH_SHORT).show();

                            dialog.dismiss();
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            Toast.makeText(mCon, "Failed to create task",
                                    Toast.LENGTH_SHORT).show();

                            dialog.dismiss();
                        }
                    });
                    //data.add(task);
                    //updateUI();
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

    private boolean isValid() {
        String errorMessage = "";
        if (name.isEmpty() || name.equals("")) {
            errorMessage += "Task title is empty\n";
        }
        if (description.isEmpty() || description.equals("")) {
            errorMessage += "Description title is empty\n";
        }
        if (percentageText.isEmpty() || percentageText.equals("")) {
            percentageText = "0";
        }

        if (errorMessage.equals("")) {
            return true;
        }
        else {
            Toast.makeText(mCon, errorMessage, Toast.LENGTH_SHORT).show();
            return false;
        }
    }
}
