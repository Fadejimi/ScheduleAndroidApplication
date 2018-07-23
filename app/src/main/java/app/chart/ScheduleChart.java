package app.chart;

import android.content.Context;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import app.model.UserInfo;
import app.rest.APIClient;
import app.rest.TaskService;
import app.rest.model.Schedule;
import app.rest.model.Task;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ScheduleChart implements ChartInterface {
    private Context context;
    private List<Task> data;
    private UserInfo userSingletonModel = UserInfo.getInstance();
    private Schedule schedule;
    List<Task> result;

    public ScheduleChart(Context context, Schedule schedule) {
        this.context = context;
        this.schedule = schedule;
    }

    public List<Task> getData() {
        String token = userSingletonModel.getToken();
        TaskService taskService = APIClient.createService(TaskService.class, token);
        Call<List<Task>> call = taskService.getTasks(schedule.getId());

        call.enqueue(new Callback<List<Task>>() {
            @Override
            public void onResponse(Call<List<Task>> call, Response<List<Task>> response) {
                result = response.body();
            }

            @Override
            public void onFailure(Call<List<Task>> call, Throwable t) {
                Toast.makeText(context, "Failed to get the tasks data", Toast.LENGTH_SHORT).show();

            }
        });

        return result;
    }
}
