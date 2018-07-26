package app.rest;



import java.util.List;

import app.rest.model.Schedule;
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

public interface ScheduleService {
    @GET("schedules")
    Observable<List<Schedule>> getSchedules();

    @POST("schedules")
    Call<Void> createSchedule(@Body() Schedule schedule);

    @PUT("schedules")
    Call<Schedule> updateSchedule(@Body() Schedule schedule);

    @DELETE("scedules/{id}")
    Call<Schedule> deleteSchedule(@Path("id") int id);

    @GET("schedules/{id}")
    Call<Schedule> getSchedule(@Path("id") int id);

}
