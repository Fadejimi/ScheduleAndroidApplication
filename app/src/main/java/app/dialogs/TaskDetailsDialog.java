package app.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;

import app.model.UserInfo;
import app.recview.TaskDetailsAdapter;
import app.rest.APIClient;
import app.rest.TaskMilestoneService;
import app.rest.model.Task;
import app.rest.model.TasksMilestone;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TaskDetailsDialog implements DialogInterface{
    private Context context;
    private Task task;
    private String percentageText;
    private UserInfo userSingletonModel = UserInfo.getInstance();

    public TaskDetailsDialog(Context context, Task task) {
        this.context = context;
        this.task = task;
    }

    public void showDialog() {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(app.scheduler.R.layout.new_task);
        dialog.setTitle("New Task");
        dialog.show();

        EditText nameEditText = dialog.findViewById(app.scheduler.R.id.title_edit_text);
        EditText descriptionEditText = dialog.findViewById(app.scheduler.R.id.description_edit_text);
        final EditText percentageEditText = dialog.findViewById(app.scheduler.R.id.percentage_text);
        BootstrapButton submitButton = dialog.findViewById(app.scheduler.R.id.btn_submit);
        BootstrapButton cancelButton = dialog.findViewById(app.scheduler.R.id.btn_cancel);

        nameEditText.setVisibility(View.GONE);
        descriptionEditText.setVisibility(View.GONE);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                percentageText = percentageEditText.getText().toString();

                if (isValid()) {
                    double percentage = Double.parseDouble(percentageText);

                    TasksMilestone tasksMilestone = new TasksMilestone();
                    tasksMilestone.setPercentage((int) percentage);

                    String token = userSingletonModel.getToken();
                    TaskMilestoneService service = APIClient.createService(TaskMilestoneService.class,
                            token);
                    Call<Void> call = service.createTaskMilestones(task.getId(), tasksMilestone);

                    call.enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            Toast.makeText(context, "Task has been created", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            Toast.makeText(context, "Task could not be created", Toast.LENGTH_SHORT).show();
                        }
                    });
                    dialog.dismiss();
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
        String errorString = "";
        if (percentageText.isEmpty() || percentageText.equals("")) {
            errorString += "Please fill in the percentage field\n";
        }
        if (isDouble(percentageText)) {
            errorString += "Invalid percentage value\n";
        }

        if (errorString.equals("")) {
            return true;
        }
        else {
            Toast.makeText(context, errorString, Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private boolean isDouble(String st) {
        try {
            Double.parseDouble(st);
            return true;
        }
        catch(Exception ex) {
            return false;
        }
    }
}
