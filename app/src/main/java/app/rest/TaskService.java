package app.rest;



import java.util.List;

import app.rest.model.Task;
import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by Fadejimi on 7/11/18.
 */

public interface TaskService {
    @GET("schedules/{sch_id}/tasks")
    Observable<List<Task>> getTasks(@Path("sch_id") int id);

    @GET("schedules/{sch_id}/tasks")
    Call<List<Task>> getTasksBySchedule(@Path("sch_id") int id);

    @POST("schedules/{sch_id}/tasks")
    Call<Void> createTask(@Path("sch_id") int id, @Body() Task task);

    @PUT("schedules/{sch_id}/tasks")
    Call<Task> updateTask(@Path("sch_id") int id, @Body() Task task);

    @DELETE("schedules/{sch_id}/tasks/{id}")
    Observable<Task> deleteTask(@Path("sch_id") int id, @Path("id") int task_id);

    @GET("schedules/{sch_id}/tasks/{id}")
    Call<Task> getTask(@Path("sch_id") int id, @Path("id") int task_id);
}
