package app.rest;

import java.util.List;

import app.rest.model.TasksMilestone;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface TaskMilestoneService {
    @GET("tasks/{id}/task_milestones")
    Call<List<TasksMilestone>> getTaskMilestones(@Path("id") long id);

    @POST("tasks/{id}/task_milestones")
    Call<Void> createTaskMilestones(@Path("id") long id, @Body() TasksMilestone tasksMilestone);

    @PUT("tasks/{task_id}/task_milestones/{id}")
    Call<Void> updateTaskMilestones(@Path("tId") long task_id, @Path("id") long id,
                                    @Body() TasksMilestone tasksMilestone);

    @DELETE("tasks/{task_id}/task_milestones/{id}")
    Call<Void> deleteTaskMilestones(@Path("tId") long task_id, @Path("id") long id);
}
