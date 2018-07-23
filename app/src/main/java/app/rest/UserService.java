package app.rest;

import app.rest.model.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by Fadejimi on 7/11/18.
 */

public interface UserService {
    @GET("users/{id}")
    Call<User> getUser(@Path("id") int id);

    @POST("users")
    Call<Void> addUser(@Body() User user);
}
